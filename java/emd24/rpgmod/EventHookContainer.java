package emd24.rpgmod;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.relauncher.Side;
import emd24.rpgmod.EntityIdMapping.EntityId;
import emd24.rpgmod.combatitems.HolyHandGrenade;
import emd24.rpgmod.combatitems.ItemBattleaxe;
import emd24.rpgmod.gui.GUIDialogue;
import emd24.rpgmod.gui.GUIDialogueEditor;
import emd24.rpgmod.gui.GUIKeyHandler;
import emd24.rpgmod.packets.PlayerDataPacket;
import emd24.rpgmod.party.PartyManagerServer;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import emd24.rpgmod.skills.Skill;
import emd24.rpgmod.skills.SkillManagerServer;
import emd24.rpgmod.skills.SkillRegistry;
import emd24.rpgmod.skills.SkillThieving;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

/**
 * Class containing event hooks in Forge. The purpose of this class is listen to
 * events that occur in-game, and add code to run when those events happen
 * 
 * @author Evan Dyke
 * @author Wesley Reardan
 *
 */
public class EventHookContainer {

	private Random rand = new Random();

	/**
	 * Registers the Player Data class, which tracks information on additional data for the player
	 * 
	 */
	@SubscribeEvent
	public void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && ExtendedPlayerData.get(event.entity) == null) {
			ExtendedPlayerData.register((EntityPlayer) event.entity);

		}

		// Register Dialogue data
		if(event.entity instanceof EntityLiving) {
			EntityLiving ent = (EntityLiving) event.entity;
			ExtendedEntityLivingDialogueData.register(ent);
		}

		// Register Data on thieving
		if(event.entity instanceof EntityLiving){
			EntityLiving ent = (EntityLiving) event.entity;
			if(((SkillThieving) SkillRegistry.getSkill("Thieving")).isThievable(EntityIdMapping.getEntityId(ent))){
				ExtendedEntityLivingData.register(ent);
			}
		}
	}

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event){

		ExtendedPlayerData data = ExtendedPlayerData.get(event.getPlayer());

		// Check level requirement on block breaking
		for(String skill : SkillRegistry.getSkillNames()){

			// Cancel and send error message if cannot be broken
			if(data.getSkill(skill).getLevel() < SkillRegistry.getSkill(skill).getBlockRequirement(event.block)){
				event.getPlayer().addChatMessage(new ChatComponentText(skill + " Level " +	SkillRegistry.getSkill(skill).getBlockRequirement(event.block)
						+ " required").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				event.setCanceled(true);
			}

		}
	}

	@SubscribeEvent
	public void onBlockHarvest(HarvestDropsEvent event){

		if(event.harvester == null){
			return;
		}

		ExtendedPlayerData data = ExtendedPlayerData.get(event.harvester);
		for(String skill : SkillRegistry.getSkillNames()){

			int experience = SkillRegistry.getSkill(skill).getExpForBlockBreak(event.block);

			if(experience > 0){ 

				boolean levelUp = data.addExp(skill, experience);

				// Notify the player
				event.harvester.addChatMessage(new ChatComponentText("Gained " + experience 
						+ " " + skill + " exp"));
				if(levelUp){
					event.harvester.addChatMessage((new ChatComponentText("Congrats! Reached " + skill + " Level " 
							+ data.getSkill(skill).getLevel()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))));
				}
			}
			// Determine if player harvests extra items
			Random rnd = new Random();
			if(rnd.nextDouble() <= SkillRegistry.getSkill(skill).getHarvestPerkProbability(event.block, 
					data.getSkill(skill).getLevel())){
				for(ItemStack item : event.drops){
					item.stackSize++;
				}
			}
		}
	}

	@SubscribeEvent 
	public void onLivingDeathEvent(LivingDeathEvent event){
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer){
			NBTTagCompound compound = new NBTTagCompound(); 
			ExtendedPlayerData.get(event.entity).saveNBTData(compound);
		}

	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.entity;
			PartyManagerServer.addPlayerToParty(player.getCommandSenderName(), 0);
			NBTTagCompound data = RPGMod.proxy.getEntityData(player.getCommandSenderName());

			if(data != null){
				ExtendedPlayerData.get(event.entity).loadNBTData(data);
			}
			ExtendedPlayerData.get(event.entity).sync();
		}
	}

	/**
	 * Event that checks for entity interaction and attempts to pickpocket NPC if player is sneaking. 
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPickpocket(EntityInteractEvent event){

		if(event.target instanceof EntityLiving && !event.entityPlayer.worldObj.isRemote){
			EntityPlayer player = event.entityPlayer;

			EntityLiving target = (EntityLiving) event.target;

			if(player.isSneaking() && (SkillRegistry.getSkill("Thieving") instanceof SkillThieving)){
				ExtendedEntityLivingData dataTarget = ExtendedEntityLivingData.get(target); 
				SkillThieving s = (SkillThieving) SkillRegistry.getSkill("Thieving");


				EntityId id = EntityIdMapping.getEntityId(event.target);

				if(!s.isThievable(id) || dataTarget.stealCoolDown > 0){
					player.addChatMessage(new ChatComponentText("Nothing to steal!"));
					return;
				}
				player.addChatMessage((new ChatComponentText("Alert Level " + dataTarget.alertLevel)));
				ExtendedPlayerData data = ExtendedPlayerData.get(player);

				// Steal loot from entity
				ItemStack loot = s.getLoot(id, dataTarget.alertLevel, data.getSkill("Thieving").getLevel());

				// Give player item and exp if successful, damage player if not
				if(loot != null){

					player.inventory.addItemStackToInventory(loot);
					player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "random.pop", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);

					dataTarget.alertLevel++;
					dataTarget.alertTimer = 1200;
					dataTarget.stealCoolDown = 200;

					int experience = s.getExperience(id);


					if(experience > 0){ 
						boolean levelUp = data.addExp(s.name, experience);

						// Notify the player
						player.addChatMessage((new ChatComponentText("Gained " + experience + " " + s.name + " exp")));
						if(levelUp){
							player.addChatMessage((new ChatComponentText("Congrats! Reached " + s.name + " Level " + data.getSkill(s.name).getLevel())
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))));
						}
					}
				} 
				else{
					player.attackEntityFrom(DamageSource.generic, 5);
					player.addChatMessage((new ChatComponentText("Got caught stealing!").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))));
					dataTarget.alertLevel = 5;
					dataTarget.alertTimer = 1200;
				}

			}
			else{
				//check for NPC dialogue
				ExtendedEntityLivingDialogueData nbtDialogue = ExtendedEntityLivingDialogueData.get(target);

				if(GUIKeyHandler.npcAdminMode) {
					Minecraft.getMinecraft().displayGuiScreen(new GUIDialogueEditor(target));
				} else {
					Minecraft.getMinecraft().displayGuiScreen(new GUIDialogue(target));
				}

				// Regular method call
				//player.interactWith(event.entity);
			}

		}


	}

	/**
	 * Event that checks to see if the player is undead, and if so causes the player to burn up
	 * like a zombie or skeleton
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void undeadPlayerBurn(LivingUpdateEvent event){
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer ent = (EntityPlayer) event.entityLiving;
			ExtendedPlayerData data = ExtendedPlayerData.get(ent);

			if (data.isUndead() && ent.worldObj.isDaytime() && !ent.worldObj.isRemote)
			{
				float f = ent.getBrightness(1.0F);

				if (f > 0.5F && ent.getRNG().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && ent.worldObj.canBlockSeeTheSky(MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ)))
				{
					boolean flag = true;
					ItemStack itemstack = ent.getEquipmentInSlot(4);

					if (itemstack != null)
					{
						if (itemstack.isItemStackDamageable())
						{
							itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

							if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
							{
								ent.renderBrokenItemStack(itemstack);
								ent.setCurrentItemOrArmor(4, (ItemStack)null);
							}
						}

						flag = false;
					}

					if (flag)
					{
						ent.setFire(8);
					}
				}
			}
		}

	}
	
	/**
	 * This method activates an entity is attacked, and updates it with the
	 * skill modifier
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onAttacked(LivingHurtEvent event){
		
		/* Only does a check if it is the player's skill, which is dynamic. Values
		 * for mobs are hardcoded for their class, as they don't level up.
		 */
		if(event.source.getSourceOfDamage() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
			ExtendedPlayerData playerData = ExtendedPlayerData.get(player);
			
			// For each additional level, player deals 5% more damage
			event.ammount += (playerData.getSkill("Strength").getLevel() - 1) * .05;
		}
	}
	
	/**
	 * This method updates the timer for thievable NPCs to allow their alert level  to gradually reduce
	 * over  time
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void updateEntityData(LivingUpdateEvent event){

		// Update thieving data on pickpocket-able NPCs
		if(((SkillThieving) SkillRegistry.getSkill("Thieving")).
				isThievable(EntityIdMapping.getEntityId(event.entityLiving)) && !event.entityLiving.worldObj.isRemote){

			ExtendedEntityLivingData data = ExtendedEntityLivingData.get((EntityLiving) event.entityLiving);
			if(data.stealCoolDown > 0){
				data.stealCoolDown--;
			}
			if(data.alertTimer > 0){
				data.alertTimer--;
			}
			if(data.alertTimer == 0 && data.alertLevel > 0){
				data.alertLevel--;
				data.alertTimer = 1200;
			}
		}
		// Regenerate the player's mana
		if(event.entity instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote){
			ExtendedPlayerData data = ExtendedPlayerData.get((EntityPlayer) event.entityLiving);
			data.manaTicks--;
			if(data.manaTicks == 0){
				data.recoverMana(1);
				data.manaTicks = data.getRegenRate();
			}



		}
	}

	/**
	 * If an entity is struck by lightning, this method checks whether struck by
	 * regular lightning, or lightning that was created by a spell.
	 * 
	 * @param event the Forge onStruckByLightning event
	 */
	@SubscribeEvent
	public void lightningStrike(EntityStruckByLightningEvent event){

		EntityLightningBolt lightning = event.lightning;
		Entity entityAttacked = event.entity;

		if(lightning instanceof MagicLightning){
			MagicLightning l = (MagicLightning) lightning;
			entityAttacked.attackEntityFrom(DamageSource.inFire, 
					(float)((MagicLightning) lightning).getPower());

			if(entityAttacked instanceof EntityCreeper){
				entityAttacked.getDataWatcher().updateObject(17, Byte.valueOf((byte)1));
			}
		}
		else{
			entityAttacked.onStruckByLightning(lightning);
		}
	}

	public void onPlayerRespawn(EntityPlayer player) {
		if(!player.worldObj.isRemote){
			RPGMod.packetPipeline.sendTo(new PlayerDataPacket(player), (EntityPlayerMP) player);
		}
	}

	@SubscribeEvent
	public void pickupHolyHandGrenade(EntityItemPickupEvent event){
		if(event.item.getEntityItem().getItem() instanceof HolyHandGrenade){
			event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, RPGMod.MOD_ID + ":holyhandgrenadepickup", 1.0F, 1.0F);
		}

	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event){
		PartyManagerServer.removePlayerFromGame(event.player.getCommandSenderName());
	}
}

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
import emd24.rpgmod.packets.GUIOpenPacket;
import emd24.rpgmod.packets.PartyInvitePacket;
import emd24.rpgmod.packets.PlayerDataPacket;
import emd24.rpgmod.party.PartyManagerServer;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import emd24.rpgmod.skills.Skill;
import emd24.rpgmod.skills.SkillManagerServer;
import emd24.rpgmod.skills.SkillRegistry;
import emd24.rpgmod.skills.SkillThieving;
import emd24.rpgmod.spells.Spell;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Start;
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
		if (event.entity instanceof EntityPlayer && ExtendedPlayerData.get((EntityPlayer) event.entity) == null) {
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

			// Add experience
			int experience = SkillRegistry.getSkill(skill).getExpForBlockBreak(event.block);

			if(experience > 0){ 
				data.addExp(skill, experience);
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
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound compound = new NBTTagCompound(); 
			ExtendedPlayerData.get(player).saveNBTData(compound);
			// Needed to save skill data on death
			RPGMod.proxy.storeEntityData(player.getCommandSenderName(), compound);
		}

	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.entity;
			PartyManagerServer.addPlayerToParty(player.getCommandSenderName(), 0);
			NBTTagCompound proxyData = RPGMod.proxy.getEntityData(player.getCommandSenderName());

			if(proxyData != null){
				ExtendedPlayerData playerData = ExtendedPlayerData.get(player);
				playerData.loadNBTData(proxyData);
				playerData.setCurrMana(playerData.getMaxMana());

			}
			ExtendedPlayerData.get(player).sync();
		}
	}

	/**
<<<<<<< HEAD
=======
	 * Method where a spell can be used on another entity (such as casting lighting on another player)
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void useSpellOn(EntityInteractEvent event){
		if(event.entityPlayer.getHeldItem() == null){
			return;
		}
		if (event.entityPlayer.getHeldItem().getItem() instanceof Spell){

			Spell s = (Spell) event.entityPlayer.getHeldItem().getItem();
			if(!s.onItemUse){
				return;
			}

			// Check to see if player has enough mana
			ExtendedPlayerData properties = ExtendedPlayerData.get(event.entityPlayer);
			if(properties.getCurrMana() < s.getManaCost()){
				if(!event.entityPlayer.worldObj.isRemote)
					event.entityPlayer.addChatMessage(new ChatComponentText("Not enough mana!"));
			}
			else if(s.getLevelRequired() > properties.getSkill("Magic").getLevel()){
				if(!event.entityPlayer.worldObj.isRemote)
					event.entityPlayer.addChatMessage((new ChatComponentText("Magic Level " + s.getLevelRequired() + 
							" required").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))));
			}
			else{
				if(s.castSpell(event.entityPlayer.getHeldItem(), event.entityPlayer, event.entityPlayer.worldObj,
						(int) event.target.posX, (int) event.target.posY, (int) event.target.posZ, 0)){
					properties.useMana(s.getManaCost());
					properties.addExp("Magic", s.getExperience());
					if (!event.entityPlayer.capabilities.isCreativeMode)
					{
						--event.entityPlayer.getHeldItem().stackSize;
					}
					if(!event.entityPlayer.worldObj.isRemote){
						event.entityPlayer.addChatMessage(new ChatComponentText("Mana remaining: " + properties.getCurrMana()));
					}
				}
			}  		

		}
	}

	/**
	 * Event that checks for entity interaction and attempts to pickpocket NPC if player is sneaking. 
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onPickpocket(EntityInteractEvent event){
		
		// Exit if casting a spell
		if((event.entityPlayer.getHeldItem() != null && (event.entityPlayer.getHeldItem().getItem() instanceof Spell))){
			return;
		}

		if(event.target instanceof EntityLiving && !event.entityPlayer.worldObj.isRemote){
			EntityPlayer player = event.entityPlayer;

			EntityLiving target = (EntityLiving) event.target;

			if(player.isSneaking() && (SkillRegistry.getSkill("Thieving") instanceof SkillThieving)){
				ExtendedEntityLivingData dataTarget = ExtendedEntityLivingData.get(target); 
				SkillThieving s = (SkillThieving) SkillRegistry.getSkill("Thieving");
				
				ExtendedPlayerData data = ExtendedPlayerData.get(player);
				

				EntityId id = EntityIdMapping.getEntityId(event.target);
				if(data.getSkill("Thieving").getLevel() < s.getLevelRequirement(id)){
					if(!player.worldObj.isRemote)
						player.addChatMessage(new ChatComponentText("Thieving Level " +s.getLevelRequirement(id)
								+ " required").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					return;
				}
				
				if(!s.isThievable(id) || dataTarget.stealCoolDown > 0){
					player.addChatMessage(new ChatComponentText("Nothing to steal!"));
					return;
				}
				player.addChatMessage((new ChatComponentText("Alert Level " + dataTarget.alertLevel)));


				// Steal loot from entity
				ItemStack loot = s.getLoot(id, dataTarget.alertLevel, data.getSkill("Thieving").getLevel());

				// Give player item and exp if successful, damage player if not
				if(loot != null){

					// Update data on NPC stolen from, raise alert level

					player.inventory.addItemStackToInventory(loot);
					player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "random.pop", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);

					dataTarget.alertLevel++;
					dataTarget.alertTimer = 1200;
					dataTarget.stealCoolDown = 200;

					// Add experience

					int experience = s.getExperience(id);

					if(experience > 0){ 
						data.addExp(s.name, experience);
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
				int entityID = target.getEntityId();
				//Load dialogue from CommonProxy if it exists
				String dialogue = CommonProxy.getDialogueData(entityID);
				if(dialogue != null)
					nbtDialogue.dialogueTree.load(dialogue);
				else
					dialogue = nbtDialogue.dialogueTree.store(); //Otherwise, get current state

				if(true) {
					//TODO: add logic so that only NPC's hit this section of code
					GUIOpenPacket packet = new GUIOpenPacket(0, entityID, dialogue);
					RPGMod.packetPipeline.sendTo(packet, (EntityPlayerMP)player);

					event.setCanceled(true);
				} else {
					// Regular method call
					player.interactWith(event.entity);
				}
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
	public void updatePlayer(LivingUpdateEvent event){
		if (event.entity instanceof EntityPlayer) {


			// Make the player catch fire if in sunlight or undead
			EntityPlayer ent = (EntityPlayer) event.entityLiving;
			ExtendedPlayerData data = ExtendedPlayerData.get(ent);

			// Check if the fly spell is in effect
			if (ent.isPotionActive(RPGMod.flyingPotion.id)) {
				ent.capabilities.allowFlying = true;
			}
			else{
				ent.capabilities.allowFlying = false;
				ent.capabilities.isFlying = false;
			}

			if (ent.getActivePotionEffect(RPGMod.flyingPotion) != null &&
					ent.getActivePotionEffect(RPGMod.flyingPotion).getDuration() == 0) {
				ent.removePotionEffect(RPGMod.flyingPotion.id);
			}

			// Check if mana drain is in effect
			/*if (ent.isPotionActive(RPGMod.manaDrain.id)) {
				ent.capabilities.allowFlying = true;
			}


			if (ent.getActivePotionEffect(RPGMod.manaDrain) != null &&
					ent.getActivePotionEffect(RPGMod.manaDrain).getDuration() == 0) {
				ent.removePotionEffect(RPGMod.manaDrain.id);
			}*/
		
			
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
		 * 
		 * Alos, check to see if direct damage dealt by player
		 */
		if(event.source.getSourceOfDamage() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
			ExtendedPlayerData playerData = ExtendedPlayerData.get(player);

			/* For each additional level, player deals 5% more damage, rounded down. An int
			 * cast is probably happening deep in the vanilla code somewheres
			 */
			event.ammount *= (1.0 + (playerData.getSkill("Strength").getLevel() - 1) * .05);

			/* Adds experience, 4 for each point of damage dealt (in half-hearts). Cannot
			 * gain more exp for damage than the target has health.
			 */
			playerData.addExp("Strength", 4 * Math.min((int) event.ammount, Math.round(event.entityLiving.getHealth())));

		}
		/* Check for indirect damage sources (such as spells or ranged items)
		 * 
		 */
		else if(event.source instanceof EntityDamageSourceIndirect){
			EntityDamageSourceIndirect indirectSource = (EntityDamageSourceIndirect) event.source;

			// Check if damaged by projectile thrown by player entity, ranged skill
			if(indirectSource.isProjectile() && (indirectSource.getEntity() instanceof EntityPlayer)){

				EntityPlayer player = (EntityPlayer) indirectSource.getEntity();
				ExtendedPlayerData playerData = ExtendedPlayerData.get(player);

				/* For each additional level, player deals 5% more damage, rounded down. An int
				 * cast is probably happening deep in the vanilla code somewheres
				 */
				event.ammount *= (1.0 + (playerData.getSkill("Ranged").getLevel() - 1) * .05);

				/* Adds experience, 4 for each point of damage dealt (in half-hearts). Cannot
				 * gain more exp for damage than the target has health.
				 */
				playerData.addExp("Ranged", 4 * Math.min((int) event.ammount, Math.round(event.entityLiving.getHealth())));
			}
		}

		/* Check if damage is from hunger, cancel if undead
		 * 
		 */
		if(event.entityLiving instanceof EntityPlayer && event.source == DamageSource.starve){
			EntityPlayer playerDamaged = (EntityPlayer) event.entityLiving;
			ExtendedPlayerData data = ExtendedPlayerData.get(playerDamaged);
			if(data.isUndead()){
				event.setCanceled(true);	
			}
		}
	}

	/**
	 * Event that checks if a melee weapon has a requirement to use in battle. 
	 * 
	 * TODO: write this method
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onItemUse(Start event){

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
			EntityPlayer player = (EntityPlayer) event.entity; 
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
	public void onLightningStrike(EntityStruckByLightningEvent event){

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

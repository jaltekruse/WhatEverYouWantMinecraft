package emd24.minecraftrpgmod;

import java.util.Random;

import emd24.minecraftrpgmod.party.PartyManagerServer;
import emd24.minecraftrpgmod.EntityIdMapping.EntityId;
import emd24.minecraftrpgmod.skills.Skill;
import emd24.minecraftrpgmod.skills.SkillManagerServer;
import emd24.minecraftrpgmod.skills.SkillRegistry;
import emd24.minecraftrpgmod.skills.SkillThieving;
import emd24.minecraftrpgmod.spells.entities.MagicLightning;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

/**
 * Class containing event hooks in Forge. The purpose of this class is listen to
 * events that occur in-game, and add code to run when those events happen
 * 
 * @author Evan Dyke
 *
 */
public class EventHookContainer {

	private Random rand = new Random();

	/**
	 * Registers the Player Data class, which tracks information on additional data for the player
	 * 
	 */
	@ForgeSubscribe
    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && ExtendedPlayerData.get(event.entity) == null) {
            ExtendedPlayerData.register((EntityPlayer) event.entity);
            
        }
    }
	
	@ForgeSubscribe
	public void onBlockBreak(BreakEvent event){

		ExtendedPlayerData data = ExtendedPlayerData.get(event.getPlayer());
		int blockId = event.block.blockID;

		// Check level requirement on block breaking
		for(String skill : SkillRegistry.getSkillNames()){

			// Cancel and send error message if cannot be broken
			if(data.getSkill(skill).getLevel() < SkillRegistry.getSkill(skill).getBlockRequirement(blockId)){
				event.getPlayer().sendChatToPlayer((new ChatMessageComponent()).addText(skill + " Level " + 
						SkillRegistry.getSkill(skill).getBlockRequirement(blockId) + " required").setColor(EnumChatFormatting.RED));
				event.setCanceled(true);
			}

		}
	}

	@ForgeSubscribe
	public void onBlockHarvest(HarvestDropsEvent event){
		
		if(event.harvester == null){
			return;
		}
		
		int blockId = event.block.blockID;
		ExtendedPlayerData data = ExtendedPlayerData.get(event.harvester);

		for(String skill : SkillRegistry.getSkillNames()){

			int experience = SkillRegistry.getSkill(skill).getExpForBlockBreak(blockId);

			if(experience > 0){ 

				boolean levelUp = data.addExp(skill, experience);

				// Notify the player
				event.harvester.sendChatToPlayer((new ChatMessageComponent()).addText("Gained " + experience 
						+ " " + skill + " exp"));
				if(levelUp){
					event.harvester.sendChatToPlayer((new ChatMessageComponent()).addText("Congrats! Reached "
							+ skill + " Level " + data.getSkill(skill).getLevel()).setColor(EnumChatFormatting.GREEN));
				}
			}
			// Determine if player harvests extra items
			Random rnd = new Random();
			if(rnd.nextDouble() <= SkillRegistry.getSkill(skill).getHarvestPerkProbability(blockId, 
					data.getSkill(skill).getLevel())){
				for(ItemStack item : event.drops){
					item.stackSize++;
				}
			}
		}
	}

	@ForgeSubscribe 
	public void onLivingDeathEvent(LivingDeathEvent event){
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer){
			NBTTagCompound compound = new NBTTagCompound(); 
			ExtendedPlayerData.get(event.entity).saveNBTData(compound);
			RPGMod.proxy.storeEntityData(((EntityPlayer) event.entity).username, compound);
		}

	}

	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.entity;
			PartyManagerServer.addPlayerToParty(player.username, 0);
			NBTTagCompound data = RPGMod.proxy.getEntityData(player.username);

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
	@ForgeSubscribe
	public void onPickpocket(EntityInteractEvent event){

		if(event.target instanceof EntityLiving && !event.entityPlayer.worldObj.isRemote){
			EntityPlayer player = event.entityPlayer;
			EntityLiving target = (EntityLiving) event.target;
			if(player.isSneaking() && (SkillRegistry.getSkill("Thieving") instanceof SkillThieving)){

				SkillThieving s = (SkillThieving) SkillRegistry.getSkill("Thieving");

				// TODO: Need to get EntityId from entity

				EntityId id = EntityIdMapping.getEntityId(event.target);

				if(!s.isThievable(id)){
					player.sendChatToPlayer((new ChatMessageComponent()).addText("Nothing to steal!"));
					return;
				}

				// Steal loot from entity
				ItemStack loot = s.getLoot(id);

				// Give player item and exp if successful, damage player if not
				if(loot != null){

					player.inventory.addItemStackToInventory(loot);
					int experience = s.getExperience(id);
					ExtendedPlayerData data = ExtendedPlayerData.get(player);

					if(experience > 0){ 
						boolean levelUp = data.addExp(s.name, experience);

						// Notify the player
						player.sendChatToPlayer((new ChatMessageComponent()).addText("Gained " + experience 
								+ " " + s.name + " exp"));
						if(levelUp){
							player.sendChatToPlayer((new ChatMessageComponent()).addText("Congrats! Reached "
									+ s.name + " Level " + data.getSkill(s.name).getLevel()).setColor(EnumChatFormatting.GREEN));
						}
					}
				} 
				else{
					player.attackEntityFrom(DamageSource.generic, 5);
					player.sendChatToPlayer((new ChatMessageComponent()).addText("Got caught stealing!").setColor(EnumChatFormatting.RED));
				}

			}
			else{
				// Regular method call
				player.interactWith(event.entity);
			}

		}


	}

	/**
	 * Event that checks to see if the player is undead, and if so causes the player to burn up
	 * like a zombie or skeleton
	 * 
	 * @param event
	 */
	@ForgeSubscribe
	public void undeadPlayerBurn(LivingUpdateEvent event){
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer ent = (EntityPlayer) event.entityLiving;
			ExtendedPlayerData data = ExtendedPlayerData.get(ent);

			if (data.isUndead() && ent.worldObj.isDaytime() && !ent.worldObj.isRemote && !ent.worldObj.isRaining())
			{
				float f = ent.getBrightness(1.0F);

				if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && ent.worldObj.canBlockSeeTheSky(MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ)))
				{
					boolean flag = true;
					ItemStack itemstack = ent.getCurrentItemOrArmor(4);

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
	 * If an entity is struck by lightning, this method checks whether struck by
	 * regular lightning, or lightning that was created by a spell.
	 * 
	 * @param event the Forge onStruckByLightning event
	 */
	@ForgeSubscribe
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
		ExtendedPlayerData.get(player).sync();;
	}
}

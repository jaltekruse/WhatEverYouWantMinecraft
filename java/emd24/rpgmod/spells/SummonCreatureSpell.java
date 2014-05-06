package emd24.rpgmod.spells;

import emd24.rpgmod.ExtendedEntityLivingData;
import emd24.rpgmod.spells.entities.MagicLightning;
import emd24.rpgmod.summons.ai.EntityAIFollowSummoner;
import emd24.rpgmod.summons.ai.EntityAISummonerHurtByTarget;
import emd24.rpgmod.summons.ai.EntityAISummonerHurtTarget;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Abstract class representing a spell that summons a being into this world.
 * 
 * @author Evan Dyke
 *
 */
public class SummonCreatureSpell extends Spell{

	private int monster_id;

	public SummonCreatureSpell(int monster_id, CreativeTabs tab) {
		super(0, tab);
		this.monster_id = monster_id;
		this.onItemRightClick = false;
		this.onItemUse = true;
	}

	
	/**
	 * Overwritten onItemUse method that summons a creature in the world. Code is modified/taken from the
	 * ItemMonsterPlacer class, with some modifications made.
	 */
	@Override

	public boolean castSpell(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side)
	{
		if (par3World.isRemote)
        {
            return false;
        } 
        else
        {

            Block block = par3World.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];
            double d0 = 0.0D;

            if (side == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }
            
            Entity entity = ItemMonsterPlacer.spawnCreature(par3World, this.monster_id, (double)x + 0.5D, (double)y + d0, (double)z + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
                {
                    ((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
                }
                if(entity instanceof EntityCreature){
                	// Here beigns code to change the mob's AI to be a loyal, aithful servant
                	EntityCreature ent = (EntityCreature) entity;
                	
                	// Get and/or create extended properties of player
                	if(ExtendedEntityLivingData.get(ent) == null){
                		ExtendedEntityLivingData.register(ent);
                	}
                	ExtendedEntityLivingData prop = ExtendedEntityLivingData.get(ent);
                	prop.summonerName = par2EntityPlayer.getCommandSenderName();
                	ent.tasks.addTask(5, new EntityAIFollowSummoner(ent, 1.0D, 10.0F, 2.0F));
                	ent.targetTasks.taskEntries.clear();
                	ent.targetTasks.addTask(1, new EntityAISummonerHurtByTarget(ent));
                	ent.targetTasks.addTask(2, new EntityAISummonerHurtTarget(ent));
                	ent.targetTasks.addTask(3, new EntityAIHurtByTarget(ent, true));
                		
                }
            }
        }
		return true;
	}
	
}



package emd24.minecraftrpgmod.spells;

import emd24.minecraftrpgmod.spells.entities.MagicLightning;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
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

	public SummonCreatureSpell(int id, int monster_id, CreativeTabs tab) {
		super(id, 0, tab);
		this.monster_id = monster_id;
	}

	/**
	 * Overwritten onItemUse method that summons a creature in the world. Code is modified/taken from the
	 * ItemMonsterPlacer class, with some modifications made.
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (par3World.isRemote)
		{
			return true;
		}
		else{
			int i1 = par3World.getBlockId(par4, par5, par6);
			par4 += Facing.offsetsXForSide[par7];
			par5 += Facing.offsetsYForSide[par7];
			par6 += Facing.offsetsZForSide[par7];
			double d0 = 0.0D;

			if (par7 == 1 && Block.blocksList[i1] != null && Block.blocksList[i1].getRenderType() == 11)
			{
				d0 = 0.5D;
			}

			if (!EntityList.entityEggs.containsKey(Integer.valueOf(this.monster_id)))
			{
				return true;
			}
			else
			{
				Entity entity = null;

				entity = EntityList.createEntityByID(this.monster_id, par3World);

				if (entity != null && entity instanceof EntityLivingBase)
				{
					EntityLiving entityliving = (EntityLiving)entity;
					entity.setLocationAndAngles((double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D, MathHelper.wrapAngleTo180_float(par3World.rand.nextFloat() * 360.0F), 0.0F);
					entityliving.rotationYawHead = entityliving.rotationYaw;
					entityliving.renderYawOffset = entityliving.rotationYaw;
					entityliving.onSpawnWithEgg((EntityLivingData)null);
					par3World.spawnEntityInWorld(entity);
					entityliving.playLivingSound();
				}
				if (entity != null)
				{
					if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
					{
						((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
					}

					if (!par2EntityPlayer.capabilities.isCreativeMode)
					{
						--par1ItemStack.stackSize;
					}
				}
			}
		}
		return true;
	}
	
}



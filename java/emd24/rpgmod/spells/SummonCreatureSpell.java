package emd24.rpgmod.spells;

import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
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
	public boolean castSpell(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (par3World.isRemote)
        {
            return false;
        } 
        else
        {
            Block block = par3World.getBlock(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double d0 = 0.0D;

            if (par7 == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            Entity entity = ItemMonsterPlacer.spawnCreature(par3World, this.monster_id, (double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D);

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
		return true;
	}
	
}



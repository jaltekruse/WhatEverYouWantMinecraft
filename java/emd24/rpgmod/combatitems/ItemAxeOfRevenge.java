package emd24.rpgmod.combatitems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 * This class represents the Axe of Revenge item. It deals more damage the lower the players health is.
 * 
 * @author Evan Dyke
 *
 */
public class ItemAxeOfRevenge extends Item {
	
	public ItemAxeOfRevenge()
	{
		super();
	}
	
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        par1ItemStack.damageItem(1, par3EntityLivingBase);
        
        /* Calculate damage for the axe of revenge. The formula goes as follows: 
         * damage = 4 + x^2 + x, and x = -log2(curr HP / Max HP)
         * 
         */
        double logPercentRemaining = Math.log(par3EntityLivingBase.getMaxHealth() / par3EntityLivingBase.getHealth()) / Math.log(2.0);
        int damage = (int) (4.0F + logPercentRemaining * (logPercentRemaining + 1));
        par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par3EntityLivingBase), damage);
        return true;
    }
	
	
}

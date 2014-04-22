package emd24.rpgmod.combatitems;

import java.util.ArrayList;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * This class represents the Axe of Revenge item. It deals more damage the lower the players health is.
 * 
 * @author Evan Dyke
 *
 */
public class ItemAxeOfRevenge extends ItemSword {

	private float damage = 5.0F;

	public ItemAxeOfRevenge()
	{
		super(ToolMaterial.IRON);
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		par1ItemStack.damageItem(1, par3EntityLivingBase);
		if(par3EntityLivingBase instanceof EntityPlayer){
			par2EntityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) par3EntityLivingBase), 
					(float) calculateDamage(par3EntityLivingBase));
		}
		else{
			par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage((EntityPlayer) par3EntityLivingBase), 
					(float) calculateDamage(par3EntityLivingBase));
		}
		return true;
	}


	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	@Override
	public Multimap getItemAttributeModifiers()
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), 
				new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.damage, 0));
		return multimap;
	}

	/**
	 * Updates the damage calculation for the weapon based on an entities properties.
	 * 
	 */
	private int calculateDamage(EntityLivingBase entityBase){
		/* Calculate damage for the axe of revenge. Does 1 extra point of damage for each 10% of
		 * health lost, starting at 5 (equivalent to stone axe) 
		 * 
		 */
		return Math.round(damage + (1 - entityBase.getHealth() / entityBase.getMaxHealth()) * 10);

	}


}

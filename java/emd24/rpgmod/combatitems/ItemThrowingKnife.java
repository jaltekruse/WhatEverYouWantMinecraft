package emd24.rpgmod.combatitems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import emd24.rpgmod.RPGMod;

public class ItemThrowingKnife extends ItemSword{
	
	private float damage;
	private ToolMaterial material;
	
	public ItemThrowingKnife(ToolMaterial material)
	{
		super(material);
		this.material = material;
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.damage = 2.0F + material.getDamageVsEntity();
	}
	
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            // Create a projectile entity for the thrown hand grenade
        	
        	ItemThrowingKnifeEntity knife = new ItemThrowingKnifeEntity(par2World, par3EntityPlayer).setDamage(this.damage); 
        	
        	// TODO: Fix throwing knives
        	par2World.spawnEntityInWorld(knife);
        }

        return par1ItemStack;
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
}

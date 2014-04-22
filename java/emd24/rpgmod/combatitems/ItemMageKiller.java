package emd24.rpgmod.combatitems;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import emd24.rpgmod.ExtendedPlayerData;

public class ItemMageKiller extends ItemSword{

	private float damage = 4.0F;
	private Random rnd = new Random();
	private float chanceDrain = 0.3F;
	
	public ItemMageKiller()
	{
		super(ToolMaterial.IRON);
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 * 
	 * @param par1ItemStack itemstack of the item attacking player has
	 * @param par2EntityLivingBase target that is attacked
	 */
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		par1ItemStack.damageItem(1, par3EntityLivingBase);
		par2EntityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) par3EntityLivingBase), damage);
		
		// TODO: needs to be fixed for attacking mobs, not just players
		if(par2EntityLivingBase instanceof EntityPlayer){
			ExtendedPlayerData  data = ExtendedPlayerData.get((EntityPlayer) par2EntityLivingBase);
			if(rnd.nextFloat() <= chanceDrain){
				int manaDrain = 10 + 2 * (data.getSkill("Strength").getLevel() - 1);
				data.useMana(manaDrain);	
			}
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

}

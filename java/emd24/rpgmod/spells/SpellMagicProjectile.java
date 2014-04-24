package emd24.rpgmod.spells;

import emd24.rpgmod.RPGMod;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.spells.entities.MagicBall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class SpellMagicProjectile extends Spell{

	public SpellMagicProjectile(int basePower, CreativeTabs tab){
		super(basePower, tab); // Create a spell with base damage of 10
		this.onItemRightClick = true;
		this.onItemUse = false;
	}
	
	@Override
	public boolean castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){

		if(!par2World.isRemote){
			par2World.spawnEntityInWorld(new MagicBall(par2World, par3EntityPlayer));
		}
		return true;
	}
}

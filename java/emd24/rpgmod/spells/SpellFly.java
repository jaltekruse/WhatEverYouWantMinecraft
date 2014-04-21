package emd24.rpgmod.spells;

import emd24.rpgmod.RPGMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * This spell enables the user to fly for a specified duration.
 * 
 * @author Evan Dyke
 *
 */
public class SpellFly extends Spell{

	public SpellFly(int basePower, CreativeTabs tab){
		super(basePower, tab);

		this.onItemRightClick = true;
		this.onItemUse = false;
	}

	@Override
	public boolean castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){

		if(par2World.isRemote){
			return false; 
		}
		if(par3EntityPlayer.isAirBorne){
			par3EntityPlayer.addChatMessage(new ChatComponentText("Unable to cast. Goodbye!"));
			return false;
		}

		par3EntityPlayer.addPotionEffect(new PotionEffect(RPGMod.flyingPotion.id, 600, 1));
		return true;
	}

}
package emd24.minecraftrpgmod.spells;

import emd24.minecraftrpgmod.ExtendedPlayerData;
import emd24.minecraftrpgmod.gui.GUISkills;
import emd24.minecraftrpgmod.gui.GUIWhatever;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;

/**
 * Spell that turns the player undead.
 * 
 * @author Evan Dyke
 *
 */
public class BecomeUndeadSpell extends Spell{

	public BecomeUndeadSpell(int id, int maxStackSize, CreativeTabs tab){
		super(id, 0, tab); //Returns super constructor: par1 is ID
		this.onItemRightClick = true;
		this.onItemUse = false;
	}
	
	@Override
	public void castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		ExtendedPlayerData data = ExtendedPlayerData.get(par3EntityPlayer);

		if (!data.isUndead()){
			data.setUndead(true);

		}
	}
}

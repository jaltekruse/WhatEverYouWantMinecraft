package emd24.rpgmod.spells;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class HealSpell extends Spell{

	private boolean party;

	public HealSpell(int basePower, CreativeTabs tab, boolean party){
		super(basePower, tab);

		this.party = party;
		this.onItemRightClick = true;
		this.onItemUse = false;

	}

	@Override
	public boolean castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		ExtendedPlayerData data = ExtendedPlayerData.get(par3EntityPlayer);

		if(par3EntityPlayer.getHealth() < par3EntityPlayer.getMaxHealth()){
			par3EntityPlayer.heal(this.getBasePower());
			return true;
		}
		if(!par2World.isRemote)
			par3EntityPlayer.addChatMessage(new ChatComponentText("At full health!"));
		return false;
	}	

}

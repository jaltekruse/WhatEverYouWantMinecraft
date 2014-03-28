package emd24.minecraftrpgmod.spells;

import emd24.minecraftrpgmod.ExtendedPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemManaHeal extends Item{

	public ItemManaHeal() {
		super();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			--par1ItemStack.stackSize;
		}

		ExtendedPlayerData data = ExtendedPlayerData.get(par3EntityPlayer);
		data.setCurrMana(data.getMaxMana());
		if(!par2World.isRemote){
			par3EntityPlayer.addChatMessage((new ChatComponentText("Mana Recovered")));
		}

		return par1ItemStack;

	}

}

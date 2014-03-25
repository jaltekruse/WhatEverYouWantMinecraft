package emd24.minecraftrpgmod.spells;

import emd24.minecraftrpgmod.ExtendedPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

public class ItemManaHeal extends Item{

	public ItemManaHeal(int id) {
		super(id);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		if (!par2EntityPlayer.capabilities.isCreativeMode)
		{
			--par1ItemStack.stackSize;
		}
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		else{
			ExtendedPlayerData data = ExtendedPlayerData.get(par2EntityPlayer);
			data.setCurrMana(data.getMaxMana());
			if(!par3World.isRemote){
				par2EntityPlayer.sendChatToPlayer((new ChatMessageComponent()).addText("Mana Recovered"));
			}
		}
		return true;

	}

}

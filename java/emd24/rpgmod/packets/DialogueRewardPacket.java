package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DialogueRewardPacket extends AbstractPacket {
	
	int playerID;
	String itemName;
	int itemQuantity;

	NBTTagCompound data;
	
	public DialogueRewardPacket()
	{
		
	}
	
	public DialogueRewardPacket(int playerID, String itemName, int itemQuantity)
	{
		this.playerID = playerID;
		this.itemName = itemName;
		this.itemQuantity = itemQuantity;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setInteger("playerID", playerID);
		data.setString("itemName", itemName);
		data.setInteger("itemQuantity", itemQuantity);
		
		ByteBufUtils.writeTag(buffer, data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);

		playerID = data.getInteger("playerID");
		itemName = data.getString("itemName");
		itemQuantity = data.getInteger("itemQuantity");

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
		Item item = (Item) Item.itemRegistry.getObject(itemName);
		player.inventory.addItemStackToInventory(new ItemStack(item, itemQuantity));
	}

}

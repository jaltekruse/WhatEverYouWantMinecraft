package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

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
	

	protected void removeItemInventory(Item item, InventoryPlayer inventory, int quantity) {
		ItemStack itemStack = new ItemStack(item, 1);
		int quantityRemoved = 0;
		for(ItemStack test : inventory.mainInventory) {
			if(test != null) {
				if(test.isItemEqual(itemStack)) {
					quantityRemoved += test.stackSize;
					if(quantityRemoved > quantity) {
						test.stackSize = (quantityRemoved - quantity);
						return;
					} else {
						test.stackSize = 0;
					}
				}
			}
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Item item = (Item) Item.itemRegistry.getObject(itemName);
		if(itemQuantity < 0) {
			//remove items from player's inventory
			removeItemInventory(item, player.inventory, (-1) * itemQuantity);
		}else if(itemQuantity > 0) {
			player.inventory.addItemStackToInventory(new ItemStack(item, itemQuantity));
		} else {
			System.err.println("DialogueRewardPacket() error: itemQuantity=0");
		}
	}

}

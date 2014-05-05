package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class GiveSpellPacket extends AbstractPacket{

	private NBTTagCompound data;
	private String playerName;
	private  int itemId;

	public GiveSpellPacket(int itemId){
		this.itemId = itemId;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setInteger("item_id", itemId);
		ByteBufUtils.writeTag(buffer, data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);


	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
		Item i = Item.getItemById(data.getInteger("item_id"));
		player.inventory.addItemStackToInventory(new ItemStack(i, 1, 0));

	}

}


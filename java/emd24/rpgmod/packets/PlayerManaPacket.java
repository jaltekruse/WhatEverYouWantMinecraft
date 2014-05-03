package emd24.rpgmod.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.party.PartyManagerClient;
import emd24.rpgmod.party.PartyManagerServer;

public class PlayerManaPacket extends AbstractPacket{

	private NBTTagCompound data;
	
	public PlayerManaPacket(){
		
	}
	
	public PlayerManaPacket(String player, int[] mana){
		data = new NBTTagCompound();
		data.setString("name", player);
		data.setIntArray("mana", mana);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		ByteBufUtils.writeTag(buffer, data);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);
		
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
			
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		String name = data.getString("name");
		int[] mana = data.getIntArray("mana");
		PartyManagerServer.handlePlayerMana(name, mana);
	}

}
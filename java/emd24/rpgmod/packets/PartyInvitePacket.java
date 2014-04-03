package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.party.PartyManagerClient;
import emd24.rpgmod.party.PartyManagerServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class PartyInvitePacket extends AbstractPacket{
	
	private NBTTagCompound data;
	
	private int type;
	private String playerName, invitingPlayer;
	
	public PartyInvitePacket(){
//		this.type = type;
//		this.playerName = playerName;
//		this.invitingPlayer = invitingPlayer;
	}
	
	public PartyInvitePacket(int type, String playerName, String invitingPlayer){
		this.type = type;
		this.playerName = playerName;
		this.invitingPlayer = invitingPlayer;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setInteger("type", type);

		data.setString("name", playerName);
		data.setString("invitingPlayerName", invitingPlayer);
		
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

		type = data.getInteger("type");
		playerName = data.getString("name");
		invitingPlayer = data.getString("invitingPlayerName");
		
		switch(type) {
		case 1:
			PartyManagerServer.addPlayerToPlayersParty(playerName, invitingPlayer);
			break;
		case 2:
			PartyManagerServer.removePlayerFromParty(playerName, invitingPlayer);
			break;
		}
	}

}
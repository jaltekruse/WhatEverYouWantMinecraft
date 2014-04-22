package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.party.PartyManagerClient;
import emd24.rpgmod.party.PartyManagerServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class PartyDataPacket extends AbstractPacket{
	
	private NBTTagCompound data;
	
	public PartyDataPacket(){
		
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		int size = PartyManagerServer.playerParty.size();
		data.setInteger("size", size);
		int index = 0;
		for(String player : PartyManagerServer.playerParty.keySet()){
			
			NBTTagCompound playerPartyInfo = new NBTTagCompound();
			playerPartyInfo.setString("name", player);
			playerPartyInfo.setInteger("partyID", PartyManagerServer.playerParty.get(player));
			
			data.setTag("" + index, playerPartyInfo);
			index++;
		}
		ByteBufUtils.writeTag(buffer, data);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);
		
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		PartyManagerClient.clear();
		int size = data.getInteger("size");
		for(int i = 0; i < size; i++){
			NBTTagCompound playerData = data.getCompoundTag("" + i);
			String name = playerData.getString("name");
			int party = playerData.getInteger("partyID");
			PartyManagerClient.setPlayerParty(name, party);
			
			//String message = "partydata pak: " + name + " : " + party;
			//player.addChatMessage(new ChatComponentText(message));
		}
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

}

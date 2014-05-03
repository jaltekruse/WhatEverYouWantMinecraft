package emd24.rpgmod.packets;

import org.apache.commons.lang3.ArrayUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.party.PartyManagerClient;
import emd24.rpgmod.party.PartyManagerServer;

public class PartyManaPacket extends AbstractPacket {
		
		private NBTTagCompound data;
		
		public PartyManaPacket(){
			
		}
		
		@Override
		public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
			data = new NBTTagCompound();
			int size = PartyManagerServer.playerMana.size();
			data.setInteger("size", size);
			int index = 0;
			for(String player : PartyManagerServer.playerMana.keySet()){
				
				NBTTagCompound playerPartyInfo = new NBTTagCompound();
				playerPartyInfo.setString("name", player);
				playerPartyInfo.setIntArray("mana", 
						ArrayUtils.toPrimitive(PartyManagerServer.playerMana.get(player)));
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
			PartyManagerClient.manaClear();
			int size = data.getInteger("size");
			for(int i = 0; i < size; i++){
				NBTTagCompound playerData = data.getCompoundTag("" + i);
				String name = playerData.getString("name");
				Integer[] mana = ArrayUtils.toObject(playerData.getIntArray("mana"));
				PartyManagerClient.handlePlayerMana(name, mana);
				
				//String message = "partydata pak: " + name + " : " + party;
				//player.addChatMessage(new ChatComponentText(message));
			}
			
		}

		@Override
		public void handleServerSide(EntityPlayer player) {
			// TODO Auto-generated method stub
			
		}


}

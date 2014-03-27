package emd24.minecraftrpgmod;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.jcraft.jogg.Packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import emd24.minecraftrpgmod.party.PartyManagerClient;
import emd24.minecraftrpgmod.party.PartyManagerServer;
import emd24.minecraftrpgmod.skills.SkillPlayer;
import emd24.minecraftrpgmod.skills.SkillManagerServer;

/**
 * Class that handles creating and sending packets.
 * 
 * @author Evan Dyke
 *
 */
public class PacketHandlerServer implements IPacketHandler, IConnectionHandler {
	public static final int GET_ALL_PARTIES = 3;
	public static String PARTY_CHANNEL = "rpgmod";
	/**
	 * Creates a packet to be sent containing updated information on a single skill
	 * for a single player.
	 * 
	 * @param player String name of player to generate packet for
	 * @param skill String name of skill to generate packet for
	 * @return packet of information to send
	 */
	public static Packet250CustomPayload getSkillUpdatePacket(String player, String skill){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			dos.writeByte(0);
			dos.writeUTF(player);
			SkillPlayer temp = SkillManagerServer.getSkill(player, skill);
			dos.writeUTF(temp.name);
			dos.writeInt(temp.getLevel());
			dos.writeInt(temp.getExperience());
			dos.close();
			packet.channel = "rpgmod";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			packet.isChunkDataPacket = false;
			return packet;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Creates a packet to be sent containing updated information all the skills
	 * for a single player.
	 * 
	 * @param player String name of player to generate packet for
	 * @return packet of information to send
	 */
	public static Packet250CustomPayload getPlayerSkillUpdatePacket(String player){
		try{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		dos.writeByte(1);
		dos.writeUTF(player);
		HashMap<String, SkillPlayer> temp = SkillManagerServer.getPlayerSkillList(player);
		dos.writeInt(temp.size());
		for(SkillPlayer skill : temp.values()){
			dos.writeUTF(skill.name);
			dos.writeInt(skill.getLevel());
			dos.writeInt(skill.getExperience());
		}
		dos.close();
		packet.channel = "rpgmod";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = false;
		return packet;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  Creates a packet to be sent containing updated information all the skills
	 * for all the players.
	 * 
	 * @return packet to be sent
	 */
	public static Packet250CustomPayload getAllSkillsUpdatePacket(){
		try{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		dos.writeByte(2);
		dos.writeInt(SkillManagerServer.players.size());

		for(String player : SkillManagerServer.players.keySet()){
			dos.writeUTF(player);
			HashMap<String, SkillPlayer> skills = SkillManagerServer.getPlayerSkillList(player);
			dos.writeInt(skills.size());
			for(SkillPlayer skill : skills.values()){
				dos.writeUTF(skill.name);
				dos.writeInt(skill.getLevel());
				dos.writeInt(skill.getExperience());
			}
		}
		dos.close();
		packet.channel = "rpgmod";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = false;
		return packet;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  Creates a packet to be sent containing updated information all the skills
	 * for all the players.
	 * 
	 * @return packet to be sent
	 */
	public static Packet250CustomPayload getPartiesPacket(){
		try{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		dos.writeByte(GET_ALL_PARTIES);
		dos.writeInt(PartyManagerServer.playerParty.size());

		for(String player : PartyManagerServer.playerParty.keySet()){
			dos.writeUTF(player);
			int party = PartyManagerServer.playerParty.get(player);
			dos.writeInt(party);
		}
		dos.close();
		packet.channel = "rpgmod";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = false;
		return packet;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reads data from a packet about all parties
	 * 
	 * @param dat byte array to read from packet
	 */
	public static void handleInviteRequest(ByteArrayDataInput dat){
		int type = dat.readByte();
		String playerName = dat.readUTF();
		String invitingPlayer = dat.readUTF();
		switch(type) {
		//invite
		case 0:
			PartyManagerServer.addPlayerToPlayersParty(playerName, invitingPlayer);
			break;
		//leave/kick
		case 1:
			PartyManagerServer.removePlayerFromParty(playerName);
			break;
		//promote
		case 2:
			//TODO check invitingPlayer for leader status
			//promote player
			
			break;
		}
	}

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler,
			INetworkManager manager) {
		PacketDispatcher.sendPacketToPlayer(getAllSkillsUpdatePacket(), player);
		PacketDispatcher.sendPacketToAllPlayers(getPlayerSkillUpdatePacket(netHandler.getPlayer().username)); 
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionClosed(INetworkManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			INetworkManager manager, Packet1Login login) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		byte packetID = dat.readByte();
		
		if(packet.channel.equals("rpgmod")){
			// Check type of packet to decode
			switch(packetID){
			case 4:
				handleInviteRequest(dat);
				break;
			}
		}
		
	}

}

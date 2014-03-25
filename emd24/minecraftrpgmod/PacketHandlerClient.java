package emd24.minecraftrpgmod;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.minecraftrpgmod.skills.SkillManagerClient;
import emd24.minecraftrpgmod.skills.SkillPlayer;
import emd24.minecraftrpgmod.skills.SkillRegistry;

/**
 * Class that handles receiving packets and reading the information.
 * 
 * @author Evan Dyke
 *
 */
@SideOnly(Side.CLIENT)
public class PacketHandlerClient implements IPacketHandler{

	/**
	 * Method that is called when a packet is received
	 */
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		byte packetID = dat.readByte();
		if(packet.channel.equals(ExtendedPlayerData.CHANNEL)){
			handleExtendedPlayer(packet, player);
		}
		else if(packet.channel.equals("rpgmod")){
			// Check type of packet to decode
			switch(packetID){
			case 0:
				handleSkillUpdate(dat);
				break;
			case 1:
				handlePlayerSkillUpdate(dat);
				break;
			case 2:
				handleAllSkillUpdate(dat);
				break;
			}
		}

	}

	/**
	 * Reads data from a packet on a single skill for a single player
	 * 
	 * @param dat byte array to read from packet
	 */
	public void handleSkillUpdate(ByteArrayDataInput dat){
		SkillManagerClient.addSkillToPlayer(dat.readUTF(), new SkillPlayer(dat.readUTF(), dat.readInt(), dat.readInt()));
	}

	/**
	 * Reads data from a packet on all skills for a single player
	 * 
	 * @param dat byte array to read from packet
	 */
	public void handlePlayerSkillUpdate(ByteArrayDataInput dat){
		String player = dat.readUTF();
		int size = dat.readInt();

		// Reads each of the players skills into a hashmap
		HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>(); 
		for(int i = 0; i < size; i++){
			String name = dat.readUTF();
			skills.put(name, new SkillPlayer(name, dat.readInt(), dat.readInt()));
		}
		SkillManagerClient.addPlayerSkillList(player, skills);
	}

	/**
	 * Reads data from a packet all skills for all players
	 * 
	 * @param dat byte array to read from packet
	 */
	public void handleAllSkillUpdate(ByteArrayDataInput dat){
		int numPlayers = dat.readInt();
		for(int i = 0; i < numPlayers; i++){
			String player = dat.readUTF();
			int size = dat.readInt();

			// Reads each of the players skills into a hashmap
			HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>(); 
			for(int j = 0; j < size; j++){
				String name = dat.readUTF();
				skills.put(name, new SkillPlayer(name, dat.readInt(), dat.readInt()));
			}
			SkillManagerClient.addPlayerSkillList(player, skills);
		}
	}
	
	/**
	 * Receives a packet on player information and updates the client.
	 * 
	 * @param packet
	 * @param player
	 */
	public void handleExtendedPlayer(Packet250CustomPayload packet, Player player){
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		ExtendedPlayerData data = ExtendedPlayerData.get((EntityPlayer) player);

		try {
			data.setMaxMana(inputStream.readInt());
			data.setCurrMana(inputStream.readInt());
			
			// Read in the player's skills
			HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>();
			int size = inputStream.readInt();
			for(int i = 0; i < size; i++){
				String name = inputStream.readUTF();
				int level = inputStream.readInt();
				int experience = inputStream.readInt();
				skills.put(name, new SkillPlayer(name, level, experience));
			}
			data.setSkillList(skills);
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}
}

package emd24.minecraftrpgmod.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.network.packet.Packet;
import cpw.mods.fml.common.network.PacketDispatcher;
import emd24.minecraftrpgmod.PacketHandlerServer;

public class PartyManagerServer {
	// Maps player name to party ID
	public static HashMap<String, Integer> playerParty
		= new HashMap<String, Integer>();
	protected static int autoincrement = 1;
	
	/**
	 * Gets a list of player names in a specified player's party.
	 * 
	 * @param name String name of player
	 * @return ArrayList of player names in the party
	 */
	public static ArrayList<String> getPlayerParty(String name){
		ArrayList<String> partyPlayerList = new ArrayList<String>();
		for(String playerName : playerParty.keySet()){
			if(playerParty.get(playerName) == playerParty.get(name)){
				partyPlayerList.add(playerName);
			}
		}
		return partyPlayerList;
	}
	
	public static void sendPlayerParties() {
		Packet packet = PacketHandlerServer.getPartiesPacket();
		PacketDispatcher.sendPacketToAllPlayers(packet);
	}
	
	public static boolean addPlayerToParty(String playerName, int partyID) {
		if(!playerParty.containsKey(playerName) || playerParty.get(playerName) == 0) {
			//no party -> join
			playerParty.put(playerName, partyID);
			
			//Update clients with new party info
			sendPlayerParties();
			return true;
		}
		return false;
		
	}
	
	public static void addPlayerToPlayersParty(String playerName, String invitingPlayer) {
		//lookup inviting player's party
		int partyID = playerParty.get(invitingPlayer);
		if(partyID == 0) {
			partyID = autoincrement++;
			playerParty.put(invitingPlayer, partyID);
		}
		addPlayerToParty(playerName, partyID);
	}
	
	public static void removePlayerFromParty(String playerName){
		playerParty.put(playerName, 0);
		sendPlayerParties();
	}
	

	public static void removePlayerFromGame(String playerName){
		playerParty.remove(playerName);
		sendPlayerParties();
	}
	
	public static void promotePlayer(String playerName) {
		
	}
}

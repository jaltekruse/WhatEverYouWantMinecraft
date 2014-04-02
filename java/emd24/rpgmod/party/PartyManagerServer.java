package emd24.rpgmod.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import emd24.rpgmod.PacketHandlerServer;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.PartyDataPacket;
import emd24.rpgmod.packets.PlayerDataPacket;

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
	
	public static boolean addPlayerToParty(String playerName, int partyID) {
		if(!playerParty.containsKey(playerName) || playerParty.get(playerName) == 0) {
			//no party -> join
			playerParty.put(playerName, partyID);
			
			//Update clients with new party info
			RPGMod.packetPipeline.sendToAll(new PartyDataPacket());
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
	
	public static void removePlayerFromParty(String playerName, String kickingPlayer){
		playerParty.put(playerName, 0);
		RPGMod.packetPipeline.sendToAll(new PartyDataPacket());
		
		//TODO: add check for player leader before kicking
	}
	

	public static void removePlayerFromGame(String playerName){
		playerParty.remove(playerName);
		RPGMod.packetPipeline.sendToAll(new PartyDataPacket());
	}
	
	public static void promotePlayer(String playerName) {
		
	}
}

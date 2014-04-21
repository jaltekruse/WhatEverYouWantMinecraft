package emd24.rpgmod.party;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PartyManagerClient {

	//Primary data structure
	public static HashMap<String, Integer> playerParty
		= new HashMap<String, Integer>();
	
	public static void clear() {
		playerParty.clear();
	}
	
	public static void setPlayerParty(String player, int party) {
		playerParty.put(player, party);
	}
	
	public static ArrayList<String> getPlayerParty(String name){
		ArrayList<String> partyPlayerList = new ArrayList<String>();
		// If the player is not in a party, don't pass back everyone in the
		// general party by short circuiting the search.
		for(String playerName : playerParty.keySet()){
			/* Since the leader has a negative partyID, we must do an absolute  
			 * value equality check.
			 */
			if(Math.abs(playerParty.get(playerName)) == Math.abs(playerParty.get(name))){
				partyPlayerList.add(playerName);
			}
		}
		return partyPlayerList;
	}
}

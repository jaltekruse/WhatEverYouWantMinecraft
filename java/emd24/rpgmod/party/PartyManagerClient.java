package emd24.rpgmod.party;

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
}

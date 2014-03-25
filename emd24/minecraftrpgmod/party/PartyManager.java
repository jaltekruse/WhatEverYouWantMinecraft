package emd24.minecraftrpgmod.party;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class PartyManager {
	
	public static ArrayList<Party> partyList;
	
	public static void CreateParty(EntityPlayer player) {
		partyList.add(new Party(player));
	}
	
	public static int PartyOfPlayer(EntityPlayer player) {
		for(int i = 0; i < partyList.size(); i++) {
			if(partyList.get(i).contains(player)) {
				return i;
			}
		}
		return -1;
	}
}
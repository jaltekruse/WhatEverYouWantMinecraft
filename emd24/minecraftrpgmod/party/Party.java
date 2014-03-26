package emd24.minecraftrpgmod.party;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class Party {
	
	protected ArrayList<EntityPlayer> players;
	protected EntityPlayer leader;
	
	public Party(EntityPlayer player) {
		players.add(player);
		leader = player;
	}
	
	public void addPlayer(EntityPlayer player) {
		players.add(player);
	}
	
	public void kickPlayer(EntityPlayer player) {
		players.remove(player);
		//Remove party from world list if no players left
		if(players.size() <= 0)
			PartyManager.partyList.remove(this);
		if(player == leader)
			leader = players.get(0);
	}
	
	public void promotePlayer(EntityPlayer player) {
		this.leader = player;
	}
	
	public String[] names() {
		String[] playerNames = new String[players.size()];
		for(int i = 0; i < players.size(); i++) {
			playerNames[i] = players.get(i).username;
		}
		return playerNames;
	}
	
	public boolean contains(EntityPlayer player) {
		return players.contains(player);
	}
}
package emd24.rpgmod.party;

import java.util.ArrayList;
import java.util.List;

public class PartyPlayerNode implements Comparable{
	public String playerName;
	public int partyID;
	
	public PartyPlayerNode(){}
	
	public PartyPlayerNode(String playerName, int partyID){
		this.playerName = playerName;
		this.partyID = partyID;
	}

	@Override
	public int compareTo(Object other) {
		if (!(other instanceof PartyPlayerNode)){
			throw new ClassCastException();
		} else {
			PartyPlayerNode otherNode = (PartyPlayerNode) other;
			if (otherNode.partyID == this.partyID){
				return 0;
			} else {
				return 1;
			}
		}
	}
}

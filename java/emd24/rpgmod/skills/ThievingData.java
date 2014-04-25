package emd24.rpgmod.skills;

import java.util.HashMap;
import com.google.common.collect.HashMultimap;

import emd24.rpgmod.EntityIdMapping.EntityId;

/**
 * This class contains data for a type of mob for thieving.
 * 
 * @author Owner
 *
 */
public class ThievingData {
	
	private final static double EPSILON = 0.00001;
	
	private EntityId entityId;
	private int level;
	private int experience;
	private double probSuccess;
	private HashMap<Integer, LootEntry> loot;
	
	public ThievingData(EntityId entityId, int experience, int level, double probSuccess, HashMap<Integer, LootEntry> loot){
		this.entityId = entityId;
		this.level = level;
		this.probSuccess = probSuccess;
		this.loot = loot;
		this.experience = experience;
		validateLoot();
	}

	public EntityId getId() {
		return entityId;
	}

	public void setId(EntityId entityId) {
		this.entityId = entityId;
	}

	public int getLevelRequirement() {
		return level;
	}

	public void setLevelRequirement(int level) {
		this.level = level;
	}

	public double getProbSuccess() {
		return probSuccess;
	}

	public void setProbSuccess(double probSuccess) {
		this.probSuccess = probSuccess;
	}

	public HashMap<Integer, LootEntry> getLoot() {
		return loot;
	}

	public void setLootMap(HashMap<Integer, LootEntry> loot) {
		this.loot = loot;
		validateLoot();
	}
	
	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	private void validateLoot(){
		
		double totalProb = 0.0;
		for(LootEntry entry : loot.values()){
			totalProb += entry.prob;
		}
		// Inequality needed to compare double values
		if(totalProb + EPSILON < 1.0 || totalProb - EPSILON > 1.0){
			throw new IllegalArgumentException("Entity loot data must total a probability of 1.0 got " +
					totalProb + " instead");
		}
	}
	
	/**
	 * Class to represent data stored with each loot item, such as probability,
	 * highest, and lowest amount recieved
	 * 
	 * @author Owner
	 *
	 */
	public static class LootEntry{
		public double prob;
		public int lowest;
		public int highest;
		public LootEntry(double prob, int lowest, int highest) {
			this.prob = prob;
			this.lowest = lowest;
			this.highest = highest;
		}
		
	}
	
	
	
	
}

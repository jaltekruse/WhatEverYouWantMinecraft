package emd24.minecraftrpgmod.skills;

import java.util.HashMap;

import emd24.minecraftrpgmod.EntityIdMapping.EntityId;

/**
 * This class contains data for a type of mob for thieving.
 * 
 * @author Owner
 *
 */
public class ThievingData {
	
	private EntityId entityId;
	private int level;
	private int experience;
	private double probSuccess;
	private HashMap<Integer, Double> loot;
	
	public ThievingData(EntityId entityId, int experience, int level, double probSuccess, HashMap<Integer, Double> loot){
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

	public HashMap<Integer, Double> getLoot() {
		return loot;
	}

	public void setLootMap(HashMap<Integer, Double> loot) {
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
		
		double totalProb = 0.0D;
		for(Double prob : loot.values()){
			totalProb += prob;
		}
		if(totalProb != 1){
			throw new IllegalArgumentException("Entity loot data must total a probability of 1.0 got " +
					totalProb + " instead");
		}
	}
	
	
	
	
}

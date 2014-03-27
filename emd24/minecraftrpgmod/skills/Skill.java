package emd24.minecraftrpgmod.skills;

import java.util.HashMap;

/**
 * Abstract class that represents a skill, what items give experience, and
 * perks. 
 * 
 * @author Evan Dyke
 *
 */
public abstract class Skill {
	
	public String name;
	
	// Maps block ID experience given when broken 
	private HashMap<Integer, Integer> expGivenOnBreak;
	private HashMap<Integer, Integer> blockRequirements;
	
	/**
	 * Connstructor for a skill.
	 * 
	 * @param name String name of skill
	 */
	public Skill(String name){
		this.name = name;
		this.expGivenOnBreak = new HashMap<Integer, Integer>();
		this.blockRequirements = new HashMap<Integer, Integer>();
	}
	/**
	 * Adds to the skill a block that gives experience when destroyed.
	 * 
	 * @param blockId id of block
	 * @param expGiven experience given from breaking block
	 */
	public void addExperienceBlockBreak(int blockId, int expGiven){
		this.expGivenOnBreak.put(blockId, expGiven);
	}
	
	/**
	 * Gets experience for breaking a block. If no entry exists,
	 * no experience is given.
	 * 
	 * @param blockId id of block to check experience for
	 * @return experience given by breaking block
	 */
	public int getExpForBlockBreak(int blockId){
		if(!this.expGivenOnBreak.containsKey(blockId)){
			return 0;
		}
		return this.expGivenOnBreak.get(blockId);
	}
	
	/**
	 * Adds a skill level requirement for a block
	 * 
	 * @param blockId id of block to break
	 * @param level level required to break block
	 */
	public void addBlockRequirement(int blockId, int level){
		this.blockRequirements.put(blockId, level);
	}
	
	/**
	 * Gets the level requirement for a block
	 * 
	 * @param blockId id of block to get
	 * @return level required
	 */
	public int getBlockRequirement(int blockId){
		if(!this.blockRequirements.containsKey(blockId)){
			return 0;
		}
		return this.blockRequirements.get(blockId);
	}
	
	// TODO: add method for registering perks 
	public abstract void registerPerks();
	
}

package emd24.rpgmod.skills;

import java.io.Serializable;

import net.minecraft.world.World;

/**
 * This class represents information on a given skill that belongs to
 * a player.
 * 
 * @author Evan Dyke
 *
 */
public class SkillPlayer implements Serializable {
	
	public static final int MAX_LEVEL = 99;
	
	private int currentExp;
	private int level;
	public String name;
	
	/**
	 * Constructor for SkillPlayer with the name of the skill, starting
	 * at level 1 with 0 exp.
	 * 
	 * @param name String name of the skill
	 */
	public SkillPlayer(String name){
		this.currentExp = 0;
		this.level = 1;
		this.name = name;
	}
	
	/**
	 * Constructor for SkillPlayer with the name of the skill, level of
	 * skill, and experience level
	 * 
	 * @param name String name of skill
	 * @param level level of skill
	 * @param experience exp gained in skill
	 */
	public SkillPlayer(String name, int level, int experience){
		this.currentExp = experience;
		this.level = level;
		this.name = name;
	}
	/**
	 * Gets the current experience value
	 * 
	 * @return
	 */
	public int getExperience() {
		return this.currentExp;
	}
	
	/**
	 * Sets the current experience value. Also updates the level if needed
	 * 
	 * @param experience
	 */
	public void setExperience(int experience) {
		if(this.currentExp > expForLevel(experience)){
			experience = expForLevel(experience);
		}
		this.currentExp = experience;
		this.level = calculateLevel(this.currentExp);
	}
	
	/**
	 * Adds experience to the skill and indicates if the player levelled up.
	 * 
	 * @param experience amount of experience to add
	 * @return boolean whether player levelled up
	 */
	public boolean addExperience(int experience){
		int currLevel = this.level;
		this.currentExp += experience;
		if(this.currentExp > expForLevel(MAX_LEVEL)){
			this.currentExp = expForLevel(MAX_LEVEL);
		}
		this.level = calculateLevel(this.currentExp);
		return (this.level > currLevel);
	}
	
	/**
	 * Gets the level of the skill.
	 * 
	 * @return level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Sets the skill to a certain level, and updates experience
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = Math.max(level, MAX_LEVEL);
		this.currentExp = expForLevel(level);
	}
	
	public void addLevels(int levels){
		this.level = Math.max(this.level + levels, MAX_LEVEL);
		this.currentExp = expForLevel(level);
	}
	
	/**
	 * Calculates the experience needed for a skill to reach a given level
	 *  
	 * @param level level to check experience for
	 * @return experience needed to reach the next level
	 */
	public static int expForLevel(int level){
		return (int) (Math.pow(level - 1, 2) * 100);
	}
	
	/**
	 * Calculates level for a given amount of experience
	 * 
	 * @param experience of player
	 * @return level obtained from experience
	 */
	public static int calculateLevel(int experience){
		return (int) (Math.sqrt(experience / 100) + 1);
	}
}

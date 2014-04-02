package emd24.rpgmod.skills;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Class the contains a registry for all of the skills in the game.
 * 
 * @author Evan Dyke
 *
 */
public class SkillRegistry {
		
	private static HashMap <String, Skill> skills = new HashMap<String, Skill>();
	
	/**
	 * Gets a set of all the names of the skills in the registry.
	 * 
	 * @return Set of skill names
	 */
	public static Set<String> getSkillNames() {
		return skills.keySet();
	}
	
	/**
	 * Gets a collection of all the skills in the registry.
	 * 
	 * @return Collection of skills
	 */
	public static Collection<Skill> getSkills() {
		return skills.values();
	}
	
	/**
	 * Gets a skill from the registry.
	 * 
	 * @param skillName String name of skill to get.
	 * @return Skill from the registry.
	 */
	public static Skill getSkill(String skillName) {
		if(skills.containsKey(skillName)) {
			return skills.get(skillName);
		}
		return null;
	}
	
	/**
	 * Checks to see if a skill exists in the registry.
	 * 
	 * @param skillName String name of skill to check
	 * @return boolean whether skill exists
	 */
	public static boolean hasSkill(String skillName) {
		return skills.containsKey(skillName);
	}
	
	/**
	 * Adds a skill to the registry.
	 * 
	 * @param skill Skill to add
	 */
	public static void registerSkill(Skill skill) {
		if(skills.containsKey(skill.name)) {
			// TODO: code to handle error
			
		}
		skills.put(skill.name, skill);
	}

	/**
	 * Removes a skill form the registry.
	 * 
	 * @param skillName name of skill to remove
	 */
	public static void unregisterSkill(String skillName) {
		if(skills.containsKey(skillName)) {
			skills.remove(skillName);
		}
	}
	
}

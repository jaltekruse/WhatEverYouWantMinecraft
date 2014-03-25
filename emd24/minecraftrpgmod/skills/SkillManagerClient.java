package emd24.minecraftrpgmod.skills;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class that represents the skill manager on the client side. Keeps track of all
 * the players and their skills.
 * 
 * @author Evan Dyke
 *
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class SkillManagerClient {
	public static HashMap<String, HashMap<String, SkillPlayer>> players = 
			new HashMap<String, HashMap<String, SkillPlayer>>();

	/**
	 * Gets a HashMap containing the a given player's skills. If the player does not exist, a
	 * new entry is created.
	 * 
	 * @param player player to get skills from
	 * @return hashmap of the player's skills
	 */
	public static HashMap<String, SkillPlayer> getPlayerSkillList(String player) {
		if(!players.containsKey(player)) {
			HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>();
			
			for(String skillName : SkillRegistry.getSkillNames()) {
				skills.put(skillName, new SkillPlayer(skillName));
			}
			
			players.put(player, skills);
			
		}
		return players.get(player);
	}
	
	/**
	 * Gets a skill for a given player.
	 * 
	 * @param player player to  get skill for
	 * @param skill which skill to return
	 * @return
	 */
	public static SkillPlayer getSkill(String player, String skill){
		HashMap<String, SkillPlayer> skills = players.get(player);
		if(!skills.containsKey(skill)) {
			addSkillToPlayer(player, new SkillPlayer(skill));
		}
		return skills.get(skill);
	}
	
	/**
	 * Adds a player to the server with a list of his or her skills.
	 * 
	 * @param player name of the player to add
	 * @param skillList HashMap of the player's skills
	 */
	public static void addPlayerSkillList(String player, HashMap<String, SkillPlayer> skillList){
		players.put(player, skillList);
	}
	
	/**
	 * Adds a skill to a given player on the server
	 * 
	 * @param player player to update
	 * @param skill skill to add for the player
	 */
	public static void addSkillToPlayer(String player, SkillPlayer skill){
		getPlayerSkillList(player).put(skill.name, skill);
	}
	
	/**
	 * Gives a player experience in a given skill
	 * 
	 * @param player player to give exp to
	 * @param skill skill to give exp in
	 * @param amount amount of exp to give
	 */
	public static void addExp(String player, String skill, int amount){
		getSkill(player, skill).addExperience(amount);
	}
	
	/**
	 * Gives a player additional levels
	 * 
	 * @param player player to give levels to
	 * @param skill skill to give levels in
	 * @param levels number of levels to add
	 */
	public static void addLevels(String player, String skill, int levels){
		getSkill(player, skill).addLevels(levels);
	}
}

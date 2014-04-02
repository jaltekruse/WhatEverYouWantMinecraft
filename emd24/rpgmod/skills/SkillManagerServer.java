package emd24.rpgmod.skills;

import java.util.HashMap;

import emd24.rpgmod.PacketHandlerServer;

/**
 * 
 * 
 * Class that represents the skill manager on the server side. Keeps track of all
 * the players and their skills.
 * 
 * 
 * @author Evan Dyke
 *
 */

@Deprecated
public class SkillManagerServer {

	public static HashMap<String, HashMap<String, SkillPlayer>> players = 
			new HashMap<String, HashMap<String, SkillPlayer>>();

	/**
	 * Gets a HashMap of a player's skills. If the player does not exist, a
	 * new entry is created.
	 * 
	 * @param player player to get skills from
	 * @return hashmap of the player's skills
	 */
	public static HashMap<String, SkillPlayer> getPlayerSkillList(String player) {
		if(!players.containsKey(player)) {
			HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>();
			
			// add the list of skills from the skill registry 
			for(String skillName : SkillRegistry.getSkillNames()) {
				skills.put(skillName, new SkillPlayer(skillName));
			}
			
			players.put(player, skills);
			
			// Notify everyone else on the server
//			PacketDispatcher.sendPacketToAllPlayers(PacketHandlerServer.getPlayerSkillUpdatePacket(player));
			
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
		
		// Notify everone else on the server
//		PacketDispatcher.sendPacketToAllPlayers(PacketHandlerServer.getPlayerSkillUpdatePacket(player));
	}
	
	/**
	 * Adds a skill to a player.
	 * 
	 * @param player player to update
	 * @param skill skill to add for the player
	 */
	public static void addSkillToPlayer(String player, SkillPlayer skill){
		getPlayerSkillList(player).put(skill.name, skill);
//		PacketDispatcher.sendPacketToAllPlayers(PacketHandlerServer.getSkillUpdatePacket(player, skill.name));
	}
	
	/**
	 * Give experience to a player in a skill.
	 * 
	 * @param player String of player name to give experience to
	 * @param skill String of skill name to give experience to
	 * @param amount in amount of experience to give
	 * @return boolean whether player leveled up
	 */
	public static boolean addExp(String player, String skill, int amount){
		boolean levelUp = getSkill(player, skill).addExperience(amount);
//		PacketDispatcher.sendPacketToAllPlayers(PacketHandlerServer.getSkillUpdatePacket(player, skill));
		return levelUp;
	}
	
	/**
	 * Adds levels to a player
	 * 
	 * @param player String of player name to give experience to
	 * @param skill String of skill name to give experience to
	 * @param amount in amount of levels to give
	 */
	public static void addLevels(String player, String skill, int levels){
		getSkill(player, skill).addLevels(levels);
	//	PacketDispatcher.sendPacketToAllPlayers(PacketHandlerServer.getSkillUpdatePacket(player, skill));
	}
}
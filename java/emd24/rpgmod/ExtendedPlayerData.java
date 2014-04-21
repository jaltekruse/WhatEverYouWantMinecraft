package emd24.rpgmod;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import emd24.rpgmod.packets.PlayerDataPacket;
import emd24.rpgmod.skills.SkillPlayer;
import emd24.rpgmod.skills.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Class that holds external data to be saved with the Player
 * 
 * @author Evan Dyke
 *
 */
public class ExtendedPlayerData implements IExtendedEntityProperties{

	public static final String IDENTIFIER = "ExtendedPlayer";
	public static final String CHANNEL = "extplayerdata";
	private final EntityPlayer player;

	// Sets whether player is undead
	private boolean undead;

	// regenRate is # of ticks to restore mana
	private int currMana, maxMana, regenRate; 
	public int manaTicks;
	private HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>();

	public ExtendedPlayerData(EntityPlayer player){
		this.player = player;
		this.maxMana = 50;
		this.currMana = this.maxMana;
		this.regenRate = 50;
		this.manaTicks = this.regenRate;

		// Add the skills to the player
		for(String skillName: SkillRegistry.getSkillNames()){
			skills.put(skillName, new SkillPlayer(skillName));
		}
	}

	public final static void register(EntityPlayer player){
		player.registerExtendedProperties(ExtendedPlayerData.IDENTIFIER, new ExtendedPlayerData(player));
	}

	public static ExtendedPlayerData get(EntityPlayer player){
		return (ExtendedPlayerData)player.getExtendedProperties(IDENTIFIER);
	}

	@Override
	public void init(Entity entity, World world) {

	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		NBTTagCompound rbt = new NBTTagCompound();
		
		rbt.setBoolean("undead", this.undead);
		rbt.setInteger("maxMana", this.maxMana);
		rbt.setInteger("currMana", this.currMana);
		rbt.setInteger("manaRegenRate", this.regenRate);

		for(SkillPlayer skill : skills.values()){
			rbt.setInteger( skill.name  + ".lvl", skill.getLevel());
			rbt.setInteger( skill.name  + ".exp", skill.getExperience());

		}

		compound.setTag(IDENTIFIER, rbt);
	}


	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		NBTTagCompound rbt = compound.getCompoundTag(IDENTIFIER);
		this.undead = rbt.getBoolean("undead");
		this.maxMana = rbt.getInteger("maxMana");
		this.currMana = rbt.getInteger("currMana");
		this.regenRate = rbt.getInteger("manaRegenRate");

		// Load information on skills
		for(String skillName: SkillRegistry.getSkillNames()){
			int level = rbt.getInteger(skillName + ".lvl");
			int experience = rbt.getInteger(skillName + ".exp");
			skills.put(skillName, new SkillPlayer(skillName, level, experience));
		}
	}

	public boolean isUndead() {
		return undead;
	}

	public void setUndead(boolean undead) {
		this.undead = undead;
		sync();
	}

	public int getCurrMana() {
		return currMana;
	}

	public void setCurrMana(int currMana) {
		this.currMana = currMana;
		sync();
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void useMana(int mana){
		this.currMana -= mana;
		sync();
	}

	public void recoverMana(int mana){
		this.currMana = Math.min(currMana + mana, maxMana);
		sync();
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
		sync();
	}

	public boolean isFullMana(){
		return (currMana == maxMana);
	}

	public HashMap<String, SkillPlayer> getSkillList(){
		return skills;
	}

	/**
	 * Gets a skill for the player.
	 * 
	 * @param skill which skill to return
	 * @return
	 */
	public SkillPlayer getSkill(String name){
		if(!skills.containsKey(name)) {
			skills.put(name, new SkillPlayer(name));
		}
		return skills.get(name);
	}

	public void setSkillList(HashMap<String, SkillPlayer> skills){
		this.skills = skills;
		sync();
	}

	/**
	 * Adds a skill to a player.
	 * 
	 * @param skill skill to add for the player
	 */
	public void addSkillToPlayer(SkillPlayer skill){
		skills.put(skill.name, skill);
		sync();
	}

	/**
	 * Give experience to a player in a skill.
	 * 
	 * @param skill String of skill name to give experience to
	 * @param amount in amount of experience to give
	 * @return boolean whether player leveled up
	 */
	public void addExp(String skill, int amount){

		boolean levelUp = skills.get(skill).addExperience(amount);

		// Notify player of exp gain and leveling up
		if(!this.player.worldObj.isRemote)
			this.player.addChatMessage(new ChatComponentText("Gained " + amount 
					+ " " + skill + " exp"));
		if(levelUp){
			// If magic skill
			if(skill == "Magic"){
				this.maxMana = 50 + 10 * (skills.get(skill).getLevel() - 1);
			}
			if(!this.player.worldObj.isRemote)
				this.player.addChatMessage((new ChatComponentText("Congrats! Reached " + skill + " Level " 
						+ skills.get(skill).getLevel()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))));
		}
		sync();
	}

	/**
	 * Adds levels to a player's skill
	 * 
	 * @param skill String of skill name to give experience to
	 * @param amount in amount of levels to give
	 */
	public void addLevels(String skill, int levels){
		skills.get(skill).addLevels(levels);
		if(skill == "Magic"){
			this.currMana = 50 + (10 * skills.get(skill).getLevel() - 1);
		}
		sync();

	}

	public int getRegenRate() {
		return regenRate;
	}

	public void setRegenRate(int regenRate) {
		this.regenRate = regenRate;
	}

	public void sync(){
		if(!player.worldObj.isRemote){
			RPGMod.packetPipeline.sendToAll(new PlayerDataPacket(player));
		}
	}
}

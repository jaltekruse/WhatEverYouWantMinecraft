package emd24.minecraftrpgmod;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import emd24.minecraftrpgmod.skills.SkillPlayer;
import emd24.minecraftrpgmod.skills.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
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
	public boolean dirty = true;
	private final EntityPlayer player;
	
	// Sets whether player is undead
	private boolean undead;
	private int currMana, maxMana;
	private HashMap<String, SkillPlayer> skills = new HashMap<String, SkillPlayer>();;
	
	public ExtendedPlayerData(EntityPlayer player){
		this.player = player;
		this.maxMana = 50;
		this.currMana = this.maxMana;
		
		// Add the skills to the player
		for(String skillName: SkillRegistry.getSkillNames()){
			skills.put(skillName, new SkillPlayer(skillName));
		}
		
		// TODO: sync data
	}

	public final static void register(EntityPlayer player){
		player.registerExtendedProperties(ExtendedPlayerData.IDENTIFIER, 
				new ExtendedPlayerData(player));
	}
	
	public static ExtendedPlayerData get(Entity player){
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
		
		for(SkillPlayer skill : skills.values()){
			rbt.setInteger( skill.name  + ".lvl", skill.getLevel());
			rbt.setInteger( skill.name  + ".exp", skill.getExperience());
			
		}
		
		compound.setCompoundTag(IDENTIFIER, rbt);
	}


	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		NBTTagCompound rbt = compound.getCompoundTag(IDENTIFIER);
		this.undead = rbt.getBoolean("undead");
		this.maxMana = rbt.getInteger("maxMana");
		this.currMana = rbt.getInteger("currMana");
		
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

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
		sync();
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
		
		//PacketDispatcher.sendPacketToAllPlayers(PacketHandler.getSkillUpdatePacket(player, skill.name));
	}
	
	/**
	 * Give experience to a player in a skill.
	 * 
	 * @param skill String of skill name to give experience to
	 * @param amount in amount of experience to give
	 * @return boolean whether player leveled up
	 */
	public boolean addExp(String skill, int amount){
		boolean levelUp = skills.get(skill).addExperience(amount);
		//PacketDispatcher.sendPacketToAllPlayers(PacketHandler.getSkillUpdatePacket(player, skill));
		sync();
		return levelUp;
	}
	
	/**
	 * Adds levels to a player's skill
	 * 
	 * @param skill String of skill name to give experience to
	 * @param amount in amount of levels to give
	 */
	public void addLevels(String skill, int levels){
		skills.get(skill).addLevels(levels);
		//PacketDispatcher.sendPacketToAllPlayers(PacketHandler.getSkillUpdatePacket(player, skill));
		sync();
	}
	
	/**
	 * Sends data to client about extended payer data stored on server.
	 */
	public final void sync(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try{
			outputStream.writeInt(this.maxMana);
			outputStream.writeInt(this.currMana);
			
			outputStream.writeInt(this.skills.size());
			for(SkillPlayer skill : skills.values()){
				outputStream.writeUTF(skill.name);
				outputStream.writeInt(skill.getLevel());
				outputStream.writeInt(skill.getExperience());
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload(CHANNEL, bos.toByteArray());
		
		if (!player.worldObj.isRemote) {
			EntityPlayerMP player1 = (EntityPlayerMP) player;
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player1);
			}
	}
	
}

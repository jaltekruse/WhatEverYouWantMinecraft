package emd24.rpgmod.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Abstract class that represents a skill, what items give experience, and
 * perks. Responsible for registering things such as item or block level
 * requirements.
 * 
 * @author Evan Dyke
 *
 */
public class Skill {
	
	public String name;
	
	// Maps block ID experience given when broken 
	private HashMap<Integer, Integer> expGivenOnBreak;
	private HashMap<Integer, Integer> blockRequirements;
	private HashMap<Integer, ArrayList<HarvestPerkEntry>> harvestPerks;
	private HashMap<Integer, Integer> itemUseRequirements;
	
	/**
	 * Connstructor for a skill.
	 * 
	 * @param name String name of skill
	 */
	public Skill(String name){
		this.name = name;
		this.expGivenOnBreak = new HashMap<Integer, Integer>();
		this.blockRequirements = new HashMap<Integer, Integer>();
		this.harvestPerks = new HashMap<Integer, ArrayList<HarvestPerkEntry>>();
		this.itemUseRequirements = new HashMap<Integer, Integer>();
	}
	/**
	 * Adds to the skill a block that gives experience when destroyed.
	 * 
	 * @param blockId id of block
	 * @param expGiven experience given from breaking block
	 */
	public void addExperienceBlockBreak(Block block, int expGiven){
		this.expGivenOnBreak.put(Block.getIdFromBlock(block), expGiven);
	}
	
	/**
	 * Gets experience for breaking a block. If no entry exists,
	 * no experience is given.
	 * 
	 * @param blockId id of block to check experience for
	 * @return experience given by breaking block
	 */
	public int getExpForBlockBreak(Block block){
		if(!this.expGivenOnBreak.containsKey(Block.getIdFromBlock(block))){
			return 0;
		}
		return this.expGivenOnBreak.get(Block.getIdFromBlock(block));
	}
	
	/**
	 * Adds a skill level requirement for a block
	 * 
	 * @param blockId id of block to break
	 * @param level level required to break block
	 */
	public void addBlockRequirement(Block block, int level){
		this.blockRequirements.put(Block.getIdFromBlock(block), level);
	}
	
	/**
	 * Gets the level requirement for a block
	 * 
	 * @param blockId id of block to get
	 * @return level required
	 */
	public int getBlockRequirement(Block block){
		if(!this.blockRequirements.containsKey(Block.getIdFromBlock(block))){
			return 0;
		}
		return this.blockRequirements.get(Block.getIdFromBlock(block));
	}
	
	/**
	 * Method that registers a HarvestPerk, where an extra block is mined 
	 * with a given probability.
	 * 
	 * @param blockId id of block to add
	 * @param level level required for perk
	 * @param prob probability of perk
	 */
	public void addHarvestPerkBlock(Block block, int level, double prob){
		if(!harvestPerks.containsKey(Block.getIdFromBlock(block))){
			harvestPerks.put(Block.getIdFromBlock(block), new ArrayList<HarvestPerkEntry>());
		}
		harvestPerks.get(Block.getIdFromBlock(block)).add(new HarvestPerkEntry(level, prob));
	}
	
	/**
	 * Method that requires an item to have a level requirement upon using
	 * (as in right-clicking).
	 * 
	 * @param item item to add requirement
	 * @param level level needed to use item
	 */
	public void addItemUseRequirement(Item item, int level){
		if(!itemUseRequirements.containsKey(Item.getIdFromItem(item))){
			itemUseRequirements.put(Item.getIdFromItem(item), level);
		}
	}
	
	/**
	 * Method that gets the level requirement for using an item
	 * 
	 * @param item item to check requirement for
	 * @return level requirement for item
	 */
	public int getItemUseRequirement(Item item){
		if(!this.itemUseRequirements.containsKey(Item.getIdFromItem(item))){
			return 0;
		}
		return this.itemUseRequirements.get(Item.getIdFromItem(item));
	}
	
	/**
	 * Gets the probability of harvesting an extra block based on the user's
	 * level.
	 * 
	 * @param blockId id of block to check
	 * @param level level of player
	 * @return probability of getting extra drop
	 */
	public double getHarvestPerkProbability(Block block, int level){
		if(!harvestPerks.containsKey(Block.getIdFromBlock(block))){
			return 0.0;
		}
		HarvestPerkEntry temp = null;
		for(HarvestPerkEntry entry : harvestPerks.get(Block.getIdFromBlock(block))){
			
			// Adds a higher perk level is found
			if(entry.level <= level && (temp == null || entry.level > temp.level)){
				temp = entry;
			}
		}
		if(temp == null){
			return 0.0;
		}
		return temp.prob;
		
	}
	
	/**
	 * Entry for an extra harvest perk. contains level required
	 * and probability
	 * 
	 * @author Owner
	 *
	 */
	private class HarvestPerkEntry{
		public int level;
		public double prob;
		public HarvestPerkEntry(int level, double prob){
			this.level = level;
			this.prob = prob;
		}
	}
}

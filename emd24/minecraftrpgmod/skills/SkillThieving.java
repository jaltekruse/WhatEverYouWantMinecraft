package emd24.minecraftrpgmod.skills;

import java.util.HashMap;
import java.util.Random;

import emd24.minecraftrpgmod.EntityIdMapping.EntityId;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Class that represents the thieving skill.
 * 
 * @author Evan Dyke
 *
 */
public class SkillThieving extends Skill{

	public static final int VILLAGER_ID = 1;
	
	private HashMap<EntityId, ThievingData> data;

	/**
	 * Constructor for thieving skill
	 * 
	 * @param name 
	 */
	public SkillThieving(String name) {
		super(name);
		data = new HashMap<EntityId, ThievingData>();
	}

	/**
	 * Checks to see if a player can steal from an entity.
	 * 
	 * @param id EntityId to check
	 * @return boolean whether player can steal from entity
	 */
	public boolean isThievable(EntityId id){
		return data.containsKey(id);
	}

	public void addThievingData(ThievingData data){
		this.data.put(data.getId(), data);
	}

	/**
	 * Gets level requirement to steal from entity.
	 * 
	 * @param id EntityId to check
	 * @return level requirement to steal
	 */
	public int getLevelRequirement(EntityId id){
		if(!data.containsKey(id)){
			return 0;
		}
		return data.get(id).getLevelRequirement();
	}

	/**
	 * Gets experience for stealing from entity.
	 * 
	 * @param id EntityId to check
	 * @return experience given
	 */
	public int getExperience(EntityId id){
		if(!data.containsKey(id)){
			return 0;
		}
		return data.get(id).getExperience();
	}

	/**
	 * Attempts to steal from the NPC by randomly selecting loot. 
	 * 
	 * 
	 * @param id entity id of NPC to steal from
	 * @param alertLevel level of alertness of target
	 * int playerLevel thieving level of the player
	 * @return ItemStack containing loot. Returns null if attempt was unsuccessful.
	 */
	public ItemStack getLoot(EntityId id, int alertLevel, int playerLevel){

		
		
		// Check to see if thieving attempt successful
		Random rnd = new Random();
		if(rnd.nextDouble() <= Math.min(data.get(id).getProbSuccess() * (1 + (playerLevel - 1.0) / 100.0) / Math.pow(2, alertLevel), 1.0)){
			
			
			/* Randomly generates loot. This is done by generating a random
			 * number that is uniformly distributed on [0.0, 1.0). It then iterates
			 * through the list and finds the cumulative probabilities. When the cumulative
			 * probability exceeds the randomly generated number, a match is found for the
			 * item id.
			 * 
			 */
			double value = rnd.nextDouble();
			double cumProb = 0.0D;
			for(int i : data.get(id).getLoot().keySet()){
				cumProb += data.get(id).getLoot().get(i);
				if(value < cumProb){
					return new ItemStack(Item.getItemById(i), 1, 0);
				}
			}
		}
		return null;
	}

}

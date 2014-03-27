package emd24.minecraftrpgmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

/**
 * This class is used to contain mappings for each creature type. It has a
 * unique identifier for each monster which enables getting external data
 * for a type of mob.
 * 
 * @author Evan Dyke
 *
 */
public class EntityIdMapping {
	public enum EntityId{
		INVALID, VILLAGER
	}
	/**
	 * Gets the EntityId that is stored for a given entity.
	 * 
	 * @param ent entity to lookup
	 * @return EntityId
	 */
	public static EntityId getEntityId(Entity ent){
		if(ent instanceof EntityVillager){
			return EntityId.VILLAGER;
		}
		return EntityId.INVALID;
		
	}
}

package emd24.rpgmod;

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
		INVALID, FARMER,  LIBRARIAN, PRIEST, BLACKSMITH, BUTCHER
	}
	/**
	 * Gets the EntityId that is stored for a given entity.
	 * 
	 * @param ent entity to lookup
	 * @return EntityId
	 */
	public static EntityId getEntityId(Entity ent){
		if(ent instanceof EntityVillager){
			EntityVillager v = (EntityVillager) ent;
			// Assign a different ID depending on profession
			switch(v.getProfession()){
				case 0:
					return EntityId.FARMER;
				case 1: 
					return EntityId.LIBRARIAN;
				case 2:
					return EntityId.PRIEST;
				case 3:
					return EntityId.BLACKSMITH;
				case 4:
					return EntityId.BUTCHER;
			}
						
		}
		return EntityId.INVALID;
		
	}
}

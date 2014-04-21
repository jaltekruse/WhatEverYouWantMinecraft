package emd24.rpgmod.spells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import emd24.rpgmod.skills.Skill;

public class SpellRegistry {
	
	private static HashMap<String, Spell> spells = new HashMap<String, Spell>();
	
	/**
	 * Gets an ArrayList of all the spells in the registry.
	 * 
	 * @return ArrayList of spells
	 */
	public static Collection<Spell> getSpells() {
		return spells.values();
	}
	
	/**
	 * Gets a spell from the registry.
	 * 
	 * @param spellName String name of skill to get.
	 * @return Spell from the registry.
	 */
	public static Spell getSpell(String spellName) {
		if(spells.containsKey(spellName)) {
			return spells.get(spellName);
		}
		return null;
	}
	
	/**
	 * Checks to see if a spell exists in the registry.
	 * 
	 * @param spellName String name of spell to check
	 * @return boolean whether spell exists
	 */
	public static boolean hasSpell(String spellName) {
		return spells.containsKey(spellName);
	}
	
	/**
	 * Adds a spell to the registry.
	 * 
	 * @param spell Spell to add
	 */
	public static void registerSpell(Spell spell) {
		if(spells.containsKey(spell.getUnlocalizedName())) {
			// TODO: code to handle error
			
		}
		spells.put(spell.getUnlocalizedName(), spell);
		GameRegistry.registerItem(spell, spell.getUnlocalizedName());
	}

	/**
	 * Removes a spell form the registry.
	 * 
	 * @param spellName name of skill to remove
	 */
	public static void unregisterSpell(String spellName) {
		if(spells.containsKey(spellName)) {
			spells.remove(spellName);
		}
	}
}

package emd24.rpgmod.combatitems;

import java.util.ArrayList;

import scala.reflect.internal.Trees.Super;

import com.google.common.collect.Multimap;

import net.minecraft.item.ItemSword;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class ItemBattleaxe extends ItemSword{

	
	public ItemBattleaxe(ToolMaterial material){
		super(material);
		// superclass has this variable private, so we store it reduntantly here
		
	}
	
	/**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
	@Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        
        // func_150931_i is responsible for getting item damage based on tool material        
        double damage = 6.0F + (double) this.func_150931_i();
        
        // Replace item attribute in the multimap
        if(multimap.containsKey(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())){
        	multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        	multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", damage, 0));
        }
        
        return multimap;
    }
	
}

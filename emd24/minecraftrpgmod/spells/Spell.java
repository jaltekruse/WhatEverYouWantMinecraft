package emd24.minecraftrpgmod.spells;

import emd24.minecraftrpgmod.ExtendedPlayerData;
import emd24.minecraftrpgmod.spells.entities.MagicLightning;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

/**
 * This class is used to represent a generic spell. It contains common
 * information that each spell keeps track of, and maintains basic functionality for
 * a spell (such as determining damage and using mana). A spell functions like an item
 * and only has one use.
 *
 * <p>Bugs: (a list of bugs and other problems)
 *
 * @author Evan Dyke
 */

public abstract class Spell extends Item {

	public enum DamageType{
		WIND, WATER, EARTH, FIRE, HOLY, POS_ENERGY, NEG_ENERGY  
	}
	private DamageType spellType;
	private int basePower;
	private int manaCost = 0; 
	private int cooldown;

	/**
	 * Creates a spell object.
	 * 
	 * @param id item id of the spell
	 * @param tab creative mode tab to place item
	 */
	public Spell(int id, int basePower, CreativeTabs tab) {
		super(id); //Returns super constructor: par1 is ID
		setMaxStackSize(1);
		setCreativeTab(tab); //Tells the game what creative mode tab it goes in
		this.basePower = basePower;

	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (par7 == 0)
		{
			--par5;
		}

		if (par7 == 1)
		{
			++par5;
		}

		if (par7 == 2)
		{
			--par6;
		}

		if (par7 == 3)
		{
			++par6;
		}

		if (par7 == 4)
		{
			--par4;
		}

		if (par7 == 5)
		{
			++par4;
		}
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		else{
			// Check to see if player has enough mana
			ExtendedPlayerData properties = ExtendedPlayerData.get(par2EntityPlayer);
			if(properties.getCurrMana() < this.manaCost){
				if(!par3World.isRemote)
					par2EntityPlayer.sendChatToPlayer((new ChatMessageComponent()).addText("Not enough mana!"));
			}

			else{
				castSpell(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
				properties.useMana(this.manaCost);
				if (!par2EntityPlayer.capabilities.isCreativeMode)
				{
					--par1ItemStack.stackSize;
				}
				if(!par3World.isRemote){
					par2EntityPlayer.sendChatToPlayer((new ChatMessageComponent()).addText("Mana remaining: " + properties.getCurrMana()));
				}
			}  		

		}
		return true;
	}

	public void castSpell(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		System.out.println("FAIL");
	}

	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
	{
		item.damageItem(getMaxDamage(), player);
		return true;
	}

	// Getters and Setters

	public DamageType getSpellType() {
		return spellType;
	}

	public void setSpellType(DamageType spellType) {
		this.spellType = spellType;
	}

	public int getBasePower() {
		return basePower;
	}

	public Spell setBasePower(int basePower) {
		this.basePower = basePower;
		return this;
	}

	public int getManaCost() {
		return manaCost;
	}

	public Spell setManaCost(int manaCost) {
		this.manaCost = manaCost;
		return this;
	}

	public int getCooldown() {
		return cooldown;
	}

	public Spell setCooldown(int cooldown) {
		this.cooldown = cooldown;
		return this;
	}

	/**
	 * Overrides method to make spell item disappear on dropping
	 * 
	 */

}

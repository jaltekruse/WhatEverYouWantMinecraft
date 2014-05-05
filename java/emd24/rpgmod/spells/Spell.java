package emd24.rpgmod.spells;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
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

public abstract class Spell extends Item implements Comparable {

	public enum DamageType{
		WIND, WATER, EARTH, FIRE, HOLY, POS_ENERGY, NEG_ENERGY  
	}

	private DamageType spellType;
	private int basePower;

	private int manaCost = 0; 
	private int levelRequired = 1;	
	private int cooldown;
	private int experience = 0;


	public boolean onItemUse;
	public boolean onItemRightClick;

	/**
	 * Creates a spell object.
	 * 
	 * @param id item id of the spell
	 * @param tab creative mode tab to place item
	 */
	public Spell(int basePower, CreativeTabs tab) {
		super(); //Returns super constructor: par1 is ID
		setMaxStackSize(1);
		setCreativeTab(tab); //Tells the game what creative mode tab it goes in
		this.basePower = basePower;

	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if(!this.onItemUse){
			return false;
		}
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
			ExtendedPlayerData properties = (ExtendedPlayerData) par2EntityPlayer.getExtendedProperties(ExtendedPlayerData.IDENTIFIER);
			if(properties.getCurrMana() < this.manaCost){
				if(!par3World.isRemote)
					par2EntityPlayer.addChatMessage(new ChatComponentText("Not enough mana!"));
			}
			else if(this.getLevelRequired() > properties.getSkill("Magic").getLevel()){
				if(!par3World.isRemote)
					par2EntityPlayer.addChatMessage((new ChatComponentText("Magic Level " + this.getLevelRequired() + 
							" required").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))));
			}
			else{
				if(castSpell(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7)){
					properties.useMana(this.manaCost);
					properties.addExp("Magic", this.getExperience());
					if (!par2EntityPlayer.capabilities.isCreativeMode)
					{
						--par1ItemStack.stackSize;
					}
					if(!par3World.isRemote){
						par2EntityPlayer.addChatMessage(new ChatComponentText("Mana remaining: " + properties.getCurrMana()));
					}
				}
			}  		

		}
		return true;
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{

		if(!this.onItemRightClick){
			return par1ItemStack;
		}

		ExtendedPlayerData properties = ExtendedPlayerData.get(par3EntityPlayer);
		if(properties.getCurrMana() < this.manaCost){
			if(!par2World.isRemote)
				par3EntityPlayer.addChatMessage(new ChatComponentText("Not enough mana!"));
		}
		else if(this.getLevelRequired() > properties.getSkill("Magic").getLevel()){
			if(!par2World.isRemote)
				par3EntityPlayer.addChatMessage((new ChatComponentText("Magic Level " + this.getLevelRequired() + 
						" required").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))));
		}
		else{
			if(castSpell(par1ItemStack, par2World, par3EntityPlayer)){
				properties.useMana(this.manaCost);
				properties.addExp("Magic", this.getExperience());
				if (!par3EntityPlayer.capabilities.isCreativeMode)
				{
					--par1ItemStack.stackSize;
				}
				if(!par2World.isRemote){
					par3EntityPlayer.addChatMessage(new ChatComponentText("Mana remaining: " + properties.getCurrMana()));
				}
			}
		}
		return par1ItemStack;
	}

	/**
	 * Spell that is activated on player right clicking on an entity
	 * 
	 * @param par1ItemStack
	 * @param par2World
	 * @param par3EntityPlayer
	 */
	public boolean castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Spell that is activated on player using item on block
	 * 
	 * @param par1ItemStack
	 * @param par2EntityPlayer
	 * @param par3World
	 * @param par4
	 * @param par5
	 * @param par6
	 * @param par7
	 * @param par8
	 * @param par9
	 * @param par10
	 */

	public boolean castSpell(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side){
		System.out.println("FAIL");
		return true;
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

	public Spell setSpellType(DamageType spellType) {
		this.spellType = spellType;
		return this;
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

	public int getLevelRequired() {
		return levelRequired;
	}

	public Spell setLevelRequired(int levelRequired) {
		this.levelRequired = levelRequired;
		return this;
	}

	public int getExperience() {
		return experience;
	}

	public Spell setExperience(int experience) {
		this.experience = experience;
		return this;
	}
	
	/**
	 * Compares two  spells for list sorting. A spell is rated first
	 * by required level, then by name.
	 * 
	 */
	@Override
	public int compareTo(Object o) {
		Spell s = (Spell) o;
		if(this.levelRequired < s.getLevelRequired()){
			return -1;
		}
		else if(this.levelRequired > s.getLevelRequired()){
			return 1;
		}
		else{
			return this.getUnlocalizedName().compareTo(s.getUnlocalizedName());
		}
	}	

}

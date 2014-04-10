package emd24.rpgmod;

/*
 * Basic importing
 */
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import emd24.rpgmod.EntityIdMapping.EntityId;
import emd24.rpgmod.combatitems.HolyHandGrenade;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.combatitems.ItemAxeOfRevenge;
import emd24.rpgmod.combatitems.ItemBattleaxe;
import emd24.rpgmod.gui.GUIKeyHandler;
import emd24.rpgmod.gui.GUIManaBar;
import emd24.rpgmod.packets.PacketPipeline;
import emd24.rpgmod.skills.Skill;
import emd24.rpgmod.skills.SkillPlayer;
import emd24.rpgmod.skills.SkillRegistry;
import emd24.rpgmod.skills.SkillThieving;
import emd24.rpgmod.skills.ThievingData;
import emd24.rpgmod.spells.*;
/*
 * Basic needed forge stuff
 */
@Mod(modid=RPGMod.MOD_ID,version=RPGMod.VERSION)

public class RPGMod {

	public static final String MOD_ID = "rpgmod";
	public static final String VERSION = "0.1";

	
	
	// List where client and server proxy code is located
	@SidedProxy(clientSide="emd24." + MOD_ID + ".ClientProxy",
			serverSide="emd24." + MOD_ID + ".CommonProxy")
	public static CommonProxy proxy;

	//Telling forge that we are creating these
	//items
	public static Item lightningSpell;
	public static Item superLightningSpell;
	public static Item becomeUndead;
	public static Item summonZombie;
	public static Item healMana;
	public static Item holyHandGrenade;
	public static Item healSelf;
	public static Item healParty;
	
	public static Item battleAxeStone;
	public static Item axeRevenge;

	public static Block sodium;
	public static Block elementium;

	private static int modEntityID = 0;

	public static final PacketPipeline packetPipeline = new PacketPipeline();


	//tools

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){

		// Initialize packet pipeline
		packetPipeline.initialise();
		
		FMLCommonHandler.instance().bus().register(new GUIKeyHandler());

		// Register Event Handler
		MinecraftForge.EVENT_BUS.register(new EventHookContainer());

		//Register Party Player Tracker
		//GameRegistry.registerPlayerTracker(new PartyPlayerTracker());
		
		initializeItems();
		initializeSkills();

		// Register Blocks

		GameRegistry.registerBlock(elementium, elementium.getUnlocalizedName());
		GameRegistry.registerBlock(sodium, sodium.getUnlocalizedName());

		// Register Items
		


		// Register Entities
		
		EntityRegistry.registerModEntity(HolyHandGrenadeEntity.class, "holy_hand_grenade", ++modEntityID, this, 64, 10, true);




		proxy.registerRenderers();
	}

	@EventHandler
	public void load(FMLInitializationEvent event){


	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			MinecraftForge.EVENT_BUS.register(new GUIManaBar(Minecraft.getMinecraft()));
		
		packetPipeline.postInitialise();
	}
	
	/**
	 * Responsible for creating and initializing items for the mod.
	 * 
	 */
	private void initializeItems(){
		
		// Initialize magic spells
		
		lightningSpell = new LightningSpell(10, CreativeTabs.tabCombat)
		.setManaCost(25).setExperience(50).setUnlocalizedName("lightning")
		.setTextureName(MOD_ID + ":lightningSpell");
		
		superLightningSpell = new LightningSpell(20, CreativeTabs.tabCombat)
		.setManaCost(40).setExperience(100).setLevelRequired(3).setUnlocalizedName("super_lightning")
		.setTextureName(MOD_ID + ":superLightning");
		
		becomeUndead = new BecomeUndeadSpell(CreativeTabs.tabCombat)
		.setManaCost(10).setExperience(10).setUnlocalizedName("become_undead")
		.setTextureName(MOD_ID + ":becomeundead");

		summonZombie = new SummonCreatureSpell(0, CreativeTabs.tabCombat)
		.setManaCost(20).setExperience(20).setUnlocalizedName("summon_zombie")
		.setTextureName(MOD_ID + ":summonzombie");

		healSelf = new HealSpell(5, CreativeTabs.tabCombat, false).setManaCost(10)
				.setExperience(10).setUnlocalizedName("heal_self");
		
		healParty = new HealSpell(15, CreativeTabs.tabCombat, true).setManaCost(35).setUnlocalizedName("heal_party");
		
		// Initialize weapons
		
		holyHandGrenade = new HolyHandGrenade().setUnlocalizedName("holy_hand_grenade")
				.setTextureName(MOD_ID + ":holyhandgrenade");
		
		battleAxeStone = new ItemBattleaxe(ToolMaterial.STONE).setUnlocalizedName("stone_battleaxe")
				.setTextureName(MOD_ID + ":stone_battleaxe");
		
		axeRevenge = new ItemAxeOfRevenge().setUnlocalizedName("axe_of_revenge")
				.setTextureName(MOD_ID + ":axe_of_revenge");

		// Initialize miscellaneous items
		
		healMana = new ItemManaHeal().setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("heal_mana");


		elementium = new BlockOre().setHardness(5.0F).setResistance(5.0F).setStepSound(Block.soundTypeStone)
				.setBlockName("elementium_ore").setBlockTextureName(MOD_ID + ":elementium_ore").setCreativeTab(CreativeTabs.tabMaterials);

		sodium = new BlockOre().setHardness(0.5F).setResistance(5.0F).setStepSound(Block.soundTypeGravel)
				.setBlockName("sodium").setBlockTextureName(MOD_ID + ":dirt");
		
		
		// Register items				
		
		GameRegistry.registerItem(lightningSpell, lightningSpell.getUnlocalizedName());
		GameRegistry.registerItem(superLightningSpell, superLightningSpell.getUnlocalizedName());
		GameRegistry.registerItem(becomeUndead, becomeUndead.getUnlocalizedName());
		GameRegistry.registerItem(summonZombie, summonZombie.getUnlocalizedName());
		GameRegistry.registerItem(healMana, healMana.getUnlocalizedName());
		GameRegistry.registerItem(healSelf, healSelf.getUnlocalizedName());
		
		GameRegistry.registerItem(battleAxeStone, battleAxeStone.getUnlocalizedName());
		
		GameRegistry.registerItem(holyHandGrenade, holyHandGrenade.getUnlocalizedName());
		GameRegistry.registerItem(axeRevenge, axeRevenge.getUnlocalizedName());
		GameRegistry.registerItem(healParty, healParty.getUnlocalizedName());
	}
	
	/**
	 * Initializes all the skills in the game, adding the correct perks.
	 * 
	 */
	private void initializeSkills(){
		
		// Mining

		Skill mining = new Skill("Mining");
		mining.addExperienceBlockBreak(Block.getBlockFromName("stone"), 50); // experience for mining STONE
		mining.addExperienceBlockBreak(Block.getBlockFromName("coal_ore"), 10); // experience for mining coal
		mining.addExperienceBlockBreak(Block.getBlockFromName("iron_ore"), 20); // experience for mining iron
		mining.addBlockRequirement(Block.getBlockFromName("elementium"), 3);
		mining.addHarvestPerkBlock(Block.getBlockFromName("log"), 5, 0.5);

		// Treepunching

		Skill treepunching = new Skill("Treepunching");
		treepunching.addExperienceBlockBreak(Block.getBlockFromName("log"), 50); // experience for getting wood
		treepunching.addHarvestPerkBlock(Block.getBlockFromName("log"), 3, 0.5);

		// Thieving

		SkillThieving thieving = new SkillThieving("Thieving");

		// Create loot of pickpocketing villager
		HashMap<Integer, Double> villagerLoot = new HashMap<Integer, Double>();
		villagerLoot.put(GameData.itemRegistry.getId("minecraft:cookie"), 0.75);
		villagerLoot.put(GameData.itemRegistry.getId("minecraft:emerald"), 0.25);

		ThievingData villagerThievingData = new ThievingData(EntityId.VILLAGER,	25, 1, 0.9, villagerLoot);
		thieving.addThievingData(villagerThievingData);
		
		
		SkillRegistry.registerSkill(mining);
		SkillRegistry.registerSkill(treepunching);
		SkillRegistry.registerSkill(thieving);
		SkillRegistry.registerSkill(new Skill("Strength"));
		SkillRegistry.registerSkill(new Skill("Magic"));
		SkillRegistry.registerSkill(new Skill("Ranged"));
	}
}

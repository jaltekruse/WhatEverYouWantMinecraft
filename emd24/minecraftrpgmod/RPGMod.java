package emd24.minecraftrpgmod;

/*
 * Basic importing
 */
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import emd24.minecraftrpgmod.skills.SkillMining;
import emd24.minecraftrpgmod.skills.SkillPlayer;
import emd24.minecraftrpgmod.skills.SkillRegistry;
import emd24.minecraftrpgmod.spells.*;
/*
 * Basic needed forge stuff
 */
@Mod(modid="RPGMod",name="RPGMod",version="v1")
@NetworkMod(clientSideRequired=true,serverSideRequired=false, serverPacketHandlerSpec = @SidedPacketHandler(packetHandler = PacketHandler.class, channels = {"rpgmod", ExtendedPlayerData.CHANNEL}), 
clientPacketHandlerSpec = @SidedPacketHandler(packetHandler = PacketHandlerClient.class, channels = {"rpgmod", ExtendedPlayerData.CHANNEL}))
public class RPGMod {

	@Instance(value="tutorialmod")
	public static RPGMod instance;

	// List where client and server proxy code is located
	@SidedProxy(clientSide="emd24.minecraftrpgmod.ClientProxy",
			serverSide="emd24.minecraftrpgmod.CommonProxy")
	public static CommonProxy proxy;
	public PacketHandler packetHandler;
	
	
	//Telling forge that we are creating these
	//items
	public static Item lightningSpell;
	public static Item superLightningSpell;
	public static Item becomeUndead;
	public static Item summonZombie;
	public static Item healMana;

	public static Block sodium;
	public static Block elementium;

	//Skill s = new Skill();

	//tools

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
		// Register packet handler
		packetHandler = new PacketHandler();
		NetworkRegistry.instance().registerConnectionHandler(packetHandler);
		
		// Register Event Handler
		MinecraftForge.EVENT_BUS.register(new EventHookContainer());
		
		// Define items

		lightningSpell = new LightningSpell(4000, 10, CreativeTabs.tabCombat)
		.setManaCost(25).setUnlocalizedName("lightningSpell")
		.setTextureName("tutorialmod:lightningSpell");

		becomeUndead = new BecomeUndeadSpell(4001, 0, CreativeTabs.tabCombat)
		.setManaCost(10).setUnlocalizedName("becomeundead")
		.setTextureName("tutorialmod:becomeundead");

		summonZombie = new SummonCreatureSpell(4002, 54, CreativeTabs.tabCombat)
		.setManaCost(20).setUnlocalizedName("summonzombie")
		.setTextureName("tutorialmod:summonzombie");
		
		healMana = new ItemManaHeal(4003).setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("healMana");
		

		elementium = (new BlockOre(2000)).setHardness(5.0F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("oreElementium").setTextureName("tutorialmod:elementium_ore");
		
		sodium = (new BlockOre(2001)).setHardness(0.5F).setResistance(5.0F).setStepSound(Block.soundGravelFootstep)
				.setUnlocalizedName("oreSodium").setTextureName("dirt");
		
		
		
		// Register Blocks
		
		GameRegistry.registerBlock(elementium, "elementium");
		GameRegistry.registerBlock(sodium, "Sodium");

		// Register items
		
		LanguageRegistry.addName(lightningSpell, "Lightning Spell");
		LanguageRegistry.addName(becomeUndead, "Become Undead");
		LanguageRegistry.addName(summonZombie, "Summon Zombie");
		LanguageRegistry.addName(healMana, "Mana Heal");
		LanguageRegistry.addName(elementium, "Elementium");

		SkillMining mining = new SkillMining("Mining");
		mining.addExperienceBlockBreak(1, 50); // experience for mining STONE
		mining.addExperienceBlockBreak(16, 10); // experience for mining coal
		mining.addExperienceBlockBreak(15, 20); // experience for mining iron
		mining.addBlockRequirement(2000, 3);
		
		SkillRegistry.registerSkill(mining);
		
		proxy.registerRenderers();
	}

	@EventHandler
	public void load(FMLInitializationEvent event){


	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}

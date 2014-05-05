package emd24.test;

import java.io.File;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;

import org.apache.logging.log4j.Level;
import org.junit.Before;
import org.junit.Test;

//import mockit.Mocked;
//import mockit.NonStrictExpectations;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.launcher.FMLServerTweaker;
import cpw.mods.fml.common.launcher.FMLTweaker;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import static org.mockito.Mockito.*;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.combatitems.HolyHandGrenade;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.gui.GUIParty;
import emd24.rpgmod.packets.PacketPipeline;
import emd24.rpgmod.party.PartyManagerServer;
import emd24.rpgmod.spells.BecomeUndeadSpell;
import emd24.rpgmod.spells.HealSpell;
import emd24.rpgmod.spells.ItemManaHeal;
import emd24.rpgmod.spells.LightningSpell;
import emd24.rpgmod.spells.Spell;
import emd24.rpgmod.spells.SummonCreatureSpell;

public class AllTests {
	
	Block theBlock;
	WorldClient2 world;
	MovingObjectPosition hit;
	CreativeTabs creativeTabs;
	ItemStack itemS;
	EntityPlayer mockPlayer1;
	EntityPlayer mockPlayer2;
	ExtendedPlayerData exPlayerData;
	
	public static final String P1_NAME = "player1";
	public static final String P2_NAME = "player2";
	
	private class BlockAirExposer extends BlockAir {}
	Block airBlock = new BlockAirExposer().setBlockName("air");
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException { // Note: It is not required to call this setUp()
		RPGMod mod = new RPGMod();
		mod.packetPipeline = mock(PacketPipeline.class);
		theBlock = new BlockDoublePlant();
		WorldProvider mockWorldProvider = mock(WorldProvider.class);
		NetHandlerPlayClient mockNHPC = mock(NetHandlerPlayClient.class);
		mockNHPC.mapStorageOrigin =  new MapStorage((ISaveHandler)null);
		WorldInfo mockWI = mock(WorldInfo.class);
		WorldSettings mockWS = new WorldSettings(mockWI);
		Profiler mockP = mock(Profiler.class);
		world = new WorldClient2(mockNHPC, mockWS, 1, EnumDifficulty.PEACEFUL, mockP);
		world.isRemote = false;
	    mockPlayer1 = getPlayer(P1_NAME);
	    mockPlayer2 = getPlayer(P2_NAME);
	    BlockDoublePlant mockBDP = mock(BlockDoublePlant.class);
	    
	    hit = mock(MovingObjectPosition.class);
	    Entity target = mock(Entity.class);
	    hit.entityHit = target;
	    creativeTabs = mock(CreativeTabs.class);
	    itemS = new ItemStack(theBlock, 3);
	}
	
	private EntityPlayer getPlayer(String name) {
		EntityPlayer mockPlayer = mock(EntityPlayerMP.class);
		mockPlayer.capabilities = new PlayerCapabilities();
		mockPlayer.capabilities.isCreativeMode = false;
		mockPlayer.worldObj = world;
		when(mockPlayer.canPlayerEdit(anyInt(), anyInt(), anyInt(), anyInt(), any(ItemStack.class))).thenReturn(true);
		exPlayerData = new ExtendedPlayerData(mockPlayer);
		when(mockPlayer.getExtendedProperties(ExtendedPlayerData.IDENTIFIER)).thenReturn(exPlayerData);
		when(mockPlayer.getCommandSenderName()).thenReturn(name);
		return mockPlayer;
	}
	
	@Test
	public void holyHandGrenade(){

		HolyHandGrenadeEntityExposer grenade = new HolyHandGrenadeEntityExposer(world);
		grenade.onImpactExposer(hit);
		
		HolyHandGrenade hhg = new HolyHandGrenade();
		
		hhg.onItemRightClick(itemS, world, mockPlayer1);
	}
	
//	private class FMLServerTweaker2 extends FMLServerTweaker {
//		public FMLServerTweaker2() {
//		}
//	    
//	    public void injectCascadingTweak(String tweakClassName)
//	    {
//	    }
//	    
//	    public File getGameDir(){
//	    	return new File("/tmp/minecraft_gameDir.tmp");
//	    }
//	}
	
	private static class TestGUIParty extends GUIParty {

		public TestGUIParty(EntityPlayer player) {
			super(player);
		}
		
		public String getInvitingPlayerName() {
			return "player name";
		}
		
		public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
		}

		public void drawDefaultBackground() {
		}
		
		public void drawRectNotStatic(int par0, int par1, int par2, int par3, int par4)  {
		}
		
		public void drawCenteredString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
		}
	}
	
	@Test
	public void testPartyGUI() {
		PartyManagerServer.addPlayerToParty(mockPlayer1.getCommandSenderName(), 0);
		PartyManagerServer.addPlayerToPlayersParty(mockPlayer2.getCommandSenderName(), mockPlayer1.getCommandSenderName());
		world.isRemote = false;
		GUIParty gp = new TestGUIParty(mockPlayer1);
		gp.height = 100;
		gp.initGui();
		GuiButton gb = mock(GuiButton.class);
		gb.id = 0;
		gp.actionPerformed(gb);
		gp.drawScreen(10, 10, 1.0f);
	}
	
	private void setPlayerHealth(EntityPlayer player, float health) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class cls = Entity.class;
		Field f = cls.getDeclaredField("dataWatcher");
		f.setAccessible(true);
		DataWatcher dw = mock(DataWatcher.class);
		when(dw.getWatchableObjectFloat(6)).thenReturn(health);
		f.set(player, dw);
	}
	
	private void setPlayerMaxHealth(EntityPlayer player, float maxHealth) {
		IAttributeInstance maxHealthAttr = mock(IAttributeInstance.class);
		// max health > health, heal can be cast
		when(maxHealthAttr.getAttributeValue()).thenReturn(8.0);
		when(player.getEntityAttribute(SharedMonsterAttributes.maxHealth)).thenReturn(maxHealthAttr);
	}
	
	/*
	  -javaagent:/Users/jaltekruse/Downloads/jacoco-0.7.0.201403182114/lib/jacocoagent.jar="output=file,destfile=/Users/jaltekruse/jacoco.exec"
	 */
	@Test
	public void testIndividualHeal() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setPlayerHealth(mockPlayer1, 6);
		setPlayerMaxHealth(mockPlayer1, 8);
		Spell spell = new HealSpell(0, creativeTabs, false);
		spell.onItemRightClick(itemS, world, mockPlayer1);	
	}
	
	@Test
	public void testIndividualHealFails() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setPlayerHealth(mockPlayer1, 4);
		setPlayerMaxHealth(mockPlayer1, 4);
		Spell spell = new HealSpell(0, creativeTabs, false);
		//max health < health, heal fails
		world.isRemote = false;
		spell.onItemRightClick(itemS, world, mockPlayer1);
	}
	
	@Test
	public void testPartyHeal() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setPlayerHealth(mockPlayer1, 4);
		setPlayerHealth(mockPlayer2, 4);
		setPlayerMaxHealth(mockPlayer1, 8);
		setPlayerMaxHealth(mockPlayer2, 8);
		PartyManagerServer.addPlayerToParty(mockPlayer1.getCommandSenderName(), 0);
		PartyManagerServer.addPlayerToPlayersParty(mockPlayer2.getCommandSenderName(), mockPlayer1.getCommandSenderName());
		world.isRemote = false;
		Spell spell = new HealSpell(0, creativeTabs, true);

		spell = new HealSpell(0, creativeTabs, true);
		spell.onItemRightClick(itemS, world, mockPlayer1);
		ItemManaHeal spellItem = new ItemManaHeal();
		spellItem.onItemRightClick(itemS, world, mockPlayer1);
		spell.onItemUse(itemS, mockPlayer1, world, 1, 1, 1, 1, 1, 1, 1);
		spell.onItemRightClick(itemS, world, mockPlayer1);
		mockPlayer1.worldObj.isRemote = false;
	}
	
	@Test
	public void testBecomeUndeadSpell() {
		mockPlayer1.worldObj.isRemote = true;
		Spell spell = new BecomeUndeadSpell(creativeTabs);
		spell.onItemRightClick(itemS, world, mockPlayer1);
	}
	
	@Test
	public void summonCreateSpell() {
		Spell spell = new SummonCreatureSpell(1, creativeTabs);
		spell.onItemUse(itemS, mockPlayer1, world, 1, 1, 1, 1, 1, 1, 1);
	}
	
	@Test
	public void lightningSpell(){
//		System.out.println("stuff");
//		String[] args = {"--version", "1.6", "--tweakClass", 
//		                       "cpw.mods.fml.common.launcher.FMLTweaker", "--accessToken", "FML"};
//		//Launch.main(args);
//		MinecraftServer mcServer = new DedicatedServer(new File("/tmp/mcServer.tmp"));
//		FMLServerTweaker fmlServerTweaker = new FMLServerTweaker2();
//
//		fmlServerTweaker.acceptOptions(new ArrayList(), new File("/tmp/minecraft_gameDir.tmp"),
//				new File("/tmp/minecraft_gameDir.tmp"), "");
//		FMLLaunchHandler.configureForServerLaunch(mock(LaunchClassLoader.class), fmlServerTweaker);
//		WorldServer mockWorldServer = getWorldServer();
//		EntityPlayerMP mockPlayer = new EntityPlayerMP(null, mockWorldServer, mock(GameProfile.class), null);
//		EntityPlayer mockPlayer = mock(EntityPlayer.class);
//		mockPlayer.worldObj = getWorldServer();
		Spell spell;
		spell = new LightningSpell(3, creativeTabs);
		mockPlayer1.worldObj.isRemote = false;
		spell.onItemUse(itemS, mockPlayer1, world, 1, 1, 1, 1, 1, 1, 1);
//		mcServer.stopServer();
	}
	
	private class HolyHandGrenadeEntityExposer extends HolyHandGrenadeEntity {
		public HolyHandGrenadeEntityExposer(World par1World) {
			super(par1World);
		}

		public void onImpactExposer(MovingObjectPosition mop) {
			this.onImpact(mop);
		}
	}
	
//	public static WorldServer getWorldServer() {
//		ISaveHandler saveHandler = mock (ISaveHandler.class);
//		when(saveHandler.loadWorldInfo()).thenReturn(mock(WorldInfo.class));
//		WorldSettings worldSettings = new WorldSettings(mock(WorldInfo.class));
//		MinecraftServer minecraftServer = mock(MinecraftServer.class);
//		DedicatedServer dedServer = mock(DedicatedServer.class);
//		when(dedServer.getFile(anyString())).thenReturn(new File("/tmp/minecraft.tmp"));
//		when(dedServer.getIntProperty(eq("view-distance"), anyInt())).thenReturn(10);
//		ServerConfigurationManager serverConfigMan = new DedicatedPlayerList(dedServer); //mock(ServerConfigurationManager.class);
//		when(minecraftServer.getConfigurationManager()).thenReturn(serverConfigMan);
//		//when(serverConfigMan.getEntityViewDistance()).thenReturn(5);
//		System.out.println(minecraftServer.getConfigurationManager().getEntityViewDistance());
//		return new WorldServer2(minecraftServer, saveHandler, "", 1, worldSettings, mock(Profiler.class));
//	}
//	
//	private static class WorldServer2 extends WorldServer {
//
//		public WorldServer2(MinecraftServer minecraftServer,
//				ISaveHandler p_i45284_2_, String p_i45284_3_, int p_i45284_4_,
//				WorldSettings p_i45284_5_, Profiler p_i45284_6_) {
//			super(minecraftServer, p_i45284_2_, p_i45284_3_, p_i45284_4_, p_i45284_5_,
//					p_i45284_6_);
//		}
//		
//		public File getChunkSaveLocation(){
//			return new File("/tmp/");
//		}
//	}
	
	private class WorldClient2 extends WorldClient {

		public WorldClient2(NetHandlerPlayClient p_i45063_1_,
				WorldSettings p_i45063_2_, int p_i45063_3_,
				EnumDifficulty p_i45063_4_, Profiler p_i45063_5_) {
			super(p_i45063_1_, p_i45063_2_, p_i45063_3_, p_i45063_4_, p_i45063_5_);
		}
		
		@Override
		public Block getBlock(int i, int j, int k){
			return theBlock;
		}
		
		public boolean setBlockToAir(int i, int j, int k) {
			return this.setBlock(i, j, k, airBlock, 0, 3);
		}
		
		public EntityPlayer getPlayerEntityByName(String name){
			if (name.equals(P1_NAME)){
				return mockPlayer1;
			}
			else if (name.equals(P2_NAME)){
				return mockPlayer2;
			}
			throw new RuntimeException("no player with that name");
		}
		
	}
	
	

}

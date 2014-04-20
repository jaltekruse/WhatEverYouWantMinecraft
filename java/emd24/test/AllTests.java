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
import emd24.rpgmod.packets.PacketPipeline;
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
	EntityPlayer mockPlayer;
	ExtendedPlayerData exPlayerData;
	
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
	    BlockDoublePlant mockBDP = mock(BlockDoublePlant.class);
	    
	    hit = mock(MovingObjectPosition.class);
	    Entity target = mock(Entity.class);
	    hit.entityHit = target;
	    creativeTabs = mock(CreativeTabs.class);
	    itemS = new ItemStack(theBlock, 3);
		mockPlayer = mock(EntityPlayerMP.class);
		mockPlayer.capabilities = new PlayerCapabilities();
		mockPlayer.capabilities.isCreativeMode = false;
		mockPlayer.worldObj = world;
		Class cls = Entity.class;
		Field f = cls.getDeclaredField("dataWatcher");
		f.setAccessible(true);
		f.set(mockPlayer, mock(DataWatcher.class));
		when(mockPlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth)).thenReturn(mock(IAttributeInstance.class));
		when(mockPlayer.canPlayerEdit(anyInt(), anyInt(), anyInt(), anyInt(), any(ItemStack.class))).thenReturn(true);
		exPlayerData = new ExtendedPlayerData(mockPlayer);
		when(mockPlayer.getExtendedProperties(ExtendedPlayerData.IDENTIFIER)).thenReturn(exPlayerData);
	}

	@Test
	public void holyHandGrenade(){

		HolyHandGrenadeEntityExposer grenade = new HolyHandGrenadeEntityExposer(world);
		grenade.onImpactExposer(hit);
		
		HolyHandGrenade hhg = new HolyHandGrenade();
		
		hhg.onItemRightClick(itemS, world, mockPlayer);
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
	
	/*
	  -javaagent:/Users/jaltekruse/Downloads/jacoco-0.7.0.201403182114/lib/jacocoagent.jar="output=file,destfile=/Users/jaltekruse/jacoco.exec"
	 */
	
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
		Spell spell = new LightningSpell(3, creativeTabs);
		mockPlayer.worldObj.isRemote = false;
		spell.onItemUse(itemS, mockPlayer, world, 1, 1, 1, 1, 1, 1, 1);
		spell = new BecomeUndeadSpell(creativeTabs);
		spell.onItemRightClick(itemS, world, mockPlayer);
		spell = new SummonCreatureSpell(1, creativeTabs);
		spell.onItemUse(itemS, mockPlayer, world, 1, 1, 1, 1, 1, 1, 1);
		spell = new HealSpell(0, creativeTabs, false);
		spell.onItemRightClick(itemS, world, mockPlayer);
		spell = new HealSpell(0, creativeTabs, true);
		spell.onItemRightClick(itemS, world, mockPlayer);
		ItemManaHeal spellItem = new ItemManaHeal();
		spellItem.onItemRightClick(itemS, world, mockPlayer);
		spell.onItemUse(itemS, mockPlayer, world, 1, 1, 1, 1, 1, 1, 1);
		spell.onItemRightClick(itemS, world, mockPlayer);
		mockPlayer.worldObj.isRemote = false;
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
		
	}
	
	

}

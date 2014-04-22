package emd24.test;

import java.io.File;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
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

import cpw.mods.fml.common.launcher.FMLTweaker;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import static org.mockito.Mockito.*;
import emd24.rpgmod.combatitems.HolyHandGrenade;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.spells.LightningSpell;

public class AllTests {
	
	Block theBlock;
	WorldClient2 world;
	MovingObjectPosition hit;
	CreativeTabs creativeTabs;
	
	private class BlockAirExposer extends BlockAir {}
	Block airBlock = new BlockAirExposer().setBlockName("air");
	
	@Before
	public void setUp() { // Note: It is not required to call this setUp()
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
	}

	@Test
	public void holyHandGrenade(){

		HolyHandGrenadeEntityExposer grenade = new HolyHandGrenadeEntityExposer(world);
		grenade.onImpactExposer(hit);
		
		HolyHandGrenade hhg = new HolyHandGrenade();
		ItemStack itemS = new ItemStack(theBlock, 3);
		EntityPlayer mockPlayer = mock(EntityPlayer.class);
		mockPlayer.capabilities = new PlayerCapabilities();
		mockPlayer.capabilities.isCreativeMode = false;
		hhg.onItemRightClick(itemS, world, mockPlayer);
	}
	
	@Test
	public void lightningSpell(){
		LightningSpell spell = new LightningSpell(3, creativeTabs);
		// TODO - finish this
	}
	
	private class HolyHandGrenadeEntityExposer extends HolyHandGrenadeEntity {
		public HolyHandGrenadeEntityExposer(World par1World) {
			super(par1World);
		}

		public void onImpactExposer(MovingObjectPosition mop) {
			this.onImpact(mop);
		}
	}
	
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

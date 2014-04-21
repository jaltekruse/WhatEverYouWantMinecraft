package emd24.rpgmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.combatitems.ItemThrowingKnifeEntity;
import emd24.rpgmod.gui.GUIKeyHandler;
import emd24.rpgmod.gui.GUIManaBar;
import emd24.rpgmod.gui.GUIPartyHUD;
import emd24.rpgmod.gui.GUISkills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		
		RenderingRegistry.registerEntityRenderingHandler(HolyHandGrenadeEntity.class, new RenderSnowball(RPGMod.holyHandGrenade));
		RenderingRegistry.registerEntityRenderingHandler(ItemThrowingKnifeEntity.class, new RenderSnowball(RPGMod.throwingKnifeStone));
	}
	
	@Override
	public void registerKeys(){
		FMLCommonHandler.instance().bus().register(new GUIKeyHandler());
	}
	
}


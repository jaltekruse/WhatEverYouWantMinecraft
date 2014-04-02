package emd24.rpgmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.gui.GUIKeyHandler;
import emd24.rpgmod.gui.GUISkills;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
			
		RenderingRegistry.registerEntityRenderingHandler(HolyHandGrenadeEntity.class, new RenderSnowball(RPGMod.holyHandGrenade));
	}
	
}


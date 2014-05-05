package emd24.rpgmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.combatitems.HolyHandGrenadeModel;
import emd24.rpgmod.combatitems.ItemThrowingKnifeEntity;
import emd24.rpgmod.combatitems.RenderHolyHandGrenade;
import emd24.rpgmod.gui.GUIDialogue;
import emd24.rpgmod.gui.GUIDialogueEditor;
import emd24.rpgmod.gui.GUIKeyHandler;
import emd24.rpgmod.gui.GUIManaBar;
import emd24.rpgmod.gui.GUIPartyHUD;
import emd24.rpgmod.gui.GUIScriptEditor;
import emd24.rpgmod.gui.GUISkills;
import emd24.rpgmod.spells.entities.MagicBall;
import emd24.rpgmod.spells.render.RenderFireEnergy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLiving;
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
		RenderingRegistry.registerEntityRenderingHandler(MagicBall.class, new RenderFireEnergy());
	}
	@Override
	public void registerKeys(){
		FMLCommonHandler.instance().bus().register(new GUIKeyHandler());
	}
	
	@Override
	public void openDialogueGUI(int entityID, String dialogue) {
		//System.err.println("entityID received: " + entityID);
		EntityLiving target = (EntityLiving) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
		//System.err.println("entityID of target: " + target.getEntityId());
		
		if(GUIKeyHandler.npcAdminMode)
			Minecraft.getMinecraft().displayGuiScreen(new GUIDialogueEditor(target, dialogue));
		else
			Minecraft.getMinecraft().displayGuiScreen(new GUIDialogue(target, dialogue));
	}

	//method for updating the client gui script editor
	@Override
	public void updateScriptEditor(String name, String content) {
		GUIScriptEditor.updateScript(name, content);
	}

}


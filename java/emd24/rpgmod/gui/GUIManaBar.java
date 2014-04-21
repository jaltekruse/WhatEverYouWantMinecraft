package emd24.rpgmod.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;


/**
 * Class that represents the mana bar that is to appear in the game.
 * 
 * @author Evan Dyke
 *
 */
public class GUIManaBar extends Gui{

	private Minecraft instance;
	
	private static final ResourceLocation texturepath = new ResourceLocation(RPGMod.MOD_ID, "textures/gui/mana_bar.png");

	public GUIManaBar(Minecraft minecraft){
		super();
		
		// Setup font renderer for writing text
		this.instance = minecraft;

	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		// Makes sure event can be cancelled and its the event type for
		// the experience bar
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{
			return;
		}

		ExtendedPlayerData data = ExtendedPlayerData.get(this.instance.thePlayer);

		// Check if mana property doesn't exist
		if(data == null || data.getMaxMana() == 0){
			return;
		}

		int xPos = 2;
		int yPos = 2;
		
		// Render texture like in file
		GL11.glColor4f(1.0f, 1.0F, 1.0F, 1.0F);
		
		// Somewhere in Minecraft vanilla code it says to do this because of a lighting bug
		GL11.glDisable(GL11.GL_LIGHTING);
		
		this.instance.getTextureManager().bindTexture(this.texturepath);
		
		// Draw outer rectangle for mana bar
		this.drawTexturedModalRect(xPos, yPos, 0, 0, 64, 4);
		
		// Calculate # of pixels to scale mana bar
		int pixelLength = (int) (((float) data.getCurrMana() / data.getMaxMana()) * 62);
		
		// Draw the inner rectangle
		this.drawTexturedModalRect(xPos + 1, yPos + 1, 0, 4, pixelLength, 2);

		// Draw Mana value
		this.drawString(this.instance.fontRenderer, data.getCurrMana() + "/" + data.getMaxMana(), xPos + 72, yPos, 0xffffffff);

	}


}

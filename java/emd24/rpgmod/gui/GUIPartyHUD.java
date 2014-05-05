package emd24.rpgmod.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.party.PartyManagerClient;
import emd24.rpgmod.party.PartyManagerServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GUIPartyHUD extends Gui {

	private Minecraft instance;
	
	private static final ResourceLocation healthtexture = new ResourceLocation(RPGMod.MOD_ID, "textures/gui/health_bar.png");
	private static final ResourceLocation manatexture = new ResourceLocation(RPGMod.MOD_ID, "textures/gui/mana_bar.png");
	private static final int xBuffer = 2;
	private static final int yBuffer = 2;
	private static final int manaHeight = 15;
	private static final int textHeight = 10;
	private static final int barHeight = 4;
	private static final int slotHeight = 2 * yBuffer + textHeight * 2 + barHeight * 2;
	private static final int white = 0xffffffff;
	
	public GUIPartyHUD(Minecraft minecraft){
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
		
		ArrayList<String> partyNames = PartyManagerClient.getPlayerParty(this.instance.thePlayer.getCommandSenderName());
		// If the player is in the generic party, don't render anything special.
		if (PartyManagerClient.playerParty.get(this.instance.thePlayer.getCommandSenderName()) == 0){
			return;
		} else { 
			int currSlot = 0;
			
			for (String curr: partyNames){
				// Skip ourself
				if (this.instance.thePlayer.getCommandSenderName().compareTo(curr) == 0){
					continue;
				}
				
				EntityPlayer currPlayer = this.instance.theWorld.getPlayerEntityByName(curr);
				
				if(currPlayer == null)
					continue;
				
				int slotOrigin =  manaHeight + slotHeight * currSlot + yBuffer;
				
				// If slot origin > screen height, we'd try to place something
				// off screen. Let's not do that.
				if (slotOrigin > this.instance.displayHeight){
					return;
				}

				// Render texture with colors like in original file
				GL11.glColor4f(1.0f, 1.0F, 1.0F, 1.0F);
				
				// Somewhere in Minecraft vanilla code it says to do this because of a lighting bug
				// Older GL issue?
				GL11.glDisable(GL11.GL_LIGHTING);
				
				// Draw Player Name
				this.drawString(this.instance.fontRenderer,  curr, xBuffer, slotOrigin, white);
				
				/* Draw Health Bar */
				this.instance.getTextureManager().bindTexture(this.healthtexture);
				// Draw outer rectangle for health bar
				this.drawTexturedModalRect(xBuffer, slotOrigin + textHeight, 0, 0, 64, 4);
				// Calculate # of pixels to scale health bar
				int pixelLength = (int) (((float) currPlayer.getHealth() / currPlayer.getMaxHealth()) * 62);
				// Draw the inner rectangle
				this.drawTexturedModalRect(xBuffer + 1, slotOrigin + textHeight + 1, 0, 4, pixelLength, 2);

				/* Draw Mana Bar */
				this.instance.getTextureManager().bindTexture(this.manatexture);
				// Draw outer rectangle for health bar
				this.drawTexturedModalRect(xBuffer, slotOrigin + textHeight + barHeight, 0, 0, 64, 4);
				try{
						// Calculate # of pixels to scale health bar
					int currMana = PartyManagerClient.playerMana.get(curr)[PartyManagerClient.CURR_MANA_LOC];
					int currMaxMana = PartyManagerClient.playerMana.get(curr)[PartyManagerClient.MAX_MANA_LOC];
					pixelLength = (int) (((float) currMana / currMaxMana) * 62);
					// Draw the inner rectangle
					this.drawTexturedModalRect(xBuffer + 1, slotOrigin + textHeight + barHeight + 1, 0, 4, pixelLength, 2);
				} catch (NullPointerException e){}
				String location = "x: " + (int) currPlayer.posX + " y: " + (int) currPlayer.posY + " z: " + (int) currPlayer.posZ;
				this.drawString(this.instance.fontRenderer,  location, xBuffer, slotOrigin + textHeight + barHeight + 1
						+ (barHeight + 1), white);
				
				
				currSlot++;
			}
		}
	}
}

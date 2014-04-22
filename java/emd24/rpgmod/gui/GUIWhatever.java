package emd24.rpgmod.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class GUIWhatever extends GuiScreen
{
	public GUIWhatever()
	{
		
	}
	
    /**
     * Set the time in the server object.
     * Stole this from CommandTime.java
     */
    protected void setTime(int time)
    {
        for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j)
        {
            MinecraftServer.getServer().worldServers[j].setWorldTime((long)time);
        }
    }
	
	public void initGui()
	{
		buttonList.clear();
		int height = 60;
		buttonList.add(new GuiButton(1, width/2 - 80, height, "Rain, rain Motherfuckers!"));
		height += 30;
		buttonList.add(new GuiButton(2, width/2 - 80, height, "Stop the fucking rain..."));
		height += 30;
		buttonList.add(new GuiButton(3, width/2 - 80, height, "I'm Blind!  Give me some fucking light!"));
		height += 30;
		buttonList.add(new GuiButton(4, width/2 - 80, height, "I'm a creature of the damned darkness!"));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 1:
			Minecraft.getMinecraft().theWorld.setRainStrength(5F);
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Rain started"));
			break;
		case 2:
			Minecraft.getMinecraft().theWorld.setRainStrength(0F);
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Rain stopped"));
			break;
		case 3:
			this.setTime(0);
			break;
		case 4:
			this.setTime(12500);	//See CommandTime.java
			break;
		default:
			break;
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();

		drawRect(20, 20, width - 20, height - 20, 0x60ff0000);
		drawCenteredString(fontRendererObj, "Change the fucking game state:", width / 2, 30, 0xffffffff);
		

		super.drawScreen(i, j, f);
	}
}
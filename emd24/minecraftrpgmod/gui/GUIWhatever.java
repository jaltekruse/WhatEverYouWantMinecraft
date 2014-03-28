package emd24.minecraftrpgmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;

public class GUIWhatever extends GuiScreen
{
	public GUIWhatever()
	{
		
	}
	
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(1, width/2 - 80, height / 2 + 20, "Rain, rain Motherfuckers!"));
		buttonList.add(new GuiButton(2, width/2 - 80, height / 2 + 50, "Stop the fucking rain..."));
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
		drawCenteredString(fontRendererObj, "Fucking Players:", width / 2, 30, 0xffffffff);

		
		String[] usernames = Minecraft.getMinecraft().getIntegratedServer().getAllUsernames();
		for(int x = 0; x < usernames.length; x++)
		{
			drawCenteredString(fontRendererObj, usernames[x], width / 2, x * 20 + 50, 0xffffffff);
		}
		

		super.drawScreen(i, j, f);
	}
}
package emd24.rpgmod.gui;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.GUIOpenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;

public class GUIScriptEditor extends GuiScreen {
	int editLine;
	ArrayList<String> lines;
	char last_char;

	static int line_length = 60;
	static int max_display_lines = 12;

	public GUIScriptEditor()
	{
		super();
		
		lines = new ArrayList<String>();
		lines.add("");
		editLine = 0;
	}
	
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(1, width*1/8, height - 60, "Save Script"));
		buttonList.add(new GuiButton(2, width*5/8, height - 60, "Run Script"));

		Keyboard.enableRepeatEvents(true);
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 1:
			//Save script
			break;
		case 2:
			//Run script
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
		drawCenteredString(fontRendererObj, "Edit your fucking script", width / 2, 30, 0xffffffff);
		
		//draw lines
		int num_lines = Math.min(lines.size(), max_display_lines);
		for(int line_num = 0; line_num < num_lines; line_num++) {
			int h = 50 + line_num * 10;
			drawString(fontRendererObj, lines.get(line_num), 30, h, 0xffffffff);
		}

		super.drawScreen(i, j, f);
	}
	
	/*
	 * Handle Keyboard Input - ansi_char, key_code
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput()
	 */
	public void keyTyped(char par1, int par2) {
        String line = lines.get(editLine);
        //TODO: add delete key? not terribly important
        
		//Up key ->  move up one line
        if (par2 == 200)
        {
            this.editLine = this.editLine - 1;
        }
        
        //Enter -> add new line
        if(par2 == Keyboard.KEY_RETURN)
        {
        	//get whitespace on current line
        	String left_trim = line.replaceAll("^\\s+", "");
        	String whitespace = StringUtils.repeat(" ", line.length() - left_trim.length());
        	
        	if(last_char == '{')
        	{
            	lines.add(editLine + 1, whitespace + "}");
            	whitespace += " ";
        	}
        	lines.add(editLine + 1, whitespace);
            this.editLine = this.editLine + 1;
        }

        //Down | ? -> move down one line
        if (par2 == 208 || par2 == 156)
        {
            this.editLine = this.editLine + 1;
            if(editLine >= lines.size())
            	lines.add("");
        }
        
        //Backspace key
        if (par2 == 14)
        {
        	if(line.length() > 0)
        	{
                line = line.substring(0, line.length() - 1);
            	lines.set(editLine, line);
        	}
        	else if(lines.size() > 1)
        	{
        		lines.remove(editLine);
        		if(editLine >= lines.size())
        			editLine--;
        	}
        }

        //character keys
        if (ChatAllowedCharacters.isAllowedCharacter(par1) && line.length() < this.line_length)
        {
            //this.tileSign.signText[this.editLine] = this.tileSign.signText[this.editLine] + par1;
        	line += par1;
        	lines.set(editLine, line);
        	last_char = par1;
        }

        //escape key
        if (par2 == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
	}
	
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}

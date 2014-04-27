package emd24.rpgmod.gui;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.GUIOpenPacket;
import emd24.rpgmod.packets.ScriptPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;

import javax.script.*;

public class GUIScriptEditor extends GuiScreen {
	int editLine;
	ArrayList<String> lines;
	char last_char;

	static int line_length = 60;
	static int max_display_lines = 12;

	ScriptEngineManager factory;
	ScriptEngine engine;
	String scriptError;
	
	GuiTextField scriptNameTextBox;
	
	static String name = "";
	static String content = "";
	static boolean updated = false;

	public GUIScriptEditor()
	{
		super();
		
		lines = new ArrayList<String>();
		lines.add("");
		editLine = 0;
		
		scriptError = "";
	}
	
	public String scriptString()
	{
		String script = "";
		for(String s : lines) {
			script += s + "\n";
		}
		return script;
	}
	
	public void loadScript()
	{
		if(updated)
		{
			this.scriptNameTextBox.setText(name);
			
			Scanner scanner = new Scanner(content);
			lines.clear();
			
			//handle the empty case
			if(!scanner.hasNext())
				lines.add("");
			
			//read lines into arraylist
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
			}
			
			//Cleanup
			scanner.close();
			updated = false;
		}
	}
	
	/**
	 * Used to update the script name and content from the server when packet is received
	 * @param name
	 * @param content
	 */
	public static void updateScript(String name, String content)
	{
		GUIScriptEditor.name = name;
		GUIScriptEditor.content = content;
		GUIScriptEditor.updated = true;
	}
	
	public void initGui()
	{
		//setup script manager
		factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");
		
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		buttonList.add(new GuiButton(1, 30, height - 60, 60, 20, "Save Script"));
		buttonList.add(new GuiButton(2, width/2, height - 60, 60, 20, "Load Script"));
		buttonList.add(new GuiButton(3, width*3/4, height - 60, 60, 20, "Run Script"));
		
		this.scriptNameTextBox = new GuiTextField(this.fontRendererObj, width / 4, 30, width/2 - 30, 12);
		this.scriptNameTextBox.setTextColor(-1);
		this.scriptNameTextBox.setDisabledTextColour(-1);
		this.scriptNameTextBox.setEnableBackgroundDrawing(true);
		this.scriptNameTextBox.setMaxStringLength(30);
		this.scriptNameTextBox.setText("script name");
		//scriptNameTextBox.setFocused(true);
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		ScriptPacket message;
		switch(guibutton.id)
		{
		case 1:
			//Save script
			name = this.scriptNameTextBox.getText();
			content = scriptString();
			message = new ScriptPacket(name, content);
			RPGMod.packetPipeline.sendToServer(message);
			break;
		case 2:
			//Load script
			name = this.scriptNameTextBox.getText();
			message = new ScriptPacket(name);
			RPGMod.packetPipeline.sendToServer(message);
			break;
		case 3:
			//Run script
			Object result;
			try {
				String script = scriptString();
				result = engine.eval(script);
				scriptError = (String) result;
			} catch(ScriptException e) {
				scriptError = e.getMessage();
			}
			break;
		default:
			break;
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.scriptNameTextBox.mouseClicked(par1, par2, par3);
	}
	
	/*
	 * Handle Keyboard Input - ansi_char, key_code
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput()
	 */
	public void keyTyped(char par1, int par2) {
        String line = lines.get(editLine);
        //TODO: add delete key? not terribly important
		
        //type in dialogue box
		if(this.scriptNameTextBox.textboxKeyTyped(par1, par2)) {
			this.scriptNameTextBox.getText();
			return;
		}
        
		//Up key ->  move up one line
        if (par2 == 200 && editLine > 0)
        {
            this.editLine = this.editLine - 1;
        }
        
        //Enter -> add new line
        if(par2 == Keyboard.KEY_RETURN)
        {
        	//get whitespace on current line
        	String left_trim = line.replaceAll("^\\s+", "");
        	String whitespace = StringUtils.repeat(" ", line.length() - left_trim.length());
        	
        	//auto-complete curly braces, indent next line further
        	if(last_char == '{')
        	{
            	lines.add(editLine + 1, whitespace + "}");
            	whitespace += " ";
        	}
        	
        	//add new line and go to it
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
        	//remove line if there are no characters left in the line
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
	
	public void drawScreen(int i, int j, float f)
	{
		//check for packet updates
		loadScript();
		
		drawDefaultBackground();
		
		//draw text box
		this.scriptNameTextBox.drawTextBox();

		drawRect(20, 20, width - 20, height - 20, 0x60ff0000);
		drawString(fontRendererObj, scriptError, 30, height - 30, 0xffffffff);
		
		//draw lines
		int num_lines = Math.min(lines.size(), max_display_lines);
		for(int line_num = 0; line_num < num_lines; line_num++) {
			int h = 50 + line_num * 10;
			drawString(fontRendererObj, lines.get(line_num), 30, h, 0xffffffff);
		}

		super.drawScreen(i, j, f);
	}
	
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}

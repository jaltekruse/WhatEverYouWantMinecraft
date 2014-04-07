/**
 * A GUI Editor used to change dialogue items between a player and an NPC
 * 
 * @author Wesley Reardan
 * 
 * @references https://www.youtube.com/watch?v=lDBuknMB014 for GuiTextField stuff
 */

package emd24.rpgmod.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import emd24.rpgmod.quest.DialogueTreeNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class GUIDialogueEditor extends GuiScreen
{
	protected String npcName;
	
	DialogueTreeNode tree;
	
	DialogueTreeNode selectedNode;
	int selectedIndex;
	int firstListItem;
	int numListElements;
	
	int backgroundColor;
	
	List<DialogueTreeNode> list;
	
	GuiTextField dialogueTextbox, itemTextbox, quantityTextbox, actionTextbox;
	
	
	public GUIDialogueEditor()
	{
		npcName = "NPC NAME HERE";
		
		tree = new DialogueTreeNode();
		
		selectedNode = tree;
		selectedIndex = 0;
		firstListItem = 0;
		numListElements = 12;
		
		backgroundColor = 0x60ff0000;
	}
	
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		buttonList.add(new GuiButton(1, width - 240, height - 60, "New Reply/Dialogue"));
		
		this.dialogueTextbox = new GuiTextField(this.fontRendererObj, width / 2, 75, width/2 - 30, 12);
		this.dialogueTextbox.setTextColor(-1);
		this.dialogueTextbox.setDisabledTextColour(-1);
		this.dialogueTextbox.setEnableBackgroundDrawing(true);
		this.dialogueTextbox.setMaxStringLength(30);
		dialogueTextbox.setFocused(true);
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 1:
			selectedNode.addChild();
			
			//selectedNode = selectedNode.addChild();
			//selectedIndex++;
			
			//Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("New reply created"));
			break;
		default:
			break;
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.dialogueTextbox.mouseClicked(par1, par2, par3);
	}
	
	public void drawScreen(int par1, int j, float f)
	{
		drawDefaultBackground();
		
		//draw text boxes
		this.dialogueTextbox.drawTextBox();

		//draw header
		drawRect(20, 20, width - 20, height - 20, 0x60ff0000);
		int h = 30;
		drawCenteredString(fontRendererObj, npcName, width / 2, h, 0xffffffff);
		drawHorizontalLine(20, 50, 500, 0xffffffff);

		//draw selected item
		int adjusted_index = (selectedIndex - firstListItem);
		if(adjusted_index >= numListElements)
			//if(selectedIndex + numListElements < list.size() - 1)
				firstListItem++;
		if(adjusted_index < 0)
			firstListItem--;
		
		int y = adjusted_index * 15 + 40;
		drawRect(20, y, width/2, y + 15, 0xff0000ff);
		
		//update list of tree
		this.list = new ArrayList<DialogueTreeNode>();
		tree.getList(this.list);

		//draw tree
		List<String> list = new ArrayList<String>();
		tree.getList(list, "");
		
		int max_index = Math.min(list.size(), firstListItem + this.numListElements);
		list = list.subList(this.firstListItem, max_index);
		for(int i = 0; i < list.size(); i++) {
			String line = list.get(i);
			h += 15;
			drawString(fontRendererObj, line, 20, h, 0xffffffff);
		}
		
		//Selected Node Drawing (right side of Window)
		h = 30;
		String dialogue = "dialogue: ";// + selectedNode.dialogueText;
		dialogueTextbox.setText(selectedNode.dialogueText);
		h += 30;
		drawString(fontRendererObj, dialogue, width / 2, h, 0xffffffff);

		String item = "item: " + selectedNode.itemNeeded;
		h += 30;
		drawString(fontRendererObj, item, width / 2, h, 0xffffffff);

		String quantity = "item quantity: " + selectedNode.itemQuantity;
		h += 30;
		drawString(fontRendererObj, quantity, width / 2, h, 0xffffffff);

		String action = "action: " + selectedNode.action;
		h += 30;
		drawString(fontRendererObj, action, width / 2, h, 0xffffffff);
		

		super.drawScreen(par1, j, f);
	}
	
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		
	}
	
	/*
	 * Handle Keyboard Input - 
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput()
	 */
	public void keyTyped(char par1, int key) {
		//String chat = par1 + ", " + key;
		//Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(chat));
		
		if(this.dialogueTextbox.textboxKeyTyped(par1, key)) {
			selectedNode.dialogueText = this.dialogueTextbox.getText();
		}
		
		switch(key) {
		case Keyboard.KEY_DOWN:
			if(selectedIndex < list.size() - 1) {
				selectedIndex++;
				selectedNode = list.get(selectedIndex);
				
				dialogueTextbox.setFocused(true);
			}
			break;
		case Keyboard.KEY_UP:
			if(selectedIndex > 0) {
				selectedIndex--;
				selectedNode = list.get(selectedIndex);
				
				dialogueTextbox.setFocused(true);
			}
			break;
		case Keyboard.KEY_RETURN:
			selectedNode.addChild();
		}
		
		super.keyTyped(par1, key);
	}
}
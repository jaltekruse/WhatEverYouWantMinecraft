package emd24.rpgmod.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import emd24.rpgmod.quest.DialogueTreeNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
	
	ArrayList<DialogueTreeNode> list;
	
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
		buttonList.clear();
		buttonList.add(new GuiButton(1, width - 240, height - 60, "New Reply/Dialogue"));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 1:
			//selectedNode.addChild();
			
			selectedNode = selectedNode.addChild();
			//selectedIndex++;
			
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("New reply created"));
			break;
		default:
			break;
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	public void drawScreen(int par1, int j, float f)
	{
		drawDefaultBackground();

		//draw header
		drawRect(20, 20, width - 20, height - 20, 0x60ff0000);
		int h = 30;
		drawCenteredString(fontRendererObj, npcName, width / 2, h, 0xffffffff);
		drawHorizontalLine(20, 50, 500, 0xffffffff);

		//draw selected item
		int adjusted_index = (selectedIndex - firstListItem);
		if(adjusted_index > numListElements)
			firstListItem++;
		if(adjusted_index < 0)
			firstListItem--;
		
		int y = adjusted_index * 15 + 40;
		drawRect(20, y, width/2, y + 15, 0xff0000ff);
		
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
		String dialogue = "dialogue: " + selectedNode.dialogueText;
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
	
	/*
	 * Handle Keyboard Input - 
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput()
	 */
	public void keyTyped(char par1, int key) {
		String chat = par1 + ", " + key;
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(chat));
		
		switch(key) {
		case Keyboard.KEY_DOWN:
			selectedIndex++;
			break;
		case Keyboard.KEY_UP:
			selectedIndex--;
			break;
		}
		
		super.keyTyped(par1, key);
	}
}
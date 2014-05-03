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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.GUIOpenPacket;
import emd24.rpgmod.quest.DialogueTreeNode;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class GUIDialogueEditor extends GuiScreen
{
	protected String npcName;
	static String defaultNPCName = "NPC NAME NOT AVAILABLE!";
	
	DialogueTreeNode tree;
	
	DialogueTreeNode selectedNode;
	int selectedIndex;
	int firstListItem;
	int numListElements;
	
	int backgroundColor;
	
	List<DialogueTreeNode> list;
	
	GuiTextField npcNameTextbox, rewardTextbox, rewardQuantityTextbox;
	GuiTextField dialogueTextbox, itemTextbox, quantityTextbox, actionTextbox;
	
	EntityLiving target;
	
	boolean initialized;
	
	public GUIDialogueEditor(EntityLiving target, String dialogue)
	{
		initialized = false;
		
		selectedNode = tree;
		selectedIndex = 0;
		firstListItem = 0;
		numListElements = 12;
		
		backgroundColor = 0x60ff0000;
		
		this.target = target;
		if(target.hasCustomNameTag())
			npcName = target.getCustomNameTag();
		else
			npcName = defaultNPCName;
		
		
		tree = new DialogueTreeNode();
		tree.load(dialogue);
	}
	
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		buttonList.add(new GuiButton(1, width/2, height - 50, width/4 - 15, 20, "New-Enter"));
		buttonList.add(new GuiButton(2, width*3/4, height - 50, width/4 - 30, 20, "Delete"));
		
		int h = 50;
		
		this.dialogueTextbox = new GuiTextField(this.fontRendererObj, width / 2, h, width/2 - 30, 12);
		this.dialogueTextbox.setTextColor(-1);
		this.dialogueTextbox.setDisabledTextColour(-1);
		this.dialogueTextbox.setEnableBackgroundDrawing(true);
		this.dialogueTextbox.setMaxStringLength(30);
		dialogueTextbox.setFocused(true);

		h+= 25;
		this.itemTextbox = new GuiTextField(this.fontRendererObj, width / 2, h, width/2 - 30, 12);
		this.itemTextbox.setTextColor(-1);
		this.itemTextbox.setDisabledTextColour(-1);
		this.itemTextbox.setEnableBackgroundDrawing(true);
		this.itemTextbox.setMaxStringLength(30);

		h+= 25;
		this.quantityTextbox = new GuiTextField(this.fontRendererObj, width / 2, h, width/2 - 30, 12);
		this.quantityTextbox.setTextColor(-1);
		this.quantityTextbox.setDisabledTextColour(-1);
		this.quantityTextbox.setEnableBackgroundDrawing(true);
		this.quantityTextbox.setMaxStringLength(30);

		h+= 25;
		this.actionTextbox = new GuiTextField(this.fontRendererObj, width / 2, h, width/2 - 30, 12);
		this.actionTextbox.setTextColor(-1);
		this.actionTextbox.setDisabledTextColour(-1);
		this.actionTextbox.setEnableBackgroundDrawing(true);
		this.actionTextbox.setMaxStringLength(30);

		h+= 25;
		this.rewardTextbox = new GuiTextField(this.fontRendererObj, width / 2, h, width/2 - 30, 12);
		this.rewardTextbox.setTextColor(-1);
		this.rewardTextbox.setDisabledTextColour(-1);
		this.rewardTextbox.setEnableBackgroundDrawing(true);
		this.rewardTextbox.setMaxStringLength(30);

		h+= 25;
		this.rewardQuantityTextbox = new GuiTextField(this.fontRendererObj, width / 2, h, width/2 - 30, 12);
		this.rewardQuantityTextbox.setTextColor(-1);
		this.rewardQuantityTextbox.setDisabledTextColour(-1);
		this.rewardQuantityTextbox.setEnableBackgroundDrawing(true);
		this.rewardQuantityTextbox.setMaxStringLength(30);

		h+= 25;
		this.npcNameTextbox = new GuiTextField(this.fontRendererObj, width / 4, 25, width/2 - 30, 12);
		this.npcNameTextbox.setTextColor(-1);
		this.npcNameTextbox.setDisabledTextColour(-1);
		this.npcNameTextbox.setEnableBackgroundDrawing(true);
		this.npcNameTextbox.setMaxStringLength(30);
		this.npcNameTextbox.setText(this.npcName);
		
		initialized = true;
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
		case 2:
			keyTyped('d', Keyboard.KEY_DELETE);
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
		this.itemTextbox.mouseClicked(par1, par2, par3);
		this.quantityTextbox.mouseClicked(par1, par2, par3);
		this.actionTextbox.mouseClicked(par1, par2, par3);
		this.rewardTextbox.mouseClicked(par1, par2, par3);
		this.rewardQuantityTextbox.mouseClicked(par1, par2, par3);
		this.npcNameTextbox.mouseClicked(par1, par2, par3);
	}
	
	public void drawScreen(int par1, int j, float f)
	{
		drawDefaultBackground();
		
		//draw text boxes
		this.dialogueTextbox.drawTextBox();
		this.itemTextbox.drawTextBox();
		this.quantityTextbox.drawTextBox();
		this.actionTextbox.drawTextBox();
		this.rewardTextbox.drawTextBox();
		this.rewardQuantityTextbox.drawTextBox();
		this.npcNameTextbox.drawTextBox();

		//draw header
		drawRect(20, 20, width - 20, height - 20, 0x60ff0000);
		int h = 30;
		//drawCenteredString(fontRendererObj, npcName, width / 2, h, 0xffffffff);
		drawHorizontalLine(20, 50, 500, 0xffffffff);

		//draw selected item
		int adjusted_index = (selectedIndex - firstListItem);
		if(adjusted_index >= numListElements)
			//if(selectedIndex + numListElements < list.size() - 1)
				firstListItem++;
		if(adjusted_index < 0)
			firstListItem--;
		
		int y = adjusted_index * 15 + 40;
		drawRect(20, y, width/2, y + 15, 0x600000ff);
		
		//update list of tree
		this.list = new ArrayList<DialogueTreeNode>();
		tree.getList(this.list);
		
		if(selectedIndex >= this.list.size())
			selectedIndex = this.list.size() - 1;
		selectedNode = this.list.get(selectedIndex);

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
		h = 40;
		String dialogue = "dialogue: ";// + selectedNode.dialogueText;
		dialogueTextbox.setText(selectedNode.dialogueText);
		drawString(fontRendererObj, dialogue, width / 2, h, 0xffffffff);

		String item = "item: ";
		itemTextbox.setText(selectedNode.itemNeeded);
		h += 25;
		drawString(fontRendererObj, item, width / 2, h, 0xffffffff);

		String quantity = "item quantity: ";
		if(selectedNode.itemQuantity >= 0)
			quantityTextbox.setText(Integer.toString(selectedNode.itemQuantity));
		h += 25;
		drawString(fontRendererObj, quantity, width / 2, h, 0xffffffff);

		String action = "action: ";
		actionTextbox.setText(selectedNode.action);
		h += 25;
		drawString(fontRendererObj, action, width / 2, h, 0xffffffff);

		String reward = "reward: ";
		rewardTextbox.setText(selectedNode.reward);
		h += 25;
		drawString(fontRendererObj, reward, width / 2, h, 0xffffffff);

		String rewardQuantity = "reward quantity: ";
		if(selectedNode.rewardQuantity >= 0)
			rewardQuantityTextbox.setText(Integer.toString(selectedNode.rewardQuantity));
		h += 25;
		drawString(fontRendererObj, quantity, width / 2, h, 0xffffffff);
		

		super.drawScreen(par1, j, f);
	}
	
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		
		//Send new Dialogue String to server
		String dialogue = tree.store();
		
		int entityID = target.getEntityId();
		GUIOpenPacket message = new GUIOpenPacket(0, entityID, dialogue);
		RPGMod.packetPipeline.sendToServer(message);

//		ExtendedEntityLivingDialogueData eeldd = ExtendedEntityLivingDialogueData.get(target);
//		eeldd.dialogueTree.load(tree.store());
	}
	
	/*
	 * Handle Keyboard Input - 
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput()
	 */
	public void keyTyped(char par1, int key) {
		if(!initialized)
			return;
		//String chat = par1 + ", " + key;
		//Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(chat));
		
		if(this.dialogueTextbox.textboxKeyTyped(par1, key)) {
			selectedNode.dialogueText = this.dialogueTextbox.getText();
		}
		if(this.itemTextbox.textboxKeyTyped(par1, key)) {
			selectedNode.itemNeeded = this.itemTextbox.getText();
		}
		if(this.quantityTextbox.textboxKeyTyped(par1, key)) {
			try {
				selectedNode.itemQuantity = Integer.parseInt(this.quantityTextbox.getText());
			}
			catch(NumberFormatException e) {
				selectedNode.itemQuantity = -1;
			}
		}
		if(this.actionTextbox.textboxKeyTyped(par1, key)) {
			selectedNode.action = this.actionTextbox.getText();
		}
		if(this.rewardTextbox.textboxKeyTyped(par1, key)) {
			selectedNode.reward = this.rewardTextbox.getText();
		}
		if(this.rewardQuantityTextbox.textboxKeyTyped(par1, key)) {
			try {
				selectedNode.rewardQuantity = Integer.parseInt(this.rewardQuantityTextbox.getText());
			}
			catch(NumberFormatException e) {
				selectedNode.rewardQuantity = -1;
			}
		}
		if(this.npcNameTextbox.textboxKeyTyped(par1, key)) {
			if(npcNameTextbox.getText().contains(defaultNPCName))
			{
				this.npcNameTextbox.setText("");
				this.npcNameTextbox.textboxKeyTyped(par1, key);
			}
			String name = this.npcNameTextbox.getText();
			this.target.setCustomNameTag(name);
		}
		
		switch(key) {
		case Keyboard.KEY_DOWN:
			if(selectedIndex < list.size() - 1) {
				selectedIndex++;
				selectedNode = list.get(selectedIndex);
				
				dialogueTextbox.setFocused(true);
				this.itemTextbox.setFocused(false);
				this.quantityTextbox.setFocused(false);
				this.actionTextbox.setFocused(false);
				this.rewardTextbox.setFocused(false);
				this.rewardQuantityTextbox.setFocused(false);
			}
			break;
		case Keyboard.KEY_UP:
			if(selectedIndex > 0) {
				selectedIndex--;
				selectedNode = list.get(selectedIndex);

				dialogueTextbox.setFocused(true);
				this.itemTextbox.setFocused(false);
				this.quantityTextbox.setFocused(false);
				this.actionTextbox.setFocused(false);
				this.rewardTextbox.setFocused(false);
				this.rewardQuantityTextbox.setFocused(false);
			}
			break;
		case Keyboard.KEY_DELETE:
			if(selectedNode != tree) {
				for(DialogueTreeNode node : list) {
					node.children.remove(selectedNode);
				}
			}
			return;
		case Keyboard.KEY_RETURN:
			selectedNode.addChild();
			break;
		}
		
		super.keyTyped(par1, key);
	}
}
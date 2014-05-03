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
import emd24.rpgmod.packets.DialogueRewardPacket;
import emd24.rpgmod.packets.GUIOpenPacket;
import emd24.rpgmod.packets.ScriptActionPacket;
import emd24.rpgmod.quest.DialogueTreeNode;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class GUIDialogue extends GuiScreen
{
	protected String npcName;
	
	DialogueTreeNode tree;
	
	DialogueTreeNode selectedNode;
	
	int backgroundColor;
	
	EntityLiving target;
	
	public GUIDialogue(EntityLiving target, String dialogue)
	{
		
		backgroundColor = 0x60ff0000;
		
		this.target = target;
		if(target.hasCustomNameTag())
			npcName = target.getCustomNameTag();
		else
			npcName = "NPC NAME NOT AVAILABLE!";
		
		tree = new DialogueTreeNode();
		tree.load(dialogue);
		
		selectedNode = tree;
	}
	
	protected int getItemQuantityInventory(Item item, InventoryPlayer inventory) {
		ItemStack itemStack = new ItemStack(item, 1);
		int quantityOnHand = 0;
		for(ItemStack test : inventory.mainInventory) {
			if(test != null) {
				if(test.isItemEqual(itemStack)) {
					quantityOnHand =+ test.stackSize;
				}
			}
		}
		return quantityOnHand;
	}
	
	protected boolean checkForItem(String itemName, Integer quantity){
		if (itemName == null || itemName.equals(""))
			return true;
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		InventoryPlayer inventory = player.inventory;
		Item item = (Item) Item.itemRegistry.getObject(itemName);
		if(item == null)
			return false;
		Integer quantityOnHand = getItemQuantityInventory(item, inventory);
		return quantityOnHand >= quantity;
	}
	
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		for(int i = 0; i < selectedNode.children.size(); i++) {
			DialogueTreeNode child = selectedNode.children.get(i);
			String item = child.itemNeeded;
			Integer quantity = child.itemQuantity;
			if(checkForItem(item, quantity)) {
				String text = child.dialogueText;
				buttonList.add(new GuiButton(i, 50, i*30 + 80, text));
			}
		}
		
	}
	
	protected void removePlayerItem() {
		String itemName = selectedNode.itemNeeded;
		Integer quantity = (-1) * selectedNode.itemQuantity; //multiple by (-1) to denote a removal of item
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		
		if(!itemName.equals("") && quantity < 0)
		{
			Integer playerID = player.getEntityId();
			DialogueRewardPacket message = new DialogueRewardPacket(playerID, itemName, quantity);
			RPGMod.packetPipeline.sendToServer(message);
		}
	}
	
	protected void givePlayerItem()
	{
		String itemName = selectedNode.reward;
		Integer quantity = selectedNode.rewardQuantity;
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		
		if(!itemName.equals("") && quantity > 0)
		{
			Integer playerID = player.getEntityId();
			DialogueRewardPacket message = new DialogueRewardPacket(playerID, itemName, quantity);
			RPGMod.packetPipeline.sendToServer(message);
		}
		/*
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		
		Item item = (Item) Item.itemRegistry.getObject(itemName);
		player.inventory.addItemStackToInventory(new ItemStack(item, quantity));*/
	}
	
	protected void runScriptOnSelectedNode()
	{
		Integer entityID = this.target.getEntityId();
		String scriptName = selectedNode.action;

		if((!scriptName.equals("")) && entityID != 0)
		{
			ScriptActionPacket message = new ScriptActionPacket(entityID, scriptName);
			RPGMod.packetPipeline.sendToServer(message);
		}
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id < selectedNode.children.size()) {
			selectedNode = selectedNode.children.get(guibutton.id);
			runScriptOnSelectedNode();
			givePlayerItem();
			removePlayerItem();
			selectedNode = selectedNode.children.get(0); //TODO: go through children looking for condition to be true
			runScriptOnSelectedNode();
			givePlayerItem();
			removePlayerItem();
			initGui();
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
	}
	
	public void drawScreen(int par1, int j, float f)
	{
		drawDefaultBackground();

		//draw header
		drawRect(20, 20, width - 20, height - 20, 0x60ff0000);
		int h = 30;
		drawCenteredString(fontRendererObj, npcName, width / 2, h, 0xffffffff);
		drawHorizontalLine(20, 50, 500, 0xffffffff);


		//draw dialogue
		drawString(fontRendererObj, selectedNode.dialogueText, 30, 60, 0xffffffff);

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
		
		super.keyTyped(par1, key);
	}
}
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
import emd24.rpgmod.packets.ScriptActionPacket;
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
	
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		for(int i = 0; i < selectedNode.children.size(); i++) {
			String text = selectedNode.children.get(i).dialogueText;
			buttonList.add(new GuiButton(i, 50, i*30 + 80, text));
		}
		
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
			selectedNode = selectedNode.children.get(0); //TODO: go through children looking for condition to be true
			runScriptOnSelectedNode();
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
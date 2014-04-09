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
	ExtendedEntityLivingDialogueData data;
	
	public GUIDialogue(EntityLiving target)
	{
		
		backgroundColor = 0x60ff0000;
		
		this.target = target;
		if(target.hasCustomNameTag())
			npcName = target.getCustomNameTag();
		else
			npcName = "NPC NAME NOT AVAILABLE!";
		
		data = ExtendedEntityLivingDialogueData.get(target);
		
		tree = data.dialogueTree;
		
		selectedNode = tree;
	}
	
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		for(int i = 0; i < selectedNode.children.size(); i++) {
			String text = selectedNode.children.get(i).dialogueText;
			buttonList.add(new GuiButton(i, 40, i*30 + 50, text));
		}
		
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id < selectedNode.children.size()) {
			selectedNode = selectedNode.children.get(guibutton.id);
			selectedNode = selectedNode.children.get(0); //TODO: go through children looking for condition to be true
			initGui();
		}
	}
	
	public boolean doesGuiPauseGame()
	{
		return true;
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
		drawCenteredString(fontRendererObj, selectedNode.dialogueText, width / 2, 40, 0xffffffff);

		super.drawScreen(par1, j, f);
	}
	
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		
		//Save NBT Data
		NBTTagCompound compound = new NBTTagCompound(); 
		data.saveNBTData(compound);
	}
	
	/*
	 * Handle Keyboard Input - 
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput()
	 */
	public void keyTyped(char par1, int key) {
		
		super.keyTyped(par1, key);
	}
}
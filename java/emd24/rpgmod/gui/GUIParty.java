package emd24.rpgmod.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.PartyDataPacket;
import emd24.rpgmod.packets.PartyInvitePacket;
import emd24.rpgmod.party.PartyManagerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import emd24.rpgmod.party.PartyPlayerNode;

/**
 * This class is the GUI that shows the party interface. Based on code by Evan Dyke.
 * 
 * @author Erik Harvey
 *
 */
public class GUIParty extends GuiScreen {

	// Keep track of who we are
	private EntityPlayer player;
	private int playerPartyID;
	
	// List of all players and their parties on the server
	ArrayList<PartyPlayerNode> list;
	
	// Variables for large-focused layout
	private int pageSize;
	private int currentPage;
	private int maxPage;
	private final int textHeight = 20;
	private final int slotHeight = 30;
	
	// Hold screen space co-ords to aid readability
	private int fivePercentWidth;
	private int tenPercentWidth;
	private int fifteenPercentWidth;
	private int eightyPercentWidth;
	private int fivePercentHeight;
	private int tenPercentHeight = fivePercentHeight * 2;
	private int nintyPercentHeight;
	
	// Buttons
	GuiButton leaveBtn;
	GuiButton[] promoteBtns;
	GuiButton[] kickBtns;
	GuiButton[] inviteBtns;
	
	String promote = "Promote";
	String kick = "Kick";
	String invite = "Invite";
	String leave = "Leave Party";
	String done = "Done";
	
	public GUIParty(EntityPlayer player){
		this.player = player;
	}
	
	public void initGui()
	{
		//Initialize and figure out page size
		currentPage = 0;
		pageSize = (int)(height * 0.8) / slotHeight;
		buttonList.clear();
	}
	
	// having thesew methods here allows me to mock the functionality in test, I cannot override static methods
	// and it is too hard to spin up the Minecraft class correctly
	public String getInvitingPlayerName() {
		return Minecraft.getMinecraft().thePlayer.getCommandSenderName();
	}

	public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
		super.drawString(par1FontRenderer, par2Str, par3, par4, par5);
	}
	
	public void drawCenteredString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
		super.drawCenteredString(par1FontRenderer, par2Str, par3, par4, par5);
	}

	public void drawDefaultBackground() {
		super.drawDefaultBackground();
	}
	
	public void drawRectNotStatic(int par0, int par1, int par2, int par3, int par4)  {
		drawRect(par0, par1, par2, par3, par4);
	}

	public void actionPerformed(GuiButton guibutton)
	{
		//We need to take the button id, figure out which player that entry maps
		//to and then send the invite to the player.
		String invitingPlayer = getInvitingPlayerName();
		PartyInvitePacket packet = null;
		PartyPlayerNode curr = null;
		int type = 0;
		
		// Close the gui
		if (guibutton.id == -1){
			this.mc.displayGuiScreen(null);
		}
		// Leave party
		else if (guibutton.id == 0){
			type = 2;
			packet = new PartyInvitePacket(type, invitingPlayer, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
		}
		// Promote buttons
		else if (guibutton.id <= pageSize){
			curr = list.get(guibutton.id - 1 + currentPage * pageSize);
			type = 3;
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
		}
		// Kick buttons
		else if (guibutton.id <= pageSize * 2){
			curr = list.get(guibutton.id - pageSize - 1 + currentPage * pageSize);
			type = 2;
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
		}
		// Invite Buttons
		else {
			curr = list.get(guibutton.id - (pageSize * 2) - 1 + currentPage * pageSize);
			type = 1;	
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
		}
	}
	


	public void keyTyped(char par1, int key) {
		switch(key) {
		// Move back a page
		case Keyboard.KEY_UP:
		case Keyboard.KEY_LEFT:
			if(currentPage > 0) {
				currentPage--;
				addButtons();
			}
			break;
		// Move forward a page
		case Keyboard.KEY_DOWN:
		case Keyboard.KEY_RIGHT:
			if(currentPage < maxPage){
				currentPage++;
				addButtons();
			}
			break;
		// Close
		case Keyboard.KEY_P:
			this.mc.displayGuiScreen(null);
			break;
		}
		super.keyTyped(par1, key);
	}
	
	
	@Override
	public void drawScreen(int i, int j, float f){
		// Get the map of players
		list = new ArrayList<PartyPlayerNode>();
		PartyPlayerNode currNode;
		Set<Map.Entry<String, Integer>> papEntrySet = PartyManagerClient.playerParty.entrySet();
		Iterator itr = papEntrySet.iterator();
		// Translate the map to a list and catch what our party number is
		while(itr.hasNext()){
			Map.Entry<String, Integer> entry = (Entry<String, Integer>) itr.next();
			if (entry.getKey().compareTo(player.getCommandSenderName()) == 0){
				this.playerPartyID = entry.getValue();
			}
			list.add(new PartyPlayerNode(entry.getKey(), entry.getValue()));
		}
		
		// Figure out our pages and add the buttons for that
		maxPage = list.size() / pageSize;
		Collections.sort(list);
		addButtons();

		// Draw background and header
		drawDefaultBackground();
		drawRectNotStatic(fivePercentWidth, fivePercentHeight, width - fivePercentWidth,
				height - fivePercentHeight, 0xffdddddd);
		drawCenteredString(fontRendererObj, "Party", width / 2, fivePercentHeight * 2, 0xffffffff);
		drawString(fontRendererObj, "Page: " + (currentPage + 1) + " of " + 
				(maxPage + 1), tenPercentWidth, nintyPercentHeight - textHeight, 0xffffffff);
		
		// Check for Leave Party Button
		if (playerPartyID == 0){
			leaveBtn.enabled = false;
		} else {
			leaveBtn.enabled = true;
		}
		
		// Fill out each slot
		for(int k = 0; k < pageSize; k++){
			// Make sure that we have somebody to put in this slot
			if(k + currentPage * pageSize < list.size()){
				//Add the player name and their party number to the display
				PartyPlayerNode curr = list.get(k + currentPage * pageSize);
				drawString(fontRendererObj, curr.playerName, tenPercentWidth,
						(k + 1) * slotHeight + fivePercentHeight * 2, 0xffffffff);
				drawString(fontRendererObj, "" + Math.abs(curr.partyID), 
						(int)(tenPercentWidth + eightyPercentWidth * .25), 
						(k + 1) * slotHeight + fivePercentHeight * 2, 0xffffffff);
				// If we are the leader and this person is in our party
				if(playerPartyID < 0 && Math.abs(playerPartyID) == curr.partyID){
					promoteBtns[k].enabled = true;
					promoteBtns[k].visible = true;
					kickBtns[k].enabled = true;
					kickBtns[k].visible = true;
					inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// If it's ourself
				else if (curr.playerName.compareTo(player.getCommandSenderName()) == 0){
					promoteBtns[k].enabled = false;
					promoteBtns[k].visible = false;
					kickBtns[k].enabled = false;
					kickBtns[k].visible = false;
					inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// We aren't the leader, but this person is in our party
				else if (playerPartyID != 0 && playerPartyID == Math.abs(curr.partyID)){ 
					promoteBtns[k].enabled = false;
					promoteBtns[k].visible = true;
					kickBtns[k].enabled = false;
					kickBtns[k].visible = true;
					inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// This person is in a different party
				else if (Math.abs(curr.partyID)!= 0){
					promoteBtns[k].visible = false;
					kickBtns[k].visible = false;
					inviteBtns[k].enabled = false;
					inviteBtns[k].visible = true;
				}
				// This person isn't in any party
				else {
					promoteBtns[k].enabled = false;
					promoteBtns[k].visible = false;
					kickBtns[k].enabled = false;
					kickBtns[k].visible = false;
					inviteBtns[k].enabled = true;
					inviteBtns[k].visible = true;
				}
			} 
			// We don't have a list item to fill this spot
			else {
				promoteBtns[k].enabled = false;
				promoteBtns[k].visible = false;
				kickBtns[k].enabled = false;
				kickBtns[k].visible = false;
				inviteBtns[k].enabled = false;
				inviteBtns[k].visible = false;
			}
		}
		super.drawScreen(i, j, f);
	}
	
	void setPercentVariables(){
		eightyPercentWidth = (int)(this.width * 0.8);
		tenPercentWidth = (int)(this.width * 0.1);
		fifteenPercentWidth = (int)(this.width * 0.15);
		fivePercentWidth = (int)(this.width * 0.05);
		fivePercentHeight = (int)(this.height * 0.05);
		nintyPercentHeight = (int)(this.height * 0.9);
	}
	
	void addButtons(){
		// Reset our buttons
		buttonList.clear();
		// Add our two sets of buttons
		addDoneLeaveButtons();
		addPromoteKickInviteButtons();
	}
	
	void addDoneLeaveButtons(){
		// Make sure that our percents are for the current screen dimensions
		setPercentVariables();
		// The leave party and leave menu buttons aren't in slots
		this.leaveBtn = new GuiButton(0, (int)(tenPercentWidth + eightyPercentWidth * .75),
				nintyPercentHeight - textHeight, 
				fifteenPercentWidth + fivePercentWidth, textHeight, leave);
		this.buttonList.add(leaveBtn);
		this.buttonList.add(new GuiButton(-1, width / 2 - (fifteenPercentWidth + fivePercentWidth) / 2,
				nintyPercentHeight - textHeight, 
				fifteenPercentWidth + fivePercentWidth, textHeight, done));	
	}
	
	void addPromoteKickInviteButtons(){
		// Make sure that our percents are for the current screen dimensions
		setPercentVariables();
		
		// Initialize our arrays for the buttons
		promoteBtns = new GuiButton[pageSize];
		kickBtns = new GuiButton[pageSize];
		inviteBtns = new GuiButton[pageSize];

		// Populate the arrays/slots
		for(int z = 1; z <= pageSize; z++){
			// Set up the promote buttons
			promoteBtns[z - 1] = new GuiButton(z, 
					(int) (tenPercentWidth + eightyPercentWidth * 0.5), 
					z * slotHeight + fivePercentHeight * 2, 
					fifteenPercentWidth, textHeight, promote);
			promoteBtns[z - 1].enabled = false;
			promoteBtns[z - 1].visible = false;
			buttonList.add(promoteBtns[z - 1]);
			
			// Set up the kick buttons
			kickBtns[z - 1] = new GuiButton(z + pageSize,
					(int) (tenPercentWidth + eightyPercentWidth * 0.75),
					z * slotHeight + fivePercentHeight * 2,
					fifteenPercentWidth, textHeight, kick);
			kickBtns[z - 1].enabled = false;
			kickBtns[z - 1].visible = false;
			buttonList.add(kickBtns[z - 1]);
			
			// Set up the invite buttons
			inviteBtns[z - 1] = new GuiButton(z + 2 * pageSize,
					(int) (tenPercentWidth + eightyPercentWidth * 0.75),
					z * slotHeight + fivePercentHeight * 2,
					fifteenPercentWidth, textHeight, invite);
			inviteBtns[z - 1].enabled = false;
			inviteBtns[z - 1].visible = false;
			buttonList.add(inviteBtns[z - 1]);
		}
	}
}
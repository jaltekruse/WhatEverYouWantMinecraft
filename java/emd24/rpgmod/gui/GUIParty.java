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

	private EntityPlayer player;
	private int playerPartyID;
	
	ArrayList<PartyPlayerNode> list;
	private int pageSize;
	private int currentPage;
	private int maxPage;
	private final int textHeight = 20;
	private final int slotHeight = 30;
	private int eightyPercentWidth;
	private int tenPercentWidth;
	private int fifteenPercentWidth;
	private int fivePercentWidth;
	private int fivePercentHeight;
	private int nintyPercentHeight;
	
	HashMap<String, Integer> pAP = PartyManagerClient.playerParty;
	
	/* TODO: IF WE GET MORE/LESS SCREEN REAL ESTATE WITH RESOLUTION CHANGES
	 * THESE ARRAYS NEED TO TURN INTO ARRAY LISTS AND WE NEED TO EDIT SOME 
	 * MATH SO WE GET A NICE LOOKING GUI */
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
		currentPage = 0;
		pageSize = (int)(height * 0.8) / slotHeight;
		//System.out.println("\n\nScreenwidth: " + this.width + "\nScreenHeight: " + this.height);
		buttonList.clear();
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		//We need to take the button id, figure out which player that entry maps
		//to and then send the invite to the player.
		String invitingPlayer = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
		PartyInvitePacket packet = null;
		PartyPlayerNode curr = null;
		int type = 0;
		//System.out.println("\n\nParty guibutton.id: " + guibutton.id);
		
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
			// They're from the same party, so it's a kick.
			type = 2;
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
		}
		// Invite Buttons
		else {
			curr = list.get(guibutton.id - (pageSize * 2) - 1 + currentPage * pageSize);
			// It's an invite!
			type = 1;	
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
		}
	}
	

	// Many thanks to Wes for this code structure.
	public void keyTyped(char par1, int key) {
		switch(key) {
		case Keyboard.KEY_UP:
		case Keyboard.KEY_LEFT:
			if(currentPage > 0) {
				currentPage--;
				addButtons();
			}
			break;
		case Keyboard.KEY_DOWN:
		case Keyboard.KEY_RIGHT:
			if(currentPage < maxPage){
				currentPage++;
				addButtons();
			}
			break;
		case Keyboard.KEY_P:
			this.mc.displayGuiScreen(null);
			break;
		}
		super.keyTyped(par1, key);
	}
	
	
	@Override
	public void drawScreen(int i, int j, float f){
		
		list = new ArrayList<PartyPlayerNode>();
		PartyPlayerNode currNode;
		Set<Map.Entry<String, Integer>> papEntrySet = pAP.entrySet();
		Iterator itr = papEntrySet.iterator();
		while(itr.hasNext()){
			Map.Entry<String, Integer> entry = (Entry<String, Integer>) itr.next();
			if (entry.getKey().compareTo(player.getCommandSenderName()) == 0){
				this.playerPartyID = entry.getValue();
			}
			list.add(new PartyPlayerNode(entry.getKey(), entry.getValue()));
		}
		
		maxPage = list.size() / pageSize;
		
		Collections.sort(list);
		
		addButtons();

		// Draw background and header
		drawDefaultBackground();
		drawRect(fivePercentWidth, fivePercentHeight, width - fivePercentWidth,
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
		
		for(int k = 0; k < pageSize; k++){
			// Make sure that we have somebody to put in this slot
			if(k + currentPage * pageSize < list.size()){
				PartyPlayerNode curr = list.get(k + currentPage * pageSize);
				drawString(fontRendererObj, curr.playerName, tenPercentWidth,
						(k + 1) * slotHeight + fivePercentHeight * 2, 0xffffffff);
				drawString(fontRendererObj, "" + curr.partyID, 
						(int)(tenPercentWidth + eightyPercentWidth * .25), 
						(k + 1) * slotHeight + fivePercentHeight * 2, 0xffffffff);
				// We are the leader and this person is in our party
				if(playerPartyID < 0 && Math.abs(playerPartyID) == curr.partyID){
					promoteBtns[k].enabled = true;
					promoteBtns[k].visible = true;
					kickBtns[k].enabled = true;
					kickBtns[k].visible = true;
					inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// Hey! It's ourself.
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
		buttonList.clear();
		addDoneLeaveButtons();
		addPromoteKickInviteButtons();
	}
	
	void addDoneLeaveButtons(){
		setPercentVariables();
		leaveBtn = new GuiButton(0, (int)(tenPercentWidth + eightyPercentWidth * .75),
				nintyPercentHeight - textHeight, 
				fifteenPercentWidth + fivePercentWidth, textHeight, leave);
		this.buttonList.add(leaveBtn);
		this.buttonList.add(new GuiButton(-1, width / 2 - (fifteenPercentWidth + fivePercentWidth) / 2,
				nintyPercentHeight - textHeight, 
				fifteenPercentWidth + fivePercentWidth, textHeight, done));	
	}
	
	void addPromoteKickInviteButtons(){
		setPercentVariables();
		promoteBtns = new GuiButton[pageSize];
		kickBtns = new GuiButton[pageSize];
		inviteBtns = new GuiButton[pageSize];

		int z = 1;
		for(GuiButton e: promoteBtns){
			promoteBtns[z - 1] = new GuiButton(z, 
					(int) (tenPercentWidth + eightyPercentWidth * 0.5), 
					z * slotHeight + fivePercentHeight * 2, 
					fifteenPercentWidth, textHeight, promote);
			promoteBtns[z - 1].enabled = false;
			promoteBtns[z - 1].visible = false;
			buttonList.add(promoteBtns[z - 1]);
			z++;
		}
		z = 1;
		for(GuiButton e: kickBtns){
			kickBtns[z - 1] = new GuiButton(z + pageSize,
					(int) (tenPercentWidth + eightyPercentWidth * 0.75),
					z * slotHeight + fivePercentHeight * 2,
					fifteenPercentWidth, textHeight, kick);
			kickBtns[z - 1].enabled = false;
			kickBtns[z - 1].visible = false;
			buttonList.add(kickBtns[z - 1]);
			z++;
		}
		z = 1;
		for(GuiButton e: inviteBtns){
			inviteBtns[z - 1] = new GuiButton(z + 2 * pageSize,
					(int) (tenPercentWidth + eightyPercentWidth * 0.75),
					z * slotHeight + fivePercentHeight * 2,
					fifteenPercentWidth, textHeight, invite);
			inviteBtns[z - 1].enabled = false;
			inviteBtns[z - 1].visible = false;
			buttonList.add(inviteBtns[z - 1]);
			z++;
		}
	}
}

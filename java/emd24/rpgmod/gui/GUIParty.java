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

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.PartyDataPacket;
import emd24.rpgmod.packets.PartyInvitePacket;
import emd24.rpgmod.party.PartyManagerClient;
/*
import emd24.minecraftrpgmod.skills.Skill;
import emd24.minecraftrpgmod.skills.SkillManagerServer;
import emd24.minecraftrpgmod.skills.SkillPlayer;
*/
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import emd24.rpgmod.party.PartyPlayerNode;

/**
 * This class is the GUI that shows the party interface. Based on code by Evan Dyke.
 * 
 * @author Erik Harvey
 *
 */
public class GUIParty extends GuiScreen {

	/*These might need to be adjusted if we update the resource*/
	public final int xSizeOfTexture = 176;
	public final int ySizeOfTexture = 166;
	
	public static final ResourceLocation resource = new ResourceLocation("RPGMod", "textures/gui/");
	private EntityPlayer player;
	private int playerPartyID;
	
	ArrayList<PartyPlayerNode> list;
	private final int pageSize = 7;
	private int currentPage;
	private int maxPage;
	
	HashMap<String, Integer> pAP = PartyManagerClient.playerParty;
	
	/* TODO: IF WE GET MORE/LESS SCREEN REAL ESTATE WITH RESOLUTION CHANGES
	 * THESE ARRAYS NEED TO TURN INTO ARRAY LISTS AND WE NEED TO EDIT SOME 
	 * MATH SO WE GET A NICE LOOKING GUI */
	GuiButton leaveBtn;
	GuiButton pBtn1, pBtn2, pBtn3, pBtn4, pBtn5, pBtn6, pBtn7;
	GuiButton kBtn1, kBtn2, kBtn3, kBtn4, kBtn5, kBtn6, kBtn7;
	GuiButton iBtn1, iBtn2, iBtn3, iBtn4, iBtn5, iBtn6, iBtn7;
	GuiButton[] promoteBtns = {pBtn1, pBtn2, pBtn3, pBtn4, pBtn5, pBtn6, pBtn7};
	GuiButton[] kickBtns = {kBtn1, kBtn2, kBtn3, kBtn4, kBtn5, kBtn6, kBtn7};
	GuiButton[] inviteBtns = {iBtn1, iBtn2, iBtn3, iBtn4, iBtn5, iBtn6, iBtn7};
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
		
		buttonList.clear();
		
		// Set up promote and kick/invite buttons
		int i = 1;
		for(GuiButton e: promoteBtns){
			promoteBtns[i - 1] = new GuiButton(i, 250, 20 * i + 30, 50, 20, promote);
			promoteBtns[i - 1].enabled = false;
			promoteBtns[i - 1].visible = false;
			buttonList.add(promoteBtns[i - 1]);
			i++;
		}
		i = 1;
		for(GuiButton e: kickBtns){
			kickBtns[i - 1] = new GuiButton(i + pageSize, 310, 20 * i + 30, 50, 20, kick);
			kickBtns[i - 1].enabled = false;
			kickBtns[i - 1].visible = false;
			buttonList.add(kickBtns[i - 1]);
			i++;
		}
		i = 1;
		for(GuiButton e: inviteBtns){
			inviteBtns[i - 1] = new GuiButton(i + 2 * pageSize, 310, 20 * i + 30, 50, 20, invite);
			inviteBtns[i - 1].enabled = false;
			inviteBtns[i - 1].visible = false;
			buttonList.add(inviteBtns[i - 1]);
			i++;
		}
		leaveBtn = new GuiButton(0, width - 140, 195, 100, 20, leave);
		buttonList.add(leaveBtn);
		buttonList.add(new GuiButton(-1, width / 2 - 50, 195, 100, 20, done));		
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		//We need to take the button id, figure out which player that entry maps
		//to and then send the invite to the player.
		String invitingPlayer = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
		PartyInvitePacket packet;
		PartyPlayerNode curr;
		int type;
		//System.out.println("\n\nParty guibutton.id: " + guibutton.id);
		switch(guibutton.id){
		// Close the gui
		case -1:
			this.mc.displayGuiScreen(null);
			break;
		// Leave party
		case 0:
			type = 2;
			packet = new PartyInvitePacket(type, invitingPlayer, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
			break;
		// Promote buttons
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			// Promote button time stuff
			break;
		// Kick buttons
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
			curr = list.get(guibutton.id - pageSize - 1 + currentPage * pageSize);
			// They're from the same party, so it's a kick.
			type = 2;
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
			break;
		// Invite Buttons
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
			curr = list.get(guibutton.id - (pageSize * 2) - 1 + currentPage * pageSize);
			// It's an invite!
			type = 1;
			packet = new PartyInvitePacket(type, curr.playerName, invitingPlayer);
			RPGMod.packetPipeline.sendToServer(packet);
			break;
		}
		
		
		/* LEAVING THIS FOR REFERENCE
		Iterator itPaP = list.iterator();
		int h = guibutton.id - 1;
		int type = 1;
		if(h >= pAP.size()) {
			h -= pAP.size();
			type = 2;
		}
		for (int i = 0; i < h; i++){
			itPaP.next();
		}
		Map.Entry<String, Integer> entry = (Entry<String, Integer>) itPaP.next();
		String playerName = entry.getKey();
		
		//Send packet
		//String invitingPlayer = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
		PartyInvitePacket packet = new PartyInvitePacket(type, playerName, invitingPlayer);
		RPGMod.packetPipeline.sendToServer(packet);*/
	}
	

	// Many thanks to Wes for this code structure.
	public void keyTyped(char par1, int key) {
		switch(key) {
		case Keyboard.KEY_UP:
		case Keyboard.KEY_LEFT:
			if(currentPage > 0) {
				currentPage--;
			}
			break;
		case Keyboard.KEY_DOWN:
		case Keyboard.KEY_RIGHT:
			if(currentPage < maxPage){
				currentPage++;
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
		
		/* TODO: ADD CURRENT_PAGE/MAX_PAGE DISPLAY */
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
		
		// Draw background and header
		drawDefaultBackground();
		drawRect(20, 20, width - 20, height - 20, 0xffdddddd);
		drawCenteredString(fontRendererObj, "Party", width / 2 - 10, 30, 0xffffffff);
		drawString(fontRendererObj, "Page: " + (currentPage + 1) + " of " + 
				(maxPage + 1), 60, 200, 0xffffffff);
		// Check for Leave Party Button
		if (playerPartyID == 0){
			leaveBtn.enabled = false;
		} else {
			leaveBtn.enabled = true;
		}
		
		for(int k = 0; k < 7; k++){
			// Make sure that we have somebody to put in this slot
			if(k + currentPage * pageSize < list.size()){
				PartyPlayerNode curr = list.get(k + currentPage * pageSize);
				drawString(fontRendererObj, curr.playerName, 50, (k + 1) * 20 + 35, 0xffffffff);
				drawString(fontRendererObj, "" + Math.abs(curr.partyID), 150, (k + 1) * 20 + 35, 0xffffffff);
				// We are the leader and this person is in our party
				if(playerPartyID < 0 && Math.abs(playerPartyID) == curr.partyID){
					promoteBtns[k].enabled = true;
					promoteBtns[k].visible = true;
					kickBtns[k].enabled = true;
					kickBtns[k].visible = true;
					//TODO: Untested, but if it isn't visible, it shouldn't be enabled.
					//inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// Hey! It's ourself.
				else if (curr.playerName.compareTo(player.getCommandSenderName()) == 0){
					//promoteBtns[k].enabled = false;
					promoteBtns[k].visible = false;
					//kickBtns[k].enabled = false;
					kickBtns[k].visible = false;
					//inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// We aren't the leader, but this person is in our party
				else if (playerPartyID != 0 && playerPartyID == Math.abs(curr.partyID)){ 
					promoteBtns[k].enabled = false;
					promoteBtns[k].visible = true;
					kickBtns[k].enabled = false;
					kickBtns[k].visible = true;
					//inviteBtns[k].enabled = false;
					inviteBtns[k].visible = false;
				} 
				// This person isn't any our party
				else {
					//promoteBtns[k].enabled = false;
					promoteBtns[k].visible = false;
					//kickBtns[k].enabled = false;
					kickBtns[k].visible = false;
					inviteBtns[k].enabled = true;
					inviteBtns[k].visible = true;
				}
			} 
			// We don't have a list item to fill this spot
			else {
				//promoteBtns[k].enabled = false;
				promoteBtns[k].visible = false;
				//kickBtns[k].enabled = false;
				kickBtns[k].visible = false;
				//inviteBtns[k].enabled = false;
				inviteBtns[k].visible = false;
			}
		}
		super.drawScreen(i, j, f);
	}
}

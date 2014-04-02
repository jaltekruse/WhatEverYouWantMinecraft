package emd24.rpgmod.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.PacketHandlerClient;
import emd24.rpgmod.PacketHandlerServer;
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
	
	//private GuiControlsScrollPanel scrollPane;
	
	HashMap<String, Integer> pAP = PartyManagerClient.playerParty;
	Set<Map.Entry<String, Integer>> playersAndParties;
	
	public GUIParty(EntityPlayer player){
		this.player = player;
	}
	
	public void initGui()
	{
		//scrollPane = new GuiControlsScrollPanel(this, options, mc);

		buttonList.clear();
		playersAndParties = pAP.entrySet();
		Iterator itPaP = playersAndParties.iterator();
		int h = 1;
		// This is a janky way of doing this. Look at MC's Control's page to
		// understand how to actually set this up correctly with a scroll bar.
		while (itPaP.hasNext()){
			itPaP.next();
			buttonList.add(new GuiButton(h, 250, h * 20 + 45, 50, 20, "Invite"));
			buttonList.add(new GuiButton(h + pAP.size(), 310, h * 20 + 45, 50, 20, "Kick"));
			h++;
		}
		
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		//We need to take the button id, figure out which player that entry maps
		//to and then send the invite to the player.
		Iterator itPaP = playersAndParties.iterator();
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
		String invitingPlayer = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
		PartyInvitePacket packet = new PartyInvitePacket(type, playerName, invitingPlayer);
		RPGMod.packetPipeline.sendToServer(packet);
	}
	
	@Override
	public void drawScreen(int i, int j, float f){
		drawDefaultBackground();
		
		drawRect(20, 20, width - 20, height - 20, 0xffdddddd);
		
		drawCenteredString(fontRendererObj, "Party", width / 2, 30, 0xffffffff);
		
		//Simplified Algo for Testing:
		//obtain a list of players in the world
		//for each player
		//draw name, draw party ID, draw invite button
		
		Iterator itPaP = playersAndParties.iterator();
		int h = 1;
		while (itPaP.hasNext()){
			Map.Entry<String, Integer> entry = (Entry<String, Integer>) itPaP.next();
			drawString(fontRendererObj, entry.getKey(), 50, h * 20 + 50, 0xffffffff);
			drawString(fontRendererObj, "" + entry.getValue(), 150, h * 20 + 50, 0xffffffff);
			// buttonList.add(new GuiButton(h, 250, h * 20 + 50, "Invite"));
			h++;
		}

		super.drawScreen(i, j, f);
	}
	/*
	if(inParty){
	putPartyMembersAtFrontOfList
	drawLeaveButton
	}
	draw an entry for each player
	if(!inOurParty){
	drawInviteButton
	} else if(inOurParty){
	if(weAreLeader){
	drawPromoteButton
	drawKickButton
	}
	drawLocation
	}
	}*/
	
	
	
	
}

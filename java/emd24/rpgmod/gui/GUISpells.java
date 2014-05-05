package emd24.rpgmod.gui;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.input.Keyboard;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.packets.DialogueRewardPacket;
import emd24.rpgmod.packets.GiveSpellPacket;
import emd24.rpgmod.packets.PartyInvitePacket;
import emd24.rpgmod.party.PartyPlayerNode;
import emd24.rpgmod.spells.Spell;
import emd24.rpgmod.spells.SpellRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Class is for GUI that shows a list of spells the player can cast. Based on code
 * based on Erik Harvey based on Evan Dyke. 
 * 
 * @author Owner
 *
 */
public class GUISpells extends GuiScreen{

	private final static int MAX_SPELLS_PER_PAGE = 9;

	ArrayList<Spell> spellList;
	private EntityPlayer player;
	private int pageSize;
	private int currentPage;
	private int maxPage;

	private int spellsPerRow;
	private int slotHeight = 50;
	private int slotWidth = 150;
	private Minecraft instance;

	// Hold screen space co-ords to aid readability
	private int borderWOffset;
	private int tenPercentWidth;
	private int fifteenPercentWidth;
	private int fifteenPercentHeight;
	private int eightyPercentWidth;
	private int borderHOffset;
	private int tenPercentHeight;
	private int nintyPercentHeight;



	public GUISpells(EntityPlayer player){
		this.player = player;
		this.instance = Minecraft.getMinecraft();

	}

	public void initGui()
	{
		currentPage = 0;
		spellsPerRow = (int) ((0.8 * width) / slotWidth);
		pageSize = Math.min((int) ((height - 2 * borderHOffset) / slotHeight) * spellsPerRow, MAX_SPELLS_PER_PAGE) ;
		//System.out.println("\n\nScreenwidth: " + this.width + "\nScreenHeight: " + this.height);
		buttonList.clear();
	}


	public void keyTyped(char par1, int key) {
		ItemStack item = null;
		switch(key) {
		// Move back a page
		case Keyboard.KEY_UP:
			break;
		case Keyboard.KEY_LEFT:
			if(currentPage > 0) {
				currentPage--;
			}
			break;
			// Move forward a page

		case Keyboard.KEY_DOWN:
		case Keyboard.KEY_RIGHT:
			if(currentPage < maxPage){
				currentPage++;
			}
			break;

			// Close
		case Keyboard.KEY_M:
			this.mc.displayGuiScreen(null);
			break;
		// Used for giving spells to player
		case Keyboard.KEY_1:
			sendPlayerItem(0);
			break;
		case Keyboard.KEY_2:
			sendPlayerItem(1);
			break;
		case Keyboard.KEY_3:
			sendPlayerItem(2);
			break;
		case Keyboard.KEY_4:
			sendPlayerItem(3);
			break;
		case Keyboard.KEY_5:
			sendPlayerItem(4);
			break;
		case Keyboard.KEY_6:
			sendPlayerItem(5);
			break;
		case Keyboard.KEY_7:
			sendPlayerItem(6);
			break;
		case Keyboard.KEY_8:
			sendPlayerItem(7);
			break;
		case Keyboard.KEY_9:
			sendPlayerItem(8);
			break;
		}


		super.keyTyped(par1, key);
	}

	@Override
	public void drawScreen(int i, int j, float f){

		setPercentVariables();

		// Get a list of all the spells
		spellList = new ArrayList<Spell>(SpellRegistry.getSpells());
		Collections.sort(spellList);

		maxPage = spellList.size() / MAX_SPELLS_PER_PAGE;

		// Draw the screen

		drawDefaultBackground();


		drawRect(borderWOffset, borderHOffset, width - borderWOffset, height - borderHOffset,
				0xffdddddd);

		drawCenteredString(fontRendererObj, "Spells", width / 2, borderHOffset * 3 / 2, 0xffffffff);

		renderText();

		renderIcons();
	}

	void setPercentVariables(){
		eightyPercentWidth = (int)(this.width * 0.8);
		tenPercentWidth = (int)(this.width * 0.1);
		fifteenPercentWidth = (int)(this.width * 0.15);
		fifteenPercentHeight = (int)(this.height * 0.15);
		borderWOffset = (int)(this.width * 0.1);
		borderHOffset = (int)(this.height * 0.1);
		tenPercentHeight = (int)(this.height * 0.1);
		nintyPercentHeight = (int)(this.height * 0.9);
	}

	/**
	 * Renders the text of the spells ot the screens
	 * 
	 */
	public void renderText(){
		for(int k = 0; k < pageSize; k++){

			int index = k + currentPage * pageSize;

			// Only want to display a maximum of 9 spells per page for using numeric keys
			if(index < spellList.size() && k < 9){
				Spell s = spellList.get(index);

				ExtendedPlayerData data = ExtendedPlayerData.get(player);
				int color = 0xffffffff;
				if(data.getSkill("Magic").getLevel() < s.getLevelRequired()){
					color = 0xffff0000;
				}


				// Determine offset of the spell slot to start drawing
				int offsetW = borderWOffset + (index % spellsPerRow) * slotWidth + (index % spellsPerRow + 1) * 
						(width - 2 * borderWOffset - spellsPerRow * slotWidth) / (spellsPerRow + 1);

				drawString(fontRendererObj, "" + (k + 1), offsetW,
						(k / spellsPerRow) * slotHeight + 2 * tenPercentHeight + slotHeight / 3, color);
				drawString(fontRendererObj, s.getItemStackDisplayName(new ItemStack(s)), offsetW + 48,
						(k / spellsPerRow) * slotHeight + 2 * tenPercentHeight + (slotHeight - 16) / 2, color);
				drawString(fontRendererObj, "Cost: " + s.getManaCost(), offsetW + 48,
						(k / spellsPerRow) * slotHeight + 2 * tenPercentHeight + (slotHeight - 16) / 2 + 16, color);

			}
		}
	}

	/**
	 * Draw the spell icons on the screen
	 * 
	 */
	public void renderIcons(){
		for(int k = 0; k < pageSize; k++){

			int index = k + currentPage * pageSize;

			// Only want to display a maximum of 9 spells per page for using numeric keys
			if(index < spellList.size() && k < 9){
				Spell s = spellList.get(index);

				// Find the location of the item icons and draw them to screen 
				ResourceLocation r = new ResourceLocation(RPGMod.MOD_ID, "textures/items/" + s.getUnlocalizedName().substring(5) + ".png");
				this.instance.getTextureManager().bindTexture(r);

				// Determine offset of the spell slot to start drawing
				int offsetW = borderWOffset + (index % spellsPerRow) * slotWidth + (index % spellsPerRow + 1) * 
						(width - 2 * borderWOffset - spellsPerRow * slotWidth) / (spellsPerRow + 1);

				this.itemRender.renderItemIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), 
						new ItemStack(s), offsetW + 16, (k / spellsPerRow) * slotHeight + 2 * tenPercentHeight + (slotHeight - 16) / 2);
			}


		}

	}
	
	private void sendPlayerItem(int k){
		String name = RPGMod.MOD_ID + ":" + spellList.get(currentPage * pageSize + k).getUnlocalizedName();
		DialogueRewardPacket packet = new DialogueRewardPacket(player.getEntityId(), name, 1);
		RPGMod.packetPipeline.sendToServer(packet);
		
	}

}

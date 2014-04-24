package emd24.rpgmod.gui;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.input.Keyboard;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.party.PartyPlayerNode;
import emd24.rpgmod.spells.Spell;
import emd24.rpgmod.spells.SpellRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
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
	private int fivePercentWidth;
	private int tenPercentWidth;
	private int fifteenPercentWidth;
	private int eightyPercentWidth;
	private int fivePercentHeight;
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
		pageSize = (int) ((height * 0.8 * spellsPerRow) / slotHeight);
		//System.out.println("\n\nScreenwidth: " + this.width + "\nScreenHeight: " + this.height);
		buttonList.clear();
	}

	public void keyTyped(char par1, int key) {
		switch(key) {
		// Move back a page
		case Keyboard.KEY_UP:
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
		}
		super.keyTyped(par1, key);
	}

	@Override
	public void drawScreen(int i, int j, float f){

		setPercentVariables();

		// Get a list of all the spells
		spellList = new ArrayList<Spell>(SpellRegistry.getSpells());
		Collections.sort(spellList);

		maxPage = spellList.size() / (2 * slotHeight);

		// Add buttons of the spells to the screen
		//addIcons();

		// Draw the screen

		drawDefaultBackground();

		drawRect(fivePercentWidth, fivePercentHeight, width - fivePercentWidth, height - fivePercentHeight,
				0xffdddddd);

		drawCenteredString(fontRendererObj, "Spells", width / 2, fivePercentHeight * 2, 0xffffffff);

		renderText();
		
		renderIcons();
	}

	void setPercentVariables(){
		eightyPercentWidth = (int)(this.width * 0.8);
		tenPercentWidth = (int)(this.width * 0.1);
		fifteenPercentWidth = (int)(this.width * 0.15);
		fivePercentWidth = (int)(this.width * 0.05);
		fivePercentHeight = (int)(this.height * 0.05);
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
				int offset = tenPercentWidth + (index % spellsPerRow) * slotWidth;


				drawString(fontRendererObj, s.getItemStackDisplayName(new ItemStack(s)), offset + 32,
						(k / spellsPerRow) * slotHeight + tenPercentHeight + (slotHeight - 16) / 2, color);
				drawString(fontRendererObj, "Cost: " + s.getManaCost(), offset + 32,
						(k / spellsPerRow) * slotHeight + + tenPercentHeight + (slotHeight - 16) / 2 + 16, color);

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
				int offset = tenPercentWidth + (index % spellsPerRow) * slotWidth;

				this.itemRender.renderItemIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), 
						new ItemStack(s), offset + 8, (k / spellsPerRow) * slotHeight + tenPercentHeight + (slotHeight - 16) / 2);
			}


		}

	}

}


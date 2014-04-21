package emd24.rpgmod.gui;

import java.util.ArrayList;
import java.util.Collections;

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
	private int slotHeight = 30;
	private Minecraft instance;
	
	
	
	public GUISpells(EntityPlayer player){
		this.player = player;
		this.instance = Minecraft.getMinecraft();
	}
	
	public void initGui()
	{
		currentPage = 0;
		pageSize = (int) ((height * 1.6) / slotHeight);
		//System.out.println("\n\nScreenwidth: " + this.width + "\nScreenHeight: " + this.height);
		buttonList.clear();
	}
	
	@Override
	public void drawScreen(int i, int j, float f){
		
		// Get a list of all the spells
		spellList = new ArrayList<Spell>(SpellRegistry.getSpells());
		Collections.sort(spellList);
		
		maxPage = spellList.size() / (2 * slotHeight);
		
		// Add buttons of the spells to the screen
		//addIcons();
		
		// Draw the screen
		
		drawDefaultBackground();

		drawRect(20, 20, width - 20, height - 20, 0xffdddddd);
		
		// Draw each spell on the screen
		for(int k = 0; k < pageSize; k++){
			if(k + currentPage * pageSize < spellList.size()){
				Spell s = spellList.get(k + currentPage * pageSize);
				ExtendedPlayerData data = ExtendedPlayerData.get(player);
				int color = 0xffffffff;
				if(data.getSkill("Magic").getLevel() < s.getLevelRequired()){
					color = 0xffff0000;
				}
				// Check which side of screen to draw on
				if(k % 2 == 0){
					// Find the location of the item icons and draw them to screen 
					ResourceLocation r = new ResourceLocation(RPGMod.MOD_ID, "textures/items/" + s.getUnlocalizedName().substring(5) + ".png");
					this.instance.getTextureManager().bindTexture(r);
					
					this.itemRender.renderItemIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), 
							new ItemStack(s), 50, (k + 1) * slotHeight + 30);
					drawString(fontRendererObj, s.getItemStackDisplayName(new ItemStack(s)), 75,
							(k + 1) * slotHeight + 30, color);
					drawString(fontRendererObj, "Cost: " + s.getManaCost(), 75,
							(k + 1) * slotHeight + 45, color);
				}
					
				else{
					ResourceLocation r = new ResourceLocation(RPGMod.MOD_ID, "textures/items/" + s.getUnlocalizedName().substring(5) + ".png");
					this.instance.getTextureManager().bindTexture(r);
					
					this.itemRender.renderItemIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), 
							new ItemStack(s), 200, k * slotHeight + 30);
					drawString(fontRendererObj, s.getItemStackDisplayName(new ItemStack(s)), 225,
							k * slotHeight + 30, color);
					drawString(fontRendererObj, "Cost: " + s.getManaCost(), 225,
							k * slotHeight + 45, color);
				}
			}
		}
	}
}

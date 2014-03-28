package emd24.minecraftrpgmod.gui;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import emd24.minecraftrpgmod.ExtendedPlayerData;
import emd24.minecraftrpgmod.RPGMod;
import emd24.minecraftrpgmod.skills.Skill;
import emd24.minecraftrpgmod.skills.SkillManagerServer;
import emd24.minecraftrpgmod.skills.SkillPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * This class is the GUI that shows the player's skills
 * 
 * @author Evan Dyke
 *
 */
public class GUISkills extends GuiScreen{
	
	public final int xSizeOfTexture = 176;
	public final int ySizeOfTexture = 166;
	
	public static final ResourceLocation resource = new ResourceLocation("RPGMod", "textures/gui/"); 
	private EntityPlayer player;
	
	
	public GUISkills(EntityPlayer player){
		this.player = player;
	}
	
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();

		drawRect(20, 20, width - 20, height - 20, 0xffdddddd);
		
		HashMap<String, SkillPlayer> playerSkills = ExtendedPlayerData.get(player).getSkillList();
		this.
		// Draw headings of the skill list
		drawString(fontRendererObj, "Skill", 50, 50, 0xffffffff);
		drawString(fontRendererObj, "Level", 150, 50, 0xffffffff);
		drawString(fontRendererObj, "Total Experience", 250, 50, 0xffffffff);
		drawString(fontRendererObj, "Next Level", 350, 50, 0xffffffff);
		
		// Draw the skill data on each row
		int h = 1;
		for(SkillPlayer skill : playerSkills.values())
		{
			drawString(fontRendererObj, skill.name, 50, h * 20 + 50, 0xffffffff);
			drawString(fontRendererObj, "" + skill.getLevel(), 150, h * 20 + 50, 0xffffffff);
			drawString(fontRendererObj, "" + skill.getExperience(), 250, h * 20 + 50, 0xffffffff);
			drawString(fontRendererObj, "" + SkillPlayer.expForLevel(skill.getLevel() + 1), 350, h * 20 + 50, 0xffffffff);
			h++;
		}
		

		super.drawScreen(i, j, f);
	}
}

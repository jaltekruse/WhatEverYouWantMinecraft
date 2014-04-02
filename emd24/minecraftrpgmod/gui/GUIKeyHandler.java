package emd24.minecraftrpgmod.gui;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class GUIKeyHandler extends KeyHandler {

	public static KeyBinding skillMenu = new KeyBinding("Skills",Keyboard.KEY_O);
	public static KeyBinding partyMenu = new KeyBinding("Party",Keyboard.KEY_P);
	public static KeyBinding whateverMenu = new KeyBinding("Whatever",Keyboard.KEY_L);
	
	public GUIKeyHandler() {
		super(new KeyBinding[]{skillMenu, partyMenu, whateverMenu},
				new boolean[]{false, false, false});
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {
		if(Minecraft.getMinecraft().currentScreen == null) {
			if(kb.keyCode == skillMenu.keyCode) {
				Minecraft.getMinecraft().displayGuiScreen(new GUISkills(Minecraft.getMinecraft().thePlayer));
			} else if(kb.keyCode == partyMenu.keyCode) {
				Minecraft.getMinecraft().displayGuiScreen(new GUIParty(Minecraft.getMinecraft().thePlayer));
			} else if(kb.keyCode == whateverMenu.keyCode) {
				Minecraft.getMinecraft().displayGuiScreen(new GUIWhatever());
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		// TODO Auto-generated method stub
		return EnumSet.of(TickType.CLIENT);
	}

}

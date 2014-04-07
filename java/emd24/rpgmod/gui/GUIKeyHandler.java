package emd24.rpgmod.gui;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class GUIKeyHandler{

	public static KeyBinding skillMenu = new KeyBinding("Skills",Keyboard.KEY_O, "GUI_open");
	public static KeyBinding partyMenu = new KeyBinding("Party",Keyboard.KEY_P, "GUI_open");
	public static KeyBinding whateverMenu = new KeyBinding("Whatever",Keyboard.KEY_L, "GUI_open");
	public static KeyBinding dialogueEditor = new KeyBinding("DialogueEditor",Keyboard.KEY_K, "GUI_open");
	
	public GUIKeyHandler() {
		ClientRegistry.registerKeyBinding(GUIKeyHandler.skillMenu);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.partyMenu);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.whateverMenu);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.dialogueEditor);
	}

	@SubscribeEvent
	public void tick(KeyInputEvent event) 
	{
		if(Minecraft.getMinecraft().currentScreen == null) {
			if(skillMenu.isPressed()) {
				Minecraft.getMinecraft().displayGuiScreen(new GUISkills(Minecraft.getMinecraft().thePlayer));
			} else if(partyMenu.isPressed()) {
				Minecraft.getMinecraft().displayGuiScreen(new GUIParty(Minecraft.getMinecraft().thePlayer));
			} else if(whateverMenu.isPressed()) {
				Minecraft.getMinecraft().displayGuiScreen(new GUIWhatever());
			}else if(dialogueEditor.isPressed()) {
				Minecraft.getMinecraft().displayGuiScreen(new GUIDialogueEditor());
			}
		}
	}

}

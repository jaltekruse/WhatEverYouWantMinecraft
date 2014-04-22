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
	public static KeyBinding magicMenu = new KeyBinding("Magic",Keyboard.KEY_M, "GUI_open");
	
	public static boolean npcAdminMode = false;
	
	public GUIKeyHandler() {
		ClientRegistry.registerKeyBinding(GUIKeyHandler.skillMenu);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.partyMenu);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.whateverMenu);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.dialogueEditor);
		ClientRegistry.registerKeyBinding(GUIKeyHandler.magicMenu);
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
			}else if(GUIKeyHandler.dialogueEditor.isPressed()) {
				npcAdminMode = ! npcAdminMode;
			} else if(magicMenu.isPressed()) {
				Minecraft.getMinecraft().displayGuiScreen(new GUISpells(Minecraft.getMinecraft().thePlayer));
			}
		}
	}

}

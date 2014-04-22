package emd24.rpgmod;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import emd24.rpgmod.gui.GUISkills;

public class CommonProxy {

	private static final Map<String, NBTTagCompound> extendedPlayerData = new HashMap<String, NBTTagCompound>();
	
	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics or entities!
	}
	
	public void registerKeys(){
		// Nothing happens here as we want keystuff to happen on client side
	}
	
	public static void storeEntityData(String name, NBTTagCompound compound)
	{
		extendedPlayerData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name)
	{
		return extendedPlayerData.remove(name);
	}
	
	//Data structure for temporary NBT Data
	private static final Map<Integer, String> dialogueData = new HashMap<Integer, String>();
	
	public static void storeDialogueData(Integer id, String dialogue)
	{
		dialogueData.put(id, dialogue);
	}

	public static String getDialogueData(Integer id)
	{
		return dialogueData.remove(id);
	}

	//Open dialogue on client side, do nothing on server
	public void openDialogueGUI(int entityID, String dialogue)
	{
		//do nothing, override in client
	}
}


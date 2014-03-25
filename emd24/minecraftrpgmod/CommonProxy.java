package emd24.minecraftrpgmod;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import emd24.minecraftrpgmod.gui.GUISkills;

public class CommonProxy {

	private static final Map<String, NBTTagCompound> extendedPlayerData = new HashMap<String, NBTTagCompound>();
	
	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics or entities!
	}
	
	public static void storeEntityData(String name, NBTTagCompound compound)
	{
	extendedPlayerData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name)
	{
	return extendedPlayerData.remove(name);
	}

}


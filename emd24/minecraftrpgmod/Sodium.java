package emd24.minecraftrpgmod;

import emd24.minecraftrpgmod.gui.GUISkills;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Sodium extends Block {

	public Sodium (Material material){
		super(material);
		
	}
	
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer){
		//ModLoader.openGUI(par5EntityPlayer, new GUISkills(par5EntityPlayer));
	}

//	@Override
//	public TileEntity createNewTileEntity(World world) {
//		// TODO Auto-generated method stub
//		return new TileEntity();
//	}
}

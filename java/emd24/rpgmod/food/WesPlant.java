package emd24.rpgmod.food;


import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.rpgmod.RPGMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.block.BlockCrops;

public class WesPlant extends BlockCrops
{
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149868_a;//the actual icons for the side of the block displayed
    //private static final String __OBFID = "CL_00000212";//Taken from Carrot or something 0.o
    //private static final String __OBFID = "CL_00069212";//ID from TomatoPlant
    private static final String __OBFID = "CL_00070212";//Still no idea what this is, so changed value a bit.

    
    //Copied from BlockCrops aka Wheat and modified heavily
    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)//why does this get passed in first param?
    {
        if (p_149691_2_ < 0 || p_149691_2_ > 7)
        {
            p_149691_2_ = 7;
        }

        return this.field_149868_a[p_149691_2_];
    }

    // -> set pre-mature: seeds?
    protected Item func_149866_i()
    {
    	return RPGMod.wesSeeds;
    }

    // -> set mature: food
    protected Item func_149865_P()
    {
    	return RPGMod.wesFood;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 2;
    }

    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149868_a = new IIcon[8];//was 4, 8 because of 8 grow cycles

        for (int i = 0; i < this.field_149868_a.length; ++i)
        {
            this.field_149868_a[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }
}
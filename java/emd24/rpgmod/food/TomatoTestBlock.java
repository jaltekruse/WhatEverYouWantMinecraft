package emd24.rpgmod.food;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emd24.rpgmod.RPGMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.block.BlockCrops;

public class TomatoTestBlock extends BlockCrops
{
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149868_a;//the actual icons for the side of the block displayed
    //private static final String __OBFID = "CL_00000212";//u wot
    private static final String __OBFID = "CL_00069212";//u wot

    //Copied from BlockCrops aka Wheat
    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        if (p_149691_2_ < 0 || p_149691_2_ > 7)
        {
            p_149691_2_ = 7;
        }

        return this.field_149868_a[p_149691_2_];
    }

    //what does this do?
    // -> set seeds?
    protected Item func_149866_i()
    {
        //return Items.carrot;
    	return RPGMod.tomatoSeeds;
    }

    //what does this do?
    // -> set food
    protected Item func_149865_P()
    {
    	return RPGMod.tomatoFood;
        //return Items.carrot;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149868_a = new IIcon[4];

        for (int i = 0; i < this.field_149868_a.length; ++i)
        {
            this.field_149868_a[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }
}
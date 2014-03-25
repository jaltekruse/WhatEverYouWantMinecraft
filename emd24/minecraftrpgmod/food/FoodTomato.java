package emd24.minecraftrpgmod.food;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FoodTomato extends ItemFood {
	
    public FoodTomato(int par1, int par2, float par3, boolean par4)
    {   
       //par1 = id
       //par2 = healAmount 
       //par3 = saturationModifier
       //par4 = isWolfsFavoriteMeat
        super(par1, par2, par3, par4); 
        this.setAlwaysEdible();
    }    

    public void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
    }
}
package emd24.rpgmod.food;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TomatoFood extends ItemFood{

	public TomatoFood(int par1, float par2, boolean par3)
	{   
	   //par1 = healAmount
	   //par2 = saturationModifier 
	   //par3 = isWolfsFavoriteMeat
		
	    super(par1, par2, par3); 
	    this.setAlwaysEdible();
	}    
	
	public void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
	    super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
	}

	
}

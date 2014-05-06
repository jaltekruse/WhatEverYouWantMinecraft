package emd24.rpgmod.food;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.JsonException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WesFood extends ItemFood {

    public WesFood(int par2, float par3, boolean par4)
    {   
       //par2 = healAmount 
       //par3 = saturationModifier
       //par4 = isWolfsFavoriteMeat
        super(par2, par3, par4); 
        this.setAlwaysEdible();
    }    

    public void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        try{
        Minecraft.getMinecraft().entityRenderer.activateNextShader();
        //Keeping this print statement for future reference on potentially grabbing specific shaders.
        System.out.println("Shader Group Name: " + Minecraft.getMinecraft().entityRenderer.getShaderGroup().getShaderGroupName());
        }catch(Exception ex){
        	System.out.println("Activating Shader failed :[");
        }

    }
}
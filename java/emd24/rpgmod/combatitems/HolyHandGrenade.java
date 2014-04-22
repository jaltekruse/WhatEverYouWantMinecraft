package emd24.rpgmod.combatitems;

import emd24.rpgmod.RPGMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Class that represents the almighty Holy Hand Grenade of Antioch Item 
 * 
 * @author Evan DYke
 *
 */
public class HolyHandGrenade extends Item{
	
	public HolyHandGrenade(){
		super();
	}
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            // Create a projectile entity for the thrown hand grenade
        	par2World.playSoundAtEntity(par3EntityPlayer, RPGMod.MOD_ID + ":holyhandgrenadethrow", 1.0F, 1.0F);
        	par2World.spawnEntityInWorld(new HolyHandGrenadeEntity(par2World, par3EntityPlayer));
        }

        return par1ItemStack;
    }
    
    
}

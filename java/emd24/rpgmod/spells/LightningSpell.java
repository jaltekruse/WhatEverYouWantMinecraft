package emd24.rpgmod.spells;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This class represents a lightning spell. When used, it creates a bolt of
 * lightning at the location where the user clicked.
 * 
 * @author Evan Dyke
 *
 */
public class LightningSpell extends Spell{

	public LightningSpell(int basePower, CreativeTabs tab) {
		super(basePower, tab); // Create a spell with base damage of 10
		this.onItemRightClick = false;
		this.onItemUse = true;
	}

	/**
	 * Method that is called when lightning spell is used on a block. Summons a bolt of lightning at the coordinates.
	 * 
	 */

	@Override

	public boolean castSpell(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side){
		
		ExtendedPlayerData playerData = ExtendedPlayerData.get(par2EntityPlayer);
		
		/* For each additional level, player deals 5% more damage, rounded down. An int
		* cast is probably happening deep in the vanilla code somewheres
		*/
		int damage = (int) (this.getBasePower() * (1.0 + (playerData.getSkill("Magic").getLevel() - 1) * .05));
		
		par3World.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
		par3World.spawnEntityInWorld(new MagicLightning(par3World, x, y, z, damage));
		return true;
	}

}

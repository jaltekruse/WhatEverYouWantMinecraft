package emd24.rpgmod.effects;

import net.minecraft.potion.Potion;

/**
 * This class represents a potion that can make the user fly.
 * 
 * @author Evan Dyke
 * 
 * Also, help was from LolCroc
 * Forum post: http://www.minecraftforum.net/topic/1682889-forge-creating-custom-potion-effects/
 *
 */

public class PotionFly extends Potion{
	
	public PotionFly(int par1, boolean par2, int par3) {
		super(par1, par2, par3);
	}

	public Potion setIconIndex(int par1, int par2) {
		super.setIconIndex(par1, par2);
		return this;
	}
}

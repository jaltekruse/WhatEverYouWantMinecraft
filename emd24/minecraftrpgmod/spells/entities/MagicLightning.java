package emd24.minecraftrpgmod.spells.entities;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

public class MagicLightning extends EntityLightningBolt{
	
	private int power;
	
	public MagicLightning(World par1World, double par2, double par4, double par6, int power) {
		super(par1World, par2, par4, par6);
		this.power = power;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

}

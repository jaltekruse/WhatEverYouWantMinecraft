package emd24.rpgmod.combatitems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemThrowingKnifeEntity extends EntityThrowable {
	
	private float damage;
	
	public ItemThrowingKnifeEntity(World par1World)
    {
        super(par1World);
    }

    public ItemThrowingKnifeEntity(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }

    public ItemThrowingKnifeEntity(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.damage);
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }

	public float getDamage() {
		return damage;
	}

	public ItemThrowingKnifeEntity setDamage(float damage) {
		this.damage = damage;
		return this;
	}
    
    
}

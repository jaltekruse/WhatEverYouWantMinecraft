package emd24.rpgmod.combatitems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemThrowingKnifeEntity extends EntityThrowable {
	
	private float damage;
	
	public ItemThrowingKnifeEntity(World par1World, float damage)
    {
        super(par1World);
        this.damage = damage;
    }

    public ItemThrowingKnifeEntity(World par1World, EntityLivingBase par2EntityLivingBase, float damage)
    {
        super(par1World, par2EntityLivingBase);
        this.damage = damage;
    }

    public ItemThrowingKnifeEntity(World par1World, double par2, double par4, double par6, float damage)
    {
        super(par1World, par2, par4, par6);
        this.damage = damage;
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
}

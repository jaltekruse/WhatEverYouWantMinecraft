package emd24.minecraftrpgmod.combatitems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class HolyHandGrenadeEntity extends EntityThrowable{
	 
	public HolyHandGrenadeEntity(World par1World)
	    {
	        super(par1World);
	    }

	    public HolyHandGrenadeEntity(World par1World, EntityLivingBase par2EntityLivingBase)
	    {
	        super(par1World, par2EntityLivingBase);
	    }

	    public HolyHandGrenadeEntity(World par1World, double par2, double par4, double par6)
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
	            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
	        }

	        if (!this.worldObj.isRemote)
	        {
	            	this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 2.0F, true);
	        }

	        for (int j = 0; j < 8; ++j)
	        {
	            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
	        }

	        if (!this.worldObj.isRemote)
	        {
	            this.setDead();
	        }
	    }
}

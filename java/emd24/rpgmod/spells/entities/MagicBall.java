package emd24.rpgmod.spells.entities;

import emd24.rpgmod.spells.Spell;
import emd24.rpgmod.spells.Spell.DamageType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class MagicBall extends EntityThrowable {

	public MagicBall(World world) {
		super(world);
	}

	public MagicBall(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	public MagicBall(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null) {
			
			// Get custom entity properties
			NBTTagCompound tag = this.getEntityData();
			int damage = tag.getInteger("mag_dmg");
			
			// We changed this to type 'float' and set to '2'; note you could just put the damage in
			// the method directly if you don't intend to change the damage variable
			
			// now in this line we don't need to cast as 'float', since our variable is already that type
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
		}

		// spawn 4 "crit" particles at the point of impact
		for (int l = 0; l < 4; ++l) {
			this.worldObj.spawnParticle("crit", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}

		// be sure to set the entity to 'dead' or it will keep updating forever and you'll end up with lots of
		// leftover entities in your world
		if (!worldObj.isRemote) {
			setDead();
		}
	}


}

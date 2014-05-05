package emd24.rpgmod.summons.ai;

import emd24.rpgmod.ExtendedEntityLivingData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowSummoner extends EntityAIBase
{
	private EntityCreature theSummoned;
	private EntityLivingBase theOwner;
	World theWorld;
	private double field_75336_f;
	private PathNavigate summonedPathfinder;
	private int field_75343_h;
	float maxDist;
	float minDist;
	private boolean field_75344_i;
	private static final String __OBFID = "CL_00001585";

	public EntityAIFollowSummoner(EntityCreature creature, double par2, float par4, float par5)
	{
		this.theSummoned = creature;
		this.theWorld = creature.worldObj;
		this.field_75336_f = par2;
		this.summonedPathfinder = creature.getNavigator();
		this.minDist = par4;
		this.maxDist = par5;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = getSummoner(theSummoned);

		if (entitylivingbase == null)
		{
			return false;
		}
		else if (this.theSummoned.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
		{
			return false;
		}
		else
		{
			this.theOwner = entitylivingbase;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return !this.summonedPathfinder.noPath() && this.theSummoned.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.field_75343_h = 0;
		this.field_75344_i = this.theSummoned.getNavigator().getAvoidsWater();
		this.theSummoned.getNavigator().setAvoidsWater(false);
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.theOwner = null;
		this.summonedPathfinder.clearPathEntity();
		this.theSummoned.getNavigator().setAvoidsWater(this.field_75344_i);
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		this.theSummoned.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.theSummoned.getVerticalFaceSpeed());

		if (--this.field_75343_h <= 0)
		{
			this.field_75343_h = 10;

			if (!this.summonedPathfinder.tryMoveToEntityLiving(this.theOwner, this.field_75336_f))
			{
				if (!this.theSummoned.getLeashed())
				{
					if (this.theSummoned.getDistanceSqToEntity(this.theOwner) >= 144.0D)
					{
						int i = MathHelper.floor_double(this.theOwner.posX) - 2;
						int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
						int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);

						for (int l = 0; l <= 4; ++l)
						{
							for (int i1 = 0; i1 <= 4; ++i1)
							{
								if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, i + l, k - 1, j + i1) && !this.theWorld.getBlock(i + l, k, j + i1).isNormalCube() && !this.theWorld.getBlock(i + l, k + 1, j + i1).isNormalCube())
								{
									this.theSummoned.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theSummoned.rotationYaw, this.theSummoned.rotationPitch);
									this.summonedPathfinder.clearPathEntity();
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	public static EntityPlayer getSummoner(EntityLivingBase ent){
		if(ent instanceof EntityLiving){
			EntityLiving living = (EntityLiving) ent;
			ExtendedEntityLivingData properties = ExtendedEntityLivingData.get(living);
			if(properties != null){
				return ent.worldObj.getPlayerEntityByName(properties.summonerName);
			}
		}
		return null;
	}
}
package emd24.rpgmod.summons.ai;

import emd24.rpgmod.ExtendedEntityLivingData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAISummonerHurtByTarget extends EntityAITarget
{
    EntityCreature theDefendingCreature;
    EntityLivingBase theOwnerAttacker;
    private int field_142051_e;
    private static final String __OBFID = "CL_00001624";

    public EntityAISummonerHurtByTarget(EntityCreature creature)
    {
        super(creature, false);
        this.theDefendingCreature = creature;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
//        if (!this.theDefendingTameable.isTamed())
//        {
//            return false;
//        }
//        else
//        {
            EntityLivingBase entitylivingbase = getSummoner(theDefendingCreature);

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theOwnerAttacker = entitylivingbase.getAITarget();
                int i = entitylivingbase.func_142015_aE();
                return i != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && func_142018_a(this.theOwnerAttacker, entitylivingbase);
            }
//        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase entitylivingbase = getSummoner(theDefendingCreature);

        if (entitylivingbase != null)
        {
            this.field_142051_e = entitylivingbase.func_142015_aE();
        }

        super.startExecuting();
    }
    
	/**
	 * Method used to get the summoner of the creature from extended player properties
	 * 
	 * @param creature
	 */
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
	
	/**
	 * Method unique to tameable class to decide which monsters it attacks. Copied from the EntityWolf class,
	 * func_142018_a
	 * 
	 * @param target - creature that was targeted
	 * @param summoner - person who summoned the creature
	 */
    public boolean func_142018_a(EntityLivingBase target, EntityLivingBase summoner)
    {
        // Don't attack Creepers of Ghasts for obvious reasons
    	if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast))
        {
            if (target instanceof EntityLiving)
            {
                // Checks to make sure does not attack other summoned creatured by summoner 
                if (EntityAISummonerHurtTarget.getSummoner(target) == summoner)
                {
                    return false;
                }
            }
            // Long line, makes sure don't attack player's horse
            return target instanceof EntityPlayer && summoner instanceof EntityPlayer && !((EntityPlayer)summoner).canAttackPlayer((EntityPlayer)target) ? false : !(target instanceof EntityHorse) || !((EntityHorse)target).isTame();
        }
        else
        {
            return false;
        }
    }
}
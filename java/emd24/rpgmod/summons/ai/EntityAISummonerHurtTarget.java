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
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Class used for summon creatures to attack a target that their summoner
 * attacks. Modified from the EntityAIOwnerHurtTarget class for tameable
 * creatures, such as wolves.
 * 
 * @author Owner
 *
 */
public class EntityAISummonerHurtTarget extends EntityAITarget {
	
	EntityCreature summonedCreature;
    EntityLivingBase theTarget;
    private int field_142050_e;
    private static final String __OBFID = "CL_00001625";

    public EntityAISummonerHurtTarget(EntityCreature par1EntityTameable)
    {
        super(par1EntityTameable, false);
        this.summonedCreature = par1EntityTameable;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
//        if (!this.theEntityTameable.isTamed())
//        {
//            return false;
//        }
//        else
//        {
            EntityLivingBase entitylivingbase = getSummoner(summonedCreature);

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != this.field_142050_e && this.isSuitableTarget(this.theTarget, false) && func_142018_a(this.theTarget, entitylivingbase);
            }
//        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase entitylivingbase = getSummoner(summonedCreature);

        if (entitylivingbase != null)
        {
            this.field_142050_e = entitylivingbase.getLastAttackerTime();
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

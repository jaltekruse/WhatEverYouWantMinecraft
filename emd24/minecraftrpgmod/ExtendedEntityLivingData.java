package emd24.minecraftrpgmod;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import emd24.minecraftrpgmod.skills.SkillPlayer;
import emd24.minecraftrpgmod.skills.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Class that holds external data to be saved with the Player
 * 
 * @author Evan Dyke
 *
 */
public class ExtendedEntityLivingData implements IExtendedEntityProperties{

	public static final String IDENTIFIER = "ExtendedEntityLiving";
	public static final String CHANNEL = "extentityliving";
	private final EntityLiving entity;
	public int alertLevel;
	public int stealCoolDown; // number of ticks until entity can be stolen from
	public int alertTimer; // number of ticks until alert level decreases
	
	// Sets whether player is undead
	
	public ExtendedEntityLivingData(EntityLiving ent){
		this.entity = ent;
		this.alertLevel = 0;
		this.stealCoolDown = 0;
		this.alertTimer = 0;
		
		// TODO: sync data
	}

	public final static void register(EntityLiving ent){
		ent.registerExtendedProperties(ExtendedEntityLivingData.IDENTIFIER, 
				new ExtendedEntityLivingData(ent));
	}
	
	public static ExtendedEntityLivingData get(EntityLiving living){
		return (ExtendedEntityLivingData)living.getExtendedProperties(IDENTIFIER);
	}
	
	@Override
	public void init(Entity entity, World world) {
		
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		NBTTagCompound rbt = new NBTTagCompound();
		
		rbt.setInteger("alertLvl", this.alertLevel);
		rbt.setInteger("stealTicks", this.stealCoolDown);
		rbt.setInteger("alertTicks", this.alertTimer);
		
		compound.setTag(IDENTIFIER, rbt);
	
	}


	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		NBTTagCompound rbt = compound.getCompoundTag(IDENTIFIER);
		this.alertLevel = rbt.getInteger("alertLvl");
		this.stealCoolDown = rbt.getInteger("stealTicks");
		this.alertLevel = rbt.getInteger("alertTicks");
	}
	
}

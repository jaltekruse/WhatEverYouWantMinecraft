package emd24.rpgmod.quest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import emd24.rpgmod.skills.SkillPlayer;
import emd24.rpgmod.skills.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Class that holds Dialogue Data
 * 
 * @author Wesley Reardan
 *
 */
public class ExtendedEntityLivingDialogueData implements IExtendedEntityProperties{

	public static final String IDENTIFIER = "ExtendedEntityLivingDialogueData";
	public static final String CHANNEL = "extentityliving";
	private final EntityLiving entity;
	
	public int alertLevel;
	public int stealCoolDown; // number of ticks until entity can be stolen from
	public int alertTimer; // number of ticks until alert level decreases
	
	public DialogueTreeNode dialogueTree;	//The dialogue-tree data structure
	
	// Sets whether player is undead
	
	public ExtendedEntityLivingDialogueData(EntityLiving ent){
		this.entity = ent;
		this.alertLevel = 0;
		this.stealCoolDown = 0;
		this.alertTimer = 0;
		
		this.dialogueTree = new DialogueTreeNode();
		
		// TODO: sync data
	}

	public final static void register(EntityLiving ent){
		ent.registerExtendedProperties(ExtendedEntityLivingDialogueData.IDENTIFIER, 
				new ExtendedEntityLivingDialogueData(ent));
	}
	
	public static ExtendedEntityLivingDialogueData get(EntityLiving living){
		return (ExtendedEntityLivingDialogueData)living.getExtendedProperties(IDENTIFIER);
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
		
		String dialogueString = dialogueTree.store();
		//System.out.println("store() output:\n" + dialogueString);
		rbt.setString("dialogueInfo", dialogueString);
		
		compound.setTag(IDENTIFIER, rbt);
	
	}


	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		NBTTagCompound rbt = compound.getCompoundTag(IDENTIFIER);
		this.alertLevel = rbt.getInteger("alertLvl");
		this.stealCoolDown = rbt.getInteger("stealTicks");
		
		String dialogueString = rbt.getString("dialogueInfo");
		//System.err.println("nbtData: " + dialogueString);
		this.dialogueTree.load(dialogueString);
	}
	
}

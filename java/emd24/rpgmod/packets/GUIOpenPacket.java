package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.gui.GUIDialogue;
import emd24.rpgmod.gui.GUIDialogueEditor;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GUIOpenPacket extends AbstractPacket {
	private NBTTagCompound data;
	private int type;
	private int entityID;
	private String dialogue;
	
	public GUIOpenPacket()
	{
	}
	
	public GUIOpenPacket(int type, int entityID, String dialogue)
	{
		this.type = type;
		this.entityID = entityID;
		this.dialogue = dialogue;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setInteger("type", type);
		data.setInteger("entityID", entityID);
		data.setString("dialogue", dialogue);
		
		ByteBufUtils.writeTag(buffer, data);

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);

		type = data.getInteger("type");
		entityID = data.getInteger("entityID");
		dialogue = data.getString("dialogue");
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//System.err.println("entityID received: " + entityID);
		EntityLiving target = (EntityLiving) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
		//System.err.println("entityID of target: " + target.getEntityId());
		
		switch(type)
		{
		//Open GUI Dialogue
		case 0:
			Minecraft.getMinecraft().displayGuiScreen(new GUIDialogue(target, dialogue));
			break;
		//Open GUI Dialogue Editor
		case 1:
			Minecraft.getMinecraft().displayGuiScreen(new GUIDialogueEditor(target, dialogue));
			break;
		default:
			assert(false);
		}

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		EntityLiving target = (EntityLiving) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
//		ExtendedEntityLivingDialogueData eeldd = ExtendedEntityLivingDialogueData.get(target);
//		eeldd.dialogueTree.load(dialogue);	//TODO: can this line be removed?
//		
//		NBTTagCompound compound = new NBTTagCompound(); 
//		eeldd.saveNBTData(compound);
		// Needed to save skill data on death
		Integer id = target.getEntityId();
		RPGMod.proxy.storeDialogueData(id, dialogue);
	}

}

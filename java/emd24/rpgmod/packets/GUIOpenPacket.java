package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.gui.GUIDialogue;
import emd24.rpgmod.gui.GUIDialogueEditor;
import emd24.rpgmod.gui.GUIKeyHandler;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class GUIOpenPacket extends AbstractPacket {
	private NBTTagCompound data;
	private int type;	//TODO: Remove
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
		//moved to common proxy to avoid server failing
		RPGMod.proxy.openDialogueGUI(entityID, dialogue);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		EntityLiving target = (EntityLiving) MinecraftServer.getServer().getEntityWorld().getEntityByID(entityID);
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

package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.quest.ExtendedEntityLivingDialogueData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class DialogueDataPacket extends AbstractPacket {
	private NBTTagCompound data;
	
	EntityLiving npc;
	
	public DialogueDataPacket() {
		
	}
	
	public DialogueDataPacket(EntityLiving npc) {
		this.npc = npc;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		ExtendedEntityLivingDialogueData eeldd = ExtendedEntityLivingDialogueData.get(npc);
		eeldd.saveNBTData(data);
		
		data.setInteger("entityID", npc.getEntityId());
		
		ByteBufUtils.writeTag(buffer, data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);
		
		if(npc == null) {
			int entityID = data.getInteger("entityID");
			npc = (EntityLiving) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		ExtendedEntityLivingDialogueData.get(npc).loadNBTData(data);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		ExtendedEntityLivingDialogueData nbtDialogue = ExtendedEntityLivingDialogueData.get(npc);
		nbtDialogue.loadNBTData(data);
	}

}

package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.gui.GUIDialogue;
import emd24.rpgmod.gui.GUIDialogueEditor;
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
	
	public GUIOpenPacket()
	{
	}
	
	public GUIOpenPacket(int type, int entityID)
	{
		this.type = type;
		this.entityID = entityID;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setInteger("type", type);
		data.setInteger("entityID", entityID);
		
		ByteBufUtils.writeTag(buffer, data);

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		type = data.getInteger("type");
		entityID = data.getInteger("entityID");
		//System.err.println("entityID received: " + entityID);
		EntityLiving target = (EntityLiving) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
		//System.err.println("entityID of target: " + target.getEntityId());
		
		switch(type)
		{
		//Open GUI Dialogue
		case 0:
			Minecraft.getMinecraft().displayGuiScreen(new GUIDialogue(target));
			break;
		//Open GUI Dialogue Editor
		case 1:
			Minecraft.getMinecraft().displayGuiScreen(new GUIDialogueEditor(target));
			break;
		default:
			assert(false);
		}

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}

package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.quest.ScriptManagerServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ScriptActionPacket extends AbstractPacket {
	Integer entityID;
	String scriptName;

	NBTTagCompound data;
	
	public ScriptActionPacket()
	{
		
	}
	
	public ScriptActionPacket(Integer entityID, String scriptName)
	{
		this.entityID = entityID;
		this.scriptName = scriptName;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setInteger("entityID", entityID);
		data.setString("scriptName", scriptName);
		
		ByteBufUtils.writeTag(buffer, data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);

		entityID = data.getInteger("entityID");
		scriptName = data.getString("scriptName");

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		ScriptManagerServer.setActiveScript(entityID, scriptName);
	}

}

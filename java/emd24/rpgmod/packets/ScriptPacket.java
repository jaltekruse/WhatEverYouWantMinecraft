package emd24.rpgmod.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.quest.ScriptManagerServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class ScriptPacket extends AbstractPacket {
	NBTTagCompound data;
	String name;
	String content;
	static String placeholder = "!REQUEST!";
	
	public ScriptPacket() {
		
	}
	
	public ScriptPacket(String name, String content) {
		this.name = name;
		this.content = content;
	}
	
	/**
	 * Send a request to get script with name.  Sets content to placeholder value.
	 * @param name name of script to load from server
	 */
	public ScriptPacket(String name) {
		this.name = name;
		this.content = placeholder;
	}
	
	public boolean isPlaceholder() {
		return content.equals(placeholder);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = new NBTTagCompound();
		data.setString("name", name);
		data.setString("content", content);
		
		ByteBufUtils.writeTag(buffer, data);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		data = ByteBufUtils.readTag(buffer);

		name = data.getString("name");
		content = data.getString("content");
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//System.err.println("Recveived:\n\tScriptName: " + name + "\n\tContent: " + content);
		RPGMod.proxy.updateScriptEditor(name, content);

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		//TODO: check for player admin rights
		//System.err.println("Server Recveived script:\n\tScriptName: " + name + "\n\tContent: " + content);
		
		//check if the request is for a load or store
		if(this.isPlaceholder()) {
			//load scripts from file
			ScriptManagerServer.load();
			
			//send script back to client
			content = ScriptManagerServer.getScript(name);
			ScriptPacket message = new ScriptPacket(name, content);
			RPGMod.packetPipeline.sendTo(message, (EntityPlayerMP) player);
		}
		else {
			//store the script server-side
			ScriptManagerServer.storeScript(name, content);
			ScriptManagerServer.store();
		}
	}

}

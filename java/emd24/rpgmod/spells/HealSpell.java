package emd24.rpgmod.spells;

import java.util.ArrayList;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.party.PartyManagerServer;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class HealSpell extends Spell{

	private boolean party;

	public HealSpell(int basePower, CreativeTabs tab, boolean party){
		super(basePower, tab);

		this.party = party;
		this.onItemRightClick = true;
		this.onItemUse = false;

	}

	@Override
	public boolean castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		
		if(party){
			boolean partyHealed = false;
			ArrayList<String> partyNames = PartyManagerServer.getPlayerParty(par3EntityPlayer.getCommandSenderName());
			// Iterate through the player's party and heal the players
			
			for(String name : partyNames){
				//DEBUG
				//System.out.println("\n\n\n"+name);
				EntityPlayer player = par2World.getPlayerEntityByName(name);
				assert(player != null);
				//DEBUG
				//System.out.println("player: "+ player.toString());
				//System.out.println("is this health: " + player.getHealth() + " less than " + player.getMaxHealth());
				if(player.getHealth() < player.getMaxHealth()){
					player.heal(this.getBasePower() / partyNames.size());
					partyHealed = true;
				}
				
			}
			if(!par2World.isRemote && !partyHealed)
				//DEBUG
				System.out.println("in that if statement");
				par3EntityPlayer.addChatMessage(new ChatComponentText("Party fully healed!"));
			return partyHealed;
		}
		else{
			if(par3EntityPlayer.getHealth() < par3EntityPlayer.getMaxHealth()){
				par3EntityPlayer.heal(this.getBasePower());
				return true;
			}
			if(!par2World.isRemote)
				par3EntityPlayer.addChatMessage(new ChatComponentText("At full health!"));
			return false;
		}
	}	

}

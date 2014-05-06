package emd24.rpgmod.spells;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.combatitems.HolyHandGrenadeEntity;
import emd24.rpgmod.spells.entities.MagicBall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;

public class SpellMagicProjectile extends Spell{

	public SpellMagicProjectile(int basePower, CreativeTabs tab){
		super(basePower, tab); // Create a spell with base damage of 10
		this.onItemRightClick = true;
		this.onItemUse = false;
	}

	@Override
	public boolean castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){

		
		
		
		if(!par2World.isRemote){
			ExtendedPlayerData playerData = ExtendedPlayerData.get(par3EntityPlayer);

			/* For each additional level, player deals 5% more damage, rounded down. An int
			 * cast is probably happening deep in the vanilla code somewheres
			 */
			int damage = (int) (this.getBasePower() * (1.0 + (playerData.getSkill("Magic").getLevel() - 1) * .05));

			MagicBall projectile = (new MagicBall(par2World, par3EntityPlayer));
			NBTTagCompound prop = projectile.getEntityData();
			prop.setInteger("mag_dmg", damage);
			prop.setString("mag_type", this.getSpellType().toString());
			par2World.spawnEntityInWorld(projectile);
		}
		return true;
	}
}

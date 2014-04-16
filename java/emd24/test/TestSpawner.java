package emd24.test;

import java.lang.reflect.Field;

import emd24.rpgmod.ExtendedPlayerData;
import emd24.rpgmod.RPGMod;
import emd24.rpgmod.spells.Spell.DamageType;
import emd24.rpgmod.spells.entities.MagicLightning;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class TestSpawner extends Item {
	
	private DamageType spellType;
	private int basePower;
	private int manaCost = 0; 
	private int cooldown;
	protected boolean onItemUse;
	protected boolean onItemRightClick;
	
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{	
		genTestArea(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
		return true;
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		castSpell(par1ItemStack, par2World, par3EntityPlayer);
		return par1ItemStack;
    }
	
	public void castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par2World.spawnEntityInWorld(new EntityEgg(par2World));
		
	}
	
	public void genTestArea(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		int box_width = 10;
		int box_height = 8;
		int number_of_tests = 50;
		int tests_per_side = (int) Math.ceil(Math.sqrt(number_of_tests));
		int blocks_per_side = (box_width + 1) * tests_per_side;
		par2EntityPlayer.setPositionAndUpdate(par2EntityPlayer.posX, par2EntityPlayer.posY, par2EntityPlayer.posZ + 3);
		// TODO - figure out where player is facing and generate away from him
		int x_origin = par4 + 10;
		Block curr_block;
		Block glass_block = RPGMod.clearerGlass;
		// outer walls
		for (int h = 0; h < box_height; h++){
			curr_block = glass_block;
			if (h < 5) {
				curr_block = Blocks.stone;
			}
			for (int i = 0; i < blocks_per_side; i++){
				for (int k = 0; k < tests_per_side + 1; k++){
					par3World.setBlock(x_origin + i, par5 + 1 + h, par6 + (k * box_width) , curr_block, 1, 1);
				}
				for (int k = 0; k < tests_per_side + 1; k++){
					par3World.setBlock(x_origin + (k * box_width), par5 + 1 + h, par6 + i , curr_block, 1, 1);
				}
				par3World.setBlock(x_origin + i, par5 + 1 + h, par6, curr_block, 1, 1);
				par3World.setBlock(x_origin + i, par5 + 1 + h, par6 + blocks_per_side, curr_block, 1, 1);
				par3World.setBlock(x_origin, par5 + 1 + h, par6 + i, curr_block, 1, 1);
				par3World.setBlock(x_origin + blocks_per_side, par5 + 1 + h, par6 + i, curr_block, 1, 1);
			}
		}
		
		Field[] fields = Blocks.class.getDeclaredFields();
		Block[] blocks = new Block[fields.length];
		int i = 0;
		for ( Field f : fields) {
			try {
				if (f.get(null) instanceof Block) {
					blocks[i] = (Block) f.get(null);
				}
			} catch (IllegalArgumentException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
			i++;
		}
		
		EntityPig curr_subject;
		int[] test_center = new int[3];
		// add test subjects
		for (int k = 0; k < tests_per_side; k++){
			for (int j = 0; j < tests_per_side; j++) {
				System.out.println(blocks[k * tests_per_side + j]);
				if ( blocks[k * tests_per_side + j] != null ){
					test_center[0] = x_origin + box_width / 2 + box_width * j;
					test_center[1] = par5 + 1;
					test_center[2] = par6 + box_width / 2 + (k * box_width);
					par3World.setBlock(test_center[0],
							test_center[1], test_center[2], 
							blocks[k * tests_per_side + j], 1, 1);
					curr_subject = new EntityPig(par3World);
					par3World.spawnEntityInWorld(curr_subject);
					curr_subject.setPositionAndUpdate(test_center[0] + 2, test_center[1], test_center[2]);
				}
			}
		}
		// ceiling
		for (i = 0; i < blocks_per_side; i++){
			for (int j = 0; j < blocks_per_side; j++){
				curr_block = RPGMod.clearerGlass;
				if (i % box_width == 0 && j % box_width == 0) {
					curr_block = Blocks.stone;
				}
				par3World.setBlock(x_origin + i, par5 + 1 + box_height, par6 + j, curr_block, 1, 1);
			}
		}
	}

}

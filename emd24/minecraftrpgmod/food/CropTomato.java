package emd24.minecraftrpgmod.food;

import java.util.Random;

import emd24.minecraftrpgmod.RPGMod;

import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class CropTomato extends Block { //extends GenericBlock instead...

    public CropTomato(int id) {
        super(id, Material.plants);//id, 32, Material.plants
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F , 1.5F, 1.0F);
    }
        
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }
        
    public int getRenderType() {
        return 6; // Magic number.
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    
    
    
    
    
    
    
    
//  @Override
//  public int getBlockTextureFromSideAndMetadata (int side, int metadata) {
//      return 32 + metadata;
//  }
//
//  @Override
//  public void updateTick (World world, int x, int y, int z, Random random) {
//      if (world.getBlockMetadata(x, y, z) == 1) {
//          return;
//      }
//
//      if (random.nextInt(isFertile(world, x, y - 1, z) ? 12 : 25) != 0) {
//          return;
//      }
//
//      world.setBlockMetadataWithNotify(x, y, z, 1);
//  }
//
//  @Override
//  public void onNeighborBlockChange (World world, int x, int y, int z,
//          int neighborId) {
//      if (!canBlockStay(world, x, y, z)) {
//          dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
//          world.setBlockWithNotify(x, y, z, 0);
//      }
//  }

  @Override
  public boolean canBlockStay (World world, int x, int y, int z) {
      Block soil = blocksList[world.getBlockId(x, y - 1, z)];
      return (world.getFullBlockLightValue(x, y, z) >= 8 || world
              .canBlockSeeTheSky(x, y, z))
              && (soil != null && soil.canSustainPlant(world, x, y - 1, z,
                      ForgeDirection.UP, RPGMod.tomatoSeed));
  }
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public int idDropped (int metadata, Random random, int par2) {
        switch (metadata) {
        case 0:
            return RPGMod.tomatoSeed.itemID;
        case 1:
            return RPGMod.tomatoFruit.itemID;
        default:
            // Error case!
            return -1; // air
        }
    }
    
  @Override
  public int idPicked (World world, int x, int y, int z) {
      return RPGMod.tomatoSeed.itemID;
  }
    
    
}

//import java.util.Random;
//
//import net.minecraft.block.material.Material;
//import net.minecraft.block.Block;
//import net.minecraft.util.AxisAlignedBB;
//import net.minecraft.world.World;
//import net.minecraftforge.common.ForgeDirection;
//
//public class CropTomato extends GenericBlock {
//
//    public CropTomato (int id) {
//        super(id, 32, Material.plants);
//        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
//        setTickRandomly(true);
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x,
//            int y, int z) {
//        return null;
//    }
//
//    @Override
//    public int getRenderType () {
//        return 6;
//    }
//
//    @Override
//    public boolean isOpaqueCube () {
//        return false;
//    }
//
//    @Override
//    public int getBlockTextureFromSideAndMetadata (int side, int metadata) {
//        return 32 + metadata;
//    }
//
//    @Override
//    public void updateTick (World world, int x, int y, int z, Random random) {
//        if (world.getBlockMetadata(x, y, z) == 1) {
//            return;
//        }
//
//        if (random.nextInt(isFertile(world, x, y - 1, z) ? 12 : 25) != 0) {
//            return;
//        }
//
//        world.setBlockMetadataWithNotify(x, y, z, 1);
//    }
//
//    @Override
//    public void onNeighborBlockChange (World world, int x, int y, int z,
//            int neighborId) {
//        if (!canBlockStay(world, x, y, z)) {
//            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
//            world.setBlockWithNotify(x, y, z, 0);
//        }
//    }
//
//    @Override
//    public boolean canBlockStay (World world, int x, int y, int z) {
//        Block soil = blocksList[world.getBlockId(x, y - 1, z)];
//        return (world.getFullBlockLightValue(x, y, z) >= 8 || world
//                .canBlockSeeTheSky(x, y, z))
//                && (soil != null && soil.canSustainPlant(world, x, y - 1, z,
//                        ForgeDirection.UP, Generic.tomatoSeeds));
//    }
//
//    @Override
//    public int idDropped (int metadata, Random random, int par2) {
//        switch (metadata) {
//        case 0:
//            return Generic.tomatoSeeds.itemID;
//        case 1:
//            return Generic.tomatoFruit.itemID;
//        default:
//            // Error case!
//            return -1; // air
//        }
//    }
//
//    @Override
//    public int idPicked (World world, int x, int y, int z) {
//        return Generic.tomatoSeeds.itemID;
//    }
//}
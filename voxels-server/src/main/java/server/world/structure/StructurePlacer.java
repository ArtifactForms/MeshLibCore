package server.world.structure;

import common.world.ChunkData;
import common.world.structure.Structure;
import common.world.structure.StructureBlock;
import common.world.structure.StructureRotation;

public class StructurePlacer {

  public static void place(
      Structure structure, ChunkData chunk, int originX, int originY, int originZ, int rotation) {

    for (StructureBlock block : structure.getBlocks()) {

      int[] r = StructureRotation.rotate(block.x, block.z, rotation);

      int worldX = originX + r[0];
      int worldY = originY + block.y;
      int worldZ = originZ + r[1];

      chunk.setBlockAt(block.block, worldX, worldY, worldZ);
    }
  }
}

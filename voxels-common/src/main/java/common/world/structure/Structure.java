package common.world.structure;

import java.util.List;

public class Structure {

  private final int sizeX;
  private final int sizeY;
  private final int sizeZ;

  private final List<StructureBlock> blocks;

  public Structure(int sizeX, int sizeY, int sizeZ, List<StructureBlock> blocks) {
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.sizeZ = sizeZ;
    this.blocks = blocks;
  }

  public List<StructureBlock> getBlocks() {
    return blocks;
  }
}

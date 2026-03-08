package common.world.structure;

import common.world.BlockType;

public class StructureBlock {

  public final int x;
  public final int y;
  public final int z;
  public final BlockType block;

  public StructureBlock(int x, int y, int z, BlockType block) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.block = block;
  }
}

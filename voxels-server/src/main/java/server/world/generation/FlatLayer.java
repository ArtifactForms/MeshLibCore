package server.world.generation;

import common.game.block.BlockType;

public class FlatLayer {

  public final BlockType block;

  public final int height;

  public FlatLayer(BlockType block, int height) {
    this.block = block;
    this.height = height;
  }
}

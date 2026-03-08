package server.world.structure;

import java.util.List;

import common.world.BlockType;
import common.world.structure.Structure;
import common.world.structure.StructureBlock;

public class TreeStructures {

  public static Structure SMALL_OAK =
      new Structure(
          5,
          6,
          5,
          List.of(
              new StructureBlock(0, 0, 0, BlockType.OAK_WOOD),
              new StructureBlock(0, 1, 0, BlockType.OAK_WOOD),
              new StructureBlock(0, 2, 0, BlockType.OAK_WOOD),
              new StructureBlock(1, 3, 0, BlockType.LEAF),
              new StructureBlock(-1, 3, 0, BlockType.LEAF),
              new StructureBlock(0, 3, 1, BlockType.LEAF),
              new StructureBlock(0, 3, -1, BlockType.LEAF),
              new StructureBlock(0, 3, 0, BlockType.LEAF)));
}

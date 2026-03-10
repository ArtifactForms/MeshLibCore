package common.world;

import common.game.block.Blocks;

class ReferenceWorld {

  private final java.util.Map<String, Short> blocks = new java.util.HashMap<>();

  void set(int x,int y,int z, short id) {
    blocks.put(key(x,y,z), id);
  }

  short get(int x,int y,int z) {
    return blocks.getOrDefault(key(x,y,z), Blocks.AIR.getId());
  }

  private String key(int x,int y,int z){
    return x+"|"+y+"|"+z;
  }
}
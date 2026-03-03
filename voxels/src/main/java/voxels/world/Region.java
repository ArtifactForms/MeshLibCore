package voxels.world;

import java.util.ArrayList;
import java.util.List;

public class Region {

  public static final int REGION_SIZE = 8; // 8x8 chunks

  private final int regionX;
  private final int regionZ;

  private final List<Chunk> chunks = new ArrayList<>();

  public Region(int regionX, int regionZ) {
    this.regionX = regionX;
    this.regionZ = regionZ;
  }

  public void addChunk(Chunk chunk) {
    chunks.add(chunk);
  }

  public List<Chunk> getChunks() {
    return chunks;
  }

  public int getRegionX() {
    return regionX;
  }

  public int getRegionZ() {
    return regionZ;
  }
}

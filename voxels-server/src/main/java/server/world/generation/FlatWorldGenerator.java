package server.world.generation;

import java.util.List;

import common.world.ChunkData;

public class FlatWorldGenerator implements WorldGenerator {

  private final List<FlatLayer> layers;

  public FlatWorldGenerator(String preset) {
    this.layers = FlatPresetParser.parse(preset);
  }

  @Override
  public void generate(ChunkData chunk) {

    for (int x = 0; x < ChunkData.WIDTH; x++) {
      for (int z = 0; z < ChunkData.DEPTH; z++) {

        int y = 0;

        for (FlatLayer layer : layers) {

          for (int i = 0; i < layer.height; i++) {

            chunk.setBlockAt(layer.block, x, y, z);
            y++;
          }
        }
      }
    }
  }
}

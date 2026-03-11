package server.world.structures;

import java.util.ArrayList;
import java.util.List;

import common.world.ChunkData;
import server.world.structures.fin.StructureGenerator;

public class StructureManager {

  private List<StructureGenerator> generators = new ArrayList<>();

  public void register(StructureGenerator g) {
    generators.add(g);
  }

  public void generateStructures(ChunkData chunk, long worldSeed) {
    int cx = chunk.getChunkX();
    int cz = chunk.getChunkZ();

    for (StructureGenerator g : generators) {
      int chunkRadius = (int) Math.ceil((float) g.getMaxRadius() / ChunkData.WIDTH);

      for (int dx = -chunkRadius; dx <= chunkRadius; dx++) {
        for (int dz = -chunkRadius; dz <= chunkRadius; dz++) {
          // WICHTIG: Wir geben die Koordinaten des "potenziellen Zentrums" mit!
          g.generateAt(chunk, cx + dx, cz + dz, worldSeed);
        }
      }
    }
  }
}

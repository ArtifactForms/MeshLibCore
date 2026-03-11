package server.world.structures.fin;

import common.world.ChunkData;

public interface StructureGenerator {

  // centerX und centerZ sind die Chunk-Koordinaten des potenziellen Ursprungs
  void generateAt(ChunkData chunk, int centerX, int centerZ, long worldSeed);

  int getMaxRadius();
}
package server.persistance;

import common.world.ChunkData;
import java.util.Optional;

/**
 * Interface for chunk persistence. Decouples the world logic from the actual storage
 * implementation.
 */
public interface ChunkRepository {

  /** Saves a single chunk. */
  void save(ChunkData chunk);

  /** Loads a chunk if it exists on disk. */
  Optional<ChunkData> load(int x, int z);

  /** Checks if a chunk has been generated and saved before. */
  boolean exists(int x, int z);
}

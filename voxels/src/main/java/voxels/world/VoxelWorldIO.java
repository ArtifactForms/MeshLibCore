package voxels.world;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class VoxelWorldIO {

  private VoxelWorldIO() {}

  public static void saveChunk(VoxelWorld world, int chunkX, int chunkZ, Path root)
      throws IOException {
    Chunk chunk = world.getChunk(chunkX, chunkZ);
    if (chunk == null) {
      return;
    }

    Files.createDirectories(root);
    Path path = chunkPath(root, chunkX, chunkZ);

    try (DataOutputStream out =
        new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
      out.writeInt(1); // format version
      out.writeInt(chunkX);
      out.writeInt(chunkZ);

      for (int y = 0; y < Chunk.SIZE_Y; y++) {
        for (int z = 0; z < Chunk.SIZE_Z; z++) {
          for (int x = 0; x < Chunk.SIZE_X; x++) {
            out.writeShort(chunk.getBlock(x, y, z));
          }
        }
      }
    }
  }

  public static Chunk loadChunk(Path root, int chunkX, int chunkZ) throws IOException {
    Path path = chunkPath(root, chunkX, chunkZ);
    if (!Files.exists(path)) {
      return null;
    }

    try (DataInputStream in =
        new DataInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
      int version = in.readInt();
      if (version != 1) {
        throw new IOException("Unsupported chunk format version: " + version);
      }

      int fileChunkX = in.readInt();
      int fileChunkZ = in.readInt();

      Chunk chunk = new Chunk(fileChunkX, fileChunkZ);

      int[] heights = new int[Chunk.SIZE_X * Chunk.SIZE_Z];
      for (int i = 0; i < heights.length; i++) heights[i] = -1;

      for (int y = 0; y < Chunk.SIZE_Y; y++) {
        for (int z = 0; z < Chunk.SIZE_Z; z++) {
          for (int x = 0; x < Chunk.SIZE_X; x++) {
            short id = in.readShort();
            chunk.setBlock(x, y, z, id);
            if (id != Blocks.AIR) {
              heights[x + Chunk.SIZE_X * z] = y;
            }
          }
        }
      }

      for (int z = 0; z < Chunk.SIZE_Z; z++) {
        for (int x = 0; x < Chunk.SIZE_X; x++) {
          int h = heights[x + Chunk.SIZE_X * z];
          chunk.setHeight(x, z, Math.max(h, 0));
        }
      }

      return chunk;
    }
  }

  public static void saveLoadedChunks(VoxelWorld world, Path root) throws IOException {
    for (Chunk chunk : world.getChunks()) {
      saveChunk(world, chunk.getChunkX(), chunk.getChunkZ(), root);
    }
  }

  public static int loadAllChunks(VoxelWorld world, Path root) throws IOException {
    if (!Files.exists(root)) {
      return 0;
    }

    int loaded = 0;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, "chunk_*.bin")) {
      for (Path path : stream) {
        String name = path.getFileName().toString();
        int[] coords = parseChunkCoords(name);
        if (coords == null) continue;

        if (loadChunkIntoWorld(world, coords[0], coords[1], root)) {
          loaded++;
        }
      }
    }

    return loaded;
  }

  private static int[] parseChunkCoords(String fileName) {
    if (!fileName.startsWith("chunk_") || !fileName.endsWith(".bin")) {
      return null;
    }

    String raw = fileName.substring("chunk_".length(), fileName.length() - ".bin".length());
    String[] parts = raw.split("_");
    if (parts.length != 2) {
      return null;
    }

    try {
      int x = Integer.parseInt(parts[0]);
      int z = Integer.parseInt(parts[1]);
      return new int[] {x, z};
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static boolean loadChunkIntoWorld(VoxelWorld world, int chunkX, int chunkZ, Path root)
      throws IOException {
    Chunk chunk = loadChunk(root, chunkX, chunkZ);
    if (chunk == null) {
      return false;
    }
    world.addChunk(chunk);
    return true;
  }

  private static Path chunkPath(Path root, int chunkX, int chunkZ) {
    return root.resolve("chunk_" + chunkX + "_" + chunkZ + ".bin");
  }
}

package server.persistance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import common.network.packets.ChunkDataPacket;
import common.world.ChunkData;

/**
 * File-based implementation of the chunk repository. It uses the compression logic from
 * ChunkDataPacket to keep disk footprint small.
 */
public class FileChunkRepository implements ChunkRepository {

  private final File storageDir;

  public FileChunkRepository(File worldDir) {
    this.storageDir = new File(worldDir, "chunks");
    if (!storageDir.exists()) storageDir.mkdirs();
  }

  @Override
  public void save(ChunkData chunk) {
    File file = getChunkFile(chunk.getChunkX(), chunk.getChunkZ());
    ChunkDataPacket serializer = new ChunkDataPacket(chunk);

    try (DataOutputStream out =
        new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
      out.writeInt(chunk.getChunkX());
      out.writeInt(chunk.getChunkZ());

      byte[] compressed = serializer.getCompressedData();
      out.writeInt(compressed.length);
      out.write(compressed);

      int[] hMap = chunk.getRawHeightMap();
      for (int h : hMap) {
        out.writeInt(h);
      }

      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<ChunkData> load(int x, int z) {
    File file = getChunkFile(x, z);
    if (!file.exists()) return Optional.empty();

    try (DataInputStream in =
        new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      int readX = in.readInt();
      int readZ = in.readInt();

      int len = in.readInt();
      byte[] compressed = new byte[len];
      in.readFully(compressed);

      int[] hMap = new int[ChunkData.WIDTH * ChunkData.DEPTH];
      for (int i = 0; i < hMap.length; i++) {
        hMap[i] = in.readInt();
      }

      ChunkDataPacket packet = new ChunkDataPacket(readX, readZ, compressed);
      return Optional.of(new ChunkData(readX, readZ, packet.decompress(), hMap));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean exists(int x, int z) {
    return getChunkFile(x, z).exists();
  }

  private File getChunkFile(int x, int z) {
    return new File(storageDir, String.format("c.%d.%d.dat", x, z));
  }
}

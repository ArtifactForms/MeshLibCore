package common.network.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.world.ChunkData;

/**
 * Packet used to transfer chunk block data from server to client.
 *
 * <p>The chunk block array is compressed before sending to reduce bandwidth usage. On the client
 * side the data can be decompressed back into a short[] block array.
 */
public class ChunkDataPacket implements Packet {

  private static final int COMPRESSION_BUFFER_SIZE = 1024;

  private int x;
  private int z;

  /** Compressed block data */
  private byte[] data;

  public ChunkDataPacket() {}

  /**
   * Creates a packet from chunk data.
   *
   * @param chunk source chunk
   */
  public ChunkDataPacket(ChunkData chunk) {
    this.x = chunk.getChunkX();
    this.z = chunk.getChunkZ();
    this.data = compress(chunk.getRawBlockData());
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(x);
    out.writeInt(z);

    out.writeInt(data.length);
    out.writeBytes(data);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readInt();
    this.z = in.readInt();

    int len = in.readInt();
    this.data = new byte[len];

    in.readBytes(this.data);
  }

  @Override
  public int getId() {
    return PacketIds.CHUNK_DATA;
  }

  /** Compresses a short[] block array into a byte[] using Deflater. */
  private byte[] compress(short[] rawShorts) {

    // Convert short[] → byte[]
    byte[] rawBytes = new byte[rawShorts.length * 2];

    for (int i = 0; i < rawShorts.length; i++) {
      rawBytes[i * 2] = (byte) (rawShorts[i] >> 8);
      rawBytes[i * 2 + 1] = (byte) rawShorts[i];
    }

    Deflater deflater = new Deflater();
    deflater.setInput(rawBytes);
    deflater.finish();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[COMPRESSION_BUFFER_SIZE];

    while (!deflater.finished()) {
      int count = deflater.deflate(buffer);
      baos.write(buffer, 0, count);
    }

    deflater.end();
    return baos.toByteArray();
  }

  /**
   * Decompresses the chunk block data back into a short[] block array.
   *
   * @return decompressed block array
   */
  public short[] decompress() {

    try {

      Inflater inflater = new Inflater();
      inflater.setInput(this.data);

      int expectedSize = ChunkData.WIDTH * ChunkData.DEPTH * ChunkData.HEIGHT * 2;

      byte[] raw = new byte[expectedSize];

      int resultLength = inflater.inflate(raw);
      inflater.end();

      short[] result = new short[resultLength / 2];

      for (int i = 0; i < result.length; i++) {
        result[i] = (short) (((raw[i * 2] & 0xFF) << 8) | (raw[i * 2 + 1] & 0xFF));
      }

      return result;

    } catch (Exception e) {
      e.printStackTrace();
      return new short[0];
    }
  }

  public int getChunkX() {
    return x;
  }

  public int getChunkZ() {
    return z;
  }
}

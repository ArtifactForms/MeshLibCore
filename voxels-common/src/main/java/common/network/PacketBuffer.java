package common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PacketBuffer {

  private DataInputStream in;
  private DataOutputStream out;

  public PacketBuffer(DataInputStream in, DataOutputStream out) {
    this.in = in;
    this.out = out;
  }

  public int readInt() throws IOException {
    return in.readInt();
  }

  public void writeInt(int v) throws IOException {
    out.writeInt(v);
  }

  public short readShort() throws IOException {
    return in.readShort();
  }

  public void writeShort(short v) throws IOException {
    out.writeShort(v);
  }

  public float readFloat() throws IOException {
    return in.readFloat();
  }

  public void writeFloat(float v) throws IOException {
    out.writeFloat(v);
  }

  public long readLong() throws IOException {
    return in.readLong();
  }

  public void writeLong(long v) throws IOException {
    out.writeLong(v);
  }

  public String readString() throws IOException {
    return in.readUTF();
  }

  public void writeString(String s) throws IOException {
    out.writeUTF(s);
  }

  public void readBytes(byte[] dest) throws IOException {
    in.readFully(dest);
  }

  public void writeBytes(byte[] src) throws IOException {
    out.write(src);
  }

  public UUID readUuid() throws IOException {
    // Reconstruct the UUID from high and low bits
    long most = in.readLong();
    long least = in.readLong();
    UUID uuid = new UUID(most, least);
    return uuid;
  }

  public void writeUuid(UUID uuid) throws IOException {
    // We split the UUID into two 64-bit longs (total 16 bytes)
    out.writeLong(uuid.getMostSignificantBits());
    out.writeLong(uuid.getLeastSignificantBits());
  }
}

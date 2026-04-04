package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.network.ServerToClientPacket;

/**
 * Packet used to synchronize the world time between server and client.
 *
 * <p>This packet typically flows from the server to clients to ensure that all connected instances
 * share a consistent notion of time (e.g. for day/night cycles, animations, or time-based logic).
 */
public class TimeUpdatePacket implements Packet, ServerToClientPacket {

  /** The current world time in ticks (or the unit defined by the game logic). */
  private long worldTime;

  /** Default constructor required for packet deserialization. */
  public TimeUpdatePacket() {}

  /**
   * Creates a new {@code TimeUpdatePacket} with the given world time.
   *
   * @param worldTime the current world time to synchronize
   */
  public TimeUpdatePacket(long worldTime) {
    this.worldTime = worldTime;
  }

  /**
   * Writes this packet's data to the given {@link PacketBuffer}.
   *
   * @param out the buffer to write to
   * @throws IOException if an I/O error occurs during writing
   */
  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeLong(worldTime);
  }

  /**
   * Reads this packet's data from the given {@link PacketBuffer}.
   *
   * @param in the buffer to read from
   * @throws IOException if an I/O error occurs during reading
   */
  @Override
  public void read(PacketBuffer in) throws IOException {
    this.worldTime = in.readLong();
  }

  /**
   * Returns the unique packet ID used for network identification.
   *
   * @return the packet ID for {@code TimeUpdatePacket}
   */
  @Override
  public int getId() {
    return PacketIds.TIME_UPDATE;
  }

  /**
   * Gets the synchronized world time contained in this packet.
   *
   * @return the world time value
   */
  public long getWorldTime() {
    return worldTime;
  }
}

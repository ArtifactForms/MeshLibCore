package common.network;

import java.io.IOException;

/**
 * A passive data carrier for network communication. Responsibility: Serialization and
 * identification only.
 */
public interface Packet {

  /** Serializes the packet data to the buffer. */
  void write(PacketBuffer out) throws IOException;

  /** Deserializes the packet data from the buffer. */
  void read(PacketBuffer in) throws IOException;

  /** @return The unique protocol ID for this packet type. */
  int getId();
}

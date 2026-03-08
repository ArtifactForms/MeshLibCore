package common.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Manages the mapping between network IDs and packet constructors. */
public class PacketRegistry {

  /** Map storing the packet ID as key and a Supplier (constructor) as value. */
  private static final Map<Integer, Supplier<Packet>> packets = new HashMap<>();

  /**
   * Registers a packet type by automatically extracting the ID from a temporary instance. This
   * ensures that the registration matches the ID defined within the packet class. * @param supplier
   * A reference to the packet constructor (e.g., MyPacket::new).
   */
  public static void register(Supplier<Packet> supplier) {
    // Create a temporary instance to retrieve the packet's ID
    Packet tempInstance = supplier.get();
    int id = tempInstance.getId();

    register(id, supplier);
  }

  /**
   * Internal method to map a specific ID to a packet supplier. * @param id The protocol ID.
   *
   * @param supplier The packet constructor.
   */
  public static void register(int id, Supplier<Packet> supplier) {
    packets.put(id, supplier);
  }

  /**
   * Creates a new packet instance for a given protocol ID. * @param id The ID received from the
   * network stream.
   *
   * @return A new instance of the corresponding Packet.
   * @throws RuntimeException if the ID is not registered.
   */
  public static Packet create(int id) {
    Supplier<Packet> supplier = packets.get(id);

    if (supplier == null) {
      throw new RuntimeException("Unknown packet id: " + id);
    }

    return supplier.get();
  }
}

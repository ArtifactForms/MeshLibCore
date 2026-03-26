package common.network.packets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.Test;

import common.game.GameMode;
import common.network.PacketBuffer;

public class GameModeUpdatePacketTest {

  @Test
  void testSerializationAllGameModes() throws Exception {

    for (GameMode mode : GameMode.values()) {

      GameModeUpdatePacket original = new GameModeUpdatePacket(mode);

      // --- write ---
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      PacketBuffer out = new PacketBuffer(null, dos);

      original.write(out);

      // --- read ---
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      DataInputStream dis = new DataInputStream(bais);
      PacketBuffer in = new PacketBuffer(dis, null);

      GameModeUpdatePacket read = new GameModeUpdatePacket();
      read.read(in);

      assertEquals(mode, read.getGameMode(), "Failed for mode: " + mode);
    }
  }

  @Test
  void testInvalidGameModeIdThrows() throws Exception {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    PacketBuffer out = new PacketBuffer(null, dos);

    out.writeByte((byte) 255); // invalid id

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInputStream dis = new DataInputStream(bais);
    PacketBuffer in = new PacketBuffer(dis, null);

    GameModeUpdatePacket packet = new GameModeUpdatePacket();

    IOException ex =
        assertThrows(
            IOException.class,
            () -> {
              packet.read(in);
            });

    assertTrue(ex.getMessage().contains("Invalid GameMode id"));
  }

  @Test
  void testWriteNullGameModeThrows() throws Exception {

    GameModeUpdatePacket packet = new GameModeUpdatePacket();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    PacketBuffer out = new PacketBuffer(null, dos);

    IOException ex =
        assertThrows(
            IOException.class,
            () -> {
              packet.write(out);
            });

    assertTrue(ex.getMessage().contains("GameMode cannot be null"));
  }
}

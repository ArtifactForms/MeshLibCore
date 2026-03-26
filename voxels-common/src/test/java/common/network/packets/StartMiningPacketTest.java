package common.network.packets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import common.network.PacketBuffer;
import common.world.BlockFace;

public class StartMiningPacketTest {

  @Test
  void testAllBlockFacesSerialization() throws Exception {

    for (BlockFace face : BlockFace.values()) {

      StartMiningPacket original = new StartMiningPacket();

      setField(original, "x", 10);
      setField(original, "y", 20);
      setField(original, "z", 30);
      setField(original, "selectedSlot", 2);
      setField(original, "face", face);
      setField(original, "pitch", 45.5f);
      setField(original, "yaw", 90.25f);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      PacketBuffer out = new PacketBuffer(null, dos);

      original.write(out);

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      DataInputStream dis = new DataInputStream(bais);
      PacketBuffer in = new PacketBuffer(dis, null);

      StartMiningPacket read = new StartMiningPacket();
      read.read(in);

      assertEquals(10, read.getX());
      assertEquals(20, read.getY());
      assertEquals(30, read.getZ());
      assertEquals(2, read.getSelectedSlot());
      assertEquals(face, read.getFace(), "Failed for face: " + face);
      assertEquals(45.5f, read.getPitch());
      assertEquals(90.25f, read.getYaw());
    }
  }

  private void setField(Object obj, String name, Object value) throws Exception {
    Field field = obj.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.set(obj, value);
  }

  @Test
  void testInvalidBlockFaceThrowsException() throws Exception {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    PacketBuffer out = new PacketBuffer(null, dos);

    out.writeInt(1); // x
    out.writeInt(2); // y
    out.writeInt(3); // z
    out.writeInt(0); // selectedSlot

    out.writeByte((byte) 255);

    out.writeFloat(0f);
    out.writeFloat(0f);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInputStream dis = new DataInputStream(bais);
    PacketBuffer in = new PacketBuffer(dis, null);

    StartMiningPacket packet = new StartMiningPacket();

    IOException exception =
        assertThrows(
            IOException.class,
            () -> {
              packet.read(in);
            });

    assertTrue(exception.getMessage().contains("Invalid BlockFace index"));
  }
}

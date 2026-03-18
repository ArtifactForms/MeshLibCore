package common.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import common.game.ItemStack;

class PacketBufferTest {

  private ByteArrayOutputStream byteOut;
  private PacketBuffer writeBuffer;

  @BeforeEach
  void setUp() {
    byteOut = new ByteArrayOutputStream();
    writeBuffer = new PacketBuffer(null, new DataOutputStream(byteOut));
  }

  private PacketBuffer getReadBuffer() {
    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
    return new PacketBuffer(new DataInputStream(byteIn), null);
  }

  @Test
  @DisplayName("Should correctly round-trip primitives")
  void testPrimitives() throws IOException {
    writeBuffer.writeInt(42);
    writeBuffer.writeFloat(3.14f);
    writeBuffer.writeBoolean(true);
    writeBuffer.writeLong(123456789L);

    PacketBuffer reader = getReadBuffer();
    assertEquals(42, reader.readInt());
    assertEquals(3.14f, reader.readFloat(), 1e-5f);
    assertTrue(reader.readBoolean());
    assertEquals(123456789L, reader.readLong());
  }

  @Test
  @DisplayName("Should handle normal and null strings")
  void testStrings() throws IOException {
    writeBuffer.writeString("Hello World");
    writeBuffer.writeString(null);
    writeBuffer.writeString("🚀"); // UTF-8 Check

    PacketBuffer reader = getReadBuffer();
    assertEquals("Hello World", reader.readString());
    assertNull(reader.readString());
    assertEquals("🚀", reader.readString());
  }

  @Test
  @DisplayName("Should throw exception if string is too large (Exploit Prevention)")
  void testStringLimit() {
    String huge = "A".repeat(Short.MAX_VALUE + 1);

    assertThrows(IOException.class, () -> writeBuffer.writeString(huge));
  }

  @Test
  @DisplayName("Should correctly round-trip UUIDs")
  void testUuid() throws IOException {
    UUID original = UUID.randomUUID();
    writeBuffer.writeUuid(original);

    PacketBuffer reader = getReadBuffer();
    assertEquals(original, reader.readUuid());
  }

  @Test
  @DisplayName("Should handle ItemStacks including null entries")
  void testItemStacks() throws IOException {
    ItemStack[] original =
        new ItemStack[] {new ItemStack((short) 1, 64), null, new ItemStack((short) 5, 1)};

    writeBuffer.writeItems(original);

    PacketBuffer reader = getReadBuffer();
    ItemStack[] result = reader.readItems();

    assertEquals(3, result.length);
    assertEquals(1, result[0].getItemId());
    assertEquals(64, result[0].getAmount());
    assertNull(result[1]);
    assertEquals(5, result[2].getItemId());
  }

  @Test
  @DisplayName("Should throw exception on negative string length (Corruption Check)")
  void testNegativeStringLength() throws IOException {
    // Manually write a malicious length to the stream
    DataOutputStream dos = new DataOutputStream(byteOut);
    dos.writeInt(-5);

    PacketBuffer reader = getReadBuffer();
    assertThrows(IOException.class, reader::readString);
  }
}

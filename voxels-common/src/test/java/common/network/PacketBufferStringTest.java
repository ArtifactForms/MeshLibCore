package common.network;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class PacketBufferStringTest {

  @Test
  void writeString_maxLength_ok() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer buffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    // Create string exactly at max byte size
    byte[] bytes = new byte[PacketBuffer.MAX_STRING_LENGTH];
    String s = new String(bytes, StandardCharsets.UTF_8);

    assertDoesNotThrow(() -> buffer.writeString(s));
  }

  @Test
  void writeString_tooLong_throws() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer buffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    // Create string exceeding max byte size
    byte[] bytes = new byte[PacketBuffer.MAX_STRING_LENGTH + 1];
    String s = new String(bytes, StandardCharsets.UTF_8);

    IOException ex = assertThrows(IOException.class, () -> buffer.writeString(s));
    assertTrue(ex.getMessage().contains("String too big"));
  }

  @Test
  void readString_lengthTooLarge_throws() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);

    // Write invalid length header (too large)
    out.writeInt(PacketBuffer.MAX_STRING_LENGTH + 1);

    PacketBuffer buffer =
        new PacketBuffer(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())), out);

    IOException ex = assertThrows(IOException.class, buffer::readString);
    assertTrue(ex.getMessage().contains("too large"));
  }

  @Test
  void readString_negativeLength_throws() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);

    // Write invalid negative length (not -1)
    out.writeInt(-2);

    PacketBuffer buffer =
        new PacketBuffer(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())), out);

    IOException ex = assertThrows(IOException.class, buffer::readString);
    assertTrue(ex.getMessage().contains("cannot be negative"));
  }

  @Test
  void writeAndReadString_roundtrip_ok() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    PacketBuffer writeBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    String original = "Hello PacketBuffer!";
    writeBuffer.writeString(original);

    PacketBuffer readBuffer =
        new PacketBuffer(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())), null);

    String read = readBuffer.readString();

    assertEquals(original, read);
  }
}

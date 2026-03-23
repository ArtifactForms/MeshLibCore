package common.network.packets;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.Test;

import common.network.PacketBuffer;
import common.network.PacketIds;

class ActionBarPacketTest {

  private PacketBuffer createBuffer(ByteArrayOutputStream baos) {
    return new PacketBuffer(
        new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
        new DataOutputStream(baos));
  }

  @Test
  void testWriteReadRoundTrip() throws IOException {
    // Arrange
    String text = "Hello ActionBar!";
    int duration = 100;

    ActionBarPacket original = new ActionBarPacket(text, duration);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    // Act - write
    original.write(writeBuffer);

    // Prepare read buffer
    PacketBuffer readBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
            new DataOutputStream(new ByteArrayOutputStream()));

    ActionBarPacket readPacket = new ActionBarPacket();
    readPacket.read(readBuffer);

    // Assert
    assertEquals(text, readPacket.getText());
    assertEquals(duration, readPacket.getDurationInTicks());
  }

  @Test
  void testNullString() throws IOException {
    // Arrange
    ActionBarPacket original = new ActionBarPacket(null, 50);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    // Act
    original.write(writeBuffer);

    PacketBuffer readBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
            new DataOutputStream(new ByteArrayOutputStream()));

    ActionBarPacket readPacket = new ActionBarPacket();
    readPacket.read(readBuffer);

    // Assert
    assertNull(readPacket.getText());
    assertEquals(50, readPacket.getDurationInTicks());
  }

  @Test
  void testGetId() {
    ActionBarPacket packet = new ActionBarPacket();
    assertEquals(PacketIds.ACTION_BAR, packet.getId());
  }

  @Test
  void testEmptyString() throws IOException {
    // Arrange
    ActionBarPacket original = new ActionBarPacket("", 10);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    // Act
    original.write(writeBuffer);

    PacketBuffer readBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
            new DataOutputStream(new ByteArrayOutputStream()));

    ActionBarPacket readPacket = new ActionBarPacket();
    readPacket.read(readBuffer);

    // Assert
    assertEquals("", readPacket.getText());
    assertEquals(10, readPacket.getDurationInTicks());
  }

  @Test
  void testMaxStringLengthBoundary() throws IOException {
    // Arrange
    int maxLength = PacketBuffer.MAX_STRING_LENGTH;

    // Create a string exactly at the allowed limit
    StringBuilder sb = new StringBuilder(maxLength);
    for (int i = 0; i < maxLength; i++) {
      sb.append('a');
    }
    String largeString = sb.toString();

    ActionBarPacket original = new ActionBarPacket(largeString, 20);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));

    // Act
    original.write(writeBuffer);

    PacketBuffer readBuffer =
        new PacketBuffer(
            new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
            new DataOutputStream(new ByteArrayOutputStream()));

    ActionBarPacket readPacket = new ActionBarPacket();
    readPacket.read(readBuffer);

    // Assert
    assertEquals(largeString, readPacket.getText());
    assertEquals(20, readPacket.getDurationInTicks());
  }
}

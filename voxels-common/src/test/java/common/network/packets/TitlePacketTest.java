package common.network.packets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import common.network.PacketBuffer;
import common.network.PacketIds;

class TitlePacketTest {

  private PacketBuffer createWriteBuffer(ByteArrayOutputStream baos) {
    return new PacketBuffer(
        new DataInputStream(new ByteArrayInputStream(new byte[0])), new DataOutputStream(baos));
  }

  private PacketBuffer createReadBuffer(ByteArrayOutputStream baos) {
    return new PacketBuffer(
        new DataInputStream(new ByteArrayInputStream(baos.toByteArray())),
        new DataOutputStream(new ByteArrayOutputStream()));
  }

  @Test
  void testWriteReadRoundTrip() throws IOException {
    // Arrange
    TitlePacket original = new TitlePacket("Main Title", "Subtitle here", 10, 70, 20);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer = createWriteBuffer(baos);

    // Act
    original.write(writeBuffer);

    TitlePacket readPacket = new TitlePacket();
    readPacket.read(createReadBuffer(baos));

    // Assert
    assertEquals("Main Title", readPacket.getTitle());
    assertEquals("Subtitle here", readPacket.getSubtitle());
    assertEquals(10, readPacket.getFadeInTicks());
    assertEquals(70, readPacket.getStayTicks());
    assertEquals(20, readPacket.getFadeOutTicks());
  }

  @Test
  void testNullStrings() throws IOException {
    // Arrange
    TitlePacket original = new TitlePacket(null, null, 5, 10, 5);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer = createWriteBuffer(baos);

    // Act
    original.write(writeBuffer);

    TitlePacket readPacket = new TitlePacket();
    readPacket.read(createReadBuffer(baos));

    // Assert
    assertNull(readPacket.getTitle());
    assertNull(readPacket.getSubtitle());
    assertEquals(5, readPacket.getFadeInTicks());
    assertEquals(10, readPacket.getStayTicks());
    assertEquals(5, readPacket.getFadeOutTicks());
  }

  @Test
  void testEmptyStrings() throws IOException {
    // Arrange
    TitlePacket original = new TitlePacket("", "", 1, 2, 3);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer = createWriteBuffer(baos);

    // Act
    original.write(writeBuffer);

    TitlePacket readPacket = new TitlePacket();
    readPacket.read(createReadBuffer(baos));

    // Assert
    assertEquals("", readPacket.getTitle());
    assertEquals("", readPacket.getSubtitle());
    assertEquals(1, readPacket.getFadeInTicks());
    assertEquals(2, readPacket.getStayTicks());
    assertEquals(3, readPacket.getFadeOutTicks());
  }

  @Test
  void testMaxStringLengthBoundary() throws IOException {
    // Arrange
    int maxLength = PacketBuffer.MAX_STRING_LENGTH;

    StringBuilder sb = new StringBuilder(maxLength);
    for (int i = 0; i < maxLength; i++) {
      sb.append('a');
    }
    String largeString = sb.toString();

    TitlePacket original = new TitlePacket(largeString, largeString, 10, 20, 30);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer writeBuffer = createWriteBuffer(baos);

    // Act
    original.write(writeBuffer);

    TitlePacket readPacket = new TitlePacket();
    readPacket.read(createReadBuffer(baos));

    // Assert
    assertEquals(largeString, readPacket.getTitle());
    assertEquals(largeString, readPacket.getSubtitle());
  }

  @Test
  void testStringTooLargeThrows() {
    int tooLarge = PacketBuffer.MAX_STRING_LENGTH + 1;

    StringBuilder sb = new StringBuilder(tooLarge);
    for (int i = 0; i < tooLarge; i++) {
      sb.append('a');
    }

    TitlePacket packet = new TitlePacket(sb.toString(), "ok", 1, 1, 1);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PacketBuffer buffer = createWriteBuffer(baos);

    assertThrows(IOException.class, () -> packet.write(buffer));
  }

  @Test
  void testGetId() {
    TitlePacket packet = new TitlePacket();
    assertEquals(PacketIds.TITLE, packet.getId());
  }
}

package config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import server.config.ServerConfig;

public class ServerConfigTest {

  @TempDir Path tempDir;

  private File getConfigFile() {
    return tempDir.resolve("server.properties").toFile();
  }

  private ServerConfig createConfig(File file) {
    return new ServerConfig(file.getAbsolutePath());
  }

  // No file → defaults must be used
  @Test
  void testDefaultsWhenFileMissing() {
    File file = getConfigFile();

    ServerConfig config = createConfig(file);
    config.load();

    assertEquals(25565, config.getPort());
    assertEquals(10, config.getMaxPlayers());
    assertEquals(8, config.getViewDistance());
    assertEquals(256, config.getMaxChatMessageLength());
    assertEquals("A Voxel Server", config.getMotd());
    assertEquals("{prefix}{name}: {message}", config.getChatFormat());
  }

  // Invalid values → fallback to defaults
  @Test
  void testInvalidValuesFallbackToDefaults() throws Exception {
    File file = getConfigFile();

    try (FileWriter writer = new FileWriter(file)) {
      writer.write("server-port=not_a_number\n");
      writer.write("max-players=invalid\n");
      writer.write("view-distance=???\n");
      writer.write("max-chat-message-length=bad\n");
    }

    ServerConfig config = createConfig(file);
    config.load();

    assertEquals(25565, config.getPort());
    assertEquals(10, config.getMaxPlayers());
    assertEquals(8, config.getViewDistance());
    assertEquals(256, config.getMaxChatMessageLength());
  }

  // Partial config → mix of file + defaults
  @Test
  void testPartialConfigUsesDefaults() throws Exception {
    File file = getConfigFile();

    try (FileWriter writer = new FileWriter(file)) {
      writer.write("server-port=12345\n");
    }

    ServerConfig config = createConfig(file);
    config.load();

    assertEquals(12345, config.getPort());
    assertEquals(10, config.getMaxPlayers());
    assertEquals(8, config.getViewDistance());
    assertEquals(256, config.getMaxChatMessageLength());
  }

  // Valid config → values must be applied
  @Test
  void testValidConfigLoadsCorrectly() throws Exception {
    File file = getConfigFile();

    try (FileWriter writer = new FileWriter(file)) {
      writer.write("server-port=30000\n");
      writer.write("max-players=20\n");
      writer.write("view-distance=12\n");
      writer.write("max-chat-message-length=512\n");
      writer.write("motd=Hello World\n");
      writer.write("chat-format=<{name}> {message}\n");
    }

    ServerConfig config = createConfig(file);
    config.load();

    assertEquals(30000, config.getPort());
    assertEquals(20, config.getMaxPlayers());
    assertEquals(12, config.getViewDistance());
    assertEquals(512, config.getMaxChatMessageLength());
    assertEquals("Hello World", config.getMotd());
    assertEquals("<{name}> {message}", config.getChatFormat());
  }

  // Ensure config file gets created
  @Test
  void testConfigFileIsCreatedIfMissing() {
    File file = getConfigFile();
    assertFalse(file.exists());

    ServerConfig config = createConfig(file);
    config.load();

    assertTrue(file.exists());
  }
}

package config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.config.ServerConfig;

public class ServerConfigTest {

  private static final String CONFIG_FILE = "server.properties";

  @BeforeEach
  void setup() {
    deleteConfigFile();
  }

  @AfterEach
  void cleanup() {
    deleteConfigFile();
  }

  private void deleteConfigFile() {
    File file = new File(CONFIG_FILE);
    if (file.exists()) {
      file.delete();
    }
  }

  // No file → defaults must be used
  @Test
  void testDefaultsWhenFileMissing() {
    ServerConfig config = new ServerConfig();
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
    try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
      writer.write("server-port=not_a_number\n");
      writer.write("max-players=invalid\n");
      writer.write("view-distance=???\n");
      writer.write("max-chat-message-length=bad\n");
    }

    ServerConfig config = new ServerConfig();
    config.load();

    assertEquals(25565, config.getPort());
    assertEquals(10, config.getMaxPlayers());
    assertEquals(8, config.getViewDistance());
    assertEquals(256, config.getMaxChatMessageLength());
  }

  // Partial config → mix of file + defaults
  @Test
  void testPartialConfigUsesDefaults() throws Exception {
    try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
      writer.write("server-port=12345\n");
    }

    ServerConfig config = new ServerConfig();
    config.load();

    assertEquals(12345, config.getPort()); // from file
    assertEquals(10, config.getMaxPlayers()); // default
    assertEquals(8, config.getViewDistance()); // default
    assertEquals(256, config.getMaxChatMessageLength()); // default
  }

  // Valid config → values must be applied
  @Test
  void testValidConfigLoadsCorrectly() throws Exception {
    try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
      writer.write("server-port=30000\n");
      writer.write("max-players=20\n");
      writer.write("view-distance=12\n");
      writer.write("max-chat-message-length=512\n");
      writer.write("motd=Hello World\n");
      writer.write("chat-format=<{name}> {message}\n");
    }

    ServerConfig config = new ServerConfig();
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
    File file = new File(CONFIG_FILE);
    assertFalse(file.exists());

    ServerConfig config = new ServerConfig();
    config.load();

    assertTrue(file.exists());
  }
}

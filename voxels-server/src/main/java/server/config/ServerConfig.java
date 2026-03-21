package server.config;

import java.io.*;
import java.util.Properties;

import common.logging.Log;

public class ServerConfig {

  private final String filePath;

  // Keys
  private static final String SERVER_PORT_KEY = "server-port";
  private static final String MAX_PLAYERS_KEY = "max-players";
  private static final String MOTD_KEY = "motd";
  private static final String CHAT_FORMAT_KEY = "chat-format";
  private static final String VIEW_DISTANCE_KEY = "view-distance";
  private static final String MAX_CHAT_MESSAGE_LENGTH_KEY = "max-chat-message-length";

  // Defaults (single source of truth)
  private static final int DEFAULT_PORT = 25565;
  private static final int DEFAULT_MAX_PLAYERS = 10;
  private static final int DEFAULT_VIEW_DISTANCE = 8;
  private static final int DEFAULT_MAX_CHAT_LENGTH = 256;

  private static final String DEFAULT_MOTD = "A Voxel Server";
  private static final String DEFAULT_CHAT_FORMAT = "{prefix}{name}: {message}";

  private final Properties props = new Properties();

  private int port;
  private int maxPlayers;
  private int maxChatMessageLength;
  private int viewDistance;
  private String motd;
  private String chatFormat;

  public ServerConfig() {
    this("server.properties"); // default behavior
  }

  public ServerConfig(String filePath) {
    this.filePath = filePath;
  }

  public void load() {

    // initialize defaults first (safe baseline)
    this.port = DEFAULT_PORT;
    this.maxPlayers = DEFAULT_MAX_PLAYERS;
    this.viewDistance = DEFAULT_VIEW_DISTANCE;
    this.maxChatMessageLength = DEFAULT_MAX_CHAT_LENGTH;
    this.motd = DEFAULT_MOTD;
    this.chatFormat = DEFAULT_CHAT_FORMAT;

    File file = new File(filePath);

    if (!file.exists()) {
      createDefaultConfig(file);
    }

    try (InputStream in = new FileInputStream(file)) {
      props.load(in);

      // Parse values safely
      this.port = parseInt(SERVER_PORT_KEY, DEFAULT_PORT);
      this.maxPlayers = parseInt(MAX_PLAYERS_KEY, DEFAULT_MAX_PLAYERS);
      this.maxChatMessageLength = parseInt(MAX_CHAT_MESSAGE_LENGTH_KEY, DEFAULT_MAX_CHAT_LENGTH);
      this.viewDistance = parseInt(VIEW_DISTANCE_KEY, DEFAULT_VIEW_DISTANCE);

      this.motd = props.getProperty(MOTD_KEY, DEFAULT_MOTD);
      this.chatFormat = props.getProperty(CHAT_FORMAT_KEY, DEFAULT_CHAT_FORMAT);

      // Optional: ensure missing values get written back
      saveIfMissing(file);

    } catch (IOException e) {
      Log.warn("Failed to load server.properties, using defaults.");
    }
  }

  private void createDefaultConfig(File file) {
    Log.info("No server.properties found. Creating default config...");

    props.setProperty(SERVER_PORT_KEY, String.valueOf(DEFAULT_PORT));
    props.setProperty(MAX_PLAYERS_KEY, String.valueOf(DEFAULT_MAX_PLAYERS));
    props.setProperty(MAX_CHAT_MESSAGE_LENGTH_KEY, String.valueOf(DEFAULT_MAX_CHAT_LENGTH));
    props.setProperty(MOTD_KEY, DEFAULT_MOTD);
    props.setProperty(CHAT_FORMAT_KEY, DEFAULT_CHAT_FORMAT);
    props.setProperty(VIEW_DISTANCE_KEY, String.valueOf(DEFAULT_VIEW_DISTANCE));

    save(file);
  }

  private void saveIfMissing(File file) {
    boolean changed = false;

    changed |= setIfMissing(SERVER_PORT_KEY, DEFAULT_PORT);
    changed |= setIfMissing(MAX_PLAYERS_KEY, DEFAULT_MAX_PLAYERS);
    changed |= setIfMissing(MAX_CHAT_MESSAGE_LENGTH_KEY, DEFAULT_MAX_CHAT_LENGTH);
    changed |= setIfMissing(MOTD_KEY, DEFAULT_MOTD);
    changed |= setIfMissing(CHAT_FORMAT_KEY, DEFAULT_CHAT_FORMAT);
    changed |= setIfMissing(VIEW_DISTANCE_KEY, DEFAULT_VIEW_DISTANCE);

    if (changed) {
      Log.info("Updating server.properties with missing values...");
      save(file);
    }
  }

  private boolean setIfMissing(String key, Object value) {
    if (!props.containsKey(key)) {
      props.setProperty(key, String.valueOf(value));
      return true;
    }
    return false;
  }

  private void save(File file) {
    try (OutputStream out = new FileOutputStream(file)) {
      props.store(out, "Voxel Server Configuration - Edit with care");
    } catch (IOException e) {
      Log.error("Could not write server.properties!");
      e.printStackTrace();
    }
  }

  private int parseInt(String key, int defaultValue) {
    try {
      String value = props.getProperty(key);
      if (value == null) return defaultValue;
      return Integer.parseInt(value);
    } catch (Exception e) {
      Log.warn("Invalid value for '" + key + "', using default: " + defaultValue);
      return defaultValue;
    }
  }

  // Getters

  public int getPort() {
    return port;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public int getMaxChatMessageLength() {
    return maxChatMessageLength;
  }

  public String getMotd() {
    return motd;
  }

  public String getChatFormat() {
    return chatFormat;
  }

  public int getViewDistance() {
    return viewDistance;
  }
}

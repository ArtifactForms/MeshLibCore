package server.config;

import java.io.*;
import java.util.Properties;

import common.logging.Log;

public class ServerConfig {

  private static final String FILE_PATH = "server.properties";
  private final Properties props = new Properties();

  private int port;
  private int maxPlayers;
  private int maxChatMessageLength;
  private String motd;
  private String chatFormat;

  public void load() {
    File file = new File(FILE_PATH);

    if (!file.exists()) {
      createDefaultConfig(file);
    }

    try (InputStream in = new FileInputStream(file)) {
      props.load(in);
      this.port = Integer.parseInt(props.getProperty("server-port", "25565"));
      this.maxPlayers = Integer.parseInt(props.getProperty("max-players", "10"));
      this.maxChatMessageLength =
          Integer.parseInt(props.getProperty("max-chat-message-length", "256"));
      this.motd = props.getProperty("motd", "A Voxel Game Server");
      this.chatFormat = props.getProperty("chat-format", "{prefix}{name}: {message}");
    } catch (IOException e) {
      System.err.println("Failed to load server.properties, using hardware defaults.");
      this.port = 25565;
    }
  }

  private void createDefaultConfig(File file) {
    Log.info("No server.properties found. Creating a new one with defaults...");

    props.setProperty("server-port", "25565");
    props.setProperty("max-players", "10");
    props.setProperty("max-chat-message-length", "256");
    props.setProperty("motd", "A Voxel Game Server");
    props.setProperty("chat-format", "{prefix}{name}: {message}");
    props.setProperty("view-distance", "8");
    props.setProperty("allow-flight", "false");

    try (OutputStream out = new FileOutputStream(file)) {
      props.store(out, "Voxel Server Configuration - Edit with care");
    } catch (IOException e) {
      Log.error("Could not write default configuration file!");
      e.printStackTrace();
    }
  }

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
}

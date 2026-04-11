package server.usecases.chat;

import server.gateways.ConfigGateway;

public class ChatFormatter {

  private static final String DEFAULT_PREFIX = "[Player]";

  private static String getPrefix() {
    // The prefix might change in the future and include ranks / permissions / groups
    return DEFAULT_PREFIX;
  }

  public static String format(ConfigGateway config, String playerName, String message) {

    String format = config.getChatFormat(); // e.g. "{prefix}{name}: {message}"

    return format
        .replace("{prefix}", getPrefix())
        .replace("{name}", playerName)
        .replace("{message}", message);
  }
}

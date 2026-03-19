package server.usecases.chat;

import server.gateways.ConfigGateway;
import server.player.ServerPlayer;

public class ChatFormatter {

  private static String getPrefix(ServerPlayer player) {

    // später: Permissions / Groups
    // jetzt simple

    return "[Player] ";
  }

  public static String format(ConfigGateway config, ServerPlayer player, String message) {

    String format = config.getChatFormat(); // "{prefix}{name}: {message}"

    return format
        .replace("{prefix}", getPrefix(player))
        .replace("{name}", player.getName())
        .replace("{message}", message);
  }
}

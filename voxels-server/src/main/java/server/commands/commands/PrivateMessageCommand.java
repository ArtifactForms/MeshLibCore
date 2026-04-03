package server.commands.commands;

import java.util.List;
import java.util.UUID;

import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.commands.CommandMessages;
import server.gateways.MessageGateway;
import server.gateways.PlayerGateway;
import server.permissions.Permissions;

public class PrivateMessageCommand extends AbstractCommand {

  private final PlayerGateway players;
  private final MessageGateway messages;

  public PrivateMessageCommand(PlayerGateway players, MessageGateway messages) {
    this.players = players;
    this.messages = messages;
  }

  @Override
  public void execute(CommandContext ctx) {

    // Console is not allowed to send PMs
    if (ctx.isConsole()) {
      ctx.reply(CommandMessages.PLAYERS_ONLY);
      return;
    }

    List<String> args = ctx.getArgs();

    if (args.size() < 2) {
      ctx.reply("§cUsage: /msg <player> <message>");
      return;
    }

    UUID senderId = ctx.getPlayer();
    String senderName = players.getName(senderId);

    String receiverName = args.get(0);
    UUID receiverId = players.getPlayerIdByName(receiverName);

    if (receiverId == null) {
      ctx.reply("§cPlayer '" + receiverName + "' not found.");
      return;
    }

    // Build message
    String messageContent = String.join(" ", args.subList(1, args.size()));

    // Format message
    String toReceiver = "§7[§dPM§7] §f" + senderName + " §7→ §fYou§7: §f" + messageContent;

    String toSender = "§7[§dPM§7] §fYou §7→ §f" + receiverName + "§7: §f" + messageContent;

    // Send
    messages.sendMessage(receiverId, toReceiver);
    messages.sendMessage(senderId, toSender);
  }

  @Override
  public String[] getAliases() {
    return new String[] {"pm", "tell", "whisper"};
  }

  @Override
  public String getName() {
    return "msg";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_MSG;
  }

  @Override
  public String getDescription() {
    return "Sends a private message to a player.";
  }
}

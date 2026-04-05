package server.commands.commands;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import common.game.GameMode;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.commands.CommandMessages;
import server.gateways.MessageGateway;
import server.permissions.Permissions;
import server.usecases.changegamemode.ChangeGameModePresenter;
import server.usecases.changegamemode.ChangeGameModeRequestModel;
import server.usecases.changegamemode.ChangeGamemode;
import server.usecases.changegamemode.ChangeGamemode.ChangeGameModeRequest;
import server.usecases.changegamemode.ChangeGamemode.ChangeGameModeResponse;

public class ChangeGameModeCommand extends AbstractCommand {

  private MessageGateway messages;

  public ChangeGameModeCommand(MessageGateway messages) {
    this.messages = messages;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      ctx.reply(CommandMessages.PLAYERS_ONLY);
      return;
    }

    List<String> args = ctx.getArgs();
    if (args.isEmpty()) {
      ctx.reply("Usage: " + getUsage());
      return;
    }

    String arg = args.get(0).toLowerCase(Locale.ROOT);
    GameMode gameMode = parseGameMode(arg);

    if (gameMode == null) {
      ctx.reply("Invalid argument: " + arg);
      return;
    }

    executeUseCase(ctx, gameMode);
  }

  private void executeUseCase(CommandContext ctx, GameMode gameMode) {
    UUID playerId = ctx.getPlayer();
    ChangeGamemode changeUseCase = ctx.getUseCases().get(ChangeGamemode.class);
    ChangeGameModeRequest request = new ChangeGameModeRequestModel(playerId, gameMode);
    ChangeGameModeResponse response = new ChangeGameModePresenter(messages);
    changeUseCase.execute(request, response);
  }

  private GameMode parseGameMode(String arg) {
    switch (arg) {
      case "creative":
      case "1":
      case "c":
        return GameMode.CREATIVE;
      case "survival":
      case "0":
      case "s":
        return GameMode.SURVIVAL;
      default:
        return null;
    }
  }

  @Override
  public String getName() {
    return "gamemode";
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"survival|creative|0|1|s|c"};
  }

  @Override
  public String[] getAliases() {
    return new String[] {"gm"};
  }

  @Override
  public String getDescription() {
    return "Changes your current game mode.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_GAMEMODE;
  }
}

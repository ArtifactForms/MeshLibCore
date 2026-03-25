package server.commands.commands;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import common.game.GameMode;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;
import server.usecases.changegamemode.ChangeGamemode;

public class ChangeGameModeCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    UUID playerId = ctx.getPlayer();

    if (playerId == null) {
      ctx.reply("This command can only be executed by a player.");
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

    ChangeGamemode changeUseCase = ctx.getServer().getUseCases().get(ChangeGamemode.class);

    if (changeUseCase.execute(playerId, gameMode)) {
      ctx.reply("Gamemode set to: " + gameMode.name().toLowerCase());
    } else {
      ctx.reply("Failed to update gamemode. Are you online?");
    }
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

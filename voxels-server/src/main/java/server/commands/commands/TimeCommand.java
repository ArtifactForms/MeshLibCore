package server.commands.commands;

import java.util.List;
import java.util.Locale;

import common.world.WorldTime;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.gateways.WorldGateway;
import server.permissions.Permissions;

public class TimeCommand extends AbstractCommand {

  private static final long DAY_LENGTH = 24000;

  private final WorldGateway world;

  public TimeCommand(WorldGateway world) {
    this.world = world;
  }

  @Override
  public String getName() {
    return "time";
  }

  @Override
  public String getDescription() {
    return "Manage world time: /time, /time set <value>, /time add <value>, /time <keyword>";
  }

  @Override
  public void execute(CommandContext ctx) {

    var args = ctx.getArgs();

    // -------------------------------------
    // /time  → GET
    // -------------------------------------
    if (args.isEmpty()) {
      long current = normalizeTime(world.getWorldTime());
      ctx.reply(
          "Day time: "
              + current
              + " World time: "
              + world.getWorldTime()
              + " Day: "
              + world.getDay());
      return;
    }

    String sub = args.get(0).toLowerCase(Locale.ROOT);

    // -------------------------------------
    // /time set <value>
    // -------------------------------------
    if (sub.equals("set")) {
      handleSet(ctx, args);
      return;
    }

    // -------------------------------------
    // /time add <value>
    // -------------------------------------
    if (sub.equals("add")) {
      handleAdd(ctx, args);
      return;
    }

    // -------------------------------------
    // /time <keyword|value> (shortcut)
    // -------------------------------------
    handleSetShortcut(ctx, sub);
  }

  // -------------------------------------
  // SUBCOMMANDS
  // -------------------------------------

  private void handleAdd(CommandContext ctx, List<String> args) {
    if (args.size() < 2) {
      ctx.reply("Usage: /time add <value>");
      return;
    }

    try {
      long delta = Long.parseLong(args.get(1));

      long newTime = world.getWorldTime() + delta;

      world.setWorldTime(newTime);

      ctx.reply("Time advanced by " + delta + " → " + newTime);

    } catch (NumberFormatException e) {
      ctx.reply("Invalid number: '" + args.get(1) + "'");
    }
  }

  private void handleSetShortcut(CommandContext ctx, String input) {
    long timeOfDay = parseTime(input, ctx);
    if (timeOfDay == -1) return;

    world.setTimeOfDay(timeOfDay);

    ctx.reply("Time set to " + input + " (" + timeOfDay + ")");
  }

  private void handleSet(CommandContext ctx, List<String> args) {
    if (args.size() < 2) {
      ctx.reply("Usage: /time set <value|keyword>");
      return;
    }

    String input = args.get(1).toLowerCase(Locale.ROOT);
    long timeOfDay = parseTime(input, ctx);
    if (timeOfDay == -1) return;

    world.setTimeOfDay(timeOfDay);

    ctx.reply("Time set to " + input + " (" + timeOfDay + ")");
  }

  // -------------------------------------
  // HELPERS
  // -------------------------------------

  private long parseTime(String input, CommandContext ctx) {
    try {
      return WorldTime.getTicksFromKeyword(input);
    } catch (IllegalArgumentException ignored) {
      // no keyword → try number
    }

    try {
      return Long.parseLong(input);
    } catch (NumberFormatException e) {
      ctx.reply("Invalid time value: '" + input + "'");
      return -1; // TODO exception
    }
  }

  private long normalizeTime(long time) {
    time = time % DAY_LENGTH;
    if (time < 0) time += DAY_LENGTH;
    return time;
  }

  // -------------------------------------

  @Override
  public String getPermission() {
    return Permissions.COMMAND_WORLD_TIME;
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"set|add|value"};
  }

  @Override
  public String[] getAliases() {
    return new String[] {"daytime"};
  }
}

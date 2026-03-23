package server.commands.commands;

import java.util.List;
import java.util.Locale;

import common.logging.Log;
import common.world.World;
import common.world.WorldTime;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.permissions.Permissions;

public class TimeCommand extends AbstractCommand {

  private static final long DAY_LENGTH = 24000;

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
    World world = ctx.getServer().getWorld();

    // -------------------------------------
    // /time  → GET
    // -------------------------------------
    if (args.isEmpty()) {
      long current = normalizeTime(world.getWorldTime());
      ctx.reply("Current time: " + current);
      return;
    }

    String sub = args.get(0).toLowerCase(Locale.ROOT);

    // -------------------------------------
    // /time set <value>
    // -------------------------------------
    if (sub.equals("set")) {
      handleSet(ctx, world, args);
      return;
    }

    // -------------------------------------
    // /time add <value>
    // -------------------------------------
    if (sub.equals("add")) {
      handleAdd(ctx, world, args);
      return;
    }

    // -------------------------------------
    // /time <keyword|value> (shortcut)
    // -------------------------------------
    handleSetShortcut(ctx, world, sub);
  }

  // -------------------------------------
  // SUBCOMMANDS
  // -------------------------------------

  private void handleSet(CommandContext ctx, World world, List<String> args) {
    if (args.size() < 2) {
      ctx.reply("Usage: /time set <value|keyword>");
      return;
    }

    String input = args.get(1).toLowerCase(Locale.ROOT);
    long newTime = parseTime(input, ctx);
    if (newTime == -1) return;

    newTime = normalizeTime(newTime);
    world.setWorldTime(newTime);

    ctx.reply("Time set to " + input + " (" + newTime + ")");
    Log.info("World time set to " + newTime + " (Input: " + input + ")");
  }

  private void handleAdd(CommandContext ctx, World world, List<String> args) {
    if (args.size() < 2) {
      ctx.reply("Usage: /time add <value>");
      return;
    }

    try {
      long delta = Long.parseLong(args.get(1));
      long newTime = normalizeTime(world.getWorldTime() + delta);

      world.setWorldTime(newTime);

      ctx.reply("Time advanced by " + delta + " → " + newTime);
      Log.info("World time increased by " + delta + " → " + newTime);

    } catch (NumberFormatException e) {
      ctx.reply("Invalid number: '" + args.get(1) + "'");
    }
  }

  private void handleSetShortcut(CommandContext ctx, World world, String input) {
    long newTime = parseTime(input, ctx);
    if (newTime == -1) return;

    newTime = normalizeTime(newTime);
    world.setWorldTime(newTime);

    ctx.reply("Time set to " + input + " (" + newTime + ")");
    Log.info("World time set to " + newTime + " (Input: " + input + ")");
  }

  // -------------------------------------
  // HELPERS
  // -------------------------------------

  private long parseTime(String input, CommandContext ctx) {
    long time = WorldTime.getTicksFromKeyword(input);

    if (time != -1) return time;

    try {
      return Long.parseLong(input);
    } catch (NumberFormatException e) {
      ctx.reply("Invalid time value: '" + input + "'");
      return -1;
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
}

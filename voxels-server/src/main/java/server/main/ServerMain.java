package server.main;

import java.util.Collection;

import common.game.ItemRegistry;
import common.game.block.BlockLoader;
import common.game.block.Blocks;
import common.logging.ConsoleLogger;
import common.logging.Log;
import common.network.NetworkPackets;
import parse.Flag;
import server.commands.Command;
import server.config.ServerConfig;
import server.modules.moderation.ModerationModule;
import server.modules.moderation.ModerationService;
import server.network.GameServer;

public class ServerMain {

  public static void main(String[] args) throws Exception {

    Log.setImplementation(new ConsoleLogger("SERVER"));
    //    Log.setImplementation(new EmptyLogger());

    ServerConfig config = new ServerConfig();
    config.load();

    int port = config.getPort();

    NetworkPackets.register();

    GameServer server = new GameServer(port, config);

    // Important: Register blocks before item registration -> ItemRegistry.init();
    registerBlocks();
    registerItems();
    registerModules(server);
//    dumpCommandMarkdown(server.getCommands());

    server.start();
  }

  private static void dumpCommandMarkdown(Collection<Command> commands) {
    StringBuilder builder = new StringBuilder();

    builder.append("# Commands\n\n");

    for (Command command : commands) {

      // Title
      builder.append("## /").append(command.getName()).append("\n\n");

      // Description
      String description = command.getDescription();
      if (description != null && !description.isEmpty()) {
        builder.append(description).append("\n\n");
      }

      // Usage (code block)
      builder.append("**Usage:**\n\n");
      builder.append("```text\n");
      builder.append(command.getUsage()).append("\n");
      builder.append("```\n\n");

      // Permission
      String permission = command.getPermission();
      if (permission != null && !permission.isEmpty()) {
        builder.append("**Permission:** `").append(permission).append("`\n\n");
      }

      // Aliases
      if (command.hasAliases()) {
        builder.append("**Aliases:** ");
        builder.append(String.join(", ", command.getAliases()));
        builder.append("\n\n");
      }

      // Arguments
      String[] args = command.getArgumentLabels();
      if (args.length > 0) {
        builder.append("**Arguments:**\n");
        for (String arg : args) {
          builder.append("- `").append(arg).append("`\n");
        }
        builder.append("\n");
      }

      // Flags
      if (command.allowsFlags()) {
        builder.append("**Flags:**\n");
        for (Flag flag : command.getFlags()) {
          builder.append("- `").append(flag.getName()).append("`\n");
        }
        builder.append("\n");
      }

      builder.append("---\n\n");
    }

    System.out.println(builder.toString());
  }

  private static void registerBlocks() {
    Blocks.initialize();
    BlockLoader.load();
  }

  private static void registerItems() {
    ItemRegistry.init();
  }

  private static void registerModules(GameServer server) {
    ModerationService moderationService = new ModerationService();
    ModerationModule module = new ModerationModule(moderationService);
    server.registerModule(module);

    //    server.registerModule(new MyModule());
  }
}

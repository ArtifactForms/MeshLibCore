package server.commands;

import common.logging.Log;
import server.commands.commands.BroadcastCommand;
import server.commands.commands.ChangeGameModeCommand;
import server.commands.commands.DayCommand;
import server.commands.commands.EchoCommand;
import server.commands.commands.ChunkCommand;
import server.commands.commands.HelpCommand;
import server.commands.commands.InventoryCommand;
import server.commands.commands.KickCommand;
import server.commands.commands.NightCommand;
import server.commands.commands.PlayersCommand;
import server.commands.commands.PositionCommand;
import server.commands.commands.PrivateMessageCommand;
import server.commands.commands.SaveCommand;
import server.commands.commands.SeedCommand;
import server.commands.commands.StopCommand;
import server.commands.commands.TeleportCommand;
import server.commands.commands.TimeCommand;
import server.commands.commands.TopCommand;
import server.commands.commands.WhoAmICommand;
import server.gateways.GatewayContext;

public class BaseCommandProvider implements CommandProvider {

  @Override
  public void registerCommands(CommandRegistry registry, GatewayContext ctx) {
    registerCommand(new BroadcastCommand(), registry);
    registerCommand(new ChangeGameModeCommand(), registry);
    registerCommand(new ChunkCommand(), registry);
    registerCommand(new DayCommand(ctx.world()), registry);
    registerCommand(new EchoCommand(), registry);
    registerCommand(new HelpCommand(ctx.commands()), registry);
    registerCommand(new InventoryCommand(), registry);
    registerCommand(new KickCommand(ctx.players()), registry);
    registerCommand(new NightCommand(ctx.world()), registry);
    registerCommand(new PlayersCommand(ctx.config()), registry);
    registerCommand(new PositionCommand(), registry);
    registerCommand(new PrivateMessageCommand(), registry);
    registerCommand(new SaveCommand(), registry);
    registerCommand(new SeedCommand(ctx.world()), registry);
    registerCommand(new StopCommand(), registry);
    registerCommand(new TeleportCommand(), registry);
    registerCommand(new TimeCommand(ctx.world()), registry);
    registerCommand(new TopCommand(ctx.world()), registry);
    registerCommand(new WhoAmICommand(ctx.players()), registry);
  }

  private void registerCommand(Command command, CommandRegistry registry) {
    registry.register(command);
    Log.info("Registered command: " + command.getClass().getSimpleName());
  }
}

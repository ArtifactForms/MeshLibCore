package server.commands;

import server.commands.commands.BroadcastCommand;
import server.commands.commands.ChangeGameModeCommand;
import server.commands.commands.ChunkCommand;
import server.commands.commands.DayCommand;
import server.commands.commands.DeopCommand;
import server.commands.commands.EchoCommand;
import server.commands.commands.HelpCommand;
import server.commands.commands.InventoryCommand;
import server.commands.commands.KickCommand;
import server.commands.commands.NightCommand;
import server.commands.commands.OpCommand;
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
    registry.register(new TeleportCommand(ctx.players()));
    registry.register(new BroadcastCommand());
    registry.register(new ChangeGameModeCommand());
    registry.register(new ChunkCommand());
    registry.register(new EchoCommand());
    registry.register(new HelpCommand(ctx.commands()));
    registry.register(new InventoryCommand(ctx.messages()));
    registry.register(new KickCommand(ctx.players(), ctx.events()));
    registry.register(new PlayersCommand(ctx.players(), ctx.config()));
    registry.register(new PositionCommand(ctx.players()));
    registry.register(new PrivateMessageCommand(ctx.players(), ctx.messages()));
    registry.register(new StopCommand(ctx.server(), ctx.players()));
    registry.register(new WhoAmICommand(ctx.players()));
    registry.register(new DayCommand(ctx.world()));
    registry.register(new NightCommand(ctx.world()));
    registry.register(new TimeCommand(ctx.world()));
    registry.register(new TopCommand(ctx.world(), ctx.players()));
    registry.register(new SaveCommand(ctx.world()));
    registry.register(new SeedCommand(ctx.world()));
    registry.register(new OpCommand(ctx.permissions(), ctx.players(), ctx.messages()));
    registry.register(new DeopCommand(ctx.permissions(), ctx.players(), ctx.messages()));
  }
}

package server.commands;

import common.logging.Log;
import server.commands.commands.ChangeGameModeCommand;
import server.commands.commands.DayCommand;
import server.commands.commands.HelpCommand;
import server.commands.commands.InventoryCommand;
import server.commands.commands.NightCommand;
import server.commands.commands.PositionCommand;
import server.commands.commands.PrivateMessageCommand;
import server.commands.commands.SeedCommand;
import server.commands.commands.StopCommand;
import server.commands.commands.TeleportCommand;
import server.commands.commands.TimeCommand;
import server.commands.commands.TopCommand;

public class BaseCommandProvider implements CommandProvider {

  @Override
  public void registerCommands(CommandRegistry registry) {
    registerCommand(new DayCommand(), registry);
    registerCommand(new HelpCommand(), registry);
    registerCommand(new InventoryCommand(), registry);
    registerCommand(new NightCommand(), registry);
    registerCommand(new PositionCommand(), registry);
    registerCommand(new PrivateMessageCommand(), registry);
    registerCommand(new SeedCommand(), registry);
    registerCommand(new StopCommand(), registry);
    registerCommand(new TeleportCommand(), registry);
    registerCommand(new TimeCommand(), registry);
    registerCommand(new TopCommand(), registry);
    registerCommand(new ChangeGameModeCommand(), registry);
  }

  private void registerCommand(Command command, CommandRegistry registry) {
    registry.register(command);
    Log.info("Registered command: " + command.getName());
  }
}

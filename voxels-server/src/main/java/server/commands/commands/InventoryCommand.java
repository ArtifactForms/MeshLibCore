package server.commands.commands;

import java.util.Locale;

import server.commands.AbstractCommand;
import server.commands.CommandArgument;
import server.commands.CommandContext;
import server.gateways.MessageGateway;
import server.permissions.Permissions;
import server.usecases.inventoryclear.InventoryClear;
import server.usecases.inventoryclear.InventoryClear.InventoryClearRequest;
import server.usecases.inventoryclear.InventoryClear.InventoryClearResponse;
import server.usecases.inventoryclear.InventoryClearPresenter;
import server.usecases.inventoryclear.InventoryClearRequestModel;

public class InventoryCommand extends AbstractCommand {

  private final MessageGateway messages;

  public InventoryCommand(MessageGateway messages) {
    this.messages = messages;
  }

  @Override
  public void execute(CommandContext ctx) {
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    if (ctx.getArgs().isEmpty()) {
      ctx.reply("Usage: /inventory clear");
      return;
    }

    String sub = ctx.getArgs().get(0).toLowerCase(Locale.ROOT);

    if (!sub.equals("clear")) {
      ctx.reply("Unknown subcommand.");
      return;
    }

    executeUseCase(ctx);
  }

  private void executeUseCase(CommandContext ctx) {
    InventoryClear useCase = ctx.getUseCases().get(InventoryClear.class);
    InventoryClearRequest request = new InventoryClearRequestModel(ctx.getPlayer());
    InventoryClearResponse response = new InventoryClearPresenter(messages);
    useCase.execute(request, response);
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {new CommandArgument("clear", true)};
  }

  @Override
  public String getName() {
    return "inventory";
  }

  @Override
  public String getDescription() {
    return "Clears the inventory.";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_INVENTORY;
  }
}

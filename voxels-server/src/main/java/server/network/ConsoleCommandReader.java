package server.network;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import server.commands.Command;
import server.commands.CommandContext;
import server.commands.CommandRegistry;
import server.gateways.GatewayContext;

public class ConsoleCommandReader implements Runnable {

  private final CommandRegistry registry;

  private final GameServer server;

  private final GatewayContext gatewayContext;

  public ConsoleCommandReader(
      CommandRegistry registry, GameServer server, GatewayContext gatewayContext) {
    this.registry = registry;
    this.server = server;
    this.gatewayContext = gatewayContext;
  }

  @Override
  public void run() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      String line = scanner.nextLine();

      if (line == null || line.isEmpty()) continue;

      handleCommand(line);
    }
  }

  private void handleCommand(String input) {

    System.out.println("INPUT");
    // Farbcode-Konvertierung
    input = translateColors(input);

    String[] parts = input.split(" ");
    String name = parts[0];

    List<String> args = Arrays.asList(parts).subList(1, parts.length);

    Command command = registry.get(name);
    if (command == null) {
      System.out.println("Unknown command: " + name);
      return;
    }

    CommandContext ctx =
        new CommandContext(
            null, args, server, gatewayContext.permissions(), gatewayContext.messages());

    command.execute(ctx);
  }

  private String translateColors(String input) {
    System.out.println("TRANSLATE CALLED!");

    for (char c : input.toCharArray()) {
      System.out.println("CHAR: " + c + " CODE: " + (int) c);
    }

    return input.replace("&", "§");
    //    return input.replace("&", "§");
  }
}

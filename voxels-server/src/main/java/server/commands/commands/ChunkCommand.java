package server.commands.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import common.network.packets.ChunkDataPacket;
import common.world.ChunkData;
import server.commands.AbstractCommand;
import server.commands.CommandContext;
import server.network.GameServer;
import server.permissions.Permissions;
import server.player.ServerPlayer;
import server.world.ServerWorld;

public class ChunkCommand extends AbstractCommand {

  @Override
  public void execute(CommandContext ctx) {
    // -------------------------------------
    // CONSOLE CHECK
    // -------------------------------------
    if (ctx.isConsole()) {
      ctx.reply("This command can only be used by a player.");
      return;
    }

    ServerPlayer player = ctx.getServer().getPlayerManager().getPlayer(ctx.getPlayer());
    if (player == null) {
      return;
    }

    // Check if there are any arguments
    if (ctx.getArgs().isEmpty()) {
      player.sendMessage("Usage: /chunk clear");
      return;
    }

    String sub = ctx.getArgs().get(0).toLowerCase(Locale.ROOT);

    if (sub.equals("clear")) {
      handleClear(player, ctx.getServer());
    }
  }

  private void handleClear(ServerPlayer player, GameServer server) {
    int chunkX = player.getChunkX();
    int chunkZ = player.getChunkZ();

    ServerWorld world = server.getWorld();
    ChunkData data = world.getChunk(chunkX, chunkZ);

    if (data == null) {
      player.sendMessage("Error: Could not find chunk data for your position.");
      return;
    }

    // 1. Clear the block data
    Arrays.fill(data.getRawBlockData(), (short) 0);
    Arrays.fill(data.getRawHeightMap(), 0);
    data.setDirty(true);

    // 2. Update the target chunk for all nearby players
    sendUpdateToNearbyPlayers(server, data, 8);

    // 3. Trigger neighbor remesh to fix Ambient Occlusion and Culling
    triggerNeighborUpdate(world, server, chunkX, chunkZ);

    player.sendMessage("Chunk [" + chunkX + ", " + chunkZ + "] cleared and neighbors updated.");
  }

  private void sendUpdateToNearbyPlayers(GameServer server, ChunkData data, int radius) {
    ChunkDataPacket packet = new ChunkDataPacket(data);
    List<ServerPlayer> players = server.getPlayerManager().getAllPlayers();

    for (ServerPlayer p : players) {
      int dx = Math.abs(p.getChunkX() - data.getChunkX());
      int dz = Math.abs(p.getChunkZ() - data.getChunkZ());

      if (dx <= radius && dz <= radius) {
        p.getConnection().send(packet);
      }
    }
  }

  private void triggerNeighborUpdate(ServerWorld world, GameServer server, int cx, int cz) {
    int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    for (int[] off : offsets) {
      int nx = cx + off[0];
      int nz = cz + off[1];
      ChunkData neighbor = world.getChunk(nx, nz);

      if (neighbor != null) {
        // Resending the neighbor forces the client to rebuild the mesh with updated AO data
        sendUpdateToNearbyPlayers(server, neighbor, 8);
      }
    }
  }

  @Override
  public String[] getArgumentLabels() {
    return new String[] {"clear"};
  }

  @Override
  public String getName() {
    return "chunk";
  }

  @Override
  public String getDescription() {
    return "Manages chunk data (e.g., /chunk clear).";
  }

  @Override
  public String getPermission() {
    return Permissions.COMMAND_CHUNK;
  }
}

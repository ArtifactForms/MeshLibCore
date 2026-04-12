package server.modules.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.network.packets.ChunkDataPacket;
import common.world.ChunkData;
import common.world.WorldMath;
import server.network.GameServer;
import server.player.ServerPlayer;
import server.world.ServerWorld;

public class ChunkTransaction {

  private final Map<Long, List<BlockChange>> changes = new HashMap<>();

  // 🔥 Async Network Thread Pool
  private static final ExecutorService NETWORK_EXECUTOR = Executors.newFixedThreadPool(2);

  public record BlockChange(int x, int y, int z, short id) {}

  private long key(int cx, int cz) {
    return (((long) cx) << 32) | (cz & 0xffffffffL);
  }

  public void setBlock(int x, int y, int z, short id) {
    int cx = WorldMath.worldToChunk(x, ChunkData.WIDTH);
    int cz = WorldMath.worldToChunk(z, ChunkData.DEPTH);

    long key = key(cx, cz);

    changes.computeIfAbsent(key, k -> new ArrayList<>()).add(new BlockChange(x, y, z, id));
  }

  public void commit(ServerWorld world, GameServer server) {

    Set<ChunkData> touchedChunks = new HashSet<>();

    // =========================
    // 1. APPLY CHANGES (SYNC)
    // =========================
    for (Map.Entry<Long, List<BlockChange>> entry : changes.entrySet()) {

      long key = entry.getKey();

      int cx = (int) (key >> 32);
      int cz = (int) (key & 0xffffffffL);

      ChunkData chunk = world.getOrCreateChunk(cx, cz);
      if (chunk == null) continue;

      for (BlockChange change : entry.getValue()) {

        int bx = change.x() - (cx * ChunkData.WIDTH);
        int bz = change.z() - (cz * ChunkData.DEPTH);
        int by = change.y();

        if (by < 0 || by >= ChunkData.HEIGHT) continue;

        chunk.setBlockId(change.id(), bx, by, bz);
      }

      chunk.setDirty(true);
      touchedChunks.add(chunk);
    }

    // 🔥 Player Snapshot (thread-safe)
    List<ServerPlayer> players = new ArrayList<>(server.getPlayerManager().getAllPlayers());

    // =========================
    // 2. SEND CHUNKS (ASYNC)
    // =========================
    for (ChunkData chunk : touchedChunks) {

      ChunkDataPacket packet = new ChunkDataPacket(chunk);

      NETWORK_EXECUTOR.submit(
          () -> {
            for (ServerPlayer player : players) {

              int dx = Math.abs(player.getChunkX() - chunk.getChunkX());
              int dz = Math.abs(player.getChunkZ() - chunk.getChunkZ());

              if (dx <= 8 && dz <= 8) {

                // ❗ falls connection nicht thread-safe ist:
                synchronized (player.getConnection()) {
                  player.getConnection().send(packet);
                }
              }
            }
          });
    }

    // =========================
    // 3. NEIGHBOR UPDATES (ASYNC)
    // =========================
    for (ChunkData chunk : touchedChunks) {

      int cx = chunk.getChunkX();
      int cz = chunk.getChunkZ();

      int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int[] off : offsets) {

        ChunkData neighbor = world.getOrCreateChunk(cx + off[0], cz + off[1]);
        if (neighbor == null) continue;

        ChunkDataPacket packet = new ChunkDataPacket(neighbor);

        NETWORK_EXECUTOR.submit(
            () -> {
              for (ServerPlayer player : players) {

                int dx = Math.abs(player.getChunkX() - neighbor.getChunkX());
                int dz = Math.abs(player.getChunkZ() - neighbor.getChunkZ());

                if (dx <= 8 && dz <= 8) {

                  synchronized (player.getConnection()) {
                    player.getConnection().send(packet);
                  }
                }
              }
            });
      }
    }

    // optional: clear changes nach commit
    changes.clear();
  }
}

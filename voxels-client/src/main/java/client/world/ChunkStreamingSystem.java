package client.world;

import client.player.ClientPlayer;
import math.Vector3f;

public class ChunkStreamingSystem {

  private final ClientPlayer player;

  private final ChunkManager chunkManager;

  public ChunkStreamingSystem(ClientPlayer player, ChunkManager chunkManager) {
    this.player = player;
    this.chunkManager = chunkManager;
  }

  public void update() {

    Vector3f pos = player.getPosition();

    chunkManager.updatePlayerPosition(pos.x, pos.y, pos.z);
  }
}

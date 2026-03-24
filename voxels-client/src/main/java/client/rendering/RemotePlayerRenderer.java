package client.rendering;

import client.player.RemotePlayer;
import engine.rendering.Graphics;
import math.Vector3f;

public class RemotePlayerRenderer {

  private RemotePlayerRenderer() {}

  public static void render(Graphics g, RemotePlayer player, Vector3f camPos) {
    float x = player.getX();
    float y = player.getY();
    float z = player.getZ();

    g.pushMatrix();
    g.translate(x - camPos.x, -(y + camPos.y) - 0.5f, z - camPos.z);
    player.getModel().render(g);

    g.popMatrix();
  }
}

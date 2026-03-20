package client.rendering;

import client.player.RemotePlayer;
import engine.rendering.Graphics;
import engine.runtime.debug.core.DebugDraw;
import math.Color;
import math.Vector3f;

public class RemotePlayerRenderer {

  private RemotePlayerRenderer() {}

  public static void render(Graphics g, RemotePlayer player) {
    float x = player.getX();
    float y = player.getY();
    float z = player.getZ();

    player.getModel().render(g);

    DebugDraw.drawSphere(new Vector3f(x, y, z), 0.5f, Color.BLUE);
  }
}

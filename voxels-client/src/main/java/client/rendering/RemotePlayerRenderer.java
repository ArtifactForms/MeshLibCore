package client.rendering;

import client.player.RemotePlayer;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import engine.runtime.debug.core.DebugDraw;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;

public class RemotePlayerRenderer {

  private static StaticGeometry geometry;

  static {
    Mesh3D mesh = new CharacterMeshCreator().create();
    geometry = new StaticGeometry(mesh);
  }

  private RemotePlayerRenderer() {}

  public static void render(Graphics g, RemotePlayer player) {
    g.pushMatrix();

    float x = player.getX();
    float y = -player.getY();
    float z = player.getZ();

    g.translate(x, y - 0.5f, z);
    geometry.render(g);

    g.popMatrix();

    DebugDraw.drawSphere(new Vector3f(x, y, z), 0.5f, Color.BLUE);
  }
}

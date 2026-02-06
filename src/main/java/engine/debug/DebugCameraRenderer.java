package engine.debug;

import engine.scene.camera.Camera;
import engine.scene.camera.FrustumGeometry;
import math.Color;
import math.Vector3f;
import workspace.ui.Graphics;

public class DebugCameraRenderer {

  private static final float LENGTH = 500;
  private static final FrustumGeometry GEOM = new FrustumGeometry();

  private DebugCameraRenderer() {
    // Utility class – no instances
  }

  public static void render(Graphics g, Camera camera) {
    if (camera == null) return;

    GEOM.update(camera);

    float weight = g.getStrokeWeight();

    g.strokeWeight(2);

    drawForward(g, camera);
    drawRight(g, camera);
    drawFrustum(g, camera);

    g.strokeWeight(weight);
  }

  private static void drawForward(Graphics g, Camera camera) {
    Vector3f pos = camera.getTransform().getPosition();
    Vector3f forward = camera.getTransform().getForward().normalize();

    g.setColor(Color.YELLOW);
    g.drawLine(pos, pos.add(forward.mult(camera.getFarPlane())));
  }

  private static void drawRight(Graphics g, Camera camera) {
    Vector3f pos = camera.getTransform().getPosition();
    Vector3f right = camera.getTransform().getRight().normalize();

    g.setColor(Color.RED);
    g.drawLine(pos, pos.add(right.mult(LENGTH)));
  }

  private static void drawFrustum(Graphics g, Camera camera) {
    // Draw near plane
    g.setColor(Color.GREEN);
    drawQuad(g, GEOM.ntl, GEOM.ntr, GEOM.nbr, GEOM.nbl);

    // Draw far plane
    g.setColor(Color.RED);
    drawQuad(g, GEOM.ftl, GEOM.ftr, GEOM.fbr, GEOM.fbl);

    // Connect planes
    g.setColor(Color.GRAY);
    g.drawLine(GEOM.ntl, GEOM.ftl);
    g.drawLine(GEOM.ntr, GEOM.ftr);
    g.drawLine(GEOM.nbl, GEOM.fbl);
    g.drawLine(GEOM.nbr, GEOM.fbr);
  }

  private static void drawQuad(Graphics g, Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
    g.drawLine(a, b);
    g.drawLine(b, c);
    g.drawLine(c, d);
    g.drawLine(d, a);
  }
}

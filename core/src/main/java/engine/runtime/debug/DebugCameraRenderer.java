package engine.runtime.debug;

import engine.rendering.Graphics;
import engine.scene.CameraMode;
import engine.scene.camera.Camera;
import engine.scene.camera.FrustumGeometry;
import math.Color;
import math.Vector3f;

/**
 * Debug utility for visualizing a camera and its view frustum geometry.
 *
 * <p>Supports both WORLD_SPACE and CAMERA_RELATIVE rendering modes.
 */
public class DebugCameraRenderer {

  private static final float LENGTH = 500;

  private static final FrustumGeometry GEOM = new FrustumGeometry();

  private DebugCameraRenderer() {
    // Utility class – no instances
  }

  public static void render(Graphics g, Camera camera) {
    if (camera == null) return;
    render(g, camera, CameraMode.WORLD_SPACE);
  }

  public static void render(Graphics g, Camera camera, CameraMode mode) {
    if (camera == null) return;

    GEOM.update(camera);

    float weight = g.getStrokeWeight();
    g.strokeWeight(2);

    Vector3f camPos = camera.getTransform().getPosition();

    g.pushMatrix();

    if (mode == CameraMode.CAMERA_RELATIVE) {
      g.translate(-camPos.x, -camPos.y, -camPos.z);
    }

    drawForward(g, camera, mode);
    drawRight(g, camera, mode);
    drawFrustum(g);

    g.popMatrix();

    g.strokeWeight(weight);
  }

  private static void drawForward(Graphics g, Camera camera, CameraMode mode) {
    Vector3f forward = camera.getTransform().getForward().normalize();

    g.setColor(Color.YELLOW);

    Vector3f origin;
    if (mode == CameraMode.CAMERA_RELATIVE) {
      origin = new Vector3f(0, 0, 0); // 🔥 Kamera ist Ursprung
    } else {
      origin = camera.getTransform().getPosition();
    }

    Vector3f end = new Vector3f(forward).multLocal(camera.getFarPlane()).addLocal(origin);

    g.drawLine(origin, end);
  }

  private static void drawRight(Graphics g, Camera camera, CameraMode mode) {
    Vector3f right = camera.getTransform().getRight().normalize();

    g.setColor(Color.RED);

    Vector3f origin;
    if (mode == CameraMode.CAMERA_RELATIVE) {
      origin = new Vector3f(0, 0, 0); // 🔥 Kamera ist Ursprung
    } else {
      origin = camera.getTransform().getPosition();
    }

    Vector3f end = new Vector3f(right).multLocal(LENGTH).addLocal(origin);

    g.drawLine(origin, end);
  }

  private static void drawFrustum(Graphics g) {
    g.setColor(Color.GREEN);
    drawQuad(g, GEOM.ntl, GEOM.ntr, GEOM.nbr, GEOM.nbl);

    g.setColor(Color.RED);
    drawQuad(g, GEOM.ftl, GEOM.ftr, GEOM.fbr, GEOM.fbl);

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

package engine.runtime.debug;

import engine.scene.camera.Camera;
import engine.scene.camera.FrustumGeometry;
import math.Color;
import math.Vector3f;
import workspace.ui.Graphics;

/**
 * Debug utility for visualizing a camera and its view frustum geometry.
 *
 * <p>This renderer visualizes:
 *
 * <ul>
 *   <li>The camera forward direction
 *   <li>The camera right direction
 *   <li>The view frustum as near and far plane quads
 *   <li>Connecting edges between near and far planes
 * </ul>
 *
 * <p>The frustum geometry is derived from {@link FrustumGeometry} and represents the camera's
 * current projection and orientation in world space.
 *
 * <p>This class is intended strictly for debugging and validation of camera orientation, projection
 * parameters, and frustum setup. It is not intended for production rendering.
 */
public class DebugCameraRenderer {

  private static final float LENGTH = 500;
  private static final FrustumGeometry GEOM = new FrustumGeometry();

  private DebugCameraRenderer() {
    // Utility class – no instances
  }

  /**
   * Renders debug visuals for the given camera.
   *
   * <p>The following elements are rendered:
   *
   * <ul>
   *   <li>Forward direction vector (yellow)
   *   <li>Right direction vector (red)
   *   <li>View frustum geometry (near/far planes and edges)
   * </ul>
   *
   * <p>The original stroke weight of the graphics context is restored after rendering.
   *
   * @param g the graphics context used for rendering
   * @param camera the camera to visualize
   */
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

  /**
   * Draws the camera's forward direction vector.
   *
   * <p>The vector originates at the camera position and extends up to the camera's far plane
   * distance.
   *
   * @param g the graphics context
   * @param camera the camera whose forward vector is rendered
   */
  private static void drawForward(Graphics g, Camera camera) {
    Vector3f pos = camera.getTransform().getPosition();
    Vector3f forward = camera.getTransform().getForward().normalize();

    g.setColor(Color.YELLOW);
    g.drawLine(pos, pos.add(forward.mult(camera.getFarPlane())));
  }

  /**
   * Draws the camera's right direction vector.
   *
   * <p>The vector originates at the camera position and extends by a fixed debug length.
   *
   * @param g the graphics context
   * @param camera the camera whose right vector is rendered
   */
  private static void drawRight(Graphics g, Camera camera) {
    Vector3f pos = camera.getTransform().getPosition();
    Vector3f right = camera.getTransform().getRight().normalize();

    g.setColor(Color.RED);
    g.drawLine(pos, pos.add(right.mult(LENGTH)));
  }

  /**
   * Draws the camera's view frustum geometry.
   *
   * <p>This includes:
   *
   * <ul>
   *   <li>The near plane (green quad)
   *   <li>The far plane (red quad)
   *   <li>Edges connecting near and far planes (gray)
   * </ul>
   *
   * @param g the graphics context
   * @param camera the camera whose frustum is rendered
   */
  private static void drawFrustum(Graphics g, Camera camera) {
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

  /**
   * Draws a wireframe quad defined by four corner points.
   *
   * <p>The points are expected to be provided in consecutive order.
   *
   * @param g the graphics context
   * @param a first corner
   * @param b second corner
   * @param c third corner
   * @param d fourth corner
   */
  private static void drawQuad(Graphics g, Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
    g.drawLine(a, b);
    g.drawLine(b, c);
    g.drawLine(c, d);
    g.drawLine(d, a);
  }
}

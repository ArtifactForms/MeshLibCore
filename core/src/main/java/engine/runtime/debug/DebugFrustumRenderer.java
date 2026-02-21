package engine.runtime.debug;

import math.Color;
import math.Plane;
import math.Vector3f;
import workspace.ui.Graphics;

/**
 * Utility class for debug rendering of a camera frustum represented by six planes.
 *
 * <p>This renderer visualizes:
 *
 * <ul>
 *   <li>Each frustum plane as a wireframe quad
 *   <li>The plane normals
 *   <li>The eight frustum corner points (plane intersections)
 * </ul>
 *
 * <p>This class is intended strictly for debugging and validation of frustum extraction logic.
 */
public final class DebugFrustumRenderer {

  private DebugFrustumRenderer() {
    // Utility class – no instances
  }

  /**
   * Draws all frustum planes, their normals, and the frustum corner points.
   *
   * @param g the graphics context used for rendering
   * @param planes the six frustum planes
   * @param planeSize the size of the rendered plane quads
   * @param normalLength the length of the rendered plane normals
   */
  public static void drawFrustum(Graphics g, Plane[] planes, float planeSize, float normalLength) {
    // Draw infinite plane quads and normals
    for (Plane plane : planes) {
      drawPlaneQuad(g, plane, planeSize, new Color(1f, 1f, 1f, 0.5f));
      drawPlaneNormal(g, plane, normalLength, new Color(1f, 1f, 0f, 1f));
    }

    // Draw the 8 frustum corner points (plane intersections)
    drawFrustumCorners(g, planes);
  }

  /**
   * Computes and renders the eight frustum corner points as small line crosses.
   *
   * <p>The expected plane order is:
   *
   * <pre>
   * 0 = left
   * 1 = right
   * 2 = bottom
   * 3 = top
   * 4 = near
   * 5 = far
   * </pre>
   *
   * @param g the graphics context
   * @param p the array of frustum planes
   */
  public static void drawFrustumCorners(Graphics g, Plane[] p) {
    if (p.length < 6) return;

    Color cornerColor = Color.CYAN;
    float crossSize = 1f; // Size of the corner cross

    // The 8 corner combinations
    Vector3f[] corners = new Vector3f[8];
    corners[0] = intersectThreePlanes(p[4], p[0], p[3]); // Near-Left-Top
    corners[1] = intersectThreePlanes(p[4], p[1], p[3]); // Near-Right-Top
    corners[2] = intersectThreePlanes(p[4], p[0], p[2]); // Near-Left-Bottom
    corners[3] = intersectThreePlanes(p[4], p[1], p[2]); // Near-Right-Bottom

    corners[4] = intersectThreePlanes(p[5], p[0], p[3]); // Far-Left-Top
    corners[5] = intersectThreePlanes(p[5], p[1], p[3]); // Far-Right-Top
    corners[6] = intersectThreePlanes(p[5], p[0], p[2]); // Far-Left-Bottom
    corners[7] = intersectThreePlanes(p[5], p[1], p[2]); // Far-Right-Bottom

    for (Vector3f corner : corners) {
      if (corner != null) {
        drawPointCross(g, corner, crossSize, cornerColor);
      }
    }
  }

  /**
   * Computes the intersection point of three planes.
   *
   * <p>Formula:
   *
   * <pre>
   * P = (-d1(n2 × n3) - d2(n3 × n1) - d3(n1 × n2)) / (n1 · (n2 × n3))
   * </pre>
   *
   * @param p1 first plane
   * @param p2 second plane
   * @param p3 third plane
   * @return the intersection point, or {@code null} if the planes do not intersect at a single
   *     point
   */
  private static Vector3f intersectThreePlanes(Plane p1, Plane p2, Plane p3) {
    Vector3f n1 = p1.getNormal();
    Vector3f n2 = p2.getNormal();
    Vector3f n3 = p3.getNormal();

    Vector3f c23 = n2.cross(n3);
    float dot = n1.dot(c23);

    // If dot is near zero, the planes are parallel or nearly parallel
    if (Math.abs(dot) < 0.0001f) return null;

    Vector3f c31 = n3.cross(n1);
    Vector3f c12 = n1.cross(n2);

    Vector3f result = new Vector3f();
    result.addLocal(c23.multLocal(-p1.getDistance()));
    result.addLocal(c31.multLocal(-p2.getDistance()));
    result.addLocal(c12.multLocal(-p3.getDistance()));

    return result.multLocal(1.0f / dot);
  }

  /**
   * Draws a small 3D cross at the given position.
   *
   * @param g the graphics context
   * @param p the center position
   * @param s half-size of the cross arms
   * @param c the color of the cross
   */
  private static void drawPointCross(Graphics g, Vector3f p, float s, Color c) {
    g.setColor(c);
    g.drawLine(new Vector3f(p.x - s, p.y, p.z), new Vector3f(p.x + s, p.y, p.z));
    g.drawLine(new Vector3f(p.x, p.y - s, p.z), new Vector3f(p.x, p.y + s, p.z));
    g.drawLine(new Vector3f(p.x, p.y, p.z - s), new Vector3f(p.x, p.y, p.z + s));
  }

  /**
   * Draws a wireframe quad representing a plane.
   *
   * @param g the graphics context
   * @param plane the plane to render
   * @param size the quad size
   * @param color the quad color
   */
  public static void drawPlaneQuad(Graphics g, Plane plane, float size, Color color) {
    Vector3f normal = plane.getNormal();
    Vector3f center = new Vector3f(normal).multLocal(-plane.getDistance());

    Vector3f u = new Vector3f();
    Vector3f v = new Vector3f();
    buildPlaneBasis(normal, u, v);

    float h = size * 0.5f;

    Vector3f p0 =
        new Vector3f(center)
            .addLocal(new Vector3f(u).multLocal(-h))
            .addLocal(new Vector3f(v).multLocal(-h));
    Vector3f p1 =
        new Vector3f(center)
            .addLocal(new Vector3f(u).multLocal(h))
            .addLocal(new Vector3f(v).multLocal(-h));
    Vector3f p2 =
        new Vector3f(center)
            .addLocal(new Vector3f(u).multLocal(h))
            .addLocal(new Vector3f(v).multLocal(h));
    Vector3f p3 =
        new Vector3f(center)
            .addLocal(new Vector3f(u).multLocal(-h))
            .addLocal(new Vector3f(v).multLocal(h));

    g.setColor(color);
    g.drawLine(p0, p1);
    g.drawLine(p1, p2);
    g.drawLine(p2, p3);
    g.drawLine(p3, p0);
  }

  /**
   * Draws the normal vector of a plane.
   *
   * @param g the graphics context
   * @param plane the plane whose normal should be drawn
   * @param length the length of the normal vector
   * @param color the color of the normal
   */
  public static void drawPlaneNormal(Graphics g, Plane plane, float length, Color color) {
    Vector3f normal = plane.getNormal();
    Vector3f origin = new Vector3f(normal).multLocal(-plane.getDistance());
    Vector3f end = new Vector3f(origin).addLocal(new Vector3f(normal).multLocal(length));

    g.setColor(color);
    g.drawLine(origin, end);
  }

  /**
   * Builds an orthonormal basis (u, v) for a plane given its normal.
   *
   * @param normal the plane normal
   * @param u output vector for the first tangent axis
   * @param v output vector for the second tangent axis
   */
  private static void buildPlaneBasis(Vector3f normal, Vector3f u, Vector3f v) {
    Vector3f helper = Math.abs(normal.y) < 0.999f ? new Vector3f(0, 1, 0) : new Vector3f(1, 0, 0);

    u.set(normal.cross(helper)).normalizeLocal();
    v.set(u.cross(normal)).normalizeLocal();
  }
}

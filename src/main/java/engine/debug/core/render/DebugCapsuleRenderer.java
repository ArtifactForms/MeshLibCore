package engine.debug.core.render;

import engine.debug.core.command.DebugCapsuleCommand;
import math.Mathf;
import workspace.ui.Graphics;

/**
 * Renders a debug capsule using simple wireframe primitives.
 *
 * <p>The capsule is assumed to be aligned along the Y-axis.
 *
 * <pre>
 * Geometry layout:
 *
 *        O   ← top hemisphere
 *        |
 *        |   ← cylinder section
 *        |
 *        O   ← bottom hemisphere
 *
 * </pre>
 *
 * The total height is defined by: (halfHeight * 2) + (radius * 2)
 *
 * <p>The cylindrical section height is: halfHeight * 2
 */
class DebugCapsuleRenderer implements DebugCommandRenderer<DebugCapsuleCommand> {

  @Override
  public Class<DebugCapsuleCommand> getCommandType() {
    return DebugCapsuleCommand.class;
  }

  /** Renders the capsule in world space. */
  @Override
  public void render(Graphics g, DebugCapsuleCommand command) {

    float radius = command.radius;
    float halfHeight = command.halfHeight;

    // Half-height of the cylindrical part (excluding hemispheres)
    float cylinderHalf = Math.max(0f, halfHeight);

    g.setColor(command.color);

    // Move to capsule center in world space
    g.pushMatrix();
    g.translate(command.center.x, command.center.y, command.center.z);

    drawCylinderLines(g, radius, cylinderHalf);
    drawCylinderRingsXZ(g, radius, cylinderHalf);
    drawBottomHemisphere(g, radius, cylinderHalf);
    drawTopHemisphere(g, radius, cylinderHalf);

    g.popMatrix();
  }

  /* =========================================================
  Cylinder Body
  ========================================================= */

  /** Draws the four vertical lines forming the cylindrical body. */
  private void drawCylinderLines(Graphics g, float radius, float half) {

    // Lines parallel to Y-axis on X extremes
    g.drawLine(-radius, -half, 0, -radius, half, 0);
    g.drawLine(radius, -half, 0, radius, half, 0);

    // Lines parallel to Y-axis on Z extremes
    g.drawLine(0, -half, -radius, 0, half, -radius);
    g.drawLine(0, -half, radius, 0, half, radius);
  }

  /** Draws circular rings in the XZ plane at the top and bottom of the cylindrical section. */
  private void drawCylinderRingsXZ(Graphics g, float radius, float half) {

    // Top ring
    g.pushMatrix();
    g.translate(0, -half, 0);
    drawXZCircle(g, radius);
    g.popMatrix();

    // Bottom ring
    g.pushMatrix();
    g.translate(0, half, 0);
    drawXZCircle(g, radius);
    g.popMatrix();
  }

  /**
   * Draws a circle lying in the XZ plane.
   *
   * <p>Note: drawOval() creates a circle in the XY plane by default. We rotate it 90° around the
   * X-axis to align it with XZ.
   */
  private void drawXZCircle(Graphics g, float radius) {

    g.pushMatrix();
    g.rotateX(Mathf.HALF_PI);
    g.drawOval(-radius, -radius, radius * 2, radius * 2);
    g.popMatrix();
  }

  /* =========================================================
  Hemispheres
  ========================================================= */

  /** Draws the bottom hemisphere at +cylinderHalf. */
  private void drawBottomHemisphere(Graphics g, float radius, float half) {

    g.pushMatrix();
    g.translate(0, half, 0);
    drawSphereRings(g, radius);
    g.popMatrix();
  }

  /** Draws the top hemisphere at -cylinderHalf. */
  private void drawTopHemisphere(Graphics g, float radius, float half) {

    g.pushMatrix();
    g.translate(0, -half, 0);
    drawSphereRings(g, radius);
    g.popMatrix();
  }

  /**
   * Draws a minimal wireframe sphere representation using two perpendicular rings:
   *
   * <p>- One ring in the XY plane - One ring in the ZY plane
   *
   * <p>This keeps the debug rendering lightweight while still readable.
   */
  private void drawSphereRings(Graphics g, float radius) {

    // Ring in XY plane
    g.drawOval(-radius, -radius, radius * 2, radius * 2);

    // Ring in ZY plane
    g.pushMatrix();
    g.rotateY(Mathf.HALF_PI);
    g.drawOval(-radius, -radius, radius * 2, radius * 2);
    g.popMatrix();
  }
}

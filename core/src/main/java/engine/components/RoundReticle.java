package engine.components;

import engine.rendering.Graphics;
import math.Color;

/**
 * A component that renders a round reticle in the center of the screen. The reticle consists of an
 * outer circle and a smaller, filled inner circle, both customizable in terms of radius and color.
 */
public class RoundReticle extends AbstractComponent implements RenderableComponent {

  private float outerRadius;

  private float innerRadius;

  private Color color;

  /**
   * Creates a default RoundReticle with preset values. Default configuration: - Outer radius: 30 -
   * Inner radius: 6 - Color: White
   */
  public RoundReticle() {
    this(30, 6, Color.WHITE);
  }

  /**
   * Creates a RoundReticle with specified outer radius, inner radius, and color.
   *
   * @param outerRadius the radius of the outer circle.
   * @param innerRadius the radius of the inner filled circle.
   * @param color the color of the reticle.
   * @throws IllegalArgumentException if any radius is non-positive or color is null.
   */
  public RoundReticle(float outerRadius, float innerRadius, Color color) {
    setOuterRadius(outerRadius);
    setInnerRadius(innerRadius);
    setColor(color);
  }

  /**
   * Renders the reticle on the screen, centered in the viewport. The reticle includes an outer
   * circle and a filled inner circle.
   *
   * @param g the {@link Graphics} context used for rendering.
   */
  @Override
  public void render(Graphics g) {
    int centerX = g.getWidth() / 2;
    int centerY = g.getHeight() / 2;

    // Render reticle circles
    g.setColor(color);
    renderCenteredOval(g, centerX, centerY, outerRadius, false);
    renderCenteredOval(g, centerX, centerY, innerRadius, true);
  }

  /**
   * Draws a centered oval at the specified position.
   *
   * @param g the {@link Graphics} context used for rendering.
   * @param centerX the x-coordinate of the oval's center.
   * @param centerY the y-coordinate of the oval's center.
   * @param radius the radius of the oval.
   * @param filled whether the oval should be filled.
   */
  private void renderCenteredOval(
      Graphics g, int centerX, int centerY, float radius, boolean filled) {
    int diameter = (int) radius;
    int topLeftX = centerX - diameter / 2;
    int topLeftY = centerY - diameter / 2;

    if (filled) {
      g.fillOval(topLeftX, topLeftY, diameter, diameter);
    } else {
      g.drawOval(topLeftX, topLeftY, diameter, diameter);
    }
  }

  /**
   * Updates the component. Currently, this method does nothing.
   *
   * @param tpf time per frame, used for animations or updates.
   */
  @Override
  public void onUpdate(float tpf) {}

  /** Called when the component is attached to a {@link engine.SceneNode}. */
  @Override
  public void onAttach() {}

  /** Called when the component is detached from a {@link engine.SceneNode}. */
  @Override
  public void onDetach() {}

  /**
   * Gets the outer radius of the reticle.
   *
   * @return the outer radius.
   */
  public float getOuterRadius() {
    return outerRadius;
  }

  /**
   * Sets the outer radius of the reticle.
   *
   * @param outerRadius the new outer radius.
   * @throws IllegalArgumentException if the radius is non-positive.
   */
  public void setOuterRadius(float outerRadius) {
    if (outerRadius <= 0) {
      throw new IllegalArgumentException("Outer radius must be greater than 0.");
    }
    this.outerRadius = outerRadius;
  }

  /**
   * Gets the inner radius of the reticle.
   *
   * @return the inner radius.
   */
  public float getInnerRadius() {
    return innerRadius;
  }

  /**
   * Sets the inner radius of the reticle.
   *
   * @param innerRadius the new inner radius.
   * @throws IllegalArgumentException if the radius is non-positive.
   */
  public void setInnerRadius(float innerRadius) {
    if (innerRadius <= 0) {
      throw new IllegalArgumentException("Inner radius must be greater than 0.");
    }
    this.innerRadius = innerRadius;
  }

  /**
   * Gets the color of the reticle.
   *
   * @return the color.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the reticle.
   *
   * @param color the new color.
   * @throws IllegalArgumentException if the color is null.
   */
  public void setColor(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null.");
    }
    this.color = color;
  }
}

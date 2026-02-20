package mesh.creator.assets;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.SolidArcCreator;
import mesh.creator.primitives.TubeCreator;
import mesh.modifier.transform.RotateXModifier;

/**
 * Creates a 3D arch shape, inspired by Unity's ProBuilder.
 *
 * <p>This class provides methods to configure and generate a 3D arch with customizable parameters
 * such as radius, thickness, arch angle, depth, and capping options.
 */
public class ProArchCreator implements IMeshCreator {

  /** The number of sides for the arch's cross-section. Must be at least 3. */
  private int sidesCount = 16;

  /** The outer radius of the arch. */
  private float radius = 1;

  /** The thickness of the arch's wall. Must be greater than 0. */
  private float thickness = 0.1f;

  /** The angle of the arch in radians. Must be between 0 and 2*PI. */
  private float archAngle = Mathf.PI;

  /** The depth of the arch. */
  private float depth = 0.5f;

  /** Whether to cap the start of the arch. */
  private boolean capStart = true;

  /** Whether to cap the end of the arch. */
  private boolean capEnd = true;

  /**
   * Creates a new 3D mesh representing the configured arch.
   *
   * @return The generated 3D mesh.
   * @throws IllegalArgumentException if any of the parameters are invalid.
   */
  @Override
  public Mesh3D create() {
    validateParameters();
    return (archAngle == Mathf.TWO_PI) ? createTube() : createArch();
  }

  /**
   * Validates the current parameter values to ensure they are within acceptable ranges.
   *
   * @throws IllegalArgumentException if any of the parameters are invalid.
   */
  private void validateParameters() {
    if (sidesCount < 3) {
      throw new IllegalArgumentException("sidesCount must be at least 3");
    }
    if (thickness <= 0) {
      throw new IllegalArgumentException("thickness must be greater than 0");
    }
    if (archAngle <= 0 || archAngle > Mathf.TWO_PI) {
      throw new IllegalArgumentException("archAngle must be between 0 and 2*PI");
    }
  }

  /**
   * Creates a 3D arch shape with a specific angle.
   *
   * @return The generated 3D arch mesh.
   */
  private Mesh3D createArch() {
    float innerRadius = calculateInnerRadius();
    float outerRadius = calculateOuterRadius();

    SolidArcCreator creator = new SolidArcCreator();

    creator.setRotationSegments(sidesCount);
    creator.setInnerRadius(innerRadius);
    creator.setOuterRadius(outerRadius);
    creator.setAngle(archAngle);
    creator.setHeight(depth);
    creator.setCapStart(capStart);
    creator.setCapEnd(capEnd);

    Mesh3D mesh = creator.create();
    new RotateXModifier(Mathf.HALF_PI).modify(mesh);

    return mesh;
  }

  /**
   * Creates a 3D tube shape (full circle).
   *
   * @return The generated 3D tube mesh.
   */
  private Mesh3D createTube() {
    float innerRadius = calculateInnerRadius();
    float outerRadius = calculateOuterRadius();

    TubeCreator creator = new TubeCreator();
    creator.setVertices(sidesCount);
    creator.setHeight(depth);
    creator.setBottomOuterRadius(outerRadius);
    creator.setTopOuterRadius(outerRadius);
    creator.setBottomInnerRadius(innerRadius);
    creator.setTopInnerRadius(innerRadius);

    Mesh3D mesh = creator.create();
    new RotateXModifier(Mathf.HALF_PI).modify(mesh);

    return mesh;
  }

  /**
   * Calculates the inner radius of the arch.
   *
   * @return The inner radius.
   */
  private float calculateInnerRadius() {
    return radius - thickness;
  }

  /**
   * Calculates the outer radius of the arch.
   *
   * @return The outer radius.
   */
  private float calculateOuterRadius() {
    return radius;
  }

  /**
   * Gets the number of sides for the arch's cross-section.
   *
   * @return The number of sides.
   */
  public int getSidesCount() {
    return sidesCount;
  }

  /**
   * Sets the number of sides for the arch's cross-section.
   *
   * @param sidesCount The new number of sides. Must be at least 3.
   */
  public void setSidesCount(int sidesCount) {
    this.sidesCount = sidesCount;
  }

  /**
   * Gets the outer radius of the arch.
   *
   * @return The outer radius.
   */
  public float getRadius() {
    return radius;
  }

  /**
   * Sets the outer radius of the arch.
   *
   * @param radius The new outer radius.
   */
  public void setRadius(float radius) {
    this.radius = radius;
  }

  /**
   * Gets the thickness of the arch's wall.
   *
   * @return The thickness.
   */
  public float getThickness() {
    return thickness;
  }

  /**
   * Sets the thickness of the arch's wall.
   *
   * @param thickness The new thickness. Must be greater than 0.
   */
  public void setThickness(float thickness) {
    this.thickness = thickness;
  }

  /**
   * Gets the angle of the arch in radians.
   *
   * @return The arch angle.
   */
  public float getArchAngle() {
    return archAngle;
  }

  /**
   * Sets the angle of the arch in radians.
   *
   * @param archAngle The new arch angle. Must be between 0 and 2*PI.
   */
  public void setArchAngle(float archAngle) {
    this.archAngle = archAngle;
  }

  /**
   * Gets the depth of the arch.
   *
   * @return The depth.
   */
  public float getDepth() {
    return depth;
  }

  /**
   * Sets the depth of the arch.
   *
   * @param depth The new depth.
   */
  public void setDepth(float depth) {
    this.depth = depth;
  }

  /**
   * Checks if the start of the arch is capped.
   *
   * @return True if the start is capped, false otherwise.
   */
  public boolean isCapStart() {
    return capStart;
  }

  /**
   * Sets whether to cap the start of the arch.
   *
   * @param capStart True to cap the start, false otherwise.
   */
  public void setCapStart(boolean capStart) {
    this.capStart = capStart;
  }

  /**
   * Checks if the end of the arch is capped.
   *
   * @return True if the end is capped, false otherwise.
   */
  public boolean isCapEnd() {
    return capEnd;
  }

  /**
   * Sets whether to cap the end of the arch.
   *
   * @param capEnd True to cap the end, false otherwise.
   */
  public void setCapEnd(boolean capEnd) {
    this.capEnd = capEnd;
  }
}

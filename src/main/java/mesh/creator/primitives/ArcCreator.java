package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

/**
 * Creates a 3D arc mesh composed of a series of vertices arranged in a circular segment. The arc is
 * defined by a start angle, end angle, radius, center, and the number of vertices.
 *
 * <p>This class implements the {@link IMeshCreator} interface, providing a modular way to create
 * arcs for use in 3D mesh generation. It can be customized to create arcs of different sizes,
 * orientations, and resolutions.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ArcCreator arcCreator = new ArcCreator();
 * arcCreator.setStartAngle(0);
 * arcCreator.setEndAngle(Mathf.HALF_PI); // 90 degrees
 * arcCreator.setRadius(2.0f);
 * arcCreator.setVertices(16);
 * arcCreator.setCenter(new Vector3f(1, 0, 1));
 * Mesh3D arcMesh = arcCreator.create();
 * }</pre>
 */
public class ArcCreator implements IMeshCreator {

  /** The starting angle of the arc in radians. Defaults to 0. */
  private float startAngle;

  /** The ending angle of the arc in radians. Defaults to {@link Mathf#TWO_PI}. */
  private float endAngle;

  /** The radius of the arc. Defaults to 1. */
  private float radius;

  /** The number of vertices that make up the arc. Defaults to 32. */
  private int vertices;

  /** The center position of the arc. Defaults to the origin (0, 0, 0). */
  private Vector3f center;

  /** The generated 3D mesh representing the arc. */
  private Mesh3D mesh;

  /** Constructs a new {@code ArcCreator} with default parameters. */
  public ArcCreator() {
    startAngle = 0;
    endAngle = Mathf.TWO_PI;
    radius = 1;
    vertices = 32;
    center = new Vector3f();
  }

  /**
   * Creates the arc mesh based on the current configuration.
   *
   * @return A {@link Mesh3D} object representing the generated arc.
   */
  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    return mesh;
  }

  /** Initializes the mesh object to store the arc's vertices. */
  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  /** Generates the vertices of the arc based on the radius, angles, and center. */
  private void createVertices() {
    float angleBetweenPoints = calculateAngleBetweenPoints();
    for (int i = 0; i < vertices; i++) {
      float currentAngle = startAngle + angleBetweenPoints * i;
      float x = center.x + radius * Mathf.cos(currentAngle);
      float z = center.z + radius * Mathf.sin(currentAngle);
      addVertex(x, center.y, z);
    }
  }

  /**
   * Adds a vertex to the mesh at the specified position.
   *
   * @param x The x-coordinate of the vertex.
   * @param y The y-coordinate of the vertex.
   * @param z The z-coordinate of the vertex.
   */
  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  /**
   * Calculates the angle between each pair of adjacent vertices.
   *
   * @return The angle between adjacent vertices in radians.
   */
  private float calculateAngleBetweenPoints() {
    return (endAngle - startAngle) / ((float) vertices - 1);
  }

  /**
   * Gets the starting angle of the arc in radians.
   *
   * @return The starting angle.
   */
  public float getStartAngle() {
    return startAngle;
  }

  /**
   * Sets the starting angle of the arc in radians.
   *
   * @param startAngle The starting angle.
   */
  public void setStartAngle(float startAngle) {
    this.startAngle = startAngle;
  }

  /**
   * Gets the ending angle of the arc in radians.
   *
   * @return The ending angle.
   */
  public float getEndAngle() {
    return endAngle;
  }

  /**
   * Sets the ending angle of the arc in radians.
   *
   * @param endAngle The ending angle.
   */
  public void setEndAngle(float endAngle) {
    this.endAngle = endAngle;
  }

  /**
   * Gets the radius of the arc.
   *
   * @return The radius.
   */
  public float getRadius() {
    return radius;
  }

  /**
   * Sets the radius of the arc.
   *
   * @param radius The radius.
   */
  public void setRadius(float radius) {
    this.radius = radius;
  }

  /**
   * Gets the number of vertices that make up the arc.
   *
   * @return The number of vertices.
   */
  public int getVertices() {
    return vertices;
  }

  /**
   * Sets the number of vertices that make up the arc. Must be at least 2.
   *
   * @param vertices The number of vertices.
   * @throws IllegalArgumentException if {@code vertices} is less than 2.
   */
  public void setVertices(int vertices) {
    if (vertices < 2) {
      throw new IllegalArgumentException("Vertex count must be at least 2.");
    }
    this.vertices = vertices;
  }

  /**
   * Gets the center position of the arc.
   *
   * @return A {@link Vector3f} representing the center position.
   */
  public Vector3f getCenter() {
    return new Vector3f(center);
  }

  /**
   * Sets the center position of the arc.
   *
   * @param center A {@link Vector3f} representing the new center position.
   * @throws IllegalArgumentException if {@code center} is {@code null}.
   */
  public void setCenter(Vector3f center) {
    if (center == null) {
      throw new IllegalArgumentException("Center cannot be null.");
    }
    this.center.set(center);
  }
}

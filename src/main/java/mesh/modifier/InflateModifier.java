package mesh.modifier;

import java.util.List;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

/**
 * A modifier that inflates or deflates a 3D mesh by displacing its vertices along their normals.
 * This creates an effect of the mesh expanding outward or inward, similar to inflation or
 * deflation.
 *
 * <p>The inflation factor controls the degree of displacement, and the direction defines whether
 * the mesh is inflated (expanded outward) or deflated (compressed inward).
 */
public class InflateModifier implements IMeshModifier {

  /** The inflation factor determines how much the mesh will be inflated or deflated. */
  private float inflationFactor;

  /**
   * The inflation amount is calculated based on the inflation factor and direction. This value is
   * used to displace each vertex along its normal.
   */
  private float inflationAmount;

  /**
   * The direction of inflation for the mesh. It can either be {@link Direction#OUTWARD} for outward
   * inflation or {@link Direction#INWARD} for inward deflation.
   */
  private Direction direction;

  /** The mesh that will be modified by this modifier. */
  private Mesh3D mesh;

  /**
   * A list of vertex normals for the mesh, used to determine the direction of inflation for each
   * vertex. These normals are used to calculate the displacement of vertices.
   */
  private List<Vector3f> vertexNormals;

  /** The direction in which the mesh will inflate. */
  public enum Direction {
    OUTWARD,
    INWARD
  }

  /**
   * Creates a new InflateModifier with the specified inflation factor and direction.
   *
   * @param inflationFactor the factor by which the mesh will be inflated or deflated, must be
   *     positive
   * @param direction the direction in which the inflation will occur
   * @throws IllegalArgumentException if the inflationFactor is negative
   * @throws IllegalArgumentException if the direction is null
   */
  public InflateModifier(float inflationFactor, Direction direction) {
    setInflationFactor(inflationFactor);
    setDirection(direction);
  }

  /**
   * Modifies the given mesh by inflating or deflating its vertices. If the inflation factor is
   * zero, no modification is performed.
   *
   * @param mesh the mesh to modify
   * @return the modified mesh
   * @throws IllegalArgumentException if the mesh is null
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null");
    }
    if (inflationFactor == 0) {
      return mesh;
    }
    setMesh(mesh);
    calculateInflationAmount();
    calculateVertexNormals();
    inflate();
    return mesh;
  }

  /** Inflates or deflates the vertices of the mesh based on the inflation factor and direction. */
  private void inflate() {
    for (int index = 0; index < mesh.getVertexCount(); index++) {
      inflateVertexAt(index);
    }
  }

  /**
   * Inflates or deflates a specific vertex by applying the calculated inflation amount.
   *
   * @param index the index of the vertex to inflate
   */
  private void inflateVertexAt(int index) {
    Vector3f vertex = mesh.getVertexAt(index);
    Vector3f normal = vertexNormals.get(index);
    vertex.addLocal(normal.mult(inflationAmount));
  }

  /**
   * Calculates the vertex normals for the mesh to determine the direction of inflation for each
   * vertex.
   */
  private void calculateVertexNormals() {
    vertexNormals = new VertexNormals(mesh).getVertexNormals();
  }

  /** Calculates the inflation amount based on the inflation factor and direction. */
  private void calculateInflationAmount() {
    inflationAmount = inflationFactor * (direction == Direction.OUTWARD ? 1 : -1);
  }

  /**
   * Sets the mesh that will be modified.
   *
   * @param mesh the mesh to set
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  /**
   * Gets the inflation factor used to modify the mesh.
   *
   * @return the inflation factor
   */
  public float getInflationFactor() {
    return inflationFactor;
  }

  /**
   * Sets the inflation factor. A value of 0 means no inflation, while a positive value inflates the
   * mesh. Negative values are not allowed.
   *
   * @param inflationFactor the inflation factor to set
   * @throws IllegalArgumentException if the inflation factor is negative
   */
  public void setInflationFactor(float inflationFactor) {
    if (inflationFactor < 0) {
      throw new IllegalArgumentException("Inflation factor cannot be negative.");
    }
    this.inflationFactor = inflationFactor;
    calculateInflationAmount();
  }

  /**
   * Gets the direction of the inflation (outward or inward).
   *
   * @return the direction of inflation
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * Sets the direction of the inflation.
   *
   * @param direction the direction of inflation (OUTWARD or INWARD)
   * @throws IllegalArgumentException if the direction is null
   */
  public void setDirection(Direction direction) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }
    this.direction = direction;
    calculateInflationAmount();
  }
}

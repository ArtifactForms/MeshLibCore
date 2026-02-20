package mesh.modifier.transform;

import math.Vector3f;
import mesh.Mesh;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * Scales a 3D mesh uniformly or non-uniformly along X, Y, and Z axes. Implements the {@link
 * IMeshModifier} interface for modularity.
 *
 * <p>This modifier scales all vertices of the provided 3D mesh based on the specified scaling
 * factors (scaleX, scaleY, scaleZ).
 */
public class ScaleModifier implements IMeshModifier {

  /** The scaling factor along the X-axis. */
  private float scaleX;

  /** The scaling factor along the Z-axis. */
  private float scaleY;

  /** The scaling factor along the Z-axis. */
  private float scaleZ;

  /** The 3D mesh currently being operated on by the modifier. */
  private Mesh mesh;

  /** Default constructor that initializes uniform scaling with factors (1, 1, 1). */
  public ScaleModifier() {
    this(1, 1, 1);
  }

  /**
   * Initializes the scaling factors uniformly along all axes.
   *
   * @param scale the uniform scale factor to apply across X, Y, and Z.
   */
  public ScaleModifier(float scale) {
    this(scale, scale, scale);
  }

  /**
   * Custom scaling constructor allowing different scaling factors for X, Y, and Z axes.
   *
   * @param scaleX the scaling factor along the X-axis.
   * @param scaleY the scaling factor along the Y-axis.
   * @param scaleZ the scaling factor along the Z-axis.
   */
  public ScaleModifier(float scaleX, float scaleY, float scaleZ) {
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.scaleZ = scaleZ;
  }

  /**
   * Modifies the provided mesh by scaling all its vertices based on the scaling factors provided
   * during construction or updates. If the provided mesh contains no vertices, the method safely
   * returns the mesh without changes.
   *
   * @param mesh the 3D mesh to scale (must not be null).
   * @return the scaled mesh.
   * @throws IllegalArgumentException if the provided mesh is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    if (mesh.getVertexCount() == 0) {
      return mesh;
    }
    setMesh(mesh);
    scaleMesh();
    return mesh;
  }

  /** Scales all vertices of the associated mesh. */
  private void scaleMesh() {
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      applyScaleToVertex(v);
    }
  }

  /**
   * Scales a single vertex by the given scaling factors.
   *
   * @param vertex the vertex to scale.
   */
  private void applyScaleToVertex(Vector3f vertex) {
    vertex.multLocal(scaleX, scaleY, scaleZ);
  }

  /**
   * Sets the mesh for this modifier to operate on.
   *
   * @param mesh the mesh to scale.
   */
  private void setMesh(Mesh mesh) {
    this.mesh = mesh;
  }

  /**
   * Retrieves the scaling factor along the X-axis.
   *
   * @return the current scaling factor along the X-axis.
   */
  public float getScaleX() {
    return scaleX;
  }

  /**
   * Updates the scaling factor along the X-axis.
   *
   * @param scaleX the new scaling factor for the X-axis.
   */
  public void setScaleX(float scaleX) {
    this.scaleX = scaleX;
  }

  /**
   * Retrieves the scaling factor along the Y-axis.
   *
   * @return the current scaling factor along the Y-axis.
   */
  public float getScaleY() {
    return scaleY;
  }

  /**
   * Updates the scaling factor along the Y-axis.
   *
   * @param scaleY the new scaling factor for the Y-axis.
   */
  public void setScaleY(float scaleY) {
    this.scaleY = scaleY;
  }

  /**
   * Retrieves the scaling factor along the Z-axis.
   *
   * @return the current scaling factor along the Z-axis.
   */
  public float getScaleZ() {
    return scaleZ;
  }

  /**
   * Updates the scaling factor along the Z-axis.
   *
   * @param scaleZ the new scaling factor for the Z-axis.
   */
  public void setScaleZ(float scaleZ) {
    this.scaleZ = scaleZ;
  }
}

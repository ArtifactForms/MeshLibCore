package mesh.modifier.transform;

import math.Vector3f;
import mesh.Mesh;
import mesh.modifier.IMeshModifier;

/**
 * {@link TranslateModifier} applies a translation (offset) to all vertices of a given 3D mesh.
 *
 * <p>This modifier translates each vertex in the provided mesh by a given 3D vector (delta). The
 * operation can be performed efficiently in parallel using Java's parallel streams for improved
 * performance on large meshes.
 */
public class TranslateModifier implements IMeshModifier {

  /** The translation vector representing the offset in 3D space. */
  private Vector3f delta;

  /** Default constructor initializes translation delta to (0, 0, 0). */
  public TranslateModifier() {
    this(new Vector3f());
  }

  /**
   * Constructs a {@link TranslateModifier} with specified translation offset along a the specified
   * axis.
   *
   * @param delta offset along the specified axis
   * @param axis the axis to translate along
   */
  public TranslateModifier(float delta, TransformAxis axis) {
    if (axis == null) {
      throw new IllegalArgumentException("TransformAxis cannot be null.");
    }
    this.delta = new Vector3f();
    if (axis == TransformAxis.X) this.delta.set(delta, 0, 0);
    if (axis == TransformAxis.Y) this.delta.set(0, delta, 0);
    if (axis == TransformAxis.Z) this.delta.set(0, 0, delta);
  }

  /**
   * Constructs a {@link TranslateModifier} with specified translation offsets along each axis.
   *
   * @param deltaX Offset along the X-axis.
   * @param deltaY Offset along the Y-axis.
   * @param deltaZ Offset along the Z-axis.
   */
  public TranslateModifier(float deltaX, float deltaY, float deltaZ) {
    this(new Vector3f(deltaX, deltaY, deltaZ));
  }

  /**
   * Constructs a {@link TranslateModifier} using a Vector3f for the specified translation delta.
   *
   * @param delta The 3D translation vector to apply to the mesh's vertices.
   * @throws IllegalArgumentException if the provided delta is null.
   */
  public TranslateModifier(Vector3f delta) {
    if (delta == null) {
      throw new IllegalArgumentException("Delta cannot be null.");
    }
    this.delta = new Vector3f(delta);
  }

  /**
   * Applies the translation to all vertices in the provided mesh by adding the delta vector to each
   * vertex. Uses parallel processing for efficiency.
   *
   * <p>If the provided mesh contains no vertices, the method safely returns the mesh without
   * changes.
   *
   * @param mesh The 3D mesh whose vertices will be translated.
   * @return The modified 3D mesh after applying the translation.
   * @throws IllegalArgumentException if the provided mesh is null.
   */
  @Override
  public Mesh modify(Mesh mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }

    if (deltaIsEmpty()) {
      return mesh;
    }

    if (mesh.getVertexCount() == 0) {
      return mesh;
    }

    for (int i = 0; i < mesh.getVertexCount(); i++) {
    	Vector3f vertex = mesh.getVertexAt(i);
        vertex.addLocal(delta);
    }

    return mesh;
  }

  /**
   * Checks if the translation delta is effectively zero across all axes. *
   *
   * <p>This is used as a performance optimization to skip the parallel stream processing if no
   * actual movement would occur.
   *
   * @return true if the translation vector is (0, 0, 0), false otherwise.
   */
  private boolean deltaIsEmpty() {
    return delta.x == 0 && delta.y == 0 && delta.z == 0;
  }

  /**
   * Sets the translation of this modifiers delta to the values provided by the new delta.
   *
   * @param delta The new delta.
   * @throws IllegalArgumentException if the provided delta is null.
   */
  public void setDelta(Vector3f delta) {
    if (delta == null) {
      throw new IllegalArgumentException("Delta cannot be null.");
    }
    this.delta.set(delta);
  }

  /**
   * Retrieves the translation offset along the X-axis.
   *
   * @return The X component of the translation delta.
   */
  public float getDeltaX() {
    return delta.x;
  }

  /**
   * Retrieves the translation offset along the Y-axis.
   *
   * @return The Y component of the translation delta.
   */
  public float getDeltaY() {
    return delta.y;
  }

  /**
   * Retrieves the translation offset along the Z-axis.
   *
   * @return The Z component of the translation delta.
   */
  public float getDeltaZ() {
    return delta.z;
  }
}

package mesh.modifier.transform;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.modifier.topology.FlipFacesModifier;

/**
 * Mirrors a 3D mesh along one of its principal axes.
 *
 * <p>The modifier reflects all vertex positions across the selected axis, effectively producing a
 * mirrored version of the mesh. Because mirroring changes the handedness of the coordinate system,
 * this operation also:
 *
 * <ul>
 *   <li>Inverts the face winding order
 *   <li>Recalculates all face normals
 * </ul>
 *
 * If the mesh uses vertex normals for shading, they must be recalculated after mirroring.
 *
 * <p>The operation is performed in-place and preserves mesh topology (vertex and face counts remain
 * unchanged).
 *
 * <h3>Axis behavior</h3>
 *
 * <ul>
 *   <li>{@code X} – mirrors across the YZ plane
 *   <li>{@code Y} – mirrors across the XZ plane (default)
 *   <li>{@code Z} – mirrors across the XY plane
 * </ul>
 *
 * <h3>Notes</h3>
 *
 * <ul>
 *   <li>Applying the same mirror twice restores the original geometry.
 *   <li>This modifier assumes a right-handed coordinate system.
 *   <li>Normals are guaranteed to be updated and normalized after execution.
 * </ul>
 */
public class MirrorModifier implements IMeshModifier {

  private TransformAxis transformAxis;

  /** Creates a {@code MirrorModifier} that mirrors the mesh along the Y axis. */
  public MirrorModifier() {
    this(TransformAxis.Y);
  }

  /**
   * Creates a {@code MirrorModifier} with the specified mirror axis.
   *
   * @param axis the axis to mirror along
   */
  public MirrorModifier(TransformAxis axis) {
    this.transformAxis = axis;
  }

  /**
   * Applies the mirror operation to the given mesh.
   *
   * @param mesh the mesh to modify
   * @return the modified mesh
   * @throws IllegalArgumentException if the mesh is {@code null}
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }

    mirror(mesh);

    // Mirroring changes handedness → must flip winding + normals
    invertWindingOrder(mesh);

    return mesh;
  }

  /** Mirrors all vertices of the mesh along the configured axis. */
  private void mirror(Mesh3D mesh) {
    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f v = mesh.getVertexAt(i);
      mirrorAlongAxis(v);
    }
  }

  /** Inverts the winding order of all faces to restore correct orientation. */
  private void invertWindingOrder(Mesh3D mesh) {
    new FlipFacesModifier().modify(mesh);
  }

  /** Mirrors a single vertex along the configured axis. */
  private void mirrorAlongAxis(Vector3f v) {
    switch (transformAxis) {
      case X:
        v.z = -v.z;
        break;
      case Y:
        v.x = -v.x;
        break;
      case Z:
        v.y = -v.y;
        break;
    }
  }

  /**
   * Returns the axis used for mirroring.
   *
   * @return the current transform axis
   */
  public TransformAxis getTransformAxis() {
    return transformAxis;
  }

  /**
   * Sets the axis used for mirroring.
   *
   * @param transformAxis the new transform axis
   * @throws IllegalArgumentException if {@code transformAxis} is {@code null}
   */
  public void setTransformAxis(TransformAxis transformAxis) {
    if (transformAxis == null) {
      throw new IllegalArgumentException("Transform axis cannot be null.");
    }
    this.transformAxis = transformAxis;
  }
}

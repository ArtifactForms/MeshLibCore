package mesh.modifier.transform;

import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A mesh modifier that applies a rotation to a 3D mesh around the X-axis.
 *
 * <p>This class implements {@link IMeshModifier} to apply a rotation transformation to a given
 * mesh. The rotation is defined by an angle in radians. It modifies the vertices of the mesh in
 * place using a computed rotation matrix.
 */
public class RotateXModifier implements IMeshModifier {

  /**
   * The current angle of rotation in radians. Defines how much the mesh should be rotated about the
   * X-axis.
   */
  private float angle;

  /**
   * The 3D mesh that will be transformed by this modifier. Represents the collection of vertices to
   * apply the rotation on.
   */
  private Mesh3D mesh;

  /**
   * The 3x3 rotation matrix used to compute the rotation transformation. This matrix is updated
   * whenever the angle changes to ensure the transformation corresponds to the current rotation.
   */
  private Matrix3f rotationMatrix;

  /** Constructs a {@link RotateXModifier} with an initial angle of 0 radians. */
  public RotateXModifier() {
    this(0);
  }

  /**
   * Constructs a {@link RotateXModifier} with a specified initial angle.
   *
   * @param angle the initial angle of rotation in radians
   */
  public RotateXModifier(float angle) {
    initializeRotationMatrix();
    setAngle(angle);
  }

  /**
   * Modifies the provided mesh by applying a rotation transformation around the X-axis.
   *
   * <p>If the provided mesh contains no vertices, the method safely returns the mesh without
   * changes.
   *
   * @param mesh the 3D mesh to rotate (must not be null)
   * @return the modified mesh after rotation
   * @throws IllegalArgumentException if the provided mesh is null
   * @see #getAngle()
   * @see #setAngle(float)
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
    rotateMesh();
    return mesh;
  }

  /** Initializes the rotation matrix to its default state. */
  private void initializeRotationMatrix() {
    rotationMatrix = new Matrix3f();
  }

  /**
   * Updates the rotation matrix based on the current angle of rotation. The matrix represents a 3D
   * rotation transformation around the X-axis.
   */
  private void updateRotationMatrix() {
    rotationMatrix.set(
        1, 0, 0, 0, Mathf.cos(angle), -Mathf.sin(angle), 0, Mathf.sin(angle), Mathf.cos(angle));
  }

  /** Applies the rotation transformation to all vertices of the mesh. */
  private void rotateMesh() {
	  for (int i = 0; i < mesh.getVertexCount(); i++) {
		  Vector3f v = mesh.getVertexAt(i);
		  applyRotationToVertex(v);
	  }
  }

  /**
   * Applies the rotation matrix to a single vertex to transform it according to the current
   * rotation.
   *
   * @param vertex the vertex to transform
   */
  private void applyRotationToVertex(Vector3f vertex) {
    vertex.multLocal(rotationMatrix);
  }

  /**
   * Assigns the provided mesh to this modifier for processing.
   *
   * @param mesh the 3D mesh to process
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  /**
   * Gets the current angle of rotation in radians.
   *
   * @return the current angle of rotation
   */
  public float getAngle() {
    return angle;
  }

  /**
   * Sets the rotation angle in radians and updates the transformation matrix if the angle has
   * changed.
   *
   * @param angle the new rotation angle in radians
   */
  public void setAngle(float angle) {
    if (this.angle == angle) return;
    this.angle = angle;
    updateRotationMatrix();
  }
}

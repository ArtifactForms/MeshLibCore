package mesh.modifier.repair;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * The `UpdateFaceNormalsModifier` recalculates and updates the normals of all faces in a 3D mesh.
 * This ensures that face normals are consistent with the current vertex positions and geometry.
 *
 * <p>This modifier is useful when the geometry of the mesh has been modified, and accurate face
 * normals are needed for rendering, physics calculations, or other operations.
 */
public class UpdateFaceNormalsModifier implements IMeshModifier {

  /** The mesh being modified. */
  private Mesh3D mesh;

  /**
   * Recalculates and updates the normals of all faces in the given 3D mesh.
   *
   * @param mesh The 3D mesh to be modified.
   * @return The modified 3D mesh with updated face normals.
   * @throws IllegalArgumentException If the provided mesh is null.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    validateMesh(mesh);
    setMesh(mesh);
    updateFaceNormals();
    return mesh;
  }

  /**
   * Iterates over all faces in the mesh and updates their normals in parallel. Parallel processing
   * is used to enhance performance for large meshes.
   */
  private void updateFaceNormals() {
    for (Face3D f : mesh.getFaces()) {
    	updateFaceNormal(f);
    }
  }

  /**
   * Calculates and updates the normal for a single face.
   *
   * @param face The face whose normal is to be updated.
   */
  private void updateFaceNormal(Face3D face) {
    Vector3f normal = mesh.calculateFaceNormal(face);
    face.normal.set(normal);
  }

  /**
   * Validates that the provided mesh is not null.
   *
   * @param mesh The mesh to validate.
   * @throws IllegalArgumentException If the mesh is null.
   */
  private void validateMesh(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
  }

  /**
   * Sets the current mesh for processing.
   *
   * @param mesh The 3D mesh to set.
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }
}

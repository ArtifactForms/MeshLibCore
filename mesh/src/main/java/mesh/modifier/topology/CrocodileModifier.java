package mesh.modifier.topology;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.conway.ConwayAmboModifier;
import mesh.geometry.MeshGeometryUtil;
import mesh.modifier.IMeshModifier;
import mesh.selection.FaceSelection;

/**
 * The `CrocodileModifier` transforms a 3D mesh by applying the Conway Ambo operation and adding
 * spike-like protrusions to the resulting faces.
 *
 * <pre>
 * - Ambo Operation: This operation slices off the corners of the original
 * polyhedron by creating slicing planes that pass through the midpoints of
 * edges. This produces new vertices and new polygonal faces, effectively
 * generating a new polyhedral structure.
 *
 * - Spike Creation: After applying the Ambo operation, spike-like protrusions
 * are added to the centers of these newly created faces. These spikes extend
 * outward, giving the resulting mesh a spiky appearance. This spike effect can
 * be viewed as a variation of the Kis operation.
 *
 * - Kis-Like Operation: While the traditional Kis operation modifies all faces
 * of a polyhedron, this implementation is limited to targeting only the newly
 * created faces resulting from the Ambo operation. This allows for a more
 * focused and controlled deformation.
 * </pre>
 */
public class CrocodileModifier implements IMeshModifier {

  /** The distance defines how much the spike protrudes outward from the face's center. */
  private float distance;

  /** The 3D mesh being modified. */
  private Mesh3D mesh;

  /** Selection utility to track faces created by the Ambo operation. */
  private FaceSelection selection;

  /**
   * Modifies the given 3D mesh by applying the Ambo operation, creating spikes, and removing
   * certain faces to produce the final transformation.
   *
   * @param mesh The 3D mesh to be transformed.
   * @return The modified 3D mesh.
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    validateMesh(mesh);
    if (mesh.getVertexCount() == 0) {
      return mesh;
    }
    setMesh(mesh);
    applConwayAmboOperation();
    selectAmboFaces();
    createSpikes();
    removeSelectedAmboFaces();
    return mesh;
  }

  /**
   * Creates spike-like protrusions by generating new triangular faces at the centers of selected
   * faces.
   */
  private void createSpikes() {
    int nextIndex = mesh.getVertexCount();
    for (Face3D face : selection.getFaces()) {
      mesh.add(calculateSpikeTip(face));
      createSpikeFaces(face, nextIndex);
      nextIndex++;
    }
  }

  /**
   * Calculates the tip of the spike based on the center of the given face and projects it outward
   * along the normal vector scaled by the distance.
   *
   * @param face The face used to calculate the spike tip.
   * @return The 3D position of the spike tip.
   */
  private Vector3f calculateSpikeTip(Face3D face) {
    Vector3f center = MeshGeometryUtil.calculateFaceCenter(mesh, face);
    Vector3f normal = MeshGeometryUtil.calculateFaceNormal(mesh, face);
    if (distance == 0) {
      return center;
    }
    return center.add(normal.mult(distance));
  }

  /**
   * Generates the triangular spike faces connecting edges of a given face to the spike tip.
   *
   * @param face The target face to create spikes for.
   * @param nextIndex The index of the newly created spike tip vertex.
   */
  private void createSpikeFaces(Face3D face, int nextIndex) {
    for (int i = 0; i < face.indices.length; i++) {
      int fromIndex = face.indices[i];
      int toIndex = face.indices[(i + 1) % face.indices.length];
      int centerIndex = nextIndex;
      Face3D triangle = new Face3D(fromIndex, toIndex, centerIndex);
      triangle.setTag("spikes");
      mesh.add(triangle);
    }
  }

  /**
   * Validates that the given mesh is not null.
   *
   * @param mesh The mesh to validate.
   */
  private void validateMesh(Mesh3D mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
  }

  /** Applies the Conway Ambo operation to the 3D mesh to generate new faces and vertices. */
  private void applConwayAmboOperation() {
    new ConwayAmboModifier().modify(mesh);
  }

  /** Selects the faces created by the Ambo operation using their assigned tags. */
  private void selectAmboFaces() {
    selection = new FaceSelection(mesh);
    selection.selectByTag("ambo");
  }

  /**
   * Removes all the newly created faces (tagged by the Ambo operation) from the mesh as part of the
   * transformation process.
   */
  private void removeSelectedAmboFaces() {
    mesh.removeFaces(selection.getFaces());
  }

  /**
   * Sets the current mesh for transformation operations.
   *
   * @param mesh The 3D mesh to operate on.
   */
  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  /**
   * Retrieves the distance value, which defines how far spikes protrude.
   *
   * @return The current distance value.
   */
  public float getDistance() {
    return distance;
  }

  /**
   * Sets the distance value, which controls how far the spike-like protrusions are displaced from
   * their originating face centers.
   *
   * @param distance The distance value to set.
   */
  public void setDistance(float distance) {
    this.distance = distance;
  }
}

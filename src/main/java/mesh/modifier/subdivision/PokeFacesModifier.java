package mesh.modifier.subdivision;

import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.geometry.MeshGeometryUtil;
import mesh.modifier.IMeshModifier;

/**
 * A mesh modifier that splits each selected face into a triangle fan.
 *
 * <p>This modifier creates a new vertex at the center of each face and connects it to the original
 * face's vertices, effectively extruding the face. The offset can be used to create spikes or
 * depressions.
 */
public class PokeFacesModifier implements IMeshModifier {

  private float pokeOffset;

  private Mesh3D mesh;

  private List<Face3D> originalFaces;

  public PokeFacesModifier() {
    this(0.1f);
  }

  public PokeFacesModifier(float pokeOffset) {
    this.pokeOffset = pokeOffset;
  }

  private void createPointedFaces(Face3D face) {
    int centerVertexIndex = mesh.getVertexCount() - 1;
    for (int i = 0; i < face.getVertexCount(); i++) {
      int index0 = centerVertexIndex;
      int index1 = face.getIndexAt(i);
      int index2 = face.getIndexAt(i + 1);
      addFace(index0, index1, index2);
    }
  }

  private void createPointedCenterVertex(Face3D face) {
    Vector3f center = calculateFaceCenter(face);
    Vector3f normal = calculateFaceNormal(face);
    center.addLocal(normal.mult(getPokeOffset()));
    addVertex(center);
  }

  private void pokeFaces() {
    for (Face3D face : originalFaces) {
      createPointedCenterVertex(face);
      createPointedFaces(face);
    }
  }

  @Override
  public Mesh3D modify(Mesh3D mesh) {
    setMesh(mesh);
    setOriginalFaces();
    pokeFaces();
    removeOriginalFaces();
    return mesh;
  }

  private void setOriginalFaces() {
    originalFaces = mesh.getFaces();
  }

  private void removeOriginalFaces() {
    mesh.removeFaces(originalFaces);
  }

  private void addFace(int... indices) {
    mesh.add(new Face3D(indices));
  }

  private void addVertex(Vector3f v) {
    mesh.add(v);
  }

  private Vector3f calculateFaceCenter(Face3D face) {
    return MeshGeometryUtil.calculateFaceCenter(mesh, face);
  }

  private Vector3f calculateFaceNormal(Face3D face) {
    return MeshGeometryUtil.calculateFaceNormal(mesh, face);
  }

  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  public float getPokeOffset() {
    return pokeOffset;
  }

  public void setPokeOffset(float pokeOffset) {
    this.pokeOffset = pokeOffset;
  }
}

package mesh.modifier.repair;

import java.util.LinkedHashSet;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class RemoveDoubleVerticesModifier implements IMeshModifier {

  private Integer decimalPlaces; // null = no rounding

  private Mesh3D temporaryMesh;

  private Mesh3D mesh;

  private LinkedHashSet<Vector3f> vertexSet;

  public RemoveDoubleVerticesModifier() {}

  public RemoveDoubleVerticesModifier(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  @Override
  public Mesh3D modify(Mesh3D mesh) {
    setMesh(mesh);

    if (decimalPlaces != null) {
      for (int i = 0; i < mesh.getVertexCount(); i++) {
        mesh.getVertexAt(i).roundLocalDecimalPlaces(decimalPlaces);
      }
    }

    initializeTmpMesh();
    initializeVertexSet();
    hashVertices();
    addAllVerticesFromSetToTmpMesh();
    createNewFaces();
    clearOldVertices();
    clearOldFaces();
    addVertices();
    addFaces();
    return mesh;
  }

  private void createNewFaces() {
    for (Face3D face : mesh.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = getVertexAt(face.indices[i]);
        int index = temporaryMesh.indexOf(v);
        face.indices[i] = index;
      }
      temporaryMesh.add(face);
    }
  }

  private void hashVertices() {
    for (Face3D face : mesh.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        vertexSet.add(getVertexAt(face.indices[i]));
      }
    }
  }

  private void addVertices() {
    mesh.addVertices(temporaryMesh.getVertices());
  }

  private void addFaces() {
    mesh.addFaces(temporaryMesh.getFaces());
  }

  private void addAllVerticesFromSetToTmpMesh() {
    temporaryMesh.addVertices(vertexSet);
  }

  private void initializeVertexSet() {
    vertexSet = new LinkedHashSet<Vector3f>();
  }

  private void initializeTmpMesh() {
    temporaryMesh = new Mesh3D();
  }

  private Vector3f getVertexAt(int index) {
    return mesh.getVertexAt(index);
  }

  private void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  private void clearOldVertices() {
    mesh.clearVertices();
  }

  private void clearOldFaces() {
    mesh.clearFaces();
  }
}

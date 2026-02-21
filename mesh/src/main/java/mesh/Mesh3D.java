package mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.modifier.transform.RotateYModifier;
import mesh.modifier.transform.RotateZModifier;
import mesh.modifier.transform.TranslateModifier;
import mesh.next.surface.SurfaceLayer;

/**
 * Mesh3D is a simple indexed triangle/face mesh implementation.
 *
 * <p>Design Intent: - Acts purely as a geometric data container. - Contains vertices and faces. -
 * No transformation or modification logic should live here.
 *
 * <p>Modifications must be applied through the mesh modifier pipeline.
 *
 * <p>This class is currently under refactoring. Legacy utility methods remain temporarily but are
 * deprecated.
 */
public class Mesh3D implements Mesh {

  private ArrayList<Vector3f> vertices;

  private ArrayList<Face3D> faces;

  private ArrayList<Vector3f> vertexNormals;

  private SurfaceLayer surfaceLayer;

  public Mesh3D() {
    vertices = new ArrayList<Vector3f>();
    faces = new ArrayList<Face3D>();
    vertexNormals = new ArrayList<Vector3f>();
    this.surfaceLayer = new SurfaceLayer();
  }

  // -------------------------------------------------------------------
  // Surface Layer
  // -------------------------------------------------------------------

  public SurfaceLayer getSurfaceLayer() {
    return surfaceLayer;
  }

  // -------------------------------------------------------------------
  // Interface / Contract
  // -------------------------------------------------------------------

  @Override
  public int getVertexCount() {
    return vertices.size();
  }

  @Override
  public int getFaceCount() {
    return faces.size();
  }

  @Override
  public Vector3f getVertexAt(int index) {
    return vertices.get(index);
  }

  @Override
  public int addVertex(float x, float y, float z) {
    int index = vertices.size();
    vertices.add(new Vector3f(x, y, z));
    return index;
  }

  @Override
  public void addFace(int... indices) {
    faces.add(new Face3D(indices));
    surfaceLayer.ensureFaceCapacity(faces.size());
  }

  // -------------------------------------------------------------------
  // Deprecated transform operations
  // -------------------------------------------------------------------

  /**
   * Rotates the mesh around the Y-axis.
   *
   * @deprecated Use {@link RotateYModifier} instead.
   */
  @Deprecated
  public Mesh3D rotateY(float angle) {
    return new RotateYModifier(angle).modify(this);
  }

  /**
   * Rotates the mesh around the Z-axis.
   *
   * @deprecated Use {@link RotateZModifier} instead.
   */
  @Deprecated
  public Mesh3D rotateZ(float angle) {
    return new RotateZModifier(angle).modify(this);
  }

  /**
   * Translates the mesh along the X-axis.
   *
   * @deprecated Use {@link TranslateModifier} instead.
   */
  @Deprecated
  public Mesh3D translateX(float tx) {
    return new TranslateModifier(tx, 0, 0).modify(this);
  }

  /**
   * Translates the mesh along the Y-axis.
   *
   * @deprecated Use {@link TranslateModifier} instead.
   */
  @Deprecated
  public Mesh3D translateY(float ty) {
    return new TranslateModifier(0, ty, 0).modify(this);
  }

//  /**
//   * Translates the mesh along the Z-axis.
//   *
//   * @deprecated Use {@link TranslateModifier} instead.
//   */
//  @Deprecated
//  public Mesh3D translateZ(float tz) {
//    return new TranslateModifier(0, 0, tz).modify(this);
//  }

  // -------------------------------------------------------------------
  // UVs
  // -------------------------------------------------------------------

  /**
   * Adds a UV coordinate to the mesh.
   *
   * <p>This method appends a new UV coordinate, specified by the parameters {@code u} and {@code
   * v}, to the list of UV coordinates for this mesh. UV coordinates are used for mapping textures
   * to the surface of the mesh, where {@code u} and {@code v} typically represent the horizontal
   * and vertical texture coordinates, respectively.
   *
   * @param u The horizontal component of the UV coordinate.
   * @param v The vertical component of the UV coordinate.
   */
  @Deprecated
  public void addUvCoordinate(float u, float v) {
    surfaceLayer.addUV(u, v);
  }

  // TEMP: shared vertex/uv indices shortcut
  // Will be removed once UV generation pipeline is stable
  @Deprecated
  public void addFaceWithSharedUVs(int... indices) {
    addFace(indices);
    int faceIndex = faces.size() - 1;
    surfaceLayer.setFaceUVIndices(faceIndex, indices);
  }
  
  // -------------------------------------------------------------------
  // Deprecated vertex access
  // -------------------------------------------------------------------

  @Deprecated
  public List<Vector3f> getVertices() {
    return new ArrayList<>(vertices);
  }

  @Deprecated
  public int indexOf(Vector3f v) {
    return vertices.indexOf(v);
  }

  public void remove(Vector3f v) {
    vertices.remove(v);
  }

  public void remove(Collection<Vector3f> vertices) {
    this.vertices.removeAll(vertices);
  }

  @Deprecated
  public void clearVertices() {
    vertices.clear();
  }

  @Deprecated
  public void addVertices(Collection<Vector3f> vertices) {
    this.vertices.addAll(vertices);
  }

  @Deprecated
  public void add(Vector3f... vertices) {
    this.vertices.addAll(Arrays.asList(vertices));
  }

  // -------------------------------------------------------------------

  @Deprecated
  public void removeFace(Face3D face) {
    faces.remove(face);
  }

  @Deprecated
  public boolean hasVertexNormals() {
    return !vertexNormals.isEmpty();
  }

  @Deprecated
  public void setVertexNormals(List<Vector3f> vertexNormals) {
    this.vertexNormals.clear();
    this.vertexNormals.addAll(vertexNormals);
  }

  @Deprecated
  public ArrayList<Vector3f> getVertexNormals() {
    return vertexNormals;
  }

  @Deprecated
  public int getUvCount() {
    return surfaceLayer.getUVCount();
  }

  @Deprecated
  public Mesh3D copy() {
    Mesh3D copy = new Mesh3D();
    List<Vector3f> vertices = copy.vertices;
    List<Face3D> faces = copy.faces;

    for (Vector3f v : this.vertices) vertices.add(new Vector3f(v));

    for (Face3D f : this.faces) faces.add(new Face3D(f));

    return copy;
  }

  @Deprecated
  public Mesh3D append(Mesh3D... meshes) {
    Mesh3D result = new Mesh3D();

    result = appendUtil(meshes);
    result = appendUtil(this, result);

    vertices.clear();
    vertices.addAll(result.vertices);

    faces.clear();
    faces.addAll(result.faces);

    return this;
  }

  @Deprecated
  private Mesh3D appendUtil(Mesh3D... meshes) {
    // FIXME copy vertices and faces
    int n = 0;
    Mesh3D mesh = new Mesh3D();
    List<Vector3f> vertices = mesh.vertices;
    List<Face3D> faces = mesh.faces;

    for (int i = 0; i < meshes.length; i++) {
      Mesh3D m = meshes[i];
      vertices.addAll(m.vertices);
      faces.addAll(meshes[i].faces);
      for (Face3D f : meshes[i].faces) {
        for (int j = 0; j < f.indices.length; j++) {
          f.indices[j] += n;
        }
      }
      n += m.getVertexCount();
    }

    return mesh;
  }

  @Deprecated
  public void clearFaces() {
    faces.clear();
  }

  @Deprecated
  public void addFaces(Collection<Face3D> faces) {
    this.faces.addAll(faces);
  }

  @Deprecated
  public void removeFaces(Collection<Face3D> faces) {
    this.faces.removeAll(faces);
  }

  @Deprecated
  public Face3D getFaceAt(int index) {
    return faces.get(index);
  }

  @Deprecated
  public List<Face3D> getFaces() {
    return new ArrayList<Face3D>(faces);
  }

  // -------------------------------------------------------------------
  // Remove early
  // -------------------------------------------------------------------
  /**
   * use {@link #addFace(int...)}
   *
   * @param faces
   */
  @Deprecated
  public void add(Face3D... faces) {
    this.faces.addAll(Arrays.asList(faces));
  }
}

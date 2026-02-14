package mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import math.Vector2f;
import math.Vector3f;
import mesh.modifier.IMeshModifier;
import mesh.modifier.repair.RemoveDoubleVerticesModifier;
import mesh.modifier.transform.RotateYModifier;
import mesh.modifier.transform.RotateZModifier;
import mesh.modifier.transform.TranslateModifier;

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

  private ArrayList<Vector2f> uvs;

  public Mesh3D() {
    vertices = new ArrayList<Vector3f>();
    faces = new ArrayList<Face3D>();
    vertexNormals = new ArrayList<Vector3f>();
    uvs = new ArrayList<Vector2f>();
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
  }

  // -------------------------------------------------------------------
  // Deprecated
  // -------------------------------------------------------------------
  
  /**
   * Applies the provided {@link IMeshModifier} to this mesh. This is congruent to {@link
   * IMeshModifier#modify(Mesh3D)}.
   *
   * @param modifier The modifier to apply to this mesh.
   * @return this
   * @deprecated Modifiers must be applied via {@link ModifierStackr}. This method bypasses
   *     validation and pipeline control.
   */
  @Deprecated
  public Mesh3D apply(IMeshModifier modifier) {
    return modifier.modify(this);
  }

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

  /**
   * Translates the mesh along the Z-axis.
   *
   * @deprecated Use {@link TranslateModifier} instead.
   */
  @Deprecated
  public Mesh3D translateZ(float tz) {
    return new TranslateModifier(0, 0, tz).modify(this);
  }

  /**
   * Removes duplicated vertices.
   *
   * @deprecated Use {@link RemoveDoubleVerticesModifier} instead.
   */
  @Deprecated
  public void removeDoubles() {
    new RemoveDoubleVerticesModifier().modify(this);
  }

  @Deprecated
  public Vector3f calculateFaceNormal(Face3D face) {
    Vector3f faceNormal = new Vector3f();
    for (int i = 0; i < face.indices.length; i++) {
      Vector3f currentVertex = vertices.get(face.indices[i]);
      Vector3f nextVertex = vertices.get(face.indices[(i + 1) % face.indices.length]);
      float x =
          (currentVertex.getY() - nextVertex.getY()) * (currentVertex.getZ() + nextVertex.getZ());
      float y =
          (currentVertex.getZ() - nextVertex.getZ()) * (currentVertex.getX() + nextVertex.getX());
      float z =
          (currentVertex.getX() - nextVertex.getX()) * (currentVertex.getY() + nextVertex.getY());
      faceNormal.addLocal(x, y, z);
    }
    return faceNormal.normalize();
  }

  @Deprecated
  public Vector3f calculateFaceCenter(Face3D face) {
    Vector3f center = new Vector3f();
    for (int i = 0; i < face.indices.length; i++) {
      center.addLocal(vertices.get(face.indices[i]));
    }
    return center.divideLocal(face.indices.length);
  }

  @Deprecated
  public List<Vector3f> getVertices() {
    return new ArrayList<>(vertices);
  }

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
    uvs.add(new Vector2f(u, v));
  }

  /**
   * Sets the UV coordinates for this mesh.
   *
   * <p>This method sets the list of UV coordinates that will be used for the mesh. The provided
   * list of UV coordinates will replace any existing UVs. The list must be a valid {@link
   * ArrayList} of {@link Vector2f} objects. If the provided list is {@code null}, an {@link
   * IllegalArgumentException} will be thrown.
   *
   * @param uvs The list of UV coordinates to be set. It must not be {@code null} and should contain
   *     {@link Vector2f} objects representing the UV mapping for the mesh.
   * @throws IllegalArgumentException if the provided {@code uvs} list is {@code null}.
   */
  @Deprecated
  public void setUvs(ArrayList<Vector2f> uvs) {
    if (uvs == null) {
      throw new IllegalArgumentException("The list of UV coordinates cannot be null.");
    }
    this.uvs = uvs;
  }

  @Deprecated
  public int getUvCount() {
    return uvs.size();
  }

  @Deprecated
  public ArrayList<Vector2f> getUVCoordinates() {
    return uvs;
  }

  /**
   * Retrieves the UV coordinates at the specified index.
   *
   * <p>This method returns the UV coordinates associated with the given index. If the index is out
   * of bounds (either negative or beyond the size of the list), a default UV coordinate (0, 0) is
   * returned to avoid potential errors or exceptions. The method does not throw an exception when
   * an invalid index is provided, ensuring that the calling code can proceed without disruption.
   *
   * @param index The index of the UV coordinate to retrieve.
   * @return The UV coordinates as a {@link Vector2f}. If the index is out of bounds, returns {@code
   *     new Vector2f(0, 0)}. The return value will never be {@code null}.
   */
  @Deprecated
  public Vector2f getUvAt(int index) {
    if (index < 0 || index >= uvs.size()) {
      return new Vector2f(0, 0);
    }
    return uvs.get(index);
  }

  @Deprecated
  public void setUvCoordinate(int index, float u, float v) {
    uvs.get(index).set(u, v);
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
  public void clearVertices() {
    vertices.clear();
  }

  @Deprecated
  public void clearFaces() {
    faces.clear();
  }

  @Deprecated
  public void addFace(boolean uvs, int... indices) {
    faces.add(new Face3D(indices, indices));
  }

  @Deprecated
  public void addVertices(Collection<Vector3f> vertices) {
    this.vertices.addAll(vertices);
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
  public void add(Vector3f... vertices) {
    this.vertices.addAll(Arrays.asList(vertices));
  }

  @Deprecated
  public Face3D getFaceAt(int index) {
    return faces.get(index);
  }

  @Deprecated
  public List<Face3D> getFaces() {
    return new ArrayList<Face3D>(faces);
  }
  
  public void remove(Vector3f v) {
	  vertices.remove(v);
  }
  
  public void remove(Collection<Vector3f> vertices) {
	  this.vertices.removeAll(vertices);
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

  @Deprecated
  public List<Face3D> getFaces(int from, int to) {
    return new ArrayList<>(faces.subList(from, to));
  }
  
  public int indexOf(Vector3f v) {
	  return vertices.indexOf(v);
  }
}

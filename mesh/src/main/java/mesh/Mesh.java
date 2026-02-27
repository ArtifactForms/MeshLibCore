package mesh;

import math.Vector3f;

/**
 * Minimal abstraction of an indexed polygon mesh.
 *
 * <p>A {@code Mesh} represents a collection of vertices and polygonal faces. Faces are defined by
 * ordered sequences of vertex indices referring to vertices stored in this mesh.
 *
 * <h2>Structural Contract</h2>
 *
 * <ul>
 *   <li>Vertex indices are zero-based and contiguous in the range {@code [0, getVertexCount())}.
 *   <li>Face indices are zero-based and contiguous in the range {@code [0, getFaceCount())}.
 *   <li>Each face must reference only valid vertex indices.
 *   <li>A valid face should contain at least three vertices.
 *   <li>The order of indices within a face defines its winding.
 * </ul>
 *
 * <h2>Mutation Semantics</h2>
 *
 * <ul>
 *   <li>Vertices can be added via {@link #addVertex(float, float, float)}.
 *   <li>Faces can be added via {@link #addFace(int...)}.
 *   <li>The behavior regarding removal or reordering of elements is implementation-defined.
 * </ul>
 *
 * <h2>Data Ownership</h2>
 *
 * <ul>
 *   <li>{@link #getVertexAt(int)} may return a direct reference to internal mutable data.
 *   <li>Callers modifying returned vertex instances directly mutate the mesh.
 *   <li>{@link FaceView} provides read-only structural access to faces.
 * </ul>
 *
 * <p>Implementations are free to choose their internal representation (e.g. indexed arrays,
 * half-edge structures, or GPU-backed buffers), as long as this contract is upheld.
 */
public interface Mesh {

  /** Returns the number of vertices in this mesh. */
  int getVertexCount();

  /** Returns the number of faces in this mesh. */
  int getFaceCount();

  /**
   * Returns the position of the vertex at the given index.
   *
   * <p>Implementations may return a direct reference to internal data. Callers should assume the
   * returned vector is mutable.
   *
   * @param index vertex index
   * @return vertex position
   */
  Vector3f getVertexAt(int index);

  /**
   * Adds a vertex to the mesh.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @param z z-coordinate
   * @return index of the newly added vertex
   */
  int addVertex(float x, float y, float z);

  /**
   * Adds a face defined by vertex indices.
   *
   * @param indices vertex indices forming the face
   */
  void addFace(int... indices);

  /**
   * Returns a read-only view of the face at the given index.
   *
   * <p>The valid range is {@code 0 <= index < getFaceCount()}.
   *
   * @param index face index
   * @return read-only face view
   * @throws IndexOutOfBoundsException if the index is invalid
   */
  FaceView getFaceAt(int index);
}

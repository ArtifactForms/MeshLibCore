package mesh;

import math.Vector3f;

/**
 * Minimal abstraction of a polygon mesh.
 *
 * <p>This interface defines the smallest stable contract required for
 * mesh construction and vertex-based manipulation. It intentionally
 * exposes only indexed vertex access and face creation.
 *
 * <p>The goal of this interface is to:
 *
 * <ul>
 *   <li>Decouple mesh consumers (e.g. modifiers) from concrete implementations</li>
 *   <li>Reduce API surface area</li>
 *   <li>Prepare for future alternative mesh representations
 *       (e.g. half-edge, winged-edge, GPU-backed meshes)</li>
 * </ul>
 *
 * <p>Implementations are free to define their internal storage model.
 * Returned vertex positions may be direct references to internal data.
 */
public interface Mesh {

  /**
   * Returns the number of vertices in this mesh.
   */
  int getVertexCount();

  /**
   * Returns the number of faces in this mesh.
   */
  int getFaceCount();

  /**
   * Returns the position of the vertex at the given index.
   *
   * <p>Implementations may return a direct reference to internal data.
   * Callers should assume the returned vector is mutable.
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
}
package mesh;

/**
 * Read-only view of a polygonal face within a {@link Mesh}.
 *
 * <p>This interface exposes the minimal contract required to inspect the topology of a face in an
 * indexed polygon mesh.
 *
 * <p>A face is defined by an ordered sequence of vertex indices. The indices refer to vertices
 * stored in the owning {@link Mesh}.
 *
 * <p>This is a read-only abstraction. Implementations may represent faces using different internal
 * data structures (e.g. simple index arrays, half-edge structures, or other topological models).
 *
 * <p>The purpose of this interface is to:
 *
 * <ul>
 *   <li>Decouple mesh consumers from concrete face implementations
 *   <li>Expose only structural/topological information
 *   <li>Prevent direct mutation of internal mesh state
 * </ul>
 *
 * <p>Index order defines the face winding and must be preserved.
 */
public interface FaceView {

  /**
   * Returns the number of vertices that define this face.
   *
   * @return vertex count of the face (>= 3 for valid polygonal faces)
   */
  int getVertexCount();

  /**
   * Returns the vertex index at the given local position.
   *
   * <p>The index refers to a vertex in the owning {@link Mesh}. The valid range is {@code 0 <= i <
   * getVertexCount()}.
   *
   * @param i local vertex position within the face
   * @return vertex index in the mesh
   * @throws IndexOutOfBoundsException if {@code i} is out of range
   */
  int getIndexAt(int i);
}

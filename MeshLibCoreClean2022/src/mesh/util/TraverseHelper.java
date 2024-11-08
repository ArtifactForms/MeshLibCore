package mesh.util;

import java.util.HashMap;

import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * A helper class for traversing and manipulating 3D meshes.
 *
 * This class provides efficient methods for accessing and modifying mesh
 * elements, such as edges and faces. By pre-processing the mesh and storing
 * edge relationships, it enables efficient traversal and modification
 * algorithms.
 */
public class TraverseHelper {

	private Mesh3D mesh;
	private HashMap<Edge3D, Face3D> edgeFaceMap;
	private HashMap<Edge3D, Edge3D> edgeNextMap;
	private HashMap<Integer, Edge3D> outgoingEdgeMap;

	/**
	 * Constructs a new TraverseHelper for the given mesh.
	 *
	 * @param mesh The 3D mesh to be traversed.
	 */
	public TraverseHelper(Mesh3D mesh) {
		this.mesh = mesh;
		edgeFaceMap = new HashMap<Edge3D, Face3D>();
		edgeNextMap = new HashMap<Edge3D, Edge3D>();
		outgoingEdgeMap = new HashMap<Integer, Edge3D>();
		map();
	}

	/**
	 * Populates the internal data structures with edge and face relationships.
	 */
	private void map() {
		for (Face3D face : mesh.faces) {
			for (int i = 0; i <= face.indices.length; i++) {
				int from = face.indices[i % face.indices.length];
				int to = face.indices[(i + 1) % face.indices.length];
				int nextFrom = to;
				int nextTo = face.indices[(i + 2) % face.indices.length];
				Edge3D edge = new Edge3D(from, to);
				Edge3D next = new Edge3D(nextFrom, nextTo);
				edgeFaceMap.put(edge, face);
				edgeNextMap.put(edge, next);
				outgoingEdgeMap.put(from, edge);
			}
		}
	}

	/**
	 * Returns the outgoing edge from the given vertex index.
	 *
	 * @param index The index of the vertex.
	 * @return The outgoing edge.
	 */
	public Edge3D getOutgoing(int index) {
		return outgoingEdgeMap.get(index);
	}

	/**
	 * Returns the face associated with the given edge.
	 *
	 * @param fromIndex The index of the first vertex of the edge.
	 * @param toIndex   The index of the second vertex of the edge.
	 * @return The face associated with the edge.
	 */
	public Face3D getFaceByEdge(int fromIndex, int toIndex) {
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		return edgeFaceMap.get(edge);

	}

	/**
	 * Returns the next edge in the face, following the given edge.
	 *
	 * @param fromIndex The index of the first vertex of the edge.
	 * @param toIndex   The index of the second vertex of the edge.
	 * @return The next edge in the face.
	 */
	public Edge3D getNext(int fromIndex, int toIndex) {
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		return edgeNextMap.get(edge);
	}

	/**
	 * Returns the reverse edge of the given edge.
	 *
	 * @param fromIndex The index of the first vertex of the edge.
	 * @param toIndex   The index of the second vertex of the edge.
	 * @return The reverse edge.
	 */
	public Edge3D getPair(int fromIndex, int toIndex) {
		return new Edge3D(toIndex, fromIndex);
	}

	/**
	 * Returns the next edge of the reverse edge.
	 *
	 * @param fromIndex The index of the first vertex of the edge.
	 * @param toIndex   The index of the second vertex of the edge.
	 * @return The next edge of the reverse edge.
	 */
	public Edge3D getPairNext(int fromIndex, int toIndex) {
		Edge3D edge = new Edge3D(toIndex, fromIndex);
		return edgeNextMap.get(edge);

	}

	/**
	 * Clears the internal data structures.
	 */
	public void clear() {
		edgeFaceMap.clear();
		edgeNextMap.clear();
	}

}

package mesh.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

	/**
	 * The mesh to traverse.
	 */
	private Mesh3D mesh;

	/**
	 * Maps edges to the faces they belong to.
	 */
	private HashMap<Edge3D, Face3D> edgeFaceMap;

	/**
	 * Maps an edge to the next edge in a face.
	 */
	private HashMap<Edge3D, Edge3D> edgeNextMap;

	/**
	 * Maps vertex indices to their outgoing edges.
	 */
	private HashMap<Integer, Edge3D> outgoingEdgeMap;

	/**
	 * A set of unique edges in the mesh.
	 */
	private HashSet<Edge3D> edges;

	/**
	 * Constructs a new TraverseHelper for the given mesh.
	 *
	 * @param mesh The 3D mesh to be traversed.
	 */
	public TraverseHelper(Mesh3D mesh) {
		setMesh(mesh);
		initialize();
		map();
	}

	/**
	 * Initializes the internal data structures used for mesh traversal.
	 */
	private void initialize() {
		edgeFaceMap = new HashMap<>();
		edgeNextMap = new HashMap<>();
		outgoingEdgeMap = new HashMap<>();
		edges = new HashSet<>();
	}

	/**
	 * Populates the internal data structures with edge and face relationships.
	 */
	private void map() {
		for (Face3D face : mesh.faces)
			mapFace(face);
	}

	/**
	 * Populates the internal data structures for a specific face.
	 *
	 * For each edge of the face, it creates an `Edge3D` object representing the
	 * edge, and adds it to the appropriate maps.
	 *
	 * @param face The face to process.
	 */
	private void mapFace(Face3D face) {
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
			collectUniqueEdge(edge);
		}
	}

	/**
	 * Collects a unique edge, ensuring it's not already present in the `edges` set.
	 *
	 * @param edge The edge to be collected.
	 */
	private void collectUniqueEdge(Edge3D edge) {
		if (edges.contains(edge.createPair()))
			return;
		edges.add(edge);
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
	 * Returns a list of all unique edges in the mesh.
	 *
	 * @return A list of unique edges.
	 */
	public List<Edge3D> getAllEdges() {
		return new ArrayList<Edge3D>(edges);
	}

	/**
	 * Clears the internal data structures.
	 */
	public void clear() {
		edgeFaceMap.clear();
		edgeNextMap.clear();
		outgoingEdgeMap.clear();
		edges.clear();
	}

	/**
	 * Sets the mesh to traverse to the specified mesh.
	 * 
	 * @param mesh The mesh to traverse.
	 */
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}

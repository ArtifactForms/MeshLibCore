package mesh.util;

import java.util.HashMap;

import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;

public class TraverseHelper {

	private Mesh3D mesh;
	private HashMap<Edge3D, Face3D> edgeFaceMap;
	private HashMap<Edge3D, Edge3D> edgeNextMap;
	private HashMap<Integer, Edge3D> outgoingEdgeMap; // Maps one of the outgoing half edges

	public TraverseHelper(Mesh3D mesh) {
		this.mesh = mesh;
		edgeFaceMap = new HashMap<Edge3D, Face3D>();
		edgeNextMap = new HashMap<Edge3D, Edge3D>();
		outgoingEdgeMap = new HashMap<Integer, Edge3D>();
		map();
	}

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

	public Edge3D getOutgoing(int index) {
		return outgoingEdgeMap.get(index);
	}

	public Face3D getFaceByEdge(int fromIndex, int toIndex) {
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		return edgeFaceMap.get(edge);
	}

	public Edge3D getNext(int fromIndex, int toIndex) {
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		return edgeNextMap.get(edge);
	}

	public Edge3D getPair(int fromIndex, int toIndex) {
		return new Edge3D(toIndex, fromIndex);
	}

	public Edge3D getPairNext(int fromIndex, int toIndex) {
		Edge3D edge = new Edge3D(toIndex, fromIndex);
		return edgeNextMap.get(edge);
	}

	public void clear() {
		edgeFaceMap.clear();
		edgeNextMap.clear();
	}

}

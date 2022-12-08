package mesh.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.TraverseHelper;

public class BevelVerticesModifier implements IMeshModifier {

	private float amount;
	private Mesh3D mesh;
	private List<Vector3f> verticesToAdd;
	private List<Face3D> facesToAdd;
	private HashMap<Edge3D, Integer> edgeToEdgePointIndex;

	public BevelVerticesModifier() {
		this(0.1f);
	}

	public BevelVerticesModifier(float amount) {
		this.amount = amount;
		verticesToAdd = new ArrayList<Vector3f>();
		facesToAdd = new ArrayList<Face3D>();
		edgeToEdgePointIndex = new HashMap<Edge3D, Integer>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		clear();
		setMesh(mesh);
		createEdgePoints();
		createFacesForVerticesAroundOldVertex();
		removeOldFaces();
		removeOldVertices();
		addNewVertices();
		addNewFaces();
		return mesh;
	}
	
	private void createEdgePointsOf(Face3D face) {
		Vector<Integer> indices = new Vector<Integer>();

		for (int i = 0; i < face.indices.length; i++) {
			Vector3f from = getVertexForIndexAt(face, i);
			Vector3f to = getVertexForIndexAt(face, i + 1);
			
			Vector3f edgePointFrom = calculateEdgePoint(from, to);
			Vector3f edgePointTo = calculateEdgePoint(to, from);
			
			addEdgePoint(edgePointFrom);
			addEdgePoint(edgePointTo);

			int edgePointFromIndex = indexOf(edgePointFrom);
			int edgePointToIndex = indexOf(edgePointTo);
			
			indices.add(edgePointToIndex);
			indices.add(edgePointFromIndex);
			
			Edge3D edge = new Edge3D(getIndexAt(face, i), getIndexAt(face, i + 1));
			edgeToEdgePointIndex.put(edge, edgePointToIndex);
		}
		
		addFace(toArray(indices));
	}
	
	private Vector3f calculateEdgePoint(Vector3f from, Vector3f to) {
		return from.subtract(to).mult(amount).add(to);
	}
	
	private void createFacesForVerticesAroundOldVertex() {
		TraverseHelper helper = new TraverseHelper(mesh);
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Edge3D outgoingEdge = helper.getOutgoing(i);
			Edge3D edge = outgoingEdge;
			Vector<Integer> indices = new Vector<Integer>();
			do {
				int index = edgeToEdgePointIndex.get(edge);
				indices.add(index);
				edge = helper.getPairNext(edge.fromIndex, edge.toIndex);
			} while (!outgoingEdge.equals(edge));
			facesToAdd.add(new Face3D(toReverseArray(indices)));
		}
	}

	private int[] toArray(Vector<Integer> values) {
		int[] a = new int[values.size()];
		for (int j = 0; j < a.length; j++) {
			a[j] = values.get(j);
		}
		return a;
	}

	private int[] toReverseArray(Vector<Integer> values) {
		int[] a = new int[values.size()];
		for (int j = 0; j < a.length; j++) {
			int index = a.length - j - 1;
			a[index] = values.get(j);
		}
		return a;
	}
	
	private void clear() {
		verticesToAdd.clear();
		facesToAdd.clear();
		edgeToEdgePointIndex.clear();
	}
	
	private void removeOldVertices() {
		mesh.vertices.clear();
	}
	
	private void removeOldFaces() {
		mesh.faces.clear();
	}
	
	private void addNewVertices() {
		mesh.vertices.addAll(verticesToAdd);
	}
	
	private void addNewFaces() {
		mesh.faces.addAll(facesToAdd);
	}

	private void createEdgePoints() {
		for (Face3D face : mesh.faces)
			createEdgePointsOf(face);
	}
	
	private void addFace(int[] indices) {
		facesToAdd.add(new Face3D(indices));
	}
	
	private int getIndexAt(Face3D face, int i) {
		return face.indices[i % face.indices.length];
	}
	
	private Vector3f getVertexForIndexAt(Face3D face, int i) {
		int index = getIndexAt(face, i);
		return mesh.getVertexAt(index);
	}

	private void addEdgePoint(Vector3f edgePoint) {
		if (!contains(edgePoint))
			verticesToAdd.add(edgePoint);
	}

	private boolean contains(Vector3f v) {
		return verticesToAdd.contains(v);
	}

	private int indexOf(Vector3f v) {
		return verticesToAdd.indexOf(v);
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}

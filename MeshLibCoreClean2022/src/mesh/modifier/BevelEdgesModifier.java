package mesh.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import math.Mathf;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.TraverseHelper;

public class BevelEdgesModifier implements IMeshModifier {

	private float inset;
	
	private float amount;
	
	private WidthType widthType = WidthType.OFFSET;
	
	private Mesh3D mesh;
	
	private List<Face3D> facesToAdd;
	
	private List<Vector3f> verticesToAdd;
	
	private HashMap<Edge3D, Edge3D> oldEdgeToNewEdge;
	
	private HashSet<Edge3D> processed;

	public BevelEdgesModifier(float amount) {
		setAmount(amount);
		processed = new HashSet<>();
		oldEdgeToNewEdge = new HashMap<>();
		verticesToAdd = new ArrayList<Vector3f>();
		facesToAdd = new ArrayList<Face3D>();
	}

	public BevelEdgesModifier() {
		this(0.1f);
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (amount == 0)
			return mesh;
		setMesh(mesh);
		clearAll();
		createInsetFaces();
		createFacesForOldEdges();
		createFacesVertex();
		clearOriginalFaces();
		clearOriginalVertices();
		addNewVertices();
		addNewFaces();
		return mesh;
	}

	private void createInsetFaces() {
		for (Face3D face : mesh.faces)
			insetFace(mesh, face);
	}

	private void insetFace(Mesh3D mesh, Face3D face) {
		int nextVertexIndex = verticesToAdd.size();
		int[] indices = createIndices(face.indices.length, nextVertexIndex);
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		extracted(face, vertices);
		extracted(vertices);
		mapOldEdgesToNewEdges(face, indices);
		addNewFace(indices);
	}

	private void extracted(Face3D face, List<Vector3f> vertices) {
		for (int i = 0; i < face.indices.length; i++) {
			Vector3f from = getVertexAt(face, i);
			Vector3f to = getVertexAt(face, i + 1);
			
			float distance = to.distance(from);
			float a = 1 / distance * getAmountByWidthType();

			Vector3f v4 = to.subtract(from).mult(a).add(from);
			Vector3f v5 = to.add(to.subtract(from).mult(-a));
			
			vertices.add(v4);
			vertices.add(v5);
		}
	}

	private float getAmountByWidthType() {
		float a;
		switch (widthType) {
		case OFFSET:
			// amount is offset of new edges from original
			a = amount * 2;
			break;
		case WIDTH:
			// amount is width of new faces
			a = inset;
			break;
		case DEPTH:
			a = inset * 2;
			break;
		default:
			// default width type offset
			a = amount * 2;
			break;
		}
		return a;
	}

	private void extracted(List<Vector3f> vertices) {
		for (int i = 1; i < vertices.size(); i += 2) {
			int a = vertices.size() - 2 + i;
			Vector3f v0 = vertices.get(a % vertices.size());
			Vector3f v1 = vertices.get((a + 1) % vertices.size());
			Vector3f v = v1.add(v0).mult(0.5f);
			verticesToAdd.add(v);
		}
	}

	private void createFacesVertex() {
		TraverseHelper helper = new TraverseHelper(mesh);
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Edge3D outgoingEdge = helper.getOutgoing(i);
			Edge3D edge = outgoingEdge;
			Vector<Integer> indices = new Vector<Integer>();
			do {
				Edge3D newEdge = oldEdgeToNewEdge.get(edge);
				int index = newEdge.fromIndex;
				indices.add(index);
				edge = helper.getPairNext(edge.fromIndex, edge.toIndex);
			} while (!outgoingEdge.equals(edge));
			facesToAdd.add(new Face3D(toReverseArray(indices)));
		}
	}

	private void createFacesForOldEdges() {
		for (Face3D face : mesh.faces)
			for (int i = 0; i < face.indices.length; i++)
				createFaceForOldEdgeAt(face, i);
	}

	private void createFaceForOldEdgeAt(Face3D face, int i) {
		Edge3D edge = getMappedEdge(createEdgeAt(face.indices, i));
		Edge3D pair = getMappedEdge(createEdgeAt(face.indices, i).createPair());

		if (isProcessed(edge) || isProcessed(pair))
			return;

		addNewFace(new int[] { edge.toIndex, edge.fromIndex, pair.toIndex, pair.fromIndex });

		markAsProcessed(edge);
		markAsProcessed(pair);
	}

	private void mapOldEdgesToNewEdges(Face3D face, int[] indices) {
		for (int i = 0; i < indices.length; i++) {
			Edge3D oldEdge = createEdgeAt(face.indices, i);
			Edge3D newEdge = createEdgeAt(indices, i);
			oldEdgeToNewEdge.put(oldEdge, newEdge);
		}
	}

	private Edge3D getMappedEdge(Edge3D edge) {
		return oldEdgeToNewEdge.get(edge);
	}

	private Edge3D createEdgeAt(int[] indices, int i) {
		Edge3D edge = new Edge3D(indices[i], indices[(i + 1) % indices.length]);
		return edge;
	}

	private int[] createIndices(int size, int nextVertexIndex) {
		int[] indices = new int[size];
		for (int i = 0; i < indices.length; i++)
			indices[i] = nextVertexIndex + i;
		return indices;
	}

	private int[] toReverseArray(Vector<Integer> values) {
		int[] a = new int[values.size()];
		for (int j = 0; j < a.length; j++) {
			int index = a.length - j - 1;
			a[index] = values.get(j);
		}
		return a;
	}

	private void clearAll() {
		processed.clear();
		oldEdgeToNewEdge.clear();
		verticesToAdd.clear();
		facesToAdd.clear();
	}

	private void markAsProcessed(Edge3D edge) {
		processed.add(edge);
	}

	private boolean isProcessed(Edge3D edge) {
		return processed.contains(edge);
	}

	private Vector3f getVertexAt(Face3D face, int index) {
		return mesh.getVertexAt(face.indices[index % face.indices.length]);
	}

	private void addNewVertices() {
		mesh.vertices.addAll(verticesToAdd);
	}

	private void addNewFaces() {
		mesh.faces.addAll(facesToAdd);
	}

	private void clearOriginalVertices() {
		mesh.vertices.clear();
	}

	private void clearOriginalFaces() {
		mesh.faces.clear();
	}

	private void addNewFace(int[] indices) {
		facesToAdd.add(new Face3D(indices));
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public void setAmount(float amount) {
		this.amount = amount;
		updateInset();
	}

	private void updateInset() {
		inset = Mathf.sqrt((amount * amount) + (amount * amount));
	}

	public WidthType getWidthType() {
		return widthType;
	}

	public void setWidthType(WidthType widthType) {
		this.widthType = widthType;
	}

}

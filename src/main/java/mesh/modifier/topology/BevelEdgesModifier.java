package mesh.modifier.topology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.TraverseHelper;

public class BevelEdgesModifier implements IMeshModifier {

    public enum WidthType {

	OFFSET, WIDTH, DEPTH

    }

    private float inset;

    private float amount;

    private WidthType widthType = WidthType.OFFSET;

    private Mesh3D mesh;

    private List<Face3D> facesToAdd;

    private List<Vector3f> verticesToAdd;

    private EdgeProcessor edgeProcessor;

    public BevelEdgesModifier(float amount) {
	setAmount(amount);
	edgeProcessor = new EdgeProcessor();
	verticesToAdd = new ArrayList<Vector3f>();
	facesToAdd = new ArrayList<Face3D>();
    }

    public BevelEdgesModifier() {
	this(0.1f);
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	validateMesh(mesh);
	if (canExitEarly(mesh)) {
	    return mesh;
	}

	setMesh(mesh);
	clearAll();

	insetFaces();

	createFacesForOldEdges();
	createFacesAroundVertices();

	clearOriginalGeometry();
	addNewGeometry();

	return mesh;
    }

    private void createFacesAroundVertices() {
	TraverseHelper helper = new TraverseHelper(mesh);
	for (int i = 0; i < mesh.getVertexCount(); i++) {
	    Edge3D outgoingEdge = helper.getOutgoing(i);
	    Edge3D edge = outgoingEdge;
	    List<Integer> indices = new ArrayList<Integer>();
	    do {
		Edge3D newEdge = edgeProcessor.getMappedEdge(edge);
		int index = newEdge.getFromIndex();
		indices.add(index);
		edge = helper.getPairNext(edge.getFromIndex(), edge.getToIndex());
	    } while (!outgoingEdge.equals(edge));
	    facesToAdd.add(new Face3D(toReverseArray(indices)));
	}
    }

    private void createFacesForOldEdges() {
	for (Face3D face : mesh.getFaces())
	    for (int i = 0; i < face.indices.length; i++)
		createFaceForOldEdgeAt(face, i);
    }

    private void createFaceForOldEdgeAt(Face3D face, int i) {
	EdgeProcessor p = edgeProcessor;
	Edge3D edge = p.getMappedEdge(p.createEdge(face.indices, i));
	Edge3D pair = p.getMappedEdge(p.createEdge(face.indices, i).createPair());

	if (p.isProcessed(edge) || p.isProcessed(pair))
	    return;

	createFaceForEdge(edge, pair);

	p.markProcessed(edge);
	p.markProcessed(pair);
    }

    private void createFaceForEdge(Edge3D edge, Edge3D pair) {
	addNewFace(edge.getToIndex(), edge.getFromIndex(), pair.getToIndex(), pair.getFromIndex());
    }

    private int[] toReverseArray(List<Integer> values) {
	Collections.reverse(values);
	return values.stream().mapToInt(x -> x).toArray();
    }

    private void insetFaces() {
	for (Face3D face : mesh.getFaces())
	    insetFace(mesh, face);
    }

    private void insetFace(Mesh3D mesh, Face3D face) {
	int nextVertexIndex = verticesToAdd.size();
	int[] indices = createIndices(face.indices.length, nextVertexIndex);
	createInsetVertices(processFaceEdges(face));
	edgeProcessor.mapOldEdgesToNewEdges(face, indices);
	addNewFace(indices);
    }

    private int[] createIndices(int size, int nextVertexIndex) {
	int[] indices = new int[size];
	for (int i = 0; i < indices.length; i++)
	    indices[i] = nextVertexIndex + i;
	return indices;
    }

    /**
 * Processes the edges of a face to calculate the new inset vertices.
 *
 * @param face the face to process.
 * @return a list of inset vertices.
 */
    private List<Vector3f> processFaceEdges(Face3D face) {
	List<Vector3f> vertices = new ArrayList<>();
	for (int i = 0; i < face.indices.length; i++) {
	    Vector3f from = getVertexAt(face, i);
	    Vector3f to = getVertexAt(face, i + 1);

	    float edgeLength = to.distance(from);
	    float insetFactor = calculateInsetFactor(edgeLength);

	    Vector3f v4 = to.subtract(from).mult(insetFactor).add(from);
	    Vector3f v5 = to.add(to.subtract(from).mult(-insetFactor));

	    vertices.add(v4);
	    vertices.add(v5);
	}
	return vertices;
    }

    /**
 * Creates the inset vertices from the processed edge vertices.
 *
 * @param vertices the processed edge vertices.
 */
    private void createInsetVertices(List<Vector3f> vertices) {
	for (int i = 1; i < vertices.size(); i += 2) {
	    int a = vertices.size() - 2 + i;
	    Vector3f v0 = vertices.get(a % vertices.size());
	    Vector3f v1 = vertices.get((a + 1) % vertices.size());
	    Vector3f v = v1.add(v0).mult(0.5f);
	    verticesToAdd.add(v);
	}
    }

    /**
 * Calculates the inset factor based on the edge length and
 * #{@link WidthType}.
 *
 * @param edgeLength the length of the edge.
 * @return the inset factor.
 */
    private float calculateInsetFactor(float edgeLength) {
	return edgeLength > 0 ? (1f / edgeLength) * getAmountByWidthType() : 0f;
    }

    private float getAmountByWidthType() {
	return switch (widthType) {
	case OFFSET -> amount * 2;
	case WIDTH -> inset;
	case DEPTH -> inset * 2;
	default -> amount * 2;
	};
    }

    private boolean canExitEarly(Mesh3D mesh) {
	return amount == 0 || mesh.getFaceCount() == 0;
    }

    private void validateMesh(Mesh3D mesh) {
	if (mesh == null) {
	    throw new IllegalArgumentException("Mesh cannot be null.");
	}
    }

    private Vector3f getVertexAt(Face3D face, int index) {
	return mesh.getVertexAt(face.indices[index % face.indices.length]);
    }

    private void clearAll() {
	edgeProcessor.clearAll();
	verticesToAdd.clear();
	facesToAdd.clear();
    }

    private void clearOriginalGeometry() {
	clearOriginalFaces();
	clearOriginalVertices();
    }

    private void addNewGeometry() {
	addNewVertices();
	addNewFaces();
    }

    private void addNewVertices() {
	mesh.addVertices(verticesToAdd);
    }

    private void addNewFaces() {
	mesh.addFaces(facesToAdd);
    }

    private void clearOriginalVertices() {
	mesh.clearVertices();
    }

    private void clearOriginalFaces() {
	mesh.clearFaces();
    }

    private void addNewFace(int... indices) {
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

    private class EdgeProcessor {

	private HashSet<Edge3D> processed;

	private HashMap<Edge3D, Edge3D> edgeMapping;

	public EdgeProcessor() {
	    processed = new HashSet<Edge3D>();
	    edgeMapping = new HashMap<Edge3D, Edge3D>();
	}

	public void mapOldEdgesToNewEdges(Face3D face, int[] indices) {
	    for (int i = 0; i < indices.length; i++) {
		Edge3D oldEdge = createEdge(face.indices, i);
		Edge3D newEdge = createEdge(indices, i);
		edgeMapping.put(oldEdge, newEdge);
	    }
	}

	public Edge3D getMappedEdge(Edge3D edge) {
	    return edgeMapping.get(edge);
	}

	public void markProcessed(Edge3D edge) {
	    processed.add(edge);
	}

	public boolean isProcessed(Edge3D edge) {
	    return processed.contains(edge);
	}

	public Edge3D createEdge(int[] indices, int i) {
	    return new Edge3D(indices[i], indices[(i + 1) % indices.length]);
	}

	public void clearAll() {
	    processed.clear();
	    edgeMapping.clear();
	}

    }

}

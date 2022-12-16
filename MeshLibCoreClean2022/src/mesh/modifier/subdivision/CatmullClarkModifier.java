package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class CatmullClarkModifier implements IMeshModifier {

    private int subdivisions;
    private int originalVertexCount;
    private Mesh3D mesh;
    private List<Face3D> facesToProcess;
    private HashMap<Edge3D, Integer> edgeToEdgePointIndex;
    private HashMap<Integer, VertexData> vertexDataMap;

    public CatmullClarkModifier() {
	this(1);
    }

    public CatmullClarkModifier(int subdivisions) {
	this.subdivisions = subdivisions;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	setMesh(mesh);
	process();
	return mesh;
    }

    private void process() {
	for (int i = 0; i < subdivisions; i++)
	    processOneIteration();
    }

    private void processOneIteration() {
	initialize();
	clearOriginalFaces();
	subdivideFaces();
	smoothVertices();
    }

    private void clearOriginalFaces() {
	mesh.clearFaces();
    }

    private void initialize() {
	originalVertexCount = mesh.getVertexCount();
	facesToProcess = new ArrayList<>(mesh.getFaces());
	edgeToEdgePointIndex = new HashMap<Edge3D, Integer>();
	vertexDataMap = new HashMap<Integer, VertexData>();
    }

    private void subdivideFaces() {
	for (Face3D face : facesToProcess)
	    subdivideFace(face);
    }

    private void subdivideFace(Face3D face) {
	FaceSplit faceSplit = new FaceSplit(face);

	for (int i = 0; i < face.indices.length; i++) {
	    Integer edgePointIndex = getMappedEdgePointIndex(createEdgeAt(face, i));
	    faceSplit.setIndexAt(i + 1, edgePointIndex);
	    setVertexDataAt(faceSplit, i);
	    addFacePointToEdgePointAt(faceSplit.getFacePoint(), edgePointIndex);
	}

	faceSplit.createNewFaces();
    }

    private void setVertexDataAt(FaceSplit faceSplit, int i) {
	Face3D face = faceSplit.getFace();
	VertexData vertexData = getVertexData(face.indices[i]);
	vertexData.incrementNumberOfOutgoingEdges();
	vertexData.addFacePoint(faceSplit.getFacePoint());
	vertexData.addEdgePoint(calculateMidPoint(createEdgeAt(face, i)));
    }

    private void addFacePointToEdgePointAt(Vector3f facePoint, int edgePointIndex) {
	Vector3f edgePoint = mesh.getVertexAt(edgePointIndex);
	edgePoint.addLocal(facePoint.mult(0.25f));
    }

    private Edge3D createEdgeAt(Face3D face, int index) {
	int fromIndex = face.indices[index];
	int toIndex = face.indices[(index + 1) % face.indices.length];
	return new Edge3D(fromIndex, toIndex);
    }

    private Vector3f calculateMidPoint(Edge3D edge) {
	Vector3f f = mesh.getVertexAt(edge.fromIndex);
	Vector3f t = mesh.getVertexAt(edge.toIndex);
	return f.add(t).mult(0.5f);
    }

    private void smoothVertices() {
	for (int i = 0; i < originalVertexCount; i++)
	    getVertexData(i).smoothOriginalVertex();
    }

    private Integer getMappedEdgePointIndex(Edge3D edge) {
	Integer edgePointIndex = edgeToEdgePointIndex.get(edge.createPair());
	if (edgePointIndex == null) {
	    edgePointIndex = addVertex(calculateMidPoint(edge).mult(0.5f));
	    edgeToEdgePointIndex.put(edge, edgePointIndex);
	}
	return edgePointIndex;
    }

    private int addVertex(Vector3f v) {
	int index = mesh.getVertexCount();
	mesh.add(v);
	return index;
    }

    private Vector3f calculateFacePoint(Face3D face) {
	return mesh.calculateFaceCenter(face);
    }

    private VertexData getVertexData(int index) {
	VertexData vertexData = vertexDataMap.get(index);
	if (vertexData == null) {
	    vertexData = new VertexData();
	    vertexData.setIndex(index);
	    vertexDataMap.put(index, vertexData);
	}
	return vertexData;
    }

    private void setMesh(Mesh3D mesh) {
	this.mesh = mesh;
    }

    private class FaceSplit {

	private int[] indices;
	private Face3D face;
	private Vector3f facePoint;

	public FaceSplit(Face3D face) {
	    this.face = face;
	    indices = new int[face.indices.length + 1];
	    facePoint = calculateFacePoint(face);
	    setIndexAt(0, addVertex(facePoint));
	}

	public void setIndexAt(int index, int value) {
	    indices[index] = value;
	}

	private void createNewFaces() {
	    for (int i = 0; i < face.indices.length; i++)
		createFace(i);
	}

	private void createFace(int i) {
	    int originalVertexIndex = face.indices[i];
	    int edgePointIndex0 = indices[i + 1];
	    int facePointIndex = indices[0];
	    int edgePointIndex1 = indices[i == 0 ? face.indices.length : i];
	    addFace(originalVertexIndex, edgePointIndex0, facePointIndex, edgePointIndex1);
	}

	private void addFace(int... indices) {
	    mesh.add(new Face3D(indices));
	}

	public Face3D getFace() {
	    return face;
	}

	public Vector3f getFacePoint() {
	    return facePoint;
	}

    }

    private class VertexData {

	private int index;
	private int numberOfOutgoingEdges;
	private List<Vector3f> facePoints;
	private List<Vector3f> edgePoints;

	public VertexData() {
	    facePoints = new ArrayList<>();
	    edgePoints = new ArrayList<>();
	}

	private void smoothOriginalVertex() {
	    int n = numberOfOutgoingEdges;
	    Vector3f originalVertex = mesh.getVertexAt(index);
	    Vector3f f = calculateFacePointsAverage();
	    Vector3f r = calculateEdgePointsAverage();
	    originalVertex.set(originalVertex.mult(n - 3).add(f.add(r.mult(2))).divide(n));
	}

	private Vector3f calculateAverage(List<Vector3f> vertices) {
	    Vector3f average = new Vector3f();
	    for (Vector3f v : vertices)
		average.addLocal(v);
	    return average.divideLocal(vertices.size());
	}

	private Vector3f calculateFacePointsAverage() {
	    return calculateAverage(facePoints);
	}

	private Vector3f calculateEdgePointsAverage() {
	    return calculateAverage(edgePoints);
	}

	public void addFacePoint(Vector3f facePoint) {
	    facePoints.add(facePoint);
	}

	public void addEdgePoint(Vector3f edgePoint) {
	    edgePoints.add(new Vector3f(edgePoint));
	}

	public void incrementNumberOfOutgoingEdges() {
	    numberOfOutgoingEdges++;
	}

	public void setIndex(int index) {
	    this.index = index;
	}

    }

    public int getSubdivisions() {
	return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
	this.subdivisions = subdivisions;
    }

}

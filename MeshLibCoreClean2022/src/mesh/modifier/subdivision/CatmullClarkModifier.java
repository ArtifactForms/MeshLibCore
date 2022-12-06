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

	private int nextIndex;
	private int subdivisions;
	private int originalVertexCount;
	private Mesh3D mesh;
	private HashMap<Edge3D, Vector3f> edgesToFacepointMap;
	private HashMap<Edge3D, Integer> edgesToEdgePointsMap;
	private HashMap<Integer, Integer> vertexIndexToNumberOfOutgoingEdgesMap;
	private HashMap<Integer, List<Vector3f>> originalVerticesToFacePointsMap;
	private HashMap<Integer, List<Vector3f>> verticesToEdgePointsMap;
	private List<Face3D> facesToAdd;
	private List<Face3D> facesToRemove;

	public CatmullClarkModifier() {
		setSubdivisions(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		setSubdivisions(subdivisions);
	}

	private void smoothVertices() {
		for (int vertexIndex = 0; vertexIndex < originalVertexCount; vertexIndex++) {
			float n = (float) vertexIndexToNumberOfOutgoingEdgesMap.get(vertexIndex);
			Vector3f originalVertex = mesh.vertices.get(vertexIndex);
			Vector3f fpSum = calculateFacePointsAverage(vertexIndex);
			Vector3f epSum = calculateEdgePointAverage(vertexIndex);
			Vector3f v = fpSum.add(epSum.mult(2f).add(originalVertex.mult(n - 3)));
			originalVertex.set(v.divideLocal(n));
		}
	}

	private void processEdgePoints() {
		for (Edge3D edge : edgesToEdgePointsMap.keySet()) {
			int index = edgesToEdgePointsMap.get(edge);
			Vector3f v0 = mesh.vertices.get(edge.fromIndex);
			Vector3f v1 = mesh.vertices.get(edge.toIndex);
			Vector3f fp0 = edgesToFacepointMap.get(edge);
			Vector3f fp1 = edgesToFacepointMap.get(new Edge3D(edge.toIndex, edge.fromIndex));
			if (v0 != null && v1 != null && fp0 != null && fp1 != null) {
				Vector3f ep = v0.add(v1).add(fp0).add(fp1).mult(0.25f);
				mesh.vertices.get(index).set(ep);
			}
		}
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		for (int i = 0; i < subdivisions; i++)
			processOneIteration();
		return mesh;
	}

	private void processOneIteration() {
		setOriginalVertexCount(mesh.getVertexCount());
		initialize();
		subdivideMesh();
		processEdgePoints();
		smoothVertices();
	}

	private void subdivideMesh() {
		nextIndex = originalVertexCount;
		for (Face3D face : mesh.faces) {
			processFace(face);
			removeFace(face);
		}
		removeOldFaces();
		addNewFaces();
	}

	private void processFace(Face3D face) {
		int[] indices = new int[face.indices.length + 1];
		Vector3f facePoint = calculateFaceCenter(face);
		indices[0] = nextIndex;
		addVertex(facePoint);
			
		for (int i = 0; i < face.indices.length; i++) {
			Edge3D edge = createEdgeAt(face, i);
			Vector3f edgePoint = calculateEdgePoint(edge);
			int edgePointIndex = mapEdgeToEdgePointIndex(edge, edgePoint);
			incrementOutgoingEdgesOfVertex(edge);
			mapEdgeToFacePoint(edge, facePoint);
			mapEdgePointByOriginalVertexIndex(face.indices[i], new Vector3f(edgePoint));
			mapFacePointByOriginalVertexIndex(face.indices[i], facePoint);
			indices[i + 1] = edgePointIndex;
		}
		
		createFaces(face, indices);
	}
		
	private Edge3D createEdgeAt(Face3D face, int index) {
		return new Edge3D(face.indices[index % face.indices.length],
				face.indices[(index + 1) % face.indices.length]);
	}

	private int mapEdgeToEdgePointIndex(Edge3D edge, Vector3f edgePoint) {
		int index = -1;
		Integer edgePointIndex = edgesToEdgePointsMap.get(edge.createPair());
		if (edgePointIndex == null) {
			index = nextIndex;
			addVertex(edgePoint);
			edgesToEdgePointsMap.put(edge, index);
		} else {
			index = edgePointIndex;
			edgesToEdgePointsMap.put(edge, edgePointIndex);
		}
		return index;
	}

	private void createFaces(Face3D face, int[] indices) {
		for (int i = 0; i < face.indices.length; i++) {
			int indexA = face.indices[i];
			int indexB = indices[i + 1];
			int facePointIndex = indices[0];
			int indexD = indices[i == 0 ? face.indices.length : i];
			facesToAdd.add(new Face3D(indexA, indexB, facePointIndex, indexD));
		}
	}
	
	private void incrementOutgoingEdgesOfVertex(Edge3D edge) {
		Integer n = vertexIndexToNumberOfOutgoingEdgesMap.get(edge.fromIndex);
		vertexIndexToNumberOfOutgoingEdgesMap.put(edge.fromIndex, n == null ? 1 : n + 1);
	}
	
	private Vector3f calculateFacePointsAverage(int originalVertexIndex) {
		return caluclateAverage(originalVerticesToFacePointsMap.get(originalVertexIndex));
	}

	private Vector3f calculateEdgePointAverage(int originalVertexIndex) {
		return caluclateAverage(verticesToEdgePointsMap.get(originalVertexIndex));
	}
	
	private Vector3f caluclateAverage(List<Vector3f> vertices) {
		Vector3f sum = new Vector3f();
		for (Vector3f v : vertices)
			sum.addLocal(v);
		return sum.mult(1f / (float) vertices.size());
	}

	private Vector3f calculateFaceCenter(Face3D face) {
		Vector3f facePoint = new Vector3f();
		for (int i = 0; i < face.indices.length; i++)
			facePoint.addLocal(mesh.vertices.get(face.indices[i]));
		return facePoint.divideLocal(face.indices.length);
	}

	private Vector3f calculateEdgePoint(Edge3D edge) {
		Vector3f from = mesh.getVertexAt(edge.fromIndex);
		Vector3f to = mesh.getVertexAt(edge.toIndex);
		return from.add(to).mult(0.5f);
	}
	
	private void mapEdgeToFacePoint(Edge3D edge, Vector3f facePoint) {
		edgesToFacepointMap.put(edge, facePoint);
	}

	private void mapEdgePointByOriginalVertexIndex(int vertexIndex, Vector3f edgePoint) {
		List<Vector3f> edgePointsList = verticesToEdgePointsMap.get(vertexIndex);
		if (edgePointsList == null) {
			edgePointsList = new ArrayList<Vector3f>();
			verticesToEdgePointsMap.put(vertexIndex, edgePointsList);
		}
		edgePointsList.add(edgePoint);
	}

	private void mapFacePointByOriginalVertexIndex(int vertexIndex, Vector3f facePoint) {
		List<Vector3f> facepoints = originalVerticesToFacePointsMap.get(vertexIndex);
		if (facepoints == null) {
			facepoints = new ArrayList<Vector3f>();
			originalVerticesToFacePointsMap.put(vertexIndex, facepoints);
		}
		facepoints.add(facePoint);
	}
	
	private void initializeMaps() {
		edgesToFacepointMap = new HashMap<>();
		edgesToEdgePointsMap = new HashMap<>();
		vertexIndexToNumberOfOutgoingEdgesMap = new HashMap<>();
		originalVerticesToFacePointsMap = new HashMap<>();
		verticesToEdgePointsMap = new HashMap<>();
	}
	
	private void initializeFaceLists() {
		facesToAdd = new ArrayList<>();
		facesToRemove = new ArrayList<>();
	}
	
	private void initialize() {
		initializeMaps();
		initializeFaceLists();
	}
	
	private void addVertex(Vector3f v) {
		mesh.vertices.add(v);
		nextIndex++;
	}
	
	private void removeOldFaces() {
		mesh.faces.removeAll(facesToRemove);
	}

	private void addNewFaces() {
		mesh.faces.addAll(facesToAdd);
	}
	
	private void removeFace(Face3D face) {
		facesToRemove.add(face);
	}
	
	private void setOriginalVertexCount(int originalVertexCount) {
		this.originalVertexCount = originalVertexCount;
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}

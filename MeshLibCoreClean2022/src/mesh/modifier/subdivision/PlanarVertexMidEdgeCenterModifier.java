package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class PlanarVertexMidEdgeCenterModifier implements IMeshModifier {

    private int fromIndex;
    private int toIndex;
    private int edgePointIndex;
    private int centerIndex;
    private int nextIndex;
    private Mesh3D mesh;
    private List<Face3D> newlyCreatedFaces;
    private HashMap<Edge3D, Integer> edgeToEdgePointIndexMap;

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	setMesh(mesh);
	initializeNextIndex();
	initializeEdgePointMap();
	initializeFaceList();
	createNewFaces();
	clearOldFaces();
	addNewFaces();
	return mesh;
    }

    private void createNewFaces() {
	for (Face3D face : mesh.getFaces()) {
	    addCenterVertexOfFace(face);
	    processFace(face);
	}
    }

    private void processFace(Face3D face) {
	for (int i = 0; i < face.indices.length; i++) {
	    fromIndex = getIndexAt(i, face);
	    toIndex = getIndexAt(i + 1, face);
	    edgePointIndex = getEdgePointIndex();
	    if (edgePointIndex < 0)
		edgePointIndex = processEdgePoint();
	    addTriangles();
	}
    }

    private int getIndexAt(int i, Face3D face) {
	return face.indices[i % face.indices.length];
    }

    private void addTriangles() {
	addTriangle(centerIndex, fromIndex, edgePointIndex);
	addTriangle(centerIndex, edgePointIndex, toIndex);
    }

    private void addTriangle(int indexA, int indexB, int indexC) {
	newlyCreatedFaces.add(new Face3D(indexA, indexB, indexC));
    }

    private void addCenterVertexOfFace(Face3D face) {
	centerIndex = nextIndex;
	mesh.add(calculateFaceCenter(face));
	nextIndex++;
    }

    private int processEdgePoint() {
	int edgePointIndex = nextIndex;
	Vector3f edgePoint = calculateMidPoint();
	mesh.add(edgePoint);
	map(fromIndex, toIndex, nextIndex);
	nextIndex++;
	return edgePointIndex;
    }

    private int getEdgePointIndex() {
	Edge3D edge = getCurrentEdge();
	Integer index = getIndexOfEdgePoint(edge);
	if (index < 0)
	    index = getIndexOfEdgePoint(edge.createPair());
	return index;
    }

    private Edge3D getCurrentEdge() {
	return new Edge3D(fromIndex, toIndex);
    }

    private int getIndexOfEdgePoint(Edge3D edge) {
	Integer index = edgeToEdgePointIndexMap.get(edge);
	return index == null ? -1 : index;
    }

    private Vector3f calculateMidPoint() {
	Vector3f from = mesh.vertices.get(fromIndex);
	Vector3f to = mesh.vertices.get(toIndex);
	return GeometryUtil.getMidpoint(from, to);
    }

    private Vector3f calculateFaceCenter(Face3D face) {
	return mesh.calculateFaceCenter(face);
    }

    private void clearOldFaces() {
	mesh.faces.clear();
    }

    private void addNewFaces() {
	mesh.faces.addAll(newlyCreatedFaces);
    }

    private void map(int fromIndex, int toIndex, int index) {
	edgeToEdgePointIndexMap.put(new Edge3D(fromIndex, toIndex), index);
    }

    private void initializeEdgePointMap() {
	edgeToEdgePointIndexMap = new HashMap<Edge3D, Integer>();
    }

    private void initializeFaceList() {
	newlyCreatedFaces = new ArrayList<Face3D>();
    }

    private void initializeNextIndex() {
	nextIndex = mesh.getVertexCount();
    }

    private void setMesh(Mesh3D mesh) {
	this.mesh = mesh;
    }

}

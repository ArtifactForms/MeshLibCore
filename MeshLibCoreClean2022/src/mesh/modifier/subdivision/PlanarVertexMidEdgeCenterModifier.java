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

	private int nextIndex;
	private Mesh3D mesh;
	private List<Face3D> toAdd;
	private HashMap<Edge3D, Integer> edgeToEdgePointIndexMap;
	
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		nextIndex = mesh.getVertexCount();
		initializeEdgePointMap();
		initializeFaceList();
		createNewFaces();
		clearOldFaces();
		addNewFaces();
		return mesh;
	}
	
	private void createNewFaces() {
		for (Face3D face : mesh.getFaces()) {
			processFace(face);
		}
	}
	
	private void processFace(Face3D face) {
		int centerIndex = nextIndex;
		Vector3f center = calculateFaceCenter(face);
		mesh.add(center);
		nextIndex++;
		for (int i = 0; i < face.indices.length; i++) {
			int fromIndex = face.indices[i];
			int toIndex = face.indices[(i + 1) % face.indices.length];
			Vector3f from = mesh.vertices.get(fromIndex);
			Vector3f to = mesh.vertices.get(toIndex);
			int edgePointIndex = getEdgePointIndex(fromIndex, toIndex);
			Vector3f edgePoint;
			if (edgePointIndex < 0) {
				edgePoint = calculateEdgePoint(from, to);
				mesh.add(edgePoint);
				map(fromIndex, toIndex, nextIndex);
				edgePointIndex = nextIndex;
				nextIndex++;;
			}
			addTriangle(centerIndex, fromIndex, edgePointIndex);
			addTriangle(centerIndex, edgePointIndex, toIndex);
		}
	}
	
	private Vector3f calculateEdgePoint(Vector3f from, Vector3f to) {
		return GeometryUtil.getMidpoint(from, to);
	}
	
	private Vector3f calculateFaceCenter(Face3D face)  {
		return mesh.calculateFaceCenter(face);
	}
	
	private void addTriangle(int indexA, int indexB, int indexC) {
		toAdd.add(new Face3D(indexA, indexB, indexC));
	}
	
	private void clearOldFaces() {
		mesh.faces.clear();
	}
	
	private void addNewFaces() {
		mesh.faces.addAll(toAdd);
	}
	
	private int getEdgePointIndex(int fromIndex, int toIndex) {
		Integer index = -1;
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		index = edgeToEdgePointIndexMap.get(edge);
		if (index != null)
			return index;
		index = edgeToEdgePointIndexMap.get(edge.createPair());
		if (index != null)
			return index;
		return -1;
	}
	
	private void map(int fromIndex, int toIndex, int index) {
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		edgeToEdgePointIndexMap.put(edge, index);
	}
	
	private void initializeEdgePointMap() {
		edgeToEdgePointIndexMap = new HashMap<Edge3D, Integer>();
	}
	
	private void initializeFaceList() {
		toAdd = new ArrayList<Face3D>();
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}

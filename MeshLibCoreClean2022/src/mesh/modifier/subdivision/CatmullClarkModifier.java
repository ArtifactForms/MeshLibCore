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

/**
 * An implementation of the Catmull-Clark subdivision surface. It was developed
 * in 1978 by Edwin Catmull and Jim Clark.
 * 
 * @version 0.3, 30 June 2016
 * @version 0.4 09 October 2022 Refactoring
 */
public class CatmullClarkModifier implements IMeshModifier {

	private boolean smooth;
	private int subdivisions;
	private int originalVertexCount;
	private Mesh3D mesh;
	private HashMap<Edge3D, Vector3f> edgeToFacePointMap;
	private HashMap<Edge3D, Integer> edgeToEdgePointIndexMap;
	private HashMap<Integer, List<Vector3f>> originalVertexIndexToFacePointsMap;
	private HashMap<Integer, List<Vector3f>> originalVertexIndexToEdgePointsMap;
	private ArrayList<Face3D> facesToAdd = new ArrayList<Face3D>();
	private ArrayList<Face3D> facesToRemove = new ArrayList<Face3D>();

	public CatmullClarkModifier() {
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		this.smooth = true;
		this.subdivisions = subdivisions;
		this.originalVertexCount = 0;
		this.edgeToFacePointMap = new HashMap<Edge3D, Vector3f>();
		this.edgeToEdgePointIndexMap = new HashMap<Edge3D, Integer>();
		this.originalVertexIndexToFacePointsMap = new HashMap<Integer, List<Vector3f>>();
		this.originalVertexIndexToEdgePointsMap = new HashMap<Integer, List<Vector3f>>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		
		for (int i = 0; i < subdivisions; i++)
			oneIteration();

		return mesh;
	}

	private void oneIteration() {
		setOriginalVertexCount(mesh.getVertexCount());
		clear();
		subdivideMesh();
		smoothMesh();
		removeOldFaces();
		addNewFaces();
	}

	private void smoothMesh() {
		if (!smooth)
			return;

		smoothEdgePoints();
		smoothVertices();
	}

	private void clear() {
		facesToRemove.clear();
		facesToAdd.clear();
		edgeToFacePointMap.clear();
		edgeToEdgePointIndexMap.clear();
		originalVertexIndexToFacePointsMap.clear();
		originalVertexIndexToEdgePointsMap.clear();
	}

	private Vector3f calculateFacePointsAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> facePoints = originalVertexIndexToFacePointsMap.get(index);
		for (Vector3f v1 : facePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) facePoints.size());
	}

	private Vector3f calculateEdgePointAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> edgePoints = originalVertexIndexToEdgePointsMap.get(index);
		for (Vector3f v1 : edgePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) edgePoints.size());
	}

	private void smoothVertices() {
		for (int i = 0; i < getOriginalVertexCount(); i++) {
			float n = (float) getNumberOfOutgoingEdgesOfVertexAt(i);
			Vector3f originalVertex = getVertexAt(i);
			Vector3f fpSum = calculateFacePointsAverage(i);
			Vector3f epSum = calculateEdgePointAverage(i);
			if (n > 2) {
				Vector3f v = fpSum.add(epSum.mult(2f).add(originalVertex.mult(n - 3)));
				v.divideLocal(n);
				originalVertex.set(v);
			} else {
				// TODO
			}
		}
	}

	private void smoothEdgePoints() {
		for (Edge3D edge : edgeToEdgePointIndexMap.keySet()) {
			int index = edgeToEdgePointIndexMap.get(edge);
			Vector3f from = getVertexAt(edge.fromIndex);
			Vector3f to = getVertexAt(edge.toIndex);
			Vector3f facePoint0 = getFacePoint(edge);
			Vector3f facePoint1 = getFacePoint(edge.createPair());
			if (from != null && to != null && facePoint0 != null && facePoint1 != null) {
				Vector3f edgePoint = from.add(to).add(facePoint0).add(facePoint1).mult(0.25f);
				getVertexAt(index).set(edgePoint);
			}
		}
	}

	private void subdivideMesh() {
		for (Face3D face : mesh.faces)
			processFace(face);
	}

	private void processFace(Face3D face) {
		int n = face.indices.length;
		int[] indices = new int[n + 1];

		Vector3f facePoint = calculateFaceCenter(face);
		indices[n] = getNextIndex();
		addVertex(facePoint);

		for (int i = 0; i < n; i++) {
			Edge3D edge = createEdgeAt(face, i);
			Vector3f edgePoint = calculateEdgePoint(face, i);

			mapVertexIndexToEdgePoint(face.indices[i], edgePoint);
			mapVertexIndexToFacePoint(face.indices[i], facePoint);
			mapEdgeToFacePoint(edge, facePoint);

			Integer edgePointIndex = edgeToEdgePointIndexMap.get(edge.createPair());
			if (edgePointIndex == null) {
				indices[i] = getNextIndex();
				edgeToEdgePointIndexMap.put(edge, getNextIndex());
				addVertex(edgePoint);
			} else {
				indices[i] = edgePointIndex;
				edgeToEdgePointIndexMap.put(edge, edgePointIndex);
			}
		}

		createNewQuadFacesCenterSplit(face, indices);
		removeFace(face);
	}

	private void removeFace(Face3D face) {
		facesToRemove.add(face);
	}

	private void createNewQuadFacesCenterSplit(Face3D face, int[] indices) {
		for (int i = 0; i < face.indices.length; i++) {
			int originalVertexIndex = face.indices[i];
			int facePointIndex = indices[face.indices.length];
			int edgePointIndex0 = indices[i];
			int edgePointIndex1 = indices[i == 0 ? face.indices.length - 1 : i - 1];

			Face3D f0 = new Face3D(originalVertexIndex, edgePointIndex0, facePointIndex, edgePointIndex1);
			facesToAdd.add(f0);
		}
	}

	private Edge3D createEdgeAt(Face3D face, int index) {
		int n = face.indices.length;
		return new Edge3D(face.indices[index % n], face.indices[(index + 1) % n]);
	}

	private Vector3f calculateFaceCenter(Face3D f) {
		Vector3f facePoint = new Vector3f();
		for (int i = 0; i < f.indices.length; i++)
			facePoint.addLocal(mesh.vertices.get(f.indices[i]));
		return facePoint.divideLocal(f.indices.length);
	}

	private Vector3f calculateEdgePoint(Face3D face, int edgeIndex) {
		Vector3f from = getVertexAt(face, edgeIndex);
		Vector3f to = getVertexAt(face, edgeIndex + 1);
		return GeometryUtil.getMidpoint(from, to);
	}

	private void mapVertexIndexToEdgePoint(int index, Vector3f edgePoint) {
		List<Vector3f> edgePointsList = originalVertexIndexToEdgePointsMap.get(index);
		if (edgePointsList == null) {
			edgePointsList = new ArrayList<Vector3f>();
			originalVertexIndexToEdgePointsMap.put(index, edgePointsList);
		}
		edgePointsList.add(edgePoint);
	}

	private void mapVertexIndexToFacePoint(int index, Vector3f facePoint) {
		List<Vector3f> facePointsList = originalVertexIndexToFacePointsMap.get(index);
		if (facePointsList == null) {
			facePointsList = new ArrayList<Vector3f>();
			originalVertexIndexToFacePointsMap.put(index, facePointsList);
		}
		facePointsList.add(facePoint);
	}

	private void mapEdgeToFacePoint(Edge3D edge, Vector3f facePoint) {
		edgeToFacePointMap.put(edge, facePoint);
	}

	private void addVertex(Vector3f v) {
		mesh.vertices.add(v);
	}

	private Vector3f getFacePoint(Edge3D edge) {
		return edgeToFacePointMap.get(edge);
	}

	private Vector3f getVertexAt(int index) {
		return mesh.vertices.get(index);
	}

	private int getNextIndex() {
		return mesh.vertices.size();
	}

	private Vector3f getVertexAt(Face3D face, int index) {
		return mesh.getVertexAt(face.indices[index % face.indices.length]);
	}

	private int getNumberOfOutgoingEdgesOfVertexAt(int index) {
		return originalVertexIndexToEdgePointsMap.get(index).size();
	}

	private void removeOldFaces() {
		mesh.faces.removeAll(facesToRemove);
	}

	private void addNewFaces() {
		mesh.faces.addAll(facesToAdd);
	}

	private int getOriginalVertexCount() {
		return originalVertexCount;
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

	public boolean isSmooth() {
		return smooth;
	}

	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}

}

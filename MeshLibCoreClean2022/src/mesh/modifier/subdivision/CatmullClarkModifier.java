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

public class CatmullClarkModifier implements IMeshModifier {

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
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		this.subdivisions = subdivisions;
		this.originalVertexCount = 0;
		this.mesh = null;
	}

	private void initializeMaps() {
		edgesToFacepointMap = new HashMap<Edge3D, Vector3f>();
		edgesToEdgePointsMap = new HashMap<Edge3D, Integer>();
		vertexIndexToNumberOfOutgoingEdgesMap = new HashMap<Integer, Integer>();
		originalVerticesToFacePointsMap = new HashMap<Integer, List<Vector3f>>();
		verticesToEdgePointsMap = new HashMap<Integer, List<Vector3f>>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.mesh = mesh;
		for (int i = 0; i < subdivisions; i++) {
			originalVertexCount = mesh.getVertexCount();
			initializeMaps();
			subdivideMesh();
			processEdgePoints();
			smoothVertices();
		}
		return mesh;
	}

	protected Vector3f getFacePointsAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> facePoints = originalVerticesToFacePointsMap.get(index);
		for (Vector3f v1 : facePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) facePoints.size());
	}

	protected Vector3f getEdgePointAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> edgePoints = verticesToEdgePointsMap.get(index);
		for (Vector3f v1 : edgePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) edgePoints.size());
	}

	protected void incrementN(Edge3D[] edges) {
		for (Edge3D edge : edges) {
			Integer n = vertexIndexToNumberOfOutgoingEdgesMap.get(edge.fromIndex);
			if (n == null) {
				n = 0;
			}
			n += 1;
			vertexIndexToNumberOfOutgoingEdgesMap.put(edge.fromIndex, n);
		}
	}

	protected void smoothVertices() {
		for (int i = 0; i < originalVertexCount; i++) {
			float n = (float) vertexIndexToNumberOfOutgoingEdgesMap.get(i);
			Vector3f d = mesh.vertices.get(i);
			Vector3f fpSum = getFacePointsAverage(i);
			Vector3f epSum = getEdgePointAverage(i);
			Vector3f v = fpSum.add(epSum.mult(2f).add(d.mult(n - 3)));
			v.divideLocal(n);
			d.set(v);
		}
	}

	protected void processEdgePoints() {
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

	private void subdivideMesh() {
		facesToAdd = new ArrayList<>();
		facesToRemove = new ArrayList<>();
		int index = mesh.getVertexCount();
		for (Face3D face : mesh.faces) {
			index = processFace(index, face);
		}	
		removeOldFaces();
		addNewFaces();
	}
	
	private void removeOldFaces() {
		mesh.faces.removeAll(facesToRemove);
	}
	
	private void addNewFaces() {
		mesh.faces.addAll(facesToAdd);
	}

	private int processFace(int index, Face3D face) {
		int n = face.indices.length;
		int[] idxs = new int[n + 1];
		Edge3D[] edges = new Edge3D[n];
		Vector3f[] vertices = new Vector3f[n];
		Vector3f[] edgePoints = new Vector3f[n];

		// face point F = average of all points defining the face (center)
		Vector3f fp = new Vector3f();
		for (int i = 0; i < n; i++) {
			Vector3f v = mesh.vertices.get(face.indices[i]);
			fp.addLocal(v);
			vertices[i] = v;
		}
		fp.divideLocal(face.indices.length);
		// store face point
		mesh.vertices.add(fp);
		// face point index
		idxs[0] = index;
		index++;

		// edges of the face
		for (int i = 0; i < n; i++) {
			Edge3D edge = new Edge3D(face.indices[i % n], face.indices[(i + 1) % n]);
			edges[i] = edge;
			// map edges to face point
			edgesToFacepointMap.put(edge, fp);
			// midpoints (edge points)
			edgePoints[i] = GeometryUtil.getMidpoint(vertices[i % n], vertices[(i + 1) % n]);
		}

		// counting edges outgoing from a vertex
		incrementN(edges);

		for (int i = 0; i < n; i++) {
			// adjacent edge already processed?
			Integer epIndex = edgesToEdgePointsMap.get(new Edge3D(edges[i].toIndex, edges[i].fromIndex));
			if (epIndex == null) {
				mesh.vertices.add(edgePoints[i]); // next index + 1
				idxs[i + 1] = index;
				edgesToEdgePointsMap.put(edges[i], index);
				index++;
			} else {
				idxs[i + 1] = epIndex;
				edgesToEdgePointsMap.put(edges[i], epIndex);
			}
		}

		for (int i = 0; i < n; i++) {
			int vIndex = face.indices[i];
			List<Vector3f> facepoints = originalVerticesToFacePointsMap.get(vIndex);
			List<Vector3f> edgePoints2 = verticesToEdgePointsMap.get(vIndex);

			// create new faces
			Face3D f0 = new Face3D(face.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? face.indices.length : i]);
			facesToAdd.add(f0);

			if (facepoints == null) {
				facepoints = new ArrayList<Vector3f>();
				originalVerticesToFacePointsMap.put(vIndex, facepoints);
			}

			if (edgePoints2 == null) {
				edgePoints2 = new ArrayList<Vector3f>();
				verticesToEdgePointsMap.put(vIndex, edgePoints2);
			}

			// map vertices to face point
			facepoints.add(fp);
			// map vertices to edge point
			edgePoints2.add(new Vector3f(edgePoints[i]));
		}

		// remove old face
		facesToRemove.add(face);
		return index;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}

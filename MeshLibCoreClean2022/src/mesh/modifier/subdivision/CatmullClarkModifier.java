package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.Pair;
import mesh.modifier.IMeshModifier;

public class CatmullClarkModifier implements IMeshModifier {

	private int subdivisions;
	private int originalVertexCount;
	private Mesh3D mesh;
	private HashMap<Pair, Vector3f> edgesToFacepointMap;
	private HashMap<Pair, Integer> edgesToEdgePointsMap;
	private HashMap<Integer, Integer> vertexIndexToNumberOfOutgoingEdgesMap;
	private HashMap<Integer, List<Vector3f>> originalVerticesToFacePointsMap;
	private HashMap<Integer, List<Vector3f>> verticesToEdgePointsMap;

	public CatmullClarkModifier() {
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		this.subdivisions = subdivisions;
		this.originalVertexCount = 0;
		this.mesh = null;
		this.edgesToFacepointMap = new HashMap<Pair, Vector3f>();
		this.edgesToEdgePointsMap = new HashMap<Pair, Integer>();
		this.vertexIndexToNumberOfOutgoingEdgesMap = new HashMap<Integer, Integer>();
		this.originalVerticesToFacePointsMap = new HashMap<Integer, List<Vector3f>>();
		this.verticesToEdgePointsMap = new HashMap<Integer, List<Vector3f>>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.mesh = mesh;
		for (int i = 0; i < subdivisions; i++) {
			originalVertexCount = mesh.getVertexCount();
			edgesToFacepointMap.clear();
			edgesToEdgePointsMap.clear();
			vertexIndexToNumberOfOutgoingEdgesMap.clear();
			originalVerticesToFacePointsMap.clear();
			verticesToEdgePointsMap.clear();
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

	protected void incrementN(Pair[] pairs) {
		for (Pair p : pairs) {
			Integer n = vertexIndexToNumberOfOutgoingEdgesMap.get(p.a);
			if (n == null) {
				n = 0;
			}
			n += 1;
			vertexIndexToNumberOfOutgoingEdgesMap.put(p.a, n);
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
		for (Pair p : edgesToEdgePointsMap.keySet()) {
			int index = edgesToEdgePointsMap.get(p);
			Vector3f v0 = mesh.vertices.get(p.a);
			Vector3f v1 = mesh.vertices.get(p.b);
			Vector3f fp0 = edgesToFacepointMap.get(p);
			Vector3f fp1 = edgesToFacepointMap.get(new Pair(p.b, p.a));
			if (v0 != null && v1 != null && fp0 != null && fp1 != null) {
				Vector3f ep = v0.add(v1).add(fp0).add(fp1).mult(0.25f);
				mesh.vertices.get(index).set(ep);
			}
		}
	}

	protected void subdivideMesh() {
		int index = mesh.getVertexCount();
		ArrayList<Face3D> facesToAdd = new ArrayList<Face3D>();
		ArrayList<Face3D> facesToRemove = new ArrayList<Face3D>();

		// for each original face
		for (Face3D f : mesh.faces) {
			int n = f.indices.length;
			int[] idxs = new int[n + 1];
			Pair[] pairs = new Pair[n];
			Vector3f[] vertices = new Vector3f[n];
			Vector3f[] edgePoints = new Vector3f[n];

			// face point F = average of all points defining the face (center)
			Vector3f fp = new Vector3f();
			for (int i = 0; i < n; i++) {
				Vector3f v = mesh.vertices.get(f.indices[i]);
				fp.addLocal(v);
				vertices[i] = v;
			}
			fp.divideLocal(f.indices.length);
			// store face point
			mesh.vertices.add(fp);
			// face point index
			idxs[0] = index;
			index++;

			// edges of the face
			for (int i = 0; i < n; i++) {
				Pair p = new Pair(f.indices[i % n], f.indices[(i + 1) % n]);
				pairs[i] = p;
				// map edges to face point
				edgesToFacepointMap.put(p, fp);
				// midpoints (edge points)
				edgePoints[i] = GeometryUtil.getMidpoint(vertices[i % n], vertices[(i + 1) % n]);
			}

			// counting edges outgoing from a vertex
			incrementN(pairs);

			for (int i = 0; i < n; i++) {
				// adjacent edge already processed?
				Integer epIndex = edgesToEdgePointsMap.get(new Pair(pairs[i].b, pairs[i].a));
				if (epIndex == null) {
					mesh.vertices.add(edgePoints[i]); // next index + 1
					idxs[i + 1] = index;
					edgesToEdgePointsMap.put(pairs[i], index);
					index++;
				} else {
					idxs[i + 1] = epIndex;
					edgesToEdgePointsMap.put(pairs[i], epIndex);
				}
			}

			for (int i = 0; i < n; i++) {
				int vIndex = f.indices[i];
				List<Vector3f> facepoints = originalVerticesToFacePointsMap.get(vIndex);
				List<Vector3f> edgePoints2 = verticesToEdgePointsMap.get(vIndex);

				// create new faces
				Face3D f0 = new Face3D(f.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? f.indices.length : i]);
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
			facesToRemove.add(f);
		}

		// remove old faces from the mesh
		mesh.faces.removeAll(facesToRemove);
		// add all new faces to the mesh
		mesh.faces.addAll(facesToAdd);
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}

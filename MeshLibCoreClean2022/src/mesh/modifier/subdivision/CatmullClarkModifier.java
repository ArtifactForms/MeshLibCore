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

/**
 * An implementation of the Catmull-Clark subdivision surface. It was developed
 * in 1978 by Edwin Catmull and Jim Clark.
 * 
 * @author - Simon Dietz
 * @version 0.3, 30 June 2016
 */
public class CatmullClarkModifier implements IMeshModifier {

	private int subdivisions;

	private int originalVertexCount;

	// the mesh to subdivide
	private Mesh3D mesh;

	// mapping edges to face points
	private HashMap<Pair, Vector3f> edgeFacepointMap;

	// mapping edges to edge point indices
	private HashMap<Pair, Integer> edges;

	// mapping vertex index to number of outgoing edges
	private HashMap<Integer, Integer> nMap;

	// mapping original vertices to face points
	private HashMap<Integer, List<Vector3f>> vertexFacepointMap;

	// mapping vertices to edge points
	private HashMap<Integer, List<Vector3f>> vertexEdgepointMap;

	public CatmullClarkModifier() {
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		super();
		this.subdivisions = subdivisions;
		this.originalVertexCount = 0;
		this.mesh = null; // TODO Decide if we initialize with = new Mesh3D();
		this.edgeFacepointMap = new HashMap<Pair, Vector3f>();
		this.edges = new HashMap<Pair, Integer>();
		this.nMap = new HashMap<Integer, Integer>();
		this.vertexFacepointMap = new HashMap<Integer, List<Vector3f>>();
		this.vertexEdgepointMap = new HashMap<Integer, List<Vector3f>>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.mesh = mesh;
		for (int i = 0; i < subdivisions; i++) {
			originalVertexCount = mesh.getVertexCount();
			edgeFacepointMap.clear();
			edges.clear();
			nMap.clear();
			vertexFacepointMap.clear();
			vertexEdgepointMap.clear();
			subdivideMesh();
			processEdgePoints();
			smoothVertices();
		}
		return mesh;
	}

	protected Vector3f getFacePointsAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> facePoints = vertexFacepointMap.get(index);
		for (Vector3f v1 : facePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) facePoints.size());
	}

	protected Vector3f getEdgePointAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> edgePoints = vertexEdgepointMap.get(index);
		for (Vector3f v1 : edgePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) edgePoints.size());
	}

	protected void incrementN(Pair[] pairs) {
		for (Pair p : pairs) {
			Integer n = nMap.get(p.a);
			if (n == null) {
				n = 0;
			}
			n += 1;
			nMap.put(p.a, n);
		}
	}

	protected void smoothVertices() {
		for (int i = 0; i < originalVertexCount; i++) {
			float n = (float) nMap.get(i);
			Vector3f d = mesh.vertices.get(i);
			Vector3f fpSum = getFacePointsAverage(i);
			Vector3f epSum = getEdgePointAverage(i);
			Vector3f v = fpSum.add(epSum.mult(2f).add(d.mult(n - 3)));
			v.divideLocal(n);
			d.set(v);
		}
	}

	protected void processEdgePoints() {
		for (Pair p : edges.keySet()) {
			int index = edges.get(p);
			Vector3f v0 = mesh.vertices.get(p.a);
			Vector3f v1 = mesh.vertices.get(p.b);
			Vector3f fp0 = edgeFacepointMap.get(p);
			Vector3f fp1 = edgeFacepointMap.get(new Pair(p.b, p.a));
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
				edgeFacepointMap.put(p, fp);
				// midpoints (edge points)
				edgePoints[i] = GeometryUtil.getMidpoint(vertices[i % n], vertices[(i + 1) % n]);
			}

			// counting edges outgoing from a vertex
			incrementN(pairs);

			for (int i = 0; i < n; i++) {
				// adjacent edge already processed?
				Integer epIndex = edges.get(new Pair(pairs[i].b, pairs[i].a));
				if (epIndex == null) {
					mesh.vertices.add(edgePoints[i]); // next index + 1
					idxs[i + 1] = index;
					edges.put(pairs[i], index);
					index++;
				} else {
					idxs[i + 1] = epIndex;
					edges.put(pairs[i], epIndex);
				}
			}

			for (int i = 0; i < n; i++) {
				int vIndex = f.indices[i];
				List<Vector3f> facepoints = vertexFacepointMap.get(vIndex);
				List<Vector3f> edgePoints2 = vertexEdgepointMap.get(vIndex);

				// create new faces
				Face3D f0 = new Face3D(f.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? f.indices.length : i]);
				facesToAdd.add(f0);

				if (facepoints == null) {
					facepoints = new ArrayList<Vector3f>();
					vertexFacepointMap.put(vIndex, facepoints);
				}

				if (edgePoints2 == null) {
					edgePoints2 = new ArrayList<Vector3f>();
					vertexEdgepointMap.put(vIndex, edgePoints2);
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

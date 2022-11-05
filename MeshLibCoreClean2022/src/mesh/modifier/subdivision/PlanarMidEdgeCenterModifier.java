package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.util.Mesh3DUtil;

/**
 * Divides the face (polygon) from its center to the middle of each edge. This
 * modifier works for faces with n vertices. The resulting mesh consists of
 * quads. This class is a new version of the former 'TessellationEdgeModifier' class.
 * 
 * <pre>
 * o-------------------o      o---------o---------o
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      o---------o---------o
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * o-------------------o      o---------o---------o  
 * 
 *           o                          o
 *          / \                        / \
 *         /   \                      /   \
 *        /     \                    /     \
 *       /       \	                /       \
 *      /         \	               o.       .o
 *     /           \              /   .   .   \
 *    /             \            /      o      \
 *   /               \          /       .       \
 *  /                 \        /        .        \
 * o-------------------o      o---------o---------o
 * </pre>
 * 
 * @version 0.1, 20 June 2016
 * @version 0.2  19 October 2022
 */
public class PlanarMidEdgeCenterModifier implements IMeshModifier {
	
	private int nextIndex;
	private int iterations;
	private Mesh3D mesh;
	private ArrayList<Face3D> newFaces = new ArrayList<>();
	private HashMap<Edge3D, Integer> edgePointIndices;
	
	public PlanarMidEdgeCenterModifier() {
		this(1);
	}
	
	public PlanarMidEdgeCenterModifier(int iterations) {
		this.iterations = iterations;
		this.edgePointIndices = new HashMap<Edge3D, Integer>();
	}
	
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		subdivide();
		return mesh;
	}
	
	private void subdivide() {
		for(int i = 0; i < iterations; i++)
			oneIteration();
	}
	
	private void subdivideFaces() {
		for (Face3D f : mesh.faces) {
			int n = f.indices.length;
			int[] idxs = new int[f.indices.length + 1];
			Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, f);
			mesh.vertices.add(center);
			idxs[0] = nextIndex;
			nextIndex++;

			// Create edge points
			for (int i = 0; i < f.indices.length; i++) {
				int from = f.indices[i % n];
				int to = f.indices[((i + 1) % n)];
				Vector3f v0 = mesh.vertices.get(from);
				Vector3f v1 = mesh.vertices.get(to);
				Vector3f ep = GeometryUtil.getMidpoint(v0, v1);
				Edge3D edge = new Edge3D(from, to);
				Integer idx = edgePointIndices.get(edge);
				
				if (idx != null) {
					idxs[i + 1] = idx;
				} else {
					mesh.vertices.add(ep);
					idxs[i + 1] = nextIndex;
					edgePointIndices.put(edge, idx);
					nextIndex++;
				}
			}

			createNewFaces(f, idxs);
		}
	}
	
	private void createNewFaces(Face3D f, int[] idxs) {
		for (int i = 0; i < f.indices.length; i++) {
			Face3D f0 = new Face3D(f.indices[i], idxs[i + 1], idxs[0],
					idxs[i == 0 ? f.indices.length : i]);
			newFaces.add(f0);
		}
	}
	
	private void oneIteration() {
		initializeNextIndex();
		subdivideFaces();
		removeOldFaces();
		addNewFaces();
		clear();
	}
	
	private void initializeNextIndex() {
		nextIndex = mesh.vertices.size();
	}
	
	private void clear() {
		newFaces.clear();
	}
	
	private void addNewFaces() {
		mesh.faces.addAll(newFaces);
	}
	
	private void removeOldFaces() {
		mesh.faces.clear();
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}

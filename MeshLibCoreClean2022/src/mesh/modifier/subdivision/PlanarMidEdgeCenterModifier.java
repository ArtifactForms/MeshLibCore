package mesh.modifier.subdivision;

import java.util.ArrayList;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * Divides the face (polygon) from its center to the middle of each edge. This
 * modifier works for faces with n vertices. The resulting mesh consists of
 * quads. This class is a new version of the former 'TessellationEdgeModifier'
 * class.
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
 * @version 0.2 19 October 2022
 */
public class PlanarMidEdgeCenterModifier implements IMeshModifier {

	private int nextIndex;
	private int iterations;
	private Mesh3D mesh;
	private ArrayList<Face3D> newFaces = new ArrayList<>();

	public PlanarMidEdgeCenterModifier() {
		this(1);
	}

	public PlanarMidEdgeCenterModifier(int iterations) {
		this.iterations = iterations;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		subdivide();
		return mesh;
	}

	private void subdivide() {
		for (int i = 0; i < iterations; i++)
			oneIteration();
	}

	private void subdivideFaces() {
		for (Face3D f : mesh.faces) {
			int n = f.indices.length;
			int[] idxs = new int[f.indices.length + 1];
			Vector3f center = mesh.calculateFaceCenter(f);
			mesh.vertices.add(center);
			idxs[0] = nextIndex;
			nextIndex++;

			// Create edge points
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f from = mesh.vertices.get(f.indices[i % n]);
				Vector3f to = mesh.vertices.get(f.indices[(i + 1) % n]);
				Vector3f edgePoint = from.add(to).mult(0.5f);
				int idx = mesh.vertices.indexOf(edgePoint);
				if (idx > -1) {
					idxs[i + 1] = idx;
				} else {
					mesh.vertices.add(edgePoint);
					idxs[i + 1] = nextIndex;
					nextIndex++;
				}
			}

			createNewFaces(f, idxs);
		}
	}

	private void createNewFaces(Face3D f, int[] idxs) {
		for (int i = 0; i < f.indices.length; i++) {
			Face3D f0 = new Face3D(f.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? f.indices.length : i]);
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

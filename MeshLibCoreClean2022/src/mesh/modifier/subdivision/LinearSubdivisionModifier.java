package mesh.modifier.subdivision;

import java.util.ArrayList;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * This modifier implements the Linear/Polyhedral Subdivision Scheme. This
 * scheme just subdivides the existing faces in the mesh into more faces. It
 * does not produce a smooth surface. This kind of subdivision scheme could be
 * used for two-pass schemes where we first need to produce new vertices in the
 * mesh before any smoothing is done. If the base mesh contains any non-convex
 * polygons then this modifier produces unexpected results.
 * 
 * <pre>
 *     o                 o
 *    / \               / \ 
 *   /   \    ---->    o---o
 *  /     \           / \ / \
 * o-------o         o---o---o
 * 
 * o-------o         o---o---o
 * |       |         |   |   |
 * |       |  ---->  o---o---o
 * |       |         |   |   |
 * o-------o         o---o---o
 * </pre>
 */
public class LinearSubdivisionModifier implements IMeshModifier {

	private int iterations;
	private int[] indices;
	private Face3D face;
	private Mesh3D mesh;
	private ArrayList<Face3D> newFaces;

	public LinearSubdivisionModifier() {
		this(1);
	}

	public LinearSubdivisionModifier(int iterations) {
		this.iterations = iterations;
		newFaces = new ArrayList<Face3D>();
	}

	private void createFaceCenter() {
		if (face.indices.length <= 3)
			return;
		Vector3f center = mesh.calculateFaceCenter(face);
		indices[0] = getNextIndex();
		mesh.add(center);
	}

	private void createEdgePoints() {
		int n = face.indices.length;
		for (int i = 0; i < n; i++) {
			Vector3f from = mesh.getVertexAt(face.indices[i % n]);
			Vector3f to = mesh.getVertexAt(face.indices[(i + 1) % n]);
			Vector3f edgePoint = from.add(to).mult(0.5f);
			int idx = mesh.vertices.indexOf(edgePoint);
			if (idx > -1) {
				indices[i + 1] = idx;
			} else {
				indices[i + 1] = getNextIndex();
				mesh.add(edgePoint);
			}
		}
	}

	private void oneToFourTriangleSplit() {
		addFace(indices[1], indices[2], indices[3]);
		addFace(face.indices[0], indices[1], indices[3]);
		addFace(face.indices[1], indices[2], indices[1]);
		addFace(face.indices[2], indices[3], indices[2]);
	}
	
	private void addFace(int... indices) {
		newFaces.add(new Face3D(indices));
	}

	private void centerSplit() {
		for (int i = 0; i < face.indices.length; i++) {
			int index0 = face.indices[i];
			int index1 = indices[i + 1];
			int index2 = indices[0];
			int index3 = indices[i == 0 ? face.indices.length : i];
			addFace(index0, index1, index2, index3);
		}
	}

	private void createFaces() {
		if (face.indices.length == 3) {
			oneToFourTriangleSplit();
		} else {
			centerSplit();
		}
	}

	private void applyFaces() {
		mesh.faces.clear();
		mesh.addFaces(newFaces);
		newFaces.clear();
	}

	private void subdivide() {
		for (Face3D face : mesh.getFaces()) {
			this.face = face;
			indices = new int[face.indices.length + 1];
			createFaceCenter();
			createEdgePoints();
			createFaces();
		}
		applyFaces();
	}
	
	private int getNextIndex() {
		return mesh.getVertexCount();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		for (int i = 0; i < iterations; i++)
			subdivide();
		return mesh;
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}

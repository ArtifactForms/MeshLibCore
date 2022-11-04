package mesh.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.Pair;
import mesh.wip.Mesh3DUtil;

public class SolidifyModifier implements IMeshModifier {

	private float thickness;

	public SolidifyModifier() {
		super();
		this.thickness = 0.01f;
	}

	public SolidifyModifier(float thickness) {
		super();
		this.thickness = thickness;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (thickness == 0)
			return mesh;
		
		Mesh3D m0 = null;
		Mesh3D copy = mesh.copy();

		// Map vertices to face normals.
		// Store the face normal of each face the vertex belongs to.
		HashMap<Vector3f, List<Vector3f>> map = new HashMap<>();
		List<Vector3f> vertexNormals = new ArrayList<Vector3f>();
		HashSet<Pair> pairs = new HashSet<>();
		
		for (Face3D f : mesh.faces) {
			int size = f.indices.length;
			// Calculate the face normal.
			Vector3f n = Mesh3DUtil.calculateFaceNormal(mesh, f);
			// For each vertex of the face.
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				List<Vector3f> list = map.get(v);
				if (list == null) {
					list = new ArrayList<Vector3f>();
					map.put(v, list);
				}
				list.add(n);
				// Map edge.
				Pair pair = new Pair(f.indices[i], f.indices[(i + 1) % size]);
				pairs.add(pair);
			}
		}

		// Calculate vertex normals.
		for (Vector3f v : mesh.vertices) {
			Vector3f n = new Vector3f();
			List<Vector3f> list = map.get(v);
			if (list == null) {
				list = new ArrayList<Vector3f>();
			}
			for (Vector3f v0 : list) {
				n.addLocal(v0);
			}
			n.divideLocal(list.size());
			n.normalizeLocal();
			vertexNormals.add(n);
		}

		// Flip inner mesh.
		for (Face3D f : copy.faces) {
			Mesh3DUtil.flipDirection(mesh, f);
		}

		// Combine meshes.
		m0 = Mesh3DUtil.append(mesh, copy);

		// Move vertices along the vertex normals.
		for (int i = 0; i < copy.vertices.size(); i++) {
			Vector3f v = copy.getVertexAt(i);
			Vector3f n = vertexNormals.get(i);
			v.set(n.mult(-thickness).add(v));
		}

		// Bridge holes if any.
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D f : faces) {
			int size = f.indices.length;
			for (int i = 0; i < f.indices.length; i++) {
				Pair pair0 = new Pair(f.indices[i], f.indices[(i + 1) % size]);
				Pair pair1 = new Pair(f.indices[(i + 1) % size], f.indices[i]);
				if (!pairs.contains(pair1)) {
					Vector3f v0 = copy.getVertexAt(pair0.a);
					Vector3f v1 = copy.getVertexAt(pair0.b);
					Vector3f v2 = mesh.getVertexAt(pair0.a);
					Vector3f v3 = mesh.getVertexAt(pair0.b);
					Mesh3DUtil.bridge(m0, v0, v1, v2, v3);
				}
			}
		}

		mesh.vertices.clear();
		mesh.faces.clear();
		mesh.addVertices(m0.vertices);
		mesh.addFaces(m0.faces);

		return mesh;
	}

}

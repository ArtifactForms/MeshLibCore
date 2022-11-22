package mesh.modifier;

import java.util.HashSet;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.Pair;
import mesh.util.Mesh3DUtil;
import mesh.util.VertexNormals;

public class SolidifyModifier implements IMeshModifier {

	private float thickness;
	private List<Vector3f> vertexNormals;

	public SolidifyModifier() {
		this.thickness = 0.01f;
	}

	public SolidifyModifier(float thickness) {
		this.thickness = thickness;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (thickness == 0)
			return mesh;
		
		Mesh3D m0 = new Mesh3D();
		Mesh3D copy = mesh.copy();

		vertexNormals = calculateVertexNormals(mesh);
		HashSet<Pair> pairs = new HashSet<>();
		
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				// Map edge.
				Pair pair = new Pair(f.indices[i], f.indices[(i + 1) % f.indices.length]);
				pairs.add(pair);
			}
		}

		// Flip inner mesh.
		for (Face3D f : copy.faces) {
			Mesh3DUtil.flipDirection(mesh, f);
		}

		// Combine meshes.
		m0.append(mesh, copy);

		moveAlongVertexNormals(copy);

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
	
	private void moveAlongVertexNormals(Mesh3D mesh) {
		for (int i = 0; i < mesh.vertices.size(); i++) {
			Vector3f v = mesh.getVertexAt(i);
			Vector3f n = vertexNormals.get(i);
			v.set(n.mult(-thickness).add(v));
		}
	}
	
	private List<Vector3f> calculateVertexNormals(Mesh3D mesh) {
		return new VertexNormals(mesh).getVertexNormals();
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

}

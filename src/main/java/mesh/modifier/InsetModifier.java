package mesh.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class InsetModifier implements IMeshModifier {

	private float inset;

	public InsetModifier(float inset) {
		this.inset = inset;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		modify(mesh, mesh.getFaces());
		return mesh;
	}

	public void modify(Mesh3D mesh, Collection<Face3D> faces) {
		for (Face3D face : faces)
			modify(mesh, face);
	}

	public void modify(Mesh3D mesh, Face3D face) {
		int n = face.indices.length;
		int idx = mesh.vertices.size();

		List<Vector3f> verts = new ArrayList<Vector3f>();

		for (int i = 0; i < n; i++) {
			Vector3f v0 = mesh.vertices.get(face.indices[i]);
			Vector3f v1 = mesh.vertices.get(face.indices[(i + 1) % face.indices.length]);

			float distance = v0.distance(v1);
			float a = (1f / distance) * inset;

			Vector3f v4 = v1.subtract(v0).mult(a).add(v0);
			Vector3f v5 = v1.add(v1.subtract(v0).mult(-a));

			verts.add(v4);
			verts.add(v5);
		}

		for (int i = 1; i < verts.size(); i += 2) {
			int a = verts.size() - 2 + i;
			Vector3f v0 = verts.get(a % verts.size());
			Vector3f v1 = verts.get((a + 1) % verts.size());
			Vector3f v = v1.add(v0).mult(0.5f);
			mesh.add(v);
		}

		for (int i = 0; i < n; i++) {
			int index0 = face.indices[i];
			int index1 = face.indices[(i + 1) % n];
			int index2 = idx + ((i + 1) % n);
			int index3 = idx + i;
			mesh.addFace(index0, index1, index2, index3);
		}

		for (int i = 0; i < n; i++) {
			face.indices[i] = idx + i;
		}
	}

	public float getInset() {
		return inset;
	}

	public void setInset(float inset) {
		this.inset = inset;
	}

}

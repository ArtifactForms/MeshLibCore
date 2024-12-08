package mesh.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class InsetModifier implements IMeshModifier, FaceModifier {

	private static final float DEFAULT_INSET = 0.1f;

	private int nextIndex;

	private float inset;

	private Mesh3D mesh;

	public InsetModifier() {
		this(DEFAULT_INSET);
	}

	public InsetModifier(float inset) {
		this.inset = inset;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		modify(mesh, mesh.getFaces());
		return mesh;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (faces == null) {
			throw new IllegalArgumentException("Faces cannot be null.");
		}
		setMesh(mesh);
		for (Face3D face : faces) {
			processFace(face);
		}
		return mesh;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (face == null) {
			throw new IllegalArgumentException("Face cannot be null.");
		}
		setMesh(mesh);
		processFace(face);
		return mesh;
	}

	private List<Vector3f> processFaceEdges(Face3D face) {
		List<Vector3f> verts = new ArrayList<>();
		for (int i = 0; i < face.indices.length; i++) {
			int index0 = face.indices[i];
			int index1 = face.indices[(i + 1) % face.indices.length];

			Vector3f v0 = mesh.vertices.get(index0);
			Vector3f v1 = mesh.vertices.get(index1);

			float edgeLength = v0.distance(v1);
			float insetFactor = calculateInsetFactor(edgeLength);

			Vector3f v4 = v1.subtract(v0).mult(insetFactor).add(v0);
			Vector3f v5 = v1.add(v1.subtract(v0).mult(-insetFactor));

			verts.add(v4);
			verts.add(v5);
		}
		return verts;
	}

	private void processFace(Face3D face) {
		updateNextIndex();
		createInsetVertices(processFaceEdges(face));
		for (int i = 0; i < face.getVertexCount(); i++) {
			createFaceAt(face, i);
		}
		for (int i = 0; i < face.getVertexCount(); i++) {
			face.indices[i] = nextIndex + i;
		}
	}

	private void createInsetVertices(List<Vector3f> vertices) {
		for (int i = 1; i < vertices.size(); i += 2) {
			int a = vertices.size() - 2 + i;
			Vector3f v0 = vertices.get(a % vertices.size());
			Vector3f v1 = vertices.get((a + 1) % vertices.size());
			Vector3f v = GeometryUtil.getMidpoint(v0, v1);
			mesh.add(v);
		}
	}

	private void createFaceAt(Face3D face, int i) {
		int n = face.indices.length;
		int index0 = face.indices[i];
		int index1 = face.indices[(i + 1) % n];
		int index2 = nextIndex + ((i + 1) % n);
		int index3 = nextIndex + i;
		mesh.addFace(index0, index1, index2, index3);
	}

	private float calculateInsetFactor(float edgeLength) {
		return edgeLength > 0 ? (1f / edgeLength) * inset : 0f;
	}

	private void updateNextIndex() {
		nextIndex = mesh.vertices.size();
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getInset() {
		return inset;
	}

	public void setInset(float inset) {
		this.inset = inset;
	}

}

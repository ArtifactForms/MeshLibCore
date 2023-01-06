package mesh.modifier.subdivision;

import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class PokeFacesModifier implements IMeshModifier {

	private float pokeOffset;
	private Mesh3D mesh;

	public PokeFacesModifier() {
		this.pokeOffset = 0.1f;
	}

	public PokeFacesModifier(float pokeOffset) {
		this.pokeOffset = pokeOffset;
	}

	private void createFaces(int index, Face3D face) {
		int vertexCount = face.indices.length;
		for (int i = 0; i < vertexCount; i++) {
			int index0 = index;
			int index1 = face.indices[i % vertexCount];
			int index2 = face.indices[(i + 1) % vertexCount];
			Face3D f = new Face3D(index0, index1, index2);
			mesh.add(f);
		}
	}

	private void createVertex(Face3D face) {
		Vector3f center = mesh.calculateFaceCenter(face);
		Vector3f normal = mesh.calculateFaceNormal(face);
		center.addLocal(normal.multLocal(pokeOffset));
		mesh.add(center);
	}

	private void extrude() {
		int index = mesh.getVertexCount();
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D face : faces) {
			createVertex(face);
			createFaces(index, face);
			index++;
		}
		mesh.faces.removeAll(faces);
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.mesh = mesh;
		extrude();
		return mesh;
	}

	public float getPokeOffset() {
		return pokeOffset;
	}

	public void setPokeOffset(float pokeOffset) {
		this.pokeOffset = pokeOffset;
	}

}

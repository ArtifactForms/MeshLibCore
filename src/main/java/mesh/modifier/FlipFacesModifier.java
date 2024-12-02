package mesh.modifier;

import java.util.Arrays;
import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;

public class FlipFacesModifier implements IMeshModifier, FaceModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		modify(mesh, mesh.faces);
		return mesh;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		invertFaceIndices(mesh, face);
		return mesh;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		for (Face3D face : faces)
			invertFaceIndices(mesh, face);
		return mesh;
	}

	private void invertFaceIndices(Mesh3D mesh, Face3D face) {
		int[] copy = Arrays.copyOf(face.indices, face.indices.length);
		for (int i = 0; i < face.indices.length; i++) {
			face.indices[i] = copy[face.indices.length - 1 - i];
		}
	}

}

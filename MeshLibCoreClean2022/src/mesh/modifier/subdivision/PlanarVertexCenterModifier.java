package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * Subdivides each face of a mesh by adding a new vertex at the center of the
 * face and connecting it to the existing vertices, creating triangular faces.
 */
public class PlanarVertexCenterModifier implements IMeshModifier {

	private Mesh3D mesh;
	private List<Face3D> newFaces;

	public PlanarVertexCenterModifier() {
		newFaces = new ArrayList<>();
	}
	
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		return modify(mesh, mesh.getFaces());
	}

	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		List<Face3D> facesToSubdivide = new ArrayList<>();
		facesToSubdivide.add(face);
		return modify(mesh, facesToSubdivide);
	}

	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> facesToSubdivide) {
		clear();
		setMesh(mesh);
		subdivideFaces(facesToSubdivide);
		addNewFaces();
		return mesh;
	}

	private void subdivideFaces(Collection<Face3D> faces) {
		for (Face3D face : faces) {
			subdivideFace(face);
			removeFaceFromMesh(face);
		}
	}

	private void subdivideFace(Face3D face) {
		int vertexCount = face.getVertexCount();
		int newVertexIndex = addVertexToMesh(calculateFaceCenter(face));

		for (int i = 0; i < vertexCount; i++) {
			int vertexIndexA = face.getIndexAt(i);
			int vertexIndexB = face.getIndexAt((i + 1) % vertexCount);
			addNewTriangularFace(vertexIndexA, vertexIndexB, newVertexIndex);
		}
	}
	
	private void clear() {
		newFaces.clear();
	}
	
	private void addNewTriangularFace(int index0, int index1, int index2) {
		newFaces.add(new Face3D(index0, index1, index2));
	}

	private void addNewFaces() {
		mesh.faces.addAll(newFaces);
	}

	private int addVertexToMesh(Vector3f vertex) {
		mesh.add(vertex);
		return mesh.getVertexCount() - 1;
	}

	private void removeFaceFromMesh(Face3D face) {
		mesh.removeFace(face);
	}

	private Vector3f calculateFaceCenter(Face3D face) {
		return mesh.calculateFaceCenter(face);
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}
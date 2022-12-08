package mesh.modifier;

import java.util.HashSet;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class RemoveDoubleVerticesModifier implements IMeshModifier {
	
	private Mesh3D temporaryMesh;
	private Mesh3D mesh;
	private HashSet<Vector3f> vertexSet;
	
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		initializeTmpMesh();
		initializeVertexSet();
		hashVertices();
		addAllVerticesFromSetToTmpMesh();
		createNewFaces();
		clearOldVertices();
		clearOldFaces();
		addVertices();
		addFaces();
		return mesh;
	}
	
	private void createNewFaces() {
		for (Face3D face : mesh.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v = getVertexAt(face.indices[i]);
				int index = temporaryMesh.vertices.indexOf(v);
				face.indices[i] = index;
			}
			temporaryMesh.add(face);
		}
	}
	
	private void hashVertices() {
		for (Face3D face : mesh.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				vertexSet.add(getVertexAt(face.indices[i]));
			}
		}
	}
	
	private void addVertices() {
		mesh.addVertices(temporaryMesh.vertices);
	}
	
	private void addFaces() {
		mesh.addFaces(temporaryMesh.faces);
	}
	
	private void addAllVerticesFromSetToTmpMesh() {
		temporaryMesh.vertices.addAll(vertexSet);
	}
	
	private void initializeVertexSet() {
		vertexSet = new HashSet<Vector3f>();
	}
	
	private void initializeTmpMesh() {
		temporaryMesh = new Mesh3D();
	}
	
	private Vector3f getVertexAt(int index) {
		return mesh.getVertexAt(index);
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}
	
	private void clearOldVertices() {
		mesh.clearVertices();
	}
	
	private void clearOldFaces() {
		mesh.clearFaces();
	}
	
}

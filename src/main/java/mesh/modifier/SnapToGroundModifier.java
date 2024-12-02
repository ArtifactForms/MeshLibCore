package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

public class SnapToGroundModifier implements IMeshModifier {

	private float groundLevel;

	private float distanceToGround;

	private Mesh3D mesh;

	public SnapToGroundModifier() {
		this(0);
	}

	public SnapToGroundModifier(float groundLevel) {
		this.groundLevel = groundLevel;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh.vertices.isEmpty())
			return mesh;
		
		setMesh(mesh);
		calculateDistanceToGround();
		translateMesh();
		return mesh;
	}

	private void translateMesh() {
		for (Vector3f vertex : mesh.getVertices()) {
			vertex.y += distanceToGround;
		}
	}

	private void calculateDistanceToGround() {
		float maxHeight = findHighestPoint();
		distanceToGround = groundLevel - maxHeight;
	}

	private float findHighestPoint() {
		float max = mesh.getVertexAt(0).y;
		for (Vector3f vertex : mesh.getVertices()) {
			max = vertex.y > max ? vertex.y : max;
		}
		return max;
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getGroundLevel() {
		return groundLevel;
	}

	public void setGroundLevel(float groundLevel) {
		this.groundLevel = groundLevel;
	}

}

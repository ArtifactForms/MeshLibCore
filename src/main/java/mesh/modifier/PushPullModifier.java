package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

public class PushPullModifier implements IMeshModifier {

	private float distance;

	private Vector3f center;

	private Mesh3D mesh;

	public PushPullModifier() {
		this(0, Vector3f.ZERO);
	}

	public PushPullModifier(float distance, Vector3f center) {
		this.distance = distance;
		this.center = center;
	}

	private void pushPullVertices() {
		for (Vector3f vertex : mesh.vertices)
			pushPullVertex(vertex);
	}

	private void pushPullVertex(Vector3f vertex) {
		float distanceToCenter = vertex.distance(center);
		float displacement = distance - distanceToCenter;
		Vector3f directionToCenter = vertex.subtract(center).normalize();
		vertex.set(directionToCenter.mult(displacement).add(center));
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		pushPullVertices();
		return mesh;
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Vector3f getCenter() {
		return center;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}

}

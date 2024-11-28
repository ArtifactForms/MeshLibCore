package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

public class PushPullModifier implements IMeshModifier {

	private float distance;

	private Vector3f center;

	public PushPullModifier() {
		this(0, Vector3f.ZERO);
	}

	public PushPullModifier(float distance, Vector3f center) {
		this.distance = distance;
		this.center = center;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		for (Vector3f vertex : mesh.vertices) {
			float distanceToCenter = vertex.distance(center);
			float displacement = this.distance - distanceToCenter;
			Vector3f directionToCenter = vertex.subtract(center).normalize();
			vertex.set(directionToCenter.mult(displacement).add(center));
		}
		return mesh;
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

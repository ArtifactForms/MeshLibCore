package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

public class TranslateModifier implements IMeshModifier {

	private float deltaX;
	
	private float deltaY;
	
	private float deltaZ;

	public TranslateModifier() {
		this(0, 0, 0);
	}
	
	public TranslateModifier(Vector3f delta) {
		this(delta.x, delta.y, delta.z);
	}

	public TranslateModifier(float deltaX, float deltaY, float deltaZ) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaZ = deltaZ;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		for (Vector3f v : mesh.vertices)
			v.addLocal(deltaX, deltaY, deltaZ);
		return mesh;
	}

	public float getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(float deltaX) {
		this.deltaX = deltaX;
	}

	public float getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(float deltaY) {
		this.deltaY = deltaY;
	}

	public float getDeltaZ() {
		return deltaZ;
	}

	public void setDeltaZ(float deltaZ) {
		this.deltaZ = deltaZ;
	}
	
}

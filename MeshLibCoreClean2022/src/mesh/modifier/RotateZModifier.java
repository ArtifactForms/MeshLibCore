package mesh.modifier;

import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.Mesh3D;

public class RotateZModifier implements IMeshModifier {

	private float angle;
	
	private Mesh3D mesh;
	
	private Matrix3f rotationMatrix;

	public RotateZModifier() {
		this(0);
	}

	public RotateZModifier(float angle) {
		this.angle = angle;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		createRotationMatrix();
		rotateMesh();
		return mesh;
	}

	private void createRotationMatrix() {
		rotationMatrix = new Matrix3f(Mathf.cos(angle), -Mathf.sin(angle), 0, Mathf.sin(angle), Mathf.cos(angle), 0, 0,
				0, 1);
	}

	private void rotateMesh() {
		for (Vector3f v : mesh.vertices)
			v.multLocal(rotationMatrix);
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}

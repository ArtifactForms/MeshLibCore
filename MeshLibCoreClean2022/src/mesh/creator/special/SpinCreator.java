package mesh.creator.special;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SpinCreator implements IMeshCreator {

	private int steps;
	private float angle;
	private boolean close;
	private Mesh3D mesh;
	private List<Vector3f> vertices;

	public SpinCreator() {
		this.steps = 9;
		this.angle = Mathf.QUARTER_PI;
		this.vertices = new ArrayList<Vector3f>();
	}

	public SpinCreator(int steps, float angle, boolean close) {
		this.steps = steps;
		this.angle = angle;
		this.close = close;
		this.vertices = new ArrayList<Vector3f>();
	}

	public void add(Vector3f v) {
		if (v != null)
			vertices.add(v);
	}

	public void addAll(Collection<Vector3f> vertices) {
		for (Vector3f v : vertices)
			if (v != null)
				this.vertices.add(v);
	}
	
	public void clearVertices() {
		vertices.clear();
	}

	private void createVertices() {
		float angle = 0;
		float angleStep = -this.angle / (float) (steps - 1);
		for (int j = 0; j < steps; j++) {
			for (int i = 0; i < vertices.size(); i++) {
				Vector3f v0 = vertices.get(i);
				Vector3f v1 = new Vector3f(Mathf.cos(angle), 0,
						Mathf.sin(angle));
				v1 = v1.multLocal(new Vector3f(v0.x, 1, v0.x));
				v1.addLocal(0, v0.y, 0);
				mesh.add(v1);
			}
			angle += angleStep;
		}
	}

	private void createFaces() {
		int n = vertices.size();
		int m = close ? steps : steps - 1;
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n - 1; i++) {
				mesh.add(createQuadFace(j, i, n));
			}
		}
	}

	private Face3D createQuadFace(int j, int i, int n) {
		Face3D face = new Face3D(new int[4]);
		face.indices[3] = ((j + 1) % steps * n) + i;
		face.indices[2] = ((j + 1) % steps * n) + i + 1;
		face.indices[1] = (j * n) + i + 1;
		face.indices[0] = (j * n) + i;
		return face;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

}

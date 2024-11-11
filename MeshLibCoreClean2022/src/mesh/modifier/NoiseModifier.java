package mesh.modifier;

import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

public class NoiseModifier implements IMeshModifier {

	private float minimum;
	
	private float maximum;
	
	private Mesh3D mesh;
	
	private List<Vector3f> vertexNormals;
	
	public NoiseModifier() {
		this(0.0f, 1.0f);
	}

	public NoiseModifier(float minimum, float maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		calculateVertexNormals();
		applyNoise();
		return mesh;
	}

	private void applyNoise() {
		for (int i = 0; i < getVertexCount(); i++) {
			float length = createRandomValue();
			Vector3f vertex = getVertexAt(i);
			Vector3f normal = getVertexNormalAt(i);
			vertex.addLocal(normal.mult(length));
		}
	}

	private Vector3f getVertexAt(int index) {
		return mesh.getVertexAt(index);
	}
	
	private Vector3f getVertexNormalAt(int index) {
		return vertexNormals.get(index);
	}

	private int getVertexCount() {
		return mesh.vertices.size();
	}

	private void calculateVertexNormals() {
		vertexNormals = new VertexNormals(mesh).getVertexNormals();
	}

	private float createRandomValue() {
		return Mathf.random(minimum, maximum);
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getMinimum() {
		return minimum;
	}

	public void setMinimum(float minimum) {
		this.minimum = minimum;
	}

	public float getMaximum() {
		return maximum;
	}

	public void setMaximum(float maximum) {
		this.maximum = maximum;
	}

}

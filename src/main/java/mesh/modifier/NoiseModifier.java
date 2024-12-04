package mesh.modifier;

import java.util.List;
import java.util.Random;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

public class NoiseModifier implements IMeshModifier {

	private float minimum;

	private float maximum;

	private long seed;

	private Random random;

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
		random = new Random(seed);
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
		return minimum + random.nextFloat() * (maximum - minimum);
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

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}

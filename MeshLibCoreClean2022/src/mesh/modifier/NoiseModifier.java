package mesh.modifier;

import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

public class NoiseModifier implements IMeshModifier {

	private float minimum;
	private float maximum;

	public NoiseModifier() {
		this(0.0f, 1.0f);
	}

	public NoiseModifier(float minimum, float maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		List<Vector3f> normals = new VertexNormals(mesh).getVertexNormals();
		for (int i = 0; i < mesh.vertices.size(); i++) {
			Vector3f vertex = mesh.getVertexAt(i);
			Vector3f normal = normals.get(i);
			vertex.addLocal(normal.mult(createRandomValue()));
		}
		return mesh;
	}
	
	private float createRandomValue() {
		return Mathf.random(minimum, maximum);
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

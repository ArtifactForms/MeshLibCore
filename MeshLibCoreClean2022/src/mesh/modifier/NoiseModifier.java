package mesh.modifier;

import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

public class NoiseModifier implements IMeshModifier {

	float minimum;
	float maximum;
	
	public NoiseModifier() {
		this.minimum = 0f;
		this.maximum = 1f;
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
			float random = Mathf.random(minimum, maximum);
			vertex.addLocal(normal.mult(random));
		}
		return mesh;
	}

}

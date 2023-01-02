package mesh.modifier;

import math.Mathf;
import mesh.Mesh3D;
import mesh.util.Bounds3;

public class FitToAABBModifier implements IMeshModifier {

	private float width;
	private float height;
	private float depth;
	private Mesh3D mesh;

	public FitToAABBModifier(float width, float height, float depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		scale(calculateScale());
		return mesh;
	}
	
	private void scale(float scale) {
		mesh.scale(scale, scale, scale);
	}
	
	private float calculateScale() {
		float min = minTargetValue();
		float max = maxSourceValue();
		return min / max;
	}
	
	private float minTargetValue() {
		return min(width, height, depth);
	}
	
	private float maxSourceValue() {
		Bounds3 bounds = mesh.calculateBounds();
		float width = bounds.getWidth();
		float height = bounds.getHeight();
		float depth = bounds.getDepth();
		return max(width, height, depth);
	}
	
	private float min(float... values) {
		return Mathf.min(values);
	}
	
	private float max(float... values) {
		return Mathf.max(values);
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

}

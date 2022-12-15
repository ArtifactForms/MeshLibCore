package mesh.modifier;

import mesh.Mesh3D;
import mesh.util.Bounds3;

public class FitToAABBModifier implements IMeshModifier {

    private float width;

    private float height;

    private float depth;

    public FitToAABBModifier(float width, float height, float depth) {
	this.width = width;
	this.height = height;
	this.depth = depth;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	Bounds3 bounds = mesh.calculateBounds();
	float sx = 1f / bounds.getWidth() * width;
	float sy = 1f / bounds.getHeight() * height;
	float sz = 1f / bounds.getDepth() * depth;
	mesh.scale(sx, sy, sz);
	return mesh;
    }

}

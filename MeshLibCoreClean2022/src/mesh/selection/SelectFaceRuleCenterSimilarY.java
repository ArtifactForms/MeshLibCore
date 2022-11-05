package mesh.selection;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.Mesh3DUtil;

public class SelectFaceRuleCenterSimilarY implements IFaceSelectionRule {
	
	float y;
	float threshold;
	
	public SelectFaceRuleCenterSimilarY(float y, float threshold) {
		this.y = y;
		this.threshold = threshold;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, face);
		Vector3f v = new Vector3f(center).setY(y);
		return center.distance(v) < threshold;
	}
	
}

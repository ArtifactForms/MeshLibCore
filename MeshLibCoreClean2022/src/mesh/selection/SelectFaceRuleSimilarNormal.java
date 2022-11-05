package mesh.selection;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.Mesh3DUtil;

public class SelectFaceRuleSimilarNormal implements IFaceSelectionRule {

	private float threshold;
	private Vector3f compare;
	
	public SelectFaceRuleSimilarNormal(float threshold, Vector3f compare) {
		this.threshold = threshold;
		this.compare = compare;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		Vector3f normal0 = Mesh3DUtil.calculateFaceNormal(mesh, face);
		float delta = normal0.distance(compare);
		return delta <= threshold;
	}

}

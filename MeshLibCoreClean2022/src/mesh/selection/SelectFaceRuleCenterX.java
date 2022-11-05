package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class SelectFaceRuleCenterX implements IFaceSelectionRule {

	private float x;
	private CompareType compare;

	public SelectFaceRuleCenterX(CompareType compare, float x) {
		this.x = x;
		this.compare = compare;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		return Compare.compare(compare, Mesh3DUtil.calculateFaceCenter(mesh, face).x, x);
	}

}

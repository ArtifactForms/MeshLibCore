package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class SelectFaceRuleCenterY implements IFaceSelectionRule {

	private float y;
	private CompareType compare;

	public SelectFaceRuleCenterY(CompareType compare, float y) {
		this.y = y;
		this.compare = compare;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		return Compare.compare(compare, Mesh3DUtil.calculateFaceCenter(mesh, face).y, y);
	}

}

package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;

public class SelectFaceRuleCenterY implements IFaceSelectionRule {

	private float y;
	private CompareType compare;

	public SelectFaceRuleCenterY(CompareType compare, float y) {
		this.y = y;
		this.compare = compare;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		return Compare.compare(compare, mesh.calculateFaceCenter(face).y, y);
	}

}

package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;

public class SelectFaceRuleCenterX implements IFaceSelectionRule {

    private float x;
    private CompareType compare;

    public SelectFaceRuleCenterX(CompareType compare, float x) {
	this.x = x;
	this.compare = compare;
    }

    @Override
    public boolean isValid(Mesh3D mesh, Face3D face) {
	return Compare.compare(compare, mesh.calculateFaceCenter(face).x, x);
    }

}

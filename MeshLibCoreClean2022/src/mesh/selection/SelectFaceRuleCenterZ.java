package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;

public class SelectFaceRuleCenterZ implements IFaceSelectionRule {

    private float z;

    private CompareType compare;

    public SelectFaceRuleCenterZ(CompareType compare, float z) {
        this.z = z;
        this.compare = compare;
    }

    @Override
    public boolean isValid(Mesh3D mesh, Face3D face) {
        return Compare.compare(compare, mesh.calculateFaceCenter(face).getZ(), z);
    }

}

package mesh.selection;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.geometry.MeshGeometryUtil;

public class SelectFaceRuleCenterDistance implements IFaceSelectionRule {

    private float distance;

    private Vector3f origin;

    private CompareType compare;

    public SelectFaceRuleCenterDistance(Vector3f origin, CompareType compare,
            float distance) {
        this.distance = distance;
        this.origin = origin;
        this.compare = compare;
    }

    @Override
    public boolean isValid(Mesh3D mesh, Face3D face) {
        return Compare.compare(compare,
                origin.distance(MeshGeometryUtil.calculateFaceCenter(mesh, face)), distance);
    }

}

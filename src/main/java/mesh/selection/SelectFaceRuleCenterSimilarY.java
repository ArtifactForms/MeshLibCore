package mesh.selection;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.geometry.MeshGeometryUtil;

public class SelectFaceRuleCenterSimilarY implements IFaceSelectionRule {

    private float y;

    private float threshold;

    public SelectFaceRuleCenterSimilarY(float y, float threshold) {
        this.y = y;
        this.threshold = threshold;
    }

    @Override
    public boolean isValid(Mesh3D mesh, Face3D face) {
        Vector3f center = MeshGeometryUtil.calculateFaceCenter(mesh, face);
        Vector3f v = new Vector3f(center).setY(y);
        return center.distance(v) < threshold;
    }

}

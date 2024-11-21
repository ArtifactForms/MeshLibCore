package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.Bounds3;

public class CenterAtModifier implements IMeshModifier {

    private Vector3f center;

    public CenterAtModifier() {
        center = new Vector3f();
    }

    public CenterAtModifier(Vector3f center) {
        this.center = center;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        Bounds3 bounds = mesh.calculateBounds();
        if (bounds.getCenter().equals(center))
            return mesh;
        Vector3f distance = center.subtract(bounds.getCenter());
        mesh.apply(new TranslateModifier(distance));
        return mesh;
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

}

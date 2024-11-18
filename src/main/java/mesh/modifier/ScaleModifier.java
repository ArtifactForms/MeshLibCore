package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

public class ScaleModifier implements IMeshModifier {

    private float scaleX;

    private float scaleY;

    private float scaleZ;
    
    private Mesh3D mesh;

    public ScaleModifier() {
        this(1, 1, 1);
    }

    public ScaleModifier(float scale) {
        this(scale, scale, scale);
    }

    public ScaleModifier(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        setMesh(mesh);
        scaleMesh();
        return mesh;
    }
    
    private void scaleMesh() {
        for (Vector3f v : mesh.vertices)
            v.multLocal(scaleX, scaleY, scaleZ);
    }
    
    private void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

}

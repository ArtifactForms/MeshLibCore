package mesh.creator.unsorted;

import java.util.ArrayList;
import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.QuadSphereCreator;
import mesh.modifier.topology.SolidifyModifier;
import mesh.util.Mesh3DUtil;

public class LatticeSphereCreator implements IMeshCreator {

    private float radius;

    private float scale;

    private float thickness;

    private int subdivisions;

    private Mesh3D mesh;

    public LatticeSphereCreator() {
        radius = 1.0f;
        scale = 0.9f;
        subdivisions = 2;
        thickness = 0.01f;
    }

    private void createQuadSphere() {
        mesh = new QuadSphereCreator(radius, subdivisions).create();
    }

    private void extrudeFaces() {
        List<Face3D> faces = new ArrayList<Face3D>(mesh.getFaces());
        for (Face3D f : faces) {
            Mesh3DUtil.extrudeFace(mesh, f, scale, 0);
        }
        mesh.faces.removeAll(faces);
    }

    private void solidify() {
        new SolidifyModifier(thickness).modify(mesh);
    }

    @Override
    public Mesh3D create() {
        createQuadSphere();
        extrudeFaces();
        solidify();
        return mesh;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public int getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
        this.subdivisions = subdivisions;
    }

}

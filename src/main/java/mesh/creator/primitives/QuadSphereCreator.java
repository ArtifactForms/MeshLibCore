package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.ScaleModifier;
import mesh.modifier.SpherifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;

public class QuadSphereCreator implements IMeshCreator {

    private float radius;

    private int subdivisions;

    private Mesh3D mesh;

    public QuadSphereCreator() {
        this(1, 3);
    }

    public QuadSphereCreator(float radius, int subdivisions) {
        this.radius = radius;
        this.subdivisions = subdivisions;
    }

    @Override
    public Mesh3D create() {
        createCube();
        subdivide();
        scale();
        pushToSphere();
        return mesh;
    }

    private void createCube() {
        mesh = new CubeCreator().create();
    }

    private void subdivide() {
        mesh.apply(new PlanarMidEdgeCenterModifier(subdivisions));
    }

    private void scale() {
        mesh.apply(new ScaleModifier(radius));
    }

    private void pushToSphere() {
        mesh.apply(new SpherifyModifier(radius));
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
        this.subdivisions = subdivisions;
    }

}

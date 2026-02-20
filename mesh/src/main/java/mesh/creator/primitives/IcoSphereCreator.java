package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.deform.SpherifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeModifier;

public class IcoSphereCreator implements IMeshCreator {

    private float radius;

    private int subdivisions;

    private Mesh3D mesh;

    public IcoSphereCreator() {
        this(1, 0);
    }

    public IcoSphereCreator(float radius, int subdivisions) {
        this.radius = radius;
        this.subdivisions = subdivisions;
    }

    @Override
    public Mesh3D create() {
        createBaseIcosahedron();
        subdivideIcosahedron();
        spherifyIcosahedron();
        return mesh;
    }

    private void createBaseIcosahedron() {
        mesh = new IcosahedronCreator().create();
    }

    private void subdivideIcosahedron() {
        new PlanarMidEdgeModifier(subdivisions).modify(mesh);
    }

    private void spherifyIcosahedron() {
        new SpherifyModifier(radius).modify(mesh);
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

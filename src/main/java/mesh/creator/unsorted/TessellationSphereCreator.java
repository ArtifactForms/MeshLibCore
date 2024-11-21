package mesh.creator.unsorted;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.SpherifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.util.Mesh3DUtil;

public class TessellationSphereCreator implements IMeshCreator {

    private float radius;

    private float thickness;

    private float scaleExtrude;

    private int subdivisions;

    private Mesh3D mesh;

    public TessellationSphereCreator() {
        radius = 6f;
        thickness = 0.1f;
        scaleExtrude = 0.8f;
        subdivisions = 0;
    }

    private void createHoles() {
        List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
        for (Face3D face : faces) {
            Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0.0f);
        }
        mesh.faces.removeAll(faces);
    }

    private void tessellate() {
        new PlanarMidEdgeCenterModifier(2).modify(mesh);
    }

    private void pushToSphere() {
        mesh.apply(new SpherifyModifier(radius));
    }

    private void flipFaceNormals() {
        Mesh3DUtil.flipDirection(mesh);
    }

    private void solidify() {
        new SolidifyModifier(thickness).modify(mesh);
    }

    @Override
    public Mesh3D create() {
        createBaseIcoSphere();
        tessellate();
        createHoles();
        pushToSphere();
        flipFaceNormals();
        solidify();
        return mesh;
    }

    private void createBaseIcoSphere() {
        mesh = new IcoSphereCreator(radius, subdivisions).create();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public float getScaleExtrude() {
        return scaleExtrude;
    }

    public void setScaleExtrude(float scaleExtrude) {
        this.scaleExtrude = scaleExtrude;
    }

    public int getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
        this.subdivisions = subdivisions;
    }

}

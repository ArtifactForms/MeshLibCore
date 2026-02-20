package mesh.creator.unsorted;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.util.Mesh3DUtil;

public class TriangleSegmentCreator implements IMeshCreator {

    private float size;

    private float height;

    private float scaleExtrude;

    private Mesh3D mesh;

    public TriangleSegmentCreator() {
        size = 1f;
        height = 0.125f;
        scaleExtrude = 0.9f;
    }

    private void createVertices() {
        addVertex(-0.866f, 0f, -0.5f);
        addVertex(0.866f, 0f, -0.5f);
        addVertex(0f, 0f, 1f);
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void createFaces() {
        mesh.addFace(0, 2, 1);
    }

    private void scale() {
        new ScaleModifier(size).modify(mesh);
    }

    private void extrude() {
        new PlanarMidEdgeCenterModifier().modify(mesh);
        List<Face3D> faces = mesh.getFaces();
        for (Face3D face : faces) {
            Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0f);
        }
        mesh.removeFaces(faces);
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        extrude();
        scale();
        solidify();
        centerOnAxisY();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void solidify() {
        new SolidifyModifier(height).modify(mesh);
    }

    private void centerOnAxisY() {
        mesh.translateY(height / 2f);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}

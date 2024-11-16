package mesh.creator.primitives;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.modifier.FlipFacesModifier;
import mesh.util.Mesh3DUtil;

public class CylinderCreator implements IMeshCreator {

    private int vertices;

    private float topRadius;

    private float bottomRadius;

    private float height;

    private FillType topCapFillType;

    private FillType bottomCapFillType;

    private Mesh3D mesh;

    public CylinderCreator() {
        vertices = 32;
        topRadius = 1;
        bottomRadius = 1;
        height = 2;
        topCapFillType = FillType.N_GON;
        bottomCapFillType = FillType.N_GON;
    }

    private void bridge(Mesh3D m0, Mesh3D m1) {
        for (int i = 0; i < vertices; i++) {
            Vector3f v0 = m1.getVertexAt(i);
            Vector3f v1 = m1.getVertexAt((i + 1) % vertices);
            Vector3f v2 = m0.getVertexAt(i);
            Vector3f v3 = m0.getVertexAt((i + 1) % vertices);
            Mesh3DUtil.bridge(mesh, v0, v1, v2, v3);
        }
    }

    private Mesh3D createTopCap() {
        CircleCreator creator = new CircleCreator();
        creator.setVertices(vertices);
        creator.setRadius(topRadius);
        creator.setFillType(topCapFillType);
        creator.setCenterY(-height / 2f);
        return creator.create();
    }

    private Mesh3D createBottomCap() {
        Mesh3D mesh;
        CircleCreator creator = new CircleCreator();
        creator.setVertices(vertices);
        creator.setRadius(bottomRadius);
        creator.setCenterY(height / 2f);
        creator.setFillType(bottomCapFillType);
        mesh = creator.create();
        return new FlipFacesModifier().modify(mesh);
    }

    @Override
    public Mesh3D create() {
        Mesh3D topCap = createTopCap();
        Mesh3D bottomCap = createBottomCap();
        initializeMesh();
        append(topCap, bottomCap);
        bridge(topCap, bottomCap);
        return mesh;
    }

    private void append(Mesh3D... meshes) {
        mesh.append(meshes);
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public float getTopRadius() {
        return topRadius;
    }

    public void setTopRadius(float topRadius) {
        this.topRadius = topRadius;
    }

    public float getBottomRadius() {
        return bottomRadius;
    }

    public void setBottomRadius(float bottomRadius) {
        this.bottomRadius = bottomRadius;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public FillType getTopCapFillType() {
        return topCapFillType;
    }

    public void setTopCapFillType(FillType topCapFillType) {
        this.topCapFillType = topCapFillType;
    }

    public FillType getBottomCapFillType() {
        return bottomCapFillType;
    }

    public void setBottomCapFillType(FillType bottomCapFillType) {
        this.bottomCapFillType = bottomCapFillType;
    }

}

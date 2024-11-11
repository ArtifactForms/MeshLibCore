package mesh.creator.special;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.SolidifyModifier;

public class SimpleStarCreator implements IMeshCreator {

    private int vertices;

    private float outerRadius;

    private float innerRadius;

    private float height;

    private Mesh3D mesh;

    public SimpleStarCreator() {
        vertices = 5;
        outerRadius = 1;
        innerRadius = 0.5f;
        height = 0.5f;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVerticesAndFaces();
        createCenterVertex();
        solidify();
        center();
        rotate();
        return mesh;
    }

    private void rotate() {
        if (vertices % 2 == 0)
            return;
        mesh.rotateY(calculateAngleStep() / 2f);
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createCenterVertex() {
        mesh.addVertex(0, 0, 0);
    }

    private void createVerticesAndFaces() {
        int centerIndex = vertices * 2;
        float angle = 0;
        float angleStep = calculateAngleStep();
        for (int i = 0; i < vertices * 2; i++) {
            float radius = i % 2 == 0 ? outerRadius : innerRadius;
            float x = radius * Mathf.cos(angle);
            float z = radius * Mathf.sin(angle);
            angle += angleStep;
            mesh.addVertex(x, 0, z);
            mesh.addFace(centerIndex, i, (i + 1) % (vertices * 2));
        }
    }

    private void center() {
        mesh.translateY(-height / 2.0f);
    }

    private void solidify() {
        new SolidifyModifier(height).modify(mesh);
    }

    private float calculateAngleStep() {
        return Mathf.TWO_PI / (float) (vertices * 2);
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public float getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(float outerRadius) {
        this.outerRadius = outerRadius;
    }

    public float getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(float innerRadius) {
        this.innerRadius = innerRadius;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}

package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.topology.SolidifyModifier;

public class SegmentedTubeCreator implements IMeshCreator {

    private int segments;

    private int vertices;

    private float outerRadius;

    private float innerRadius;

    private float height;

    private Mesh3D mesh;

    public SegmentedTubeCreator() {
        segments = 2;
        vertices = 32;
        outerRadius = 1;
        innerRadius = 0.5f;
        height = 2;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createQuadFaces();
        centerOnAxisY();
        solidify();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void centerOnAxisY() {
        mesh.translateY(-height / 2);
    }

    private void solidify() {
        if (outerRadius == innerRadius)
            return;
        new SolidifyModifier(outerRadius - innerRadius).modify(mesh);
    }

    private void createVertices() {
        float segmentHeight = height / (float) segments;
        for (int i = 0; i <= segments; i++) {
            Mesh3D mesh = new CircleCreator(vertices, outerRadius).create();
            mesh.translateY(i * segmentHeight);
            this.mesh.append(mesh);
        }
    }

    private void createQuadFaces() {
        for (int i = 0; i < segments; i++) {
            for (int j = 0; j < vertices; j++) {
                addFace(i, j);
            }
        }
    }

    private void addFace(int i, int j) {
        int idx0 = toOneDimensionalIndex(i, j);
        int idx1 = toOneDimensionalIndex(i + 1, j);
        int idx2 = toOneDimensionalIndex(i + 1, j + 1);
        int idx3 = toOneDimensionalIndex(i, j + 1);
        mesh.addFace(idx0, idx1, idx2, idx3);
    }

    private int toOneDimensionalIndex(int i, int j) {
        return Mathf.toOneDimensionalIndex(i, j % vertices, vertices);
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
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

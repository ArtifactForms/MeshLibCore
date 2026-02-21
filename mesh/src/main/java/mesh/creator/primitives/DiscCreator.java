package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DiscCreator implements IMeshCreator {

    private int rotationSegments;

    private int discSegments;

    private float outerRadius;

    private float innerRadius;

    private Mesh3D mesh;

    public DiscCreator() {
        rotationSegments = 32;
        discSegments = 1;
        outerRadius = 1.0f;
        innerRadius = 0.5f;
    }

    private void addFace(int i, int j, int segments) {
        if (i >= segments)
            return;
        int idx0 = toOneDimensionalIndex(i, j);
        int idx1 = toOneDimensionalIndex(i + 1, j);
        int idx2 = toOneDimensionalIndex(i + 1, j + 1);
        int idx3 = toOneDimensionalIndex(i, j + 1);
        mesh.addFace(idx0, idx1, idx2, idx3);
    }

    private int toOneDimensionalIndex(int i, int j) {
        return Mathf.toOneDimensionalIndex(i, j % rotationSegments,
                rotationSegments);
    }

    private void createDisc(int segments, float startRadius) {
        float angle = 0;
        float radius = (outerRadius - innerRadius) / (float) discSegments;
        mesh = new Mesh3D();

        for (int i = 0; i <= segments; i++) {
            for (int j = 0; j < rotationSegments; j++) {
                float x = (startRadius + (i * radius)) * Mathf.cos(angle);
                float y = 0;
                float z = (startRadius + (i * radius)) * Mathf.sin(angle);
                mesh.addVertex(x, y, z);
                addFace(i, j, segments);
                angle += Mathf.TWO_PI / (float) rotationSegments;
            }
            angle = 0;
        }
    }

    private void createTriangleFan() {
        int idx = mesh.getVertexCount();
        mesh.addVertex(0, 0, 0);
        for (int i = 0; i < rotationSegments; i++) {
            mesh.addFace(idx, i, (i + 1) % rotationSegments);
        }
    }

    @Override
    public Mesh3D create() {
        if (innerRadius > 0) {
            createDisc(discSegments, innerRadius);
        } else {
            createDisc(discSegments - 1, outerRadius / discSegments);
            createTriangleFan();
        }
        return mesh;
    }

    public int getRotationSegments() {
        return rotationSegments;
    }

    public void setRotationSegments(int rotationSegments) {
        this.rotationSegments = rotationSegments;
    }

    public int getDiscSegments() {
        return discSegments;
    }

    public void setDiscSegments(int discSegments) {
        this.discSegments = discSegments;
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

}

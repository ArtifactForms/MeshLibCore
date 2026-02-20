package mesh.creator.special;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SpinCreator implements IMeshCreator {

    private int steps;

    private float angle;

    private boolean close;

    private Mesh3D mesh;

    private List<Vector3f> vertices;

    public SpinCreator() {
        steps = 9;
        angle = Mathf.QUARTER_PI;
        vertices = new ArrayList<Vector3f>();
    }

    public SpinCreator(int steps, float angle, boolean close) {
        this.steps = steps;
        this.angle = angle;
        this.close = close;
        this.vertices = new ArrayList<Vector3f>();
    }

    public void add(Vector3f v) {
        if (v != null)
            vertices.add(v);
    }

    public void addAll(Collection<Vector3f> vertices) {
        for (Vector3f v : vertices)
            if (v != null)
                this.vertices.add(v);
    }

    public void clearVertices() {
        vertices.clear();
    }

    private void createVertices() {
        float angleStep = -this.angle / (float) (steps - 1);
        for (int j = 0; j < steps; j++)
            createVerticesAtAngle(j * angleStep);
    }

    private void createVerticesAtAngle(float angle) {
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f v0 = vertices.get(i);
            Vector3f v1 = createPointOnCircle(angle, v0.getY());
            Vector3f v2 = new Vector3f(v0.getX(), 1, v0.getX());
            v1.multLocal(v2);
            mesh.addVertex(v1.x, v1.y, v1.z);
        }
    }

    private Vector3f createPointOnCircle(float angle, float y) {
        float x = Mathf.cos(angle);
        float z = Mathf.sin(angle);
        return new Vector3f(x, y, z);
    }

    private void createFaces() {
        int n = vertices.size();
        int m = close ? steps : steps - 1;
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n - 1; i++) {
                addQuadFace(j, i, n);
            }
        }
    }

    private void addQuadFace(int j, int i, int n) {
        int idx0 = (j * n) + i;
        int idx1 = (j * n) + i + 1;
        int idx2 = ((j + 1) % steps * n) + i + 1;
        int idx3 = ((j + 1) % steps * n) + i;
        mesh.addFace(idx0, idx1, idx2, idx3);;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

}

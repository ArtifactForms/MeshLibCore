package mesh.creator.special;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class AntiprismCreator implements IMeshCreator {

    private int n;

    private Mesh3D mesh;

    public AntiprismCreator() {
        this(6);
    }

    public AntiprismCreator(int n) {
        this.n = n;
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

    private void createVertices() {
        float a = Mathf.cos(Mathf.PI / n);
        float b = Mathf.cos(Mathf.TWO_PI / n);
        float h = Mathf.sqrt((a - b) / 2.0f);
        for (int k = 0; k < 2 * n; k++) {
            float x = Mathf.cos(k * Mathf.PI / n);
            float z = Mathf.sin(k * Mathf.PI / n);
            float y = Mathf.pow(-1, k) * h;
            mesh.addVertex(x, y, z);
        }
    }

    private void createFaces() {
        int n2 = n + n;
        int[] indicesTop = new int[n];
        int[] indicesBottom = new int[n];
        for (int i = 0; i < n2; i++) {
            int index0 = i % n2;
            int index1 = (i + 1) % n2;
            int index2 = (i + 2) % n2;
            if (i % 2 == 1) {
                mesh.addFace(index0, index1, index2);
                indicesTop[i / 2] = i;
            } else {
                mesh.addFace(index0, index2, index1);
                indicesBottom[(n - 1) - (i / 2)] = i;
            }
        }
        mesh.add(new Face3D(indicesTop));
        mesh.add(new Face3D(indicesBottom));
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}

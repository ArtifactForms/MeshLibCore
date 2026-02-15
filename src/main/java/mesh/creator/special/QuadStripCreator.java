package mesh.creator.special;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class QuadStripCreator implements IMeshCreator {

    private List<Vector3f> vertices;

    private Mesh3D mesh;

    public QuadStripCreator() {
        vertices = new ArrayList<Vector3f>();
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void createFaces() {
        for (int i = 0; i <= vertices.size() - 4; i += 2) {
            int a = i;
            int b = i + 1;
            int c = i + 3;
            int d = i + 2;
            Face3D face = new Face3D(a, b, c, d);
            mesh.add(face);
        }
    }

    private void createVertices() {
        for (Vector3f v : vertices) {
            mesh.addVertex(v.x, v.y, v.z);
        }
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector3f(x, y, z));
    }

    public void clear() {
        vertices.clear();
    }

}

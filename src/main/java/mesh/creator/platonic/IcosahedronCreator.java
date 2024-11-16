package mesh.creator.platonic;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class IcosahedronCreator implements IMeshCreator {

    private float a = 0.723600f;

    private float b = 0.447215f;

    private float c = 0.850640f;

    private float d = 0.525720f;

    private float e = 0.276385f;

    private float f = 0.894425f;

    private float size;

    private Mesh3D mesh;

    public IcosahedronCreator() {
        this(1);
    }

    public IcosahedronCreator(float size) {
        this.size = size;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        scaleMesh();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createVertices() {
        addVertex(0, -1, 0);
        addVertex(a, -b, d);
        addVertex(-e, -b, c);
        addVertex(-f, -b, 0);
        addVertex(-e, -b, -c);
        addVertex(a, -b, -d);
        addVertex(e, b, c);
        addVertex(-a, b, d);
        addVertex(-a, b, -d);
        addVertex(e, b, -c);
        addVertex(f, b, 0);
        addVertex(0, 1, 0);
    }

    private void createFaces() {
        createTopFaces();
        createSideTopFaces();
        createSideBottomFaces();
        createBottomFaces();
    }

    private void createSideTopFaces() {
        addFace(1, 5, 10);
        addFace(2, 1, 6);
        addFace(3, 2, 7);
        addFace(4, 3, 8);
        addFace(5, 4, 9);
    }

    private void createSideBottomFaces() {
        addFace(10, 6, 1);
        addFace(6, 7, 2);
        addFace(7, 8, 3);
        addFace(8, 9, 4);
        addFace(9, 10, 5);
    }

    private void createTopFaces() {
        addFace(2, 0, 1);
        addFace(1, 0, 5);
        addFace(3, 0, 2);
        addFace(4, 0, 3);
        addFace(5, 0, 4);
    }

    private void createBottomFaces() {
        addFace(6, 10, 11);
        addFace(7, 6, 11);
        addFace(8, 7, 11);
        addFace(9, 8, 11);
        addFace(10, 9, 11);
    }

    private void addFace(int... vertices) {
        mesh.add(new Face3D(vertices));
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void scaleMesh() {
        mesh.scale(size);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

}

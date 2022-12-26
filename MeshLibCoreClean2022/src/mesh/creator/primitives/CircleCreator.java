package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class CircleCreator implements IMeshCreator {

    private int vertices;
    private float radius;
    private float centerY;
    private FillType fillType;
    private Mesh3D mesh;

    public CircleCreator() {
	this(32, 1);
    }

    public CircleCreator(int vertices, float radius) {
	this(vertices, radius, 0);
    }

    public CircleCreator(int vertices, float radius, float centerY) {
	this.vertices = vertices;
	this.radius = radius;
	this.centerY = centerY;
	this.fillType = FillType.NOTHING;
    }

    private void createVertices() {
	float angle = 0;
	float step = Mathf.TWO_PI / (float) vertices;
	for (int i = 0; i < vertices; i++) {
	    float x = radius * Mathf.cos(angle);
	    float z = radius * Mathf.sin(angle);
	    mesh.add(new Vector3f(x, centerY, z));
	    angle += step;
	}
    }

    private void createCenterVertex() {
	if (fillType != FillType.TRIANGLE_FAN)
	    return;
	mesh.addVertex(0, centerY, 0);
    }

    private void createFaces() {
	switch (fillType) {
	case NOTHING:
	    break;
	case TRIANGLE_FAN:
	    createTriangleFan();
	    break;
	case N_GON:
	    createNGon();
	    break;
	default:
	    break;
	}
    }

    private void createTriangleFan() {
	for (int i = 0; i < vertices; i++) {
	    int idx0 = i % vertices;
	    int idx1 = (i + 1) % vertices;
	    mesh.addFace(idx0, idx1, vertices);
	}
    }

    private void createNGon() {
	int[] indices = new int[vertices];
	for (int i = 0; i < vertices; i++) {
	    indices[i] = i;
	}
	mesh.addFace(indices);
    }

    @Override
    public Mesh3D create() {
	initializeMesh();
	createVertices();
	createCenterVertex();
	createFaces();
	return mesh;
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

    public float getRadius() {
	return radius;
    }

    public void setRadius(float radius) {
	this.radius = radius;
    }

    public FillType getFillType() {
	return fillType;
    }

    public void setFillType(FillType fillType) {
	this.fillType = fillType;
    }

    public float getCenterY() {
	return centerY;
    }

    public void setCenterY(float centerY) {
	this.centerY = centerY;
    }

}

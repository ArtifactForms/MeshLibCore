package mesh.creator.unsorted;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;

public class NubCreator implements IMeshCreator {

    private int subdivisions;
    private int heightSegments;
    private int rotationSegments;
    private float radius;
    private float minorRadius;
    private float segmentHeight;
    private Mesh3D mesh;

    public NubCreator() {
	subdivisions = 1;
	heightSegments = 7;
	rotationSegments = 16;
	radius = 1;
	minorRadius = 0.7f;
	segmentHeight = 0.5f;
    }

    @Override
    public Mesh3D create() {
	initializeMesh();
	createVertices();
	createQuadFaces();
	capTop();
	capBottom();
	centerOnAxisY();
	subdivide();
	return mesh;
    }

    private void initializeMesh() {
	mesh = new Mesh3D();
    }

    private void createVertices() {
	for (int i = 0; i <= heightSegments; i++)
	    createSegmentVerticesAt(i);
    }

    private Mesh3D createMajorCircle(float centerY) {
	return createCircle(radius, centerY);
    }

    private Mesh3D createMinorCircle(float centerY) {
	return createCircle(minorRadius, centerY);
    }

    private Mesh3D createCircle(float radius, float centerY) {
	CircleCreator creator = new CircleCreator();
	creator.setRadius(radius);
	creator.setVertices(rotationSegments);
	creator.setCenterY(centerY);
	return creator.create();
    }

    private void createSegmentVerticesAt(int i) {
	Mesh3D majorCircle = createMajorCircle(segmentHeight * i);
	Mesh3D minorCircle = createMinorCircle(segmentHeight * i);
	if (i % 2 == 0) {
	    mesh.append(minorCircle, majorCircle);
	} else {
	    mesh.append(majorCircle, minorCircle);
	}
    }

    private void createQuadFaces() {
	for (int i = 0; i <= (heightSegments * 2); i++)
	    for (int j = 0; j < rotationSegments; j++)
		addFace(i, j);
    }

    private void addFace(int i, int j) {
	int idx0 = toOneDimensionalIndex(i, j);
	int idx1 = toOneDimensionalIndex(i + 1, j);
	int idx2 = toOneDimensionalIndex(i + 1, j + 1);
	int idx3 = toOneDimensionalIndex(i, j + 1);
	mesh.addFace(idx0, idx1, idx2, idx3);
    }

    private int toOneDimensionalIndex(int i, int j) {
	return Mathf.toOneDimensionalIndex(i, j % rotationSegments, rotationSegments);
    }

    private void capTop() {
	int[] indices = new int[rotationSegments];
	for (int i = 0; i < rotationSegments; i++)
	    indices[i] = i;
	mesh.addFace(indices);
    }

    private void capBottom() {
	int[] indices = new int[rotationSegments];
	for (int i = 0; i < rotationSegments; i++) {
	    indices[i] = mesh.vertices.size() - 1 - i;
	}
	mesh.addFace(indices);
    }

    private void centerOnAxisY() {
	mesh.translateY(-(heightSegments * segmentHeight) * 0.5f);
    }

    private void subdivide() {
	new CatmullClarkModifier(subdivisions).modify(mesh);
    }

    public int getSubdivisions() {
	return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
	this.subdivisions = subdivisions;
    }

    public float getRadius() {
	return radius;
    }

    public void setRadius(float radius) {
	this.radius = radius;
    }

    public float getMinorRadius() {
	return minorRadius;
    }

    public void setMinorRadius(float minorRadius) {
	this.minorRadius = minorRadius;
    }

    public int getHeightSegments() {
	return heightSegments;
    }

    public void setHeightSegments(int heightSegments) {
	this.heightSegments = heightSegments;
    }

    public int getRotationSegments() {
	return rotationSegments;
    }

    public void setRotationSegments(int rotationSegments) {
	this.rotationSegments = rotationSegments;
    }

    public float getSegmentHeight() {
	return segmentHeight;
    }

    public void setSegmentHeight(float segmentHeight) {
	this.segmentHeight = segmentHeight;
    }

}

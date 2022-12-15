package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.ArcCreator;
import mesh.modifier.SolidifyModifier;

public class ArchDoorCreator implements IMeshCreator {

    private int segments;

    private float radius;

    private float depth;

    private float extendBottom;

    private Mesh3D mesh;

    public ArchDoorCreator() {
	segments = 16;
	radius = 1;
	depth = 0.1f;
	extendBottom = 3;
    }

    @Override
    public Mesh3D create() {
	initializeMesh();
	createTopVertices();
	createBottomVertices();
	createFaces();
	solidify();
	translate();
	return mesh;
    }

    private void initializeMesh() {
	mesh = new Mesh3D();
    }

    private void createBottomVertices() {
	Mesh3D bottom = createArcCreator().create();
	bottom.rotateX(Mathf.HALF_PI);
	for (Vector3f v : bottom.vertices)
	    v.setY(0);
	mesh.append(bottom);
    }

    private void solidify() {
	new SolidifyModifier(depth).modify(mesh);
    }

    private void translate() {
	mesh.translateX(radius / 2f);
	mesh.translateZ(depth / 2f);
    }

    private void createFaces() {
	createQuadFaces(0, segments + 1);
    }

    private void createTopVertices() {
	Mesh3D top = createArcCreator().create();
	top.rotateX(Mathf.HALF_PI);
	top.translateY(-extendBottom);
	mesh.append(top);
    }

    private ArcCreator createArcCreator() {
	ArcCreator creator = new ArcCreator();
	creator.setVertices(segments + 1);
	creator.setRadius(radius);
	creator.setStartAngle(Mathf.HALF_PI);
	creator.setEndAngle(Mathf.PI);
	return creator;
    }

    private void createQuadFaces(int indexA, int indexB) {
	int n = segments + 1;
	for (int i = 0; i < segments; i++) {
	    int index0 = indexA + i % n;
	    int index1 = indexA + (i + 1) % n;
	    int index2 = indexB + (i + 1) % n;
	    int index3 = indexB + i % n;
	    mesh.addFace(index3, index2, index1, index0);
	}
    }

    public int getSegments() {
	return segments;
    }

    public void setSegments(int segments) {
	this.segments = segments;
    }

    public float getRadius() {
	return radius;
    }

    public void setRadius(float radius) {
	this.radius = radius;
    }

    public float getDepth() {
	return depth;
    }

    public void setDepth(float depth) {
	this.depth = depth;
    }

    public float getExtendBottom() {
	return extendBottom;
    }

    public void setExtendBottom(float extendBottom) {
	this.extendBottom = extendBottom;
    }

}

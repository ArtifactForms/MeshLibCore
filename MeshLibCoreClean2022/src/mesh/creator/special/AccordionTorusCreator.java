package mesh.creator.special;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;

public class AccordionTorusCreator implements IMeshCreator {

	private float majorRadius;
	
	private float[] minorRadii;
	
	private int majorSegments;
	
	private int minorSegments;
	
	private Mesh3D mesh;

	public AccordionTorusCreator() {
		majorRadius = 1f;
		majorSegments = 48;
		minorRadii = new float[majorSegments];
		minorSegments = 12;
		updateRadii();
	}

	private Mesh3D createMinorCircleAt(int index) {
		float minorRadius = getMinorRadiusAt(index);
		CircleCreator creator = new CircleCreator();
		creator.setRadius(minorRadius);
		creator.setVertices(minorSegments);
		return creator.create();
	}

	private void createVertices() {
		float majorAngle = 0;
		float majorStep = Mathf.TWO_PI / majorSegments;
		for (int i = 0; i < majorSegments; i++) {
			Mesh3D circle = createMinorCircleAt(i);
			circle.rotateZ(-Mathf.HALF_PI);
			circle.rotateY(Mathf.HALF_PI - majorAngle);
			translateToPointOnCircle(circle, majorAngle);
			mesh.addVertices(circle.getVertices());
			majorAngle += majorStep;
		}
	}

	private void translateToPointOnCircle(Mesh3D mesh, float majorAngle) {
		float x = majorRadius * Mathf.cos(majorAngle);
		float z = majorRadius * Mathf.sin(majorAngle);
		mesh.translate(x, 0, z);
	}

	private void createFaces() {
		for (int i = 0; i < majorSegments; i++)
			for (int j = 0; j < minorSegments; j++)
				createFaceAt(i, j);
	}

	private void createFaceAt(int i, int j) {
		int index0 = toOneDimensionalIndex(i + 1, j);
		int index1 = toOneDimensionalIndex(i, j);
		int index2 = toOneDimensionalIndex(i, j + 1);
		int index3 = toOneDimensionalIndex(i + 1, j + 1);
		mesh.add(new Face3D(index0, index1, index2, index3));
	}

	private int toOneDimensionalIndex(int i, int j) {
		return i % majorSegments * minorSegments + j % minorSegments;
	}

	private void updateRadii() {
		float majorRadius = 0.25f * this.majorRadius;
		float minorRadius = 0.125f * this.majorRadius;
		minorRadii = new float[majorSegments + 1];
		for (int i = 0; i < majorSegments; i++) {
			minorRadii[i] = (i % 2 == 0) ? majorRadius : minorRadius;
		}
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

	public float getMajorRadius() {
		return majorRadius;
	}

	public void setMajorRadius(float majorRadius) {
		this.majorRadius = majorRadius;
		updateRadii();
	}

	public float[] getMinorRadii() {
		return minorRadii;
	}

	public void setMinorRadii(float[] minorRadi) {
		this.minorRadii = minorRadi;
	}

	public int getMajorSegments() {
		return majorSegments;
	}

	public void setMajorSegments(int majorSegments) {
		this.majorSegments = majorSegments;
		updateRadii();
	}

	public int getMinorSegments() {
		return minorSegments;
	}

	public void setMinorSegments(int minorSegments) {
		this.minorSegments = minorSegments;
	}

	public float getMinorRadiusAt(int index) {
		return minorRadii[index];
	}

	public void setMinorRadiusAt(int index, float radius) {
		minorRadii[index] = radius;
	}

}

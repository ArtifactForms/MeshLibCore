package mesh.creator.special;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

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

	private void createVertices() {
		float majorAngle = 0;
		float minorAngle = 0;
		float majorStep = Mathf.TWO_PI / majorSegments;
		float minorStep = Mathf.TWO_PI / minorSegments;
		Vector3f[] verts = new Vector3f[majorSegments * minorSegments];

		for (int j = 0; j < majorSegments; j++) {
			float majorX = majorRadius * Mathf.cos(majorAngle);
			float majorZ = majorRadius * Mathf.sin(majorAngle);
			Vector3f v0 = new Vector3f(majorX, 0, majorZ);
			
			for (int i = 0; i < minorSegments; i++) {
				float minorRadius = getMinorRadiusAt(j);
				float minorX = minorRadius * Mathf.cos(minorAngle);
				float minorY = minorRadius * Mathf.sin(minorAngle);
				Vector3f v1 = new Vector3f(minorX, minorY, 0);
				rotate(v1, majorAngle);
				v1.addLocal(v0);
				minorAngle += minorStep;
				verts[j * minorSegments + i] = v1;
			}
			majorAngle += majorStep;
		}

		mesh.add(verts);
	}
	
	private void rotate(Vector3f v, float majorAngle) {
		float angle = Mathf.TWO_PI - majorAngle;
		float x = Mathf.cos(angle) * v.x + Mathf.sin(angle) * v.z;
		float z = -Mathf.sin(angle) * v.x + Mathf.cos(angle) * v.z;
		v.set(x, v.y, z);
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
	
	private int toOneDimensionalIndex(int i , int j) {
		return i % majorSegments * minorSegments + j % minorSegments;
	}
	
	private void updateRadii() {
		minorRadii = new float[majorSegments + 1];
		for (int i = 0; i < majorSegments; i++) {
			minorRadii[i] = (i % 2 == 0) ? (0.25f * majorRadius) : 0.125f * (majorRadius);
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

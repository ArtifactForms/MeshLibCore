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
			Vector3f v0 = new Vector3f(majorRadius * Mathf.cos(majorAngle), 0, majorRadius * Mathf.sin(majorAngle));
			for (int i = 0; i < minorSegments; i++) {
				float minorRadius = getMinorRadiusAt(j);
				Vector3f v1 = new Vector3f(minorRadius * Mathf.cos(minorAngle), minorRadius * Mathf.sin(minorAngle), 0);
				// Rotate
				float angle = Mathf.TWO_PI - majorAngle;
				float x2 = Mathf.cos(angle) * v1.x + Mathf.sin(angle) * v1.z;
				float z2 = -Mathf.sin(angle) * v1.x + Mathf.cos(angle) * v1.z;
				v1.set(x2, v1.y, z2);
				v1.addLocal(v0);
				minorAngle += minorStep;
				verts[j * minorSegments + i] = v1;
			}
			majorAngle += majorStep;
		}

		mesh.add(verts);
	}

	private void createFaces() {
		for (int j = 0; j < majorSegments; j++) {
			for (int i = 0; i < minorSegments; i++) {
				int[] k = new int[] { j % majorSegments, (j + 1) % majorSegments, i % minorSegments,
						(i + 1) % minorSegments };
				int index0 = k[1] * minorSegments + k[2];
				int index1 = k[0] * minorSegments + k[2];
				int index2 = k[1] * minorSegments + k[3];
				int index3 = k[0] * minorSegments + k[3];
				Face3D f = new Face3D(index0, index1, index3, index2);
				mesh.add(f);
			}
		}
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

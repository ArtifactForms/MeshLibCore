package mesh.creator.unsorted;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class AccordionTorusCreator implements IMeshCreator {

	private float majorRadius;
	private float[] minorRadi;
	private int majorSegments;
	private int minorSegments;
	private Mesh3D mesh;

	public AccordionTorusCreator() {
		this.majorRadius = 1f;
		this.majorSegments = 48;
		this.minorRadi = new float[majorSegments];
		this.minorSegments = 12;
		for (int i = 0; i < majorSegments; i++) {
			minorRadi[i] = (i % 2 == 0) ? 0.25f : 0.125f;
		}
	}

	public AccordionTorusCreator(float majorRadius, float[] minorRadi,
		int majorSegments, int minorSegments) {
		this.majorRadius = majorRadius;
		this.minorRadi = minorRadi;
		this.majorSegments = majorSegments;
		this.minorSegments = minorSegments;
	}

	private void createVertices() {
		float majorAngle = 0;
		float minorAngle = 0;
		float majorStep = Mathf.TWO_PI / majorSegments;
		float minorStep = Mathf.TWO_PI / minorSegments;
		Vector3f[] verts = new Vector3f[majorSegments * minorSegments];

		for (int j = 0; j < majorSegments; j++) {
			Vector3f v0 = new Vector3f(majorRadius * Mathf.cos(majorAngle), 0,
					majorRadius * Mathf.sin(majorAngle));
			for (int i = 0; i < minorSegments; i++) {
				float minorRadius = getMinorRadiusAt(j);
				Vector3f v1 = new Vector3f(minorRadius * Mathf.cos(minorAngle),
						minorRadius * Mathf.sin(minorAngle), 0);
				// Rotate
				float a = Mathf.TWO_PI - majorAngle;
				float x2 = Mathf.cos(a) * v1.x + Mathf.sin(a) * v1.z;
				float z2 = -Mathf.sin(a) * v1.x + Mathf.cos(a) * v1.z;
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
				int[] k = new int[] { j % majorSegments,
						(j + 1) % majorSegments, i % minorSegments,
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

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		return mesh;
	}

	public float getMajorRadius() {
		return majorRadius;
	}

	public void setMajorRadius(float majorRadius) {
		this.majorRadius = majorRadius;
	}

	public float[] getMinorRadi() {
		return minorRadi;
	}

	public void setMinorRadi(float[] minorRadi) {
		this.minorRadi = minorRadi;
	}

	public int getMajorSegments() {
		return majorSegments;
	}

	public void setMajorSegments(int majorSegments) {
		this.majorSegments = majorSegments;
	}

	public int getMinorSegments() {
		return minorSegments;
	}

	public void setMinorSegments(int minorSegments) {
		this.minorSegments = minorSegments;
	}

	public float getMinorRadiusAt(int index) {
		return minorRadi[index];
	}

	public void setMinorRadiusAt(int index, float radius) {
		minorRadi[index] = radius;
	}

}

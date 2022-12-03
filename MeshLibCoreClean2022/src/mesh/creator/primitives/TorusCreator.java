package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TorusCreator implements IMeshCreator {
	
	private float majorRadius;
	private float minorRadius;
	private int majorSegments;
	private int minorSegments;
	private Mesh3D mesh;

	public TorusCreator() {
		majorRadius = 1f;
		minorRadius = 0.25f;
		majorSegments = 48;
		minorSegments = 12;
	}
	
	private void createVertices() {
		float u = 0;
		float v = 0;
		float stepU = Mathf.TWO_PI / minorSegments;
		float stepV = Mathf.TWO_PI / majorSegments;
				
		for (int i = 0; i < majorSegments; i++) {
			for (int j = 0; j < minorSegments; j++) {
				float x = (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.cos(v);
				float y = minorRadius * Mathf.sin(u);
				float z = (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.sin(v);
				mesh.add(new Vector3f(x, y, z));
				u += stepU;
			}
			u = 0;
			v += stepV;
		}
	}
	
	private void createFaces() {
		for (int j = 0; j < majorSegments; j++) {
			for (int i = 0; i < minorSegments; i++) {
				int[] k = new int[] { j % majorSegments, (j + 1) % majorSegments,
						i % minorSegments, (i + 1) % minorSegments };
				int index0 = k[1] * minorSegments + k[2];
				int index1 = k[0] * minorSegments + k[2];
				int index2 = k[1] * minorSegments + k[3];
				int index3 = k[0] * minorSegments + k[3];
				addFace(index0, index1, index3, index2);
			}
		}
	}
	
	private void addFace(int... indices) {
		mesh.add(new Face3D(indices));
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
	}

	public float getMinorRadius() {
		return minorRadius;
	}

	public void setMinorRadius(float minorRadius) {
		this.minorRadius = minorRadius;
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

}

package mesh.creator.unsorted;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.wip.Mesh3DUtil;

public class NubCreator implements IMeshCreator {

	private float radius = 1;
	private float minorRadius = 0.7f;
	private int subdivisions = 1;
	private int heightSegments = 7;
	private int rotationSegments = 16;
	private Mesh3D mesh;
	
	private void createQuadFaces() {
		for (int i = 0; i <= (heightSegments * 2); i++) {
			for (int j = 0; j < rotationSegments; j++) {
				addFace(i, j);
			}
		}
	}
	
	private void addFace(int i, int j) {
		int idx0 = Mathf.toOneDimensionalIndex(i, j, rotationSegments);
		int idx1 = Mathf.toOneDimensionalIndex(i + 1, j, rotationSegments);
		int idx2 = Mathf.toOneDimensionalIndex(i + 1, (j + 1) % rotationSegments, rotationSegments);
		int idx3 = Mathf.toOneDimensionalIndex(i, (j + 1) % rotationSegments, rotationSegments);
		mesh.addFace(idx0, idx1, idx2, idx3);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		
		int n = heightSegments;
		for (int i = 0; i <= n; i++) {
			Mesh3D circle0 = new CircleCreator(rotationSegments, radius).create();
			Mesh3D circle1 = new CircleCreator(rotationSegments, minorRadius).create();
			circle0.translateY(0.5f * i);
			circle1.translateY(0.5f * i);
			if (i % 2 == 0) {
				mesh = Mesh3DUtil.append(mesh, circle1, circle0);
			} else {
				mesh = Mesh3DUtil.append(mesh, circle0, circle1);
			}
		}
		
		createQuadFaces();
		translate();
		subdivide();
		
		return mesh;
	}
	
	private void translate() {
		mesh.translateY(-heightSegments * 0.25f);
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

}

package mesh.creator.unsorted;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.wip.Mesh3DUtil;

public class NubCreator implements IMeshCreator {

	private float radius = 1;
	private float radius2 = 0.7f;
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
			Mesh3D circle1 = new CircleCreator(rotationSegments, radius2).create();
			circle0.translateY(0.5f * i);
			circle1.translateY(0.5f * i);
			if (i % 2 == 0) {
				mesh = Mesh3DUtil.append(mesh, circle1, circle0);
			} else {
				mesh = Mesh3DUtil.append(mesh, circle0, circle1);
			}
		}
		
		createQuadFaces();
		subdivide();
		
		return mesh;
	}
	
	private void subdivide() {
		new CatmullClarkModifier(1).modify(mesh);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
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

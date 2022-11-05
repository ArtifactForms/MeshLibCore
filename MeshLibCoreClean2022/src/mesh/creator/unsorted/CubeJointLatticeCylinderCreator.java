package mesh.creator.unsorted;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.util.Mesh3DUtil;

public class CubeJointLatticeCylinderCreator implements IMeshCreator {

	private int vertices;
	private int subdivisionsY;
	private float radius;
	private float height;
	private float jointSize;
	private float scale0;
	private float scale1;

	public CubeJointLatticeCylinderCreator() {
		this.vertices = 32;
		this.subdivisionsY = 6;
		this.radius = 1f;
		this.height = 2f;
		this.jointSize = 0.01f;
		this.scale0 = 0.5f;
		this.scale1 = 0.5f;
	}
	
	public CubeJointLatticeCylinderCreator(int vertices, int subdivisionsY,
			float radius, float height, float jointSize, float scale0,
			float scale1) {
		this.vertices = vertices;
		this.subdivisionsY = subdivisionsY;
		this.radius = radius;
		this.height = height;
		this.jointSize = jointSize;
		this.scale0 = scale0;
		this.scale1 = scale1;
	}
	
	@Override
	public Mesh3D create() {
		float dy = height / subdivisionsY;
		float angle = 0;
		float step = Mathf.TWO_PI / vertices;
		Mesh3D mesh = new Mesh3D();
		Mesh3D[][] cubes = new Mesh3D[subdivisionsY + 1][vertices];

		// Create joints
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {
				float x = radius * Mathf.cos(angle);
				float y = radius * Mathf.sin(angle);
				cubes[i][j] = new CubeCreator(jointSize).create();
				cubes[i][j].rotateY(-angle);
				cubes[i][j].translate(x, i * dy, y);
				mesh = Mesh3DUtil.append(mesh, cubes[i][j]);
				angle += step;
			}
			angle = 0;
		}

		// Connect joints
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {

				Face3D f0 = cubes[i][j].faces.get(3);
				Face3D f1 = cubes[i][(j + 1) % cubes[0].length].faces.get(5);
				Mesh3DUtil.extrudeFace(mesh, f0, scale0, 0.0f);
				Mesh3DUtil.extrudeFace(mesh, f1, scale0, 0.0f);
				Mesh3DUtil.flipDirection(mesh, f1);
				Mesh3DUtil.bridge(mesh, f0, f1);
				mesh.faces.remove(f0);
				mesh.faces.remove(f1);

				if ((i + 1) < cubes.length) {
					Face3D f2 = cubes[i][j].faces.get(1);
					Face3D f3 = cubes[i + 1][j].faces.get(0);
					Mesh3DUtil.extrudeFace(mesh, f2, scale1, 0.0f);
					Mesh3DUtil.extrudeFace(mesh, f3, scale1, 0.0f);
					Mesh3DUtil.flipDirection(mesh, f3);
					Mesh3DUtil.bridge(mesh, f2, f3);
					mesh.faces.remove(f2);
					mesh.faces.remove(f3);
				}
			}
		}

		mesh.translate(0, -subdivisionsY * dy / 2f, 0);

		return mesh;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public int getSubdivisionsY() {
		return subdivisionsY;
	}

	public void setSubdivisionsY(int subdivisionsY) {
		this.subdivisionsY = subdivisionsY;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getJointSize() {
		return jointSize;
	}

	public void setJointSize(float jointSize) {
		this.jointSize = jointSize;
	}

	public float getScale0() {
		return scale0;
	}

	public void setScale0(float scale0) {
		this.scale0 = scale0;
	}

	public float getScale1() {
		return scale1;
	}

	public void setScale1(float scale1) {
		this.scale1 = scale1;
	}

}

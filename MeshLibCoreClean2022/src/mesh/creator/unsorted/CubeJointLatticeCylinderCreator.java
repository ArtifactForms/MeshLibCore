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
	private Mesh3D mesh;

	private Mesh3D[][] cubes;

	public CubeJointLatticeCylinderCreator() {
		vertices = 32;
		subdivisionsY = 3;
		radius = 1f;
		height = 2f;
		jointSize = 0.01f;
		scale0 = 0.5f;
		scale1 = 0.5f;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		initializeCubes();
		createJoints();
		connectJoints();
		centerOnAxisY();
		return mesh;
	}

	private void createJoints() {
		float dy = height / subdivisionsY;
		float angle = 0;
		float step = Mathf.TWO_PI / vertices;

		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {
				float x = radius * Mathf.cos(angle);
				float y = radius * Mathf.sin(angle);
				cubes[i][j] = new CubeCreator(jointSize).create();
				cubes[i][j].rotateY(-angle);
				cubes[i][j].translate(x, i * dy, y);
				mesh.append(cubes[i][j]);
				angle += step;
			}
			angle = 0;
		}
	}

	private void connectJoints() {
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {
				connectJointsAt(i, j);
			}
		}
	}

	private void connectJointsAt(int i, int j) {
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

	private void initializeCubes() {
		cubes = new Mesh3D[subdivisionsY + 1][vertices];
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void centerOnAxisY() {
		mesh.translate(0, -subdivisionsY * (height / subdivisionsY) / 2f, 0);
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

package mesh.creator.platonic;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class IcosahedronCreator implements IMeshCreator {

	private static final float[][] VERTICES = { { 0.000000f, -1.000000f, 0.000000f },
			{ 0.723600f, -0.447215f, 0.525720f }, { -0.276385f, -0.447215f, 0.850640f },
			{ -0.894425f, -0.447215f, 0.000000f }, { -0.276385f, -0.447215f, -0.850640f },
			{ 0.723600f, -0.447215f, -0.525720f }, { 0.276385f, 0.447215f, 0.850640f },
			{ -0.723600f, 0.447215f, 0.525720f }, { -0.723600f, 0.447215f, -0.525720f },
			{ 0.276385f, 0.447215f, -0.850640f }, { 0.894425f, 0.447215f, -0.000000f },
			{ 0.000000f, 1.000000f, -0.000000f }, };

	private static final int[][] FACES = { { 2, 0, 1 }, { 1, 0, 5 }, { 3, 0, 2 }, { 4, 0, 3 }, { 5, 0, 4 },
			{ 1, 5, 10 }, { 2, 1, 6 }, { 3, 2, 7 }, { 4, 3, 8 }, { 5, 4, 9 }, { 10, 6, 1 }, { 6, 7, 2 }, { 7, 8, 3 },
			{ 8, 9, 4 }, { 9, 10, 5 }, { 6, 10, 11 }, { 7, 6, 11 }, { 8, 7, 11 }, { 9, 8, 11 }, { 10, 9, 11 }, };

	private float size;

	private Mesh3D mesh;

	public IcosahedronCreator() {
		this(1);
	}

	public IcosahedronCreator(float size) {
		this.size = size;
	}

	private void createVertices() {
		for (int i = 0; i < VERTICES.length; i++) {
			mesh.add(new Vector3f(VERTICES[i][0], VERTICES[i][1], VERTICES[i][2]));
		}
	}

	private void createFaces() {
		for (int i = 0; i < FACES.length; i++) {
			mesh.add(new Face3D(FACES[i][0], FACES[i][1], FACES[i][2]));
		}
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		scaleMesh();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void scaleMesh() {
		mesh.scale(size);
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

}

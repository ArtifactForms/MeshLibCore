package mesh.creator.unsorted;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.util.Mesh3DUtil;

public class CubeJointLatticeCreator implements IMeshCreator {

	private int subdivisionsX;
	private int subdivisionsY;
	private float tileSizeX;
	private float tileSizeY;
	private float jointSize;
	private float scaleX;
	private float scaleY;
	private Mesh3D mesh;
	private Mesh3D[][] cubes;
	
	public CubeJointLatticeCreator() {
		this.subdivisionsX = 10;
		this.subdivisionsY = 10;
		this.tileSizeX = 0.1f;
		this.tileSizeY = 0.1f;
		this.jointSize = 0.01f;
		this.scaleX = 0.5f;
		this.scaleY = 0.5f;
	}
	
	public CubeJointLatticeCreator(int subdivisionsX, int subdivisionsY,
			float tileSizeX, float tileSizeY, float jointSize, float scaleX,
			float scaleY) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsY = subdivisionsY;
		this.tileSizeX = tileSizeX;
		this.tileSizeY = tileSizeY;
		this.jointSize = jointSize;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public Mesh3D create() {
		initializeCubes();
		initializeMesh();
		createJoints();
		connectJoints();
		centerOnAxisY();
		return mesh;
	}
	
	private void initializeCubes() {
		cubes = new Mesh3D[subdivisionsY + 1][subdivisionsX + 1];
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createJoints() {
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {
				cubes[i][j] = new CubeCreator(jointSize).create();
				cubes[i][j].translate(j * tileSizeX, i * tileSizeY, 0);
				mesh.append(cubes[i][j]);
			}
		}
	}

	private void connectJoints() {
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {
				if ((j + 1) < cubes[0].length)
					leftRight(i, j);
				if ((i + 1) < cubes.length)
					topBottom(i, j);
			}
		}
	}

	private void topBottom(int i, int j) {
		Face3D f2 = cubes[i][j].faces.get(1); // bottom
		Face3D f3 = cubes[i + 1][j].faces.get(0); // top
		Mesh3DUtil.extrudeFace(mesh, f2, scaleY, 0.0f);
		Mesh3DUtil.extrudeFace(mesh, f3, scaleY, 0.0f);
		Mesh3DUtil.flipDirection(mesh, f3);
		Mesh3DUtil.bridge(mesh, f2, f3);
		removeFace(f2);
		removeFace(f3);
	}

	private void leftRight(int i, int j) {
		Face3D f0 = cubes[i][j].faces.get(2); // right
		Face3D f1 = cubes[i][j + 1].faces.get(4); // left
		Mesh3DUtil.extrudeFace(mesh, f0, scaleX, 0.0f);
		Mesh3DUtil.extrudeFace(mesh, f1, scaleX, 0.0f);
		Mesh3DUtil.flipDirection(mesh, f1);
		Mesh3DUtil.bridge(mesh, f0, f1);
		removeFace(f0);
		removeFace(f1);
	}
	
	private void removeFace(Face3D face) {
		mesh.removeFace(face);
	}
	
	private void centerOnAxisY() {
		mesh.translate(-subdivisionsX * tileSizeX / 2f, -subdivisionsY * tileSizeY / 2f, 0);
	}

	public int getSubdivisionsX() {
		return subdivisionsX;
	}

	public void setSubdivisionsX(int subdivisionsX) {
		this.subdivisionsX = subdivisionsX;
	}

	public int getSubdivisionsY() {
		return subdivisionsY;
	}

	public void setSubdivisionsY(int subdivisionsY) {
		this.subdivisionsY = subdivisionsY;
	}

	public float getTileSizeX() {
		return tileSizeX;
	}

	public void setTileSizeX(float tileSizeX) {
		this.tileSizeX = tileSizeX;
	}

	public float getTileSizeY() {
		return tileSizeY;
	}

	public void setTileSizeY(float tileSizeY) {
		this.tileSizeY = tileSizeY;
	}

	public float getJointSize() {
		return jointSize;
	}

	public void setJointSize(float jointSize) {
		this.jointSize = jointSize;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
}

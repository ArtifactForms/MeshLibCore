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
		subdivisionsX = 10;
		subdivisionsY = 10;
		tileSizeX = 0.1f;
		tileSizeY = 0.1f;
		jointSize = 0.01f;
		scaleX = 0.5f;
		scaleY = 0.5f;
	}

	@Override
	public Mesh3D create() {
		initializeCubes();
		initializeMesh();
		createJoints();
		connectJoints();
		centerOnAxisXY();
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
					connectRightLeft(i, j);
				if ((i + 1) < cubes.length)
					connectBottomTop(i, j);
			}
		}
	}

	private void connectBottomTop(int i, int j) {
		Face3D bottom = cubes[i][j].faces.get(1);
		Face3D top = cubes[i + 1][j].faces.get(0);
		connect(bottom, top, scaleY);
	}

	private void connectRightLeft(int i, int j) {
		Face3D right = cubes[i][j].faces.get(2);
		Face3D left = cubes[i][j + 1].faces.get(4);
		connect(right, left, scaleX);
	}
	
	private void connect(Face3D f0, Face3D f1, float scale) {
		extrude(f0, scale);
		extrude(f1, scale);
		flipDirection(f1);
		Mesh3DUtil.bridge(mesh, f0, f1);
		removeFace(f0);
		removeFace(f1);
	}
	
	private void flipDirection(Face3D face) {
		Mesh3DUtil.flipDirection(mesh, face);
	}
	
	private void extrude(Face3D face, float scale) {
		Mesh3DUtil.extrudeFace(mesh, face, scale, 0.0f);
	}
	
	private void removeFace(Face3D face) {
		mesh.removeFace(face);
	}
	
	private void centerOnAxisXY() {
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

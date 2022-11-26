package mesh.creator.unsorted;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.util.Mesh3DUtil;

public class CubeJointLatticeCubeCreator implements IMeshCreator {

	private int subdivisionsX;
	private int subdivisionsY;
	private int subdivisionsZ;
	private float tileSizeX;
	private float tileSizeY;
	private float tileSizeZ;
	private float jointSize;
	private float scaleX;
	private float scaleY;
	private float scaleZ;
	private Mesh3D mesh;
	private Mesh3D[][][] cubes;

	public CubeJointLatticeCubeCreator() {
		subdivisionsX = 10;
		subdivisionsY = 10;
		subdivisionsZ = 10;
		tileSizeX = 0.1f;
		tileSizeY = 0.1f;
		tileSizeZ = 0.1f;
		jointSize = 0.01f;
		scaleX = 0.5f;
		scaleY = 0.5f;
		scaleZ = 0.5f;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		initializeCubes();
		createJoints();
		connectJoints();
		centerOnOrigin();
		return mesh;
	}
	
	private void createJoints() {
		for (int k = 0; k < cubes[0][0].length; k++) {
			for (int i = 0; i < cubes.length; i++) {
				for (int j = 0; j < cubes[0].length; j++) {
					cubes[i][j][k] = new CubeCreator(jointSize).create();
					cubes[i][j][k].translate(j * tileSizeX, i * tileSizeY, k * tileSizeZ);
					mesh.append(cubes[i][j][k]);
				}
			}
		}
	}

	private void connectJoints() {
		for (int k = 0; k < cubes[0][0].length; k++) {
			for (int i = 0; i < cubes.length; i++) {
				for (int j = 0; j < cubes[0].length; j++) {
					if ((j + 1) < cubes[0].length)
						connectRightLeft(k, i, j);
					if ((i + 1) < cubes.length)
						connectBottomTop(k, i, j);
					if ((k + 1) < cubes[0][0].length)
						connectFrontBack(k, i, j);
				}
			}
		}
	}

	private void connect(Face3D a, Face3D b, float extrude) {
		Mesh3DUtil.extrudeFace(mesh, a, extrude, 0.0f);
		Mesh3DUtil.extrudeFace(mesh, b, extrude, 0.0f);
		Mesh3DUtil.flipDirection(mesh, b);
		Mesh3DUtil.bridge(mesh, a, b);
		mesh.faces.remove(a);
		mesh.faces.remove(b);
	}

	private void connectBottomTop(int k, int i, int j) {
		Face3D bottom = cubes[i][j][k].faces.get(1);
		Face3D top = cubes[i + 1][j][k].faces.get(0);
		connect(bottom, top, scaleY);
	}

	private void connectFrontBack(int k, int i, int j) {
		Face3D front = cubes[i][j][k].faces.get(3);
		Face3D back = cubes[i][j][k + 1].faces.get(5);
		connect(front, back, scaleZ);
	}

	private void connectRightLeft(int k, int i, int j) {
		Face3D right = cubes[i][j][k].faces.get(2);
		Face3D left = cubes[i][j + 1][k].faces.get(4);
		connect(right, left, scaleX);
	}

	private void centerOnOrigin() {
		mesh.translate(-subdivisionsX * tileSizeX / 2f, -subdivisionsY * tileSizeY / 2f,
				-subdivisionsZ * tileSizeZ / 2f);
	}

	private void initializeCubes() {
		cubes = new Mesh3D[subdivisionsY + 1][subdivisionsX + 1][subdivisionsZ + 1];
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
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

	public int getSubdivisionsZ() {
		return subdivisionsZ;
	}

	public void setSubdivisionsZ(int subdivisionsZ) {
		this.subdivisionsZ = subdivisionsZ;
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

	public float getTileSizeZ() {
		return tileSizeZ;
	}

	public void setTileSizeZ(float tileSizeZ) {
		this.tileSizeZ = tileSizeZ;
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

	public float getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}

}

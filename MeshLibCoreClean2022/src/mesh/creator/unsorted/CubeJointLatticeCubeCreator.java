package mesh.creator.unsorted;

import java.util.List;

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
		subdivisionsX = 5;
		subdivisionsY = 5;
		subdivisionsZ = 5;
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
		centerAtOrigin();
		return mesh;
	}

	private void createJoints() {
		for (int z = 0; z < cubes[0][0].length; z++)
			for (int y = 0; y < cubes.length; y++)
				for (int x = 0; x < cubes[0].length; x++)
					createJointAt(x, y, z);
	}

	private void createJointAt(int x, int y, int z) {
		cubes[y][x][z] = new CubeCreator(jointSize).create();
		cubes[y][x][z].translate(x * tileSizeX, y * tileSizeY, z * tileSizeZ);
		mesh.append(cubes[y][x][z]);
	}

	private void connectJoints() {
		for (int z = 0; z < cubes[0][0].length; z++)
			for (int y = 0; y < cubes.length; y++)
				for (int x = 0; x < cubes[0].length; x++)
					connectJointAt(x, y, z);
	}

	private void connectJointAt(int x, int y, int z) {
		if ((x + 1) < cubes[0].length)
			connectRightLeft(x, y, z);
		if ((y + 1) < cubes.length)
			connectBottomTop(x, y, z);
		if ((z + 1) < cubes[0][0].length)
			connectFrontBack(x, y, z);
	}

	private void connect(Face3D a, Face3D b, float extrude) {
		Mesh3DUtil.extrudeFace(mesh, a, extrude, 0.0f);
		Mesh3DUtil.extrudeFace(mesh, b, extrude, 0.0f);
		Mesh3DUtil.flipDirection(mesh, b);
		Mesh3DUtil.bridge(mesh, a, b);
		mesh.faces.remove(a);
		mesh.faces.remove(b);
	}
	
	private void connectRightLeft(int x, int y, int z) {
		Face3D right = getRightFaceOfCubeAt(x, y, z);
		Face3D left = getLeftFaceOfCubeAt(x + 1, y, z);
		connect(right, left, scaleX);
	}

	private void connectBottomTop(int x, int y, int z) {
		Face3D bottom = getBottomFaceOfCubeAt(x, y, z);
		Face3D top = getTopFaceOfCubeAt(x, y + 1, z);
		connect(bottom, top, scaleY);
	}

	private void connectFrontBack(int x, int y, int z) {
		Face3D front = getFrontFaceOfCubeAt(x, y, z);
		Face3D back = getBackFaceOfCubeAt(x, y, z + 1);
		connect(front, back, scaleZ);
	}

	private List<Face3D> getFacesOfCubeAt(int x, int y, int z) {
		return cubes[y][x][z].faces;
	}
	
	private Face3D getTopFaceOfCubeAt(int x, int y, int z) {
		return getFacesOfCubeAt(x, y, z).get(0);
	}
	
	private Face3D getBottomFaceOfCubeAt(int x, int y, int z) {
		return getFacesOfCubeAt(x, y, z).get(1);
	}
	
	private Face3D getRightFaceOfCubeAt(int x, int y, int z) {
		return getFacesOfCubeAt(x, y, z).get(2);
	}
	
	private Face3D getFrontFaceOfCubeAt(int x, int y, int z) {
		return getFacesOfCubeAt(x, y, z).get(3);
	}
	
	private Face3D getLeftFaceOfCubeAt(int x, int y, int z) {
		return getFacesOfCubeAt(x, y, z).get(4);
	}
	
	private Face3D getBackFaceOfCubeAt(int x, int y, int z) {
		return getFacesOfCubeAt(x, y, z).get(5);
	}

	private void centerAtOrigin() {
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

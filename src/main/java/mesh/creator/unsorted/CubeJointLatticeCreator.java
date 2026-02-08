package mesh.creator.unsorted;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.topology.FlipFacesModifier;
import mesh.modifier.transform.TranslateModifier;
import mesh.util.FaceBridging;
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

	private FlipFacesModifier flipFacesModifier;

	public CubeJointLatticeCreator() {
		subdivisionsX = 10;
		subdivisionsY = 10;
		tileSizeX = 0.1f;
		tileSizeY = 0.1f;
		jointSize = 0.01f;
		scaleX = 0.5f;
		scaleY = 0.5f;
		flipFacesModifier = new FlipFacesModifier();
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
				cubes[i][j].apply(new TranslateModifier(j * tileSizeX, i * tileSizeY, 0));
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

	private void connect(Face3D face0, Face3D face1, float scale) {
		extrude(face0, scale);
		extrude(face1, scale);
		flipDirection(face1);
		bridge(face0, face1);
		removeFace(face0);
		removeFace(face1);
	}

	private void bridge(Face3D face0, Face3D face1) {
		FaceBridging.bridge(mesh, face0, face1);
	}

	private void flipDirection(Face3D face) {
		flipFacesModifier.modify(mesh, face);
	}

	private void extrude(Face3D face, float scale) {
		Mesh3DUtil.extrudeFace(mesh, face, scale, 0.0f);
	}

	private void removeFace(Face3D face) {
		mesh.removeFace(face);
	}

	private void centerOnAxisXY() {
		float tx = -subdivisionsX * tileSizeX / 2f;
		float ty = -subdivisionsY * tileSizeY / 2f;
		mesh.apply(new TranslateModifier(tx, ty, 0));
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

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

	public CubeJointLatticeCubeCreator() {
		this.subdivisionsX = 10;
		this.subdivisionsY = 10;
		this.subdivisionsZ = 10;
		this.tileSizeX = 0.1f;
		this.tileSizeY = 0.1f;
		this.tileSizeZ = 0.1f;
		this.jointSize = 0.01f;
		this.scaleX = 0.5f;
		this.scaleY = 0.5f;
		this.scaleZ = 0.5f;
	}

	public CubeJointLatticeCubeCreator(int subdivisionsX, int subdivisionsY,
			int subdivisionsZ, float tileSizeX, float tileSizeY,
			float tileSizeZ, float jointSize, float scaleX, float scaleY,
			float scaleZ) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsY = subdivisionsY;
		this.subdivisionsZ = subdivisionsZ;
		this.tileSizeX = tileSizeX;
		this.tileSizeY = tileSizeY;
		this.tileSizeZ = tileSizeZ;
		this.jointSize = jointSize;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();
		Mesh3D[][][] cubes = new Mesh3D[subdivisionsY + 1][subdivisionsX + 1][subdivisionsZ + 1];

		// Create joints
		for (int k = 0; k < cubes[0][0].length; k++) {
			for (int i = 0; i < cubes.length; i++) {
				for (int j = 0; j < cubes[0].length; j++) {
					cubes[i][j][k] = new CubeCreator(jointSize).create();
					cubes[i][j][k].translate(j * tileSizeX, i * tileSizeY, k
							* tileSizeZ);
					mesh = Mesh3DUtil.append(mesh, cubes[i][j][k]);
				}
			}
		}

		// Connect joints
		for (int k = 0; k < cubes[0][0].length; k++) {
			for (int i = 0; i < cubes.length; i++) {
				for (int j = 0; j < cubes[0].length; j++) {

					if ((j + 1) < cubes[0].length) {
						Face3D f0 = cubes[i][j][k].faces.get(2); // right
						Face3D f1 = cubes[i][j + 1][k].faces.get(4); // left
						Mesh3DUtil.extrudeFace(mesh, f0, scaleX, 0.0f);
						Mesh3DUtil.extrudeFace(mesh, f1, scaleX, 0.0f);
						Mesh3DUtil.flipDirection(mesh, f1);
						Mesh3DUtil.bridge(mesh, f0, f1);
						mesh.faces.remove(f0);
						mesh.faces.remove(f1);
					}

					if ((i + 1) < cubes.length) {
						Face3D f2 = cubes[i][j][k].faces.get(1); // bottom
						Face3D f3 = cubes[i + 1][j][k].faces.get(0); // top
						Mesh3DUtil.extrudeFace(mesh, f2, scaleY, 0.0f);
						Mesh3DUtil.extrudeFace(mesh, f3, scaleY, 0.0f);
						Mesh3DUtil.flipDirection(mesh, f3);
						Mesh3DUtil.bridge(mesh, f2, f3);
						mesh.faces.remove(f2);
						mesh.faces.remove(f3);
					}
					
					if ((k + 1) < cubes[0][0].length) {
						Face3D f2 = cubes[i][j][k].faces.get(3); // front
						Face3D f3 = cubes[i][j][k + 1].faces.get(5); // back
						Mesh3DUtil.extrudeFace(mesh, f2, scaleZ, 0.0f);
						Mesh3DUtil.extrudeFace(mesh, f3, scaleZ, 0.0f);
						Mesh3DUtil.flipDirection(mesh, f3);
						Mesh3DUtil.bridge(mesh, f2, f3);
						mesh.faces.remove(f2);
						mesh.faces.remove(f3);
					}
				}
			}
		}
		
		mesh.translate(-subdivisionsX * tileSizeX / 2f, -subdivisionsY
				* tileSizeY / 2f, -subdivisionsZ * tileSizeZ / 2f);

		return mesh;
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

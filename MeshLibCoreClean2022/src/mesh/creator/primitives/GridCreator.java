package mesh.creator.primitives;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class GridCreator implements IMeshCreator {

	private int subdivisionsX;
	
	private int subdivisionsZ;
	
	private float tileSizeX;
	
	private float tileSizeZ;
	
	private float offsetX;
	
	private float offsetZ;
	
	private Mesh3D mesh;
	
	public GridCreator() {
		subdivisionsX = 10;
		subdivisionsZ = 10;
		tileSizeX = 0.1f;
		tileSizeZ = 0.1f;
	}
	
	public GridCreator(int subdivisionsX, int subdivisionsZ, float radius) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsZ = subdivisionsZ;
		this.tileSizeX = (radius + radius) / subdivisionsX;
		this.tileSizeZ = (radius + radius) / subdivisionsZ;
	}
	
	public GridCreator(int subdivisionsX, int subdivisionsZ, float tileSizeX,
			float tileSizeZ) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsZ = subdivisionsZ;
		this.tileSizeX = tileSizeX;
		this.tileSizeZ = tileSizeZ;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		updateOffset();
		createVertices();
		createFaces();
		return mesh;
	}
	
	private void updateOffset() {
		offsetX = (tileSizeX * subdivisionsX) / 2f;
		offsetZ = (tileSizeZ * subdivisionsZ) / 2f;
	}
	
	private void createVertices() {
		for (int i = 0; i <= subdivisionsZ; i++) {
			for (int j = 0; j <= subdivisionsX; j++) {
				Vector3f v = new Vector3f(j * tileSizeX, 0, i * tileSizeZ);
				v.subtractLocal(offsetX, 0, offsetZ);
				mesh.vertices.add(v);
			}
		}
	}
	
	private void createFaces() {
		for (int i = 0; i < subdivisionsZ; i++) {
			for (int j = 0; j < subdivisionsX; j++) {
				int index = i * (subdivisionsX + 1) + j;
				Face3D face = new Face3D(index, index + 1, index
						+ subdivisionsX + 2, index + subdivisionsX + 1);
				mesh.faces.add(face);
			}
		}
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

	public float getTileSizeZ() {
		return tileSizeZ;
	}

	public void setTileSizeZ(float tileSizeZ) {
		this.tileSizeZ = tileSizeZ;
	}
	
}

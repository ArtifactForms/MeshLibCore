package mesh.creator.unsorted;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;
import mesh.modifier.SolidifyModifier;
import mesh.wip.Mesh3DUtil;

public class LatticeCreator implements IMeshCreator {

	private int subdivisionsX;
	private int subdivisionsZ;
	private float openingPercent;
	private float tileSizeX;
	private float tileSizeZ;
	private float height;
	private Mesh3D mesh;

	public LatticeCreator() {
		this.subdivisionsX = 10;
		this.subdivisionsZ = 10;
		this.openingPercent = 0.5f;
		this.tileSizeX = 0.2f;
		this.tileSizeZ = 0.2f;
		this.height = 0.2f;
	}

	public LatticeCreator(int subdivisionsX, int subdivisionsZ,
			float tileSizeX, float tileSizeZ, float height) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsZ = subdivisionsZ;
		this.openingPercent = 0.5f;
		this.tileSizeX = tileSizeX;
		this.tileSizeZ = tileSizeZ;
		this.height = height;
	}

	public LatticeCreator(int subdivisionsX, int subdivisionsZ, float scale,
			float tileSizeX, float tileSizeZ, float height) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsZ = subdivisionsZ;
		this.openingPercent = scale;
		this.tileSizeX = tileSizeX;
		this.tileSizeZ = tileSizeZ;
		this.height = height;
	}
	
	private void createFaces() {
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D f : faces) {
			Mesh3DUtil.extrudeFace(mesh, f, openingPercent, 0f);
		}
		mesh.faces.removeAll(faces);
	}

	@Override
	public Mesh3D create() {
		mesh = new GridCreator(subdivisionsX, subdivisionsZ, tileSizeX,
				tileSizeZ).create();
		createFaces();
		new SolidifyModifier(height).modify(mesh);
		mesh.translateY(-height / 2f);
		return mesh;
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

	public float getOpeningPercent() {
		return openingPercent;
	}

	public void setOpeningPercent(float openingPercent) {
		this.openingPercent = openingPercent;
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

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}

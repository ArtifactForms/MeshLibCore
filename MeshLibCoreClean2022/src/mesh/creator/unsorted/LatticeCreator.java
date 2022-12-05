package mesh.creator.unsorted;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;
import mesh.modifier.SolidifyModifier;
import mesh.util.Mesh3DUtil;

public class LatticeCreator implements IMeshCreator {

	private int subdivisionsX;
	private int subdivisionsZ;
	private float openingPercent;
	private float tileSizeX;
	private float tileSizeZ;
	private float height;
	private Mesh3D mesh;

	public LatticeCreator() {
		subdivisionsX = 10;
		subdivisionsZ = 10;
		openingPercent = 0.5f;
		tileSizeX = 0.2f;
		tileSizeZ = 0.2f;
		height = 0.2f;
	}
	
	private void createFaces() {
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D f : faces)
			Mesh3DUtil.extrudeFace(mesh, f, openingPercent, 0f);
		mesh.faces.removeAll(faces);
	}

	@Override
	public Mesh3D create() {
		createBaseGrid();
		createFaces();
		solidify();
		centerOnAxisY();
		return mesh;
	}
	
	private void createBaseGrid() {
		GridCreator creator = new GridCreator();
		creator.setSubdivisionsX(subdivisionsX);
		creator.setSubdivisionsZ(subdivisionsZ);
		creator.setTileSizeX(tileSizeX);
		creator.setTileSizeZ(tileSizeZ);
		mesh = creator.create();
	}
	
	private void centerOnAxisY() {
		mesh.translateY(-height / 2f);
	}
	
	private void solidify() {
		new SolidifyModifier(height).modify(mesh);
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

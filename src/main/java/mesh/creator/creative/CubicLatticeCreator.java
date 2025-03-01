package mesh.creator.creative;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.CenterAtModifier;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.TranslateModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.selection.FaceSelection;

public class CubicLatticeCreator implements IMeshCreator {

	private int segmentsX;

	private int segmentsY;

	private int segmentsZ;

	private int subdivisions;

	private Mesh3D mesh;

	public CubicLatticeCreator() {
		segmentsX = 3;
		segmentsY = 3;
		segmentsZ = 3;
		subdivisions = 1;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createSegments();
		removeDoubleFaces();
		removeDoubleVertices();
		centerAtOrigin();
		solidify();
		subdivide();
		return mesh;
	}

	private Mesh3D createSegment() {
		Mesh3D mesh = new CubeCreator().create();
		ExtrudeModifier modifier = new ExtrudeModifier();
		modifier.setScale(1.0f);
		modifier.setAmount(0.5f);
		modifier.setRemoveFaces(true);
		modifier.modify(mesh);
		return mesh;
	}

	private void createSegments() {
		for (int z = 0; z < segmentsZ; z++)
			for (int y = 0; y < segmentsY; y++)
				for (int x = 0; x < segmentsX; x++)
					createSegmentAt(x, y, z);
	}

	private void createSegmentAt(int x, int y, int z) {
		Mesh3D segment = createSegment();
		segment.apply(new TranslateModifier(x * 3, y * 3, z * 3));
		this.mesh.append(segment);
	}

	private void removeDoubleFaces() {
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectDoubles();
		mesh.removeFaces(selection.getFaces());
	}

	private void removeDoubleVertices() {
		mesh.removeDoubles();
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void centerAtOrigin() {
		new CenterAtModifier(Vector3f.ZERO).modify(mesh);
	}

	private void subdivide() {
		new CatmullClarkModifier(subdivisions).modify(mesh);
	}

	private void solidify() {
		new SolidifyModifier(0.3f).modify(mesh);
	}

	public int getSegmentsX() {
		return segmentsX;
	}

	public void setSegmentsX(int segmentsX) {
		this.segmentsX = segmentsX;
	}

	public int getSegmentsY() {
		return segmentsY;
	}

	public void setSegmentsY(int segmentsY) {
		this.segmentsY = segmentsY;
	}

	public int getSegmentsZ() {
		return segmentsZ;
	}

	public void setSegmentsZ(int segmentsZ) {
		this.segmentsZ = segmentsZ;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}

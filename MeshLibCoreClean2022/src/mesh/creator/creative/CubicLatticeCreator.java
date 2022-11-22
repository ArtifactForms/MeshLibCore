package mesh.creator.creative;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class CubicLatticeCreator implements IMeshCreator {

	private int segmentsX = 3;
	private int segmentsY = 3;
	private int segmentsZ = 3;
	private int subdivisions = 2;
	private Mesh3D mesh;

	@Override
	public Mesh3D create() {
		initializeMesh();
		createSegments();
		removeDoubleFaces();
		removeDoubleVertices();
		translate();
		solidify();
		subdivide();
		return mesh;
	}

	private Mesh3D createSegment() {
		Mesh3D mesh = new CubeCreator().create();
		List<Face3D> faces = mesh.getFaces();
		for (Face3D face : faces)
			Mesh3DUtil.extrudeFace(mesh, face, 1.0f, 0.5f);
		mesh.faces.removeAll(faces);
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
		segment.translate(x * 3, y * 3, z * 3);
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

	private void translate() {
		mesh.translateX(-((segmentsX - 1) * 3f) / 2f);
		mesh.translateY(-((segmentsY - 1) * 3f) / 2f);
		mesh.translateZ(-((segmentsZ - 1) * 3f) / 2f);
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

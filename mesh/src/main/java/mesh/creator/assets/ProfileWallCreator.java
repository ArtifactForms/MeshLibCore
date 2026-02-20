package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.special.QuadStripCreator;
import mesh.modifier.topology.FlipFacesModifier;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;
import mesh.selection.FaceSelection;

public class ProfileWallCreator implements IMeshCreator {

	private int segmentsCount;

	private float y;

	private float maxDepth;

	private float width;

	private boolean corner;

	private QuadStripCreator creator;

	private Mesh3D mesh;

	public ProfileWallCreator() {
		creator = new QuadStripCreator();
		width = 2;
	}

	public void clear() {
		segmentsCount = 0;
		y = 0;
		maxDepth = 0;
		creator.clear();
	}

	@Override
	public Mesh3D create() {
		mesh = createSegment();
		createCorner();
		return mesh;
	}

	private void createInnerCornerFaces() {
		if (!corner)
			return;

		int indexA = segmentsCount * 2;
		int indexB = segmentsCount * 6;

		for (int i = 0; i < segmentsCount * 2 - 2; i += 2) {
			int d = indexA + i;
			int c = indexB + i;
			int b = indexB + i + 2;
			int a = indexA + i + 2;
			mesh.addFace(a, b, c, d);
		}
	}

	private void createOuterCornerFaces() {
		if (!corner)
			return;

		int indexA = segmentsCount * 2;
		int indexB = segmentsCount * 6;

		for (int i = 0; i < segmentsCount * 2 - 2; i += 2) {
			int a = indexA + i + 1;
			int b = indexB + i + 1;
			int c = indexB + i + 3;
			int d = indexA + i + 3;
			mesh.addFace(a, b, c, d);
		}
	}

	private void createBottomCornerFace() {
		int a = segmentsCount * 2;
		int b = a + 1;
		int c = segmentsCount * 6;
		int d = c + 1;
		mesh.addFace(c, d, b, a);
	}

	private void createTopCornerFace() {
		int a = segmentsCount * 4 - 2;
		int b = a + 1;
		int c = segmentsCount * 8 - 2;
		int d = c + 1;
		mesh.addFace(a, b, d, c);
	}

	private void createCorner() {
		if (!corner)
			return;
		createSecondSegment();
		createInnerCornerFaces();
		createOuterCornerFaces();
		createBottomCornerFace();
		createTopCornerFace();
	}

	private void createSecondSegment() {
		Mesh3D mesh2 = createSegment();
		mesh2.rotateY(-Mathf.HALF_PI);
		new ScaleModifier(1, 1, -1).modify(mesh2);
		new FlipFacesModifier().modify(mesh2);
		mesh.append(mesh2);
	}

	private Mesh3D createSegment() {
		Mesh3D mesh = creator.create();
		new SolidifyModifier(calculateWidth()).modify(mesh);
		new TranslateModifier(width / 2, 0, 0).modify(mesh);

		if (corner) {
			FaceSelection selection = new FaceSelection(mesh);
			selection.selectSimilarNormal(new Vector3f(-1, 0, 0), 0.1f);
			mesh.removeFaces(selection.getFaces());
		}

		return mesh;
	}

	private float calculateWidth() {
		float width = corner ? this.width / 2f : this.width;
		width -= corner ? maxDepth : 0;
		return width;
	}

	public void add(float depth, float height) {
		y += height;
		segmentsCount++;
		maxDepth = depth > maxDepth ? depth : maxDepth;
		creator.addVertex(0, -y, -depth / 2f);
		creator.addVertex(0, -y, depth / 2f);
	}

	public boolean isCorner() {
		return corner;
	}

	public void setCorner(boolean corner) {
		this.corner = corner;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

}

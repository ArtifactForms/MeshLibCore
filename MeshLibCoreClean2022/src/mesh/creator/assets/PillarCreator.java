package mesh.creator.assets;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CylinderCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class PillarCreator implements IMeshCreator {

	private int rotationSegments;
	
	private int topSegments;
	
	private int bottomSegments;
	
	private float topHeight;
	
	private float bottomHeight;
	
	private float centerHeight;
	
	private float radius;
	
	private FillType capFillType;
	
	private Mesh3D mesh;
	
	private FaceSelection selection;

	public PillarCreator() {
		rotationSegments = 8;
		topSegments = 2;
		bottomSegments = 2;
		topHeight = 1.0f;
		bottomHeight = 2.0f;
		centerHeight = 5.0f;
		radius = 1.0f;
		capFillType = FillType.N_GON;
	}

	private void processCaps() {
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectByVertexCount(rotationSegments);

		switch (capFillType) {
		case N_GON:
			break;
		case TRIANGLE_FAN:
			new PlanarVertexCenterModifier().modify(mesh, selection.getFaces());
			break;
		case NOTHING:
			mesh.removeFaces(selection.getFaces());
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + capFillType);
		}
	}

	private float getBottomSegmentHeight() {
		return bottomHeight / (float) bottomSegments;
	}

	private float getTopSegmentHeight() {
		return topHeight / (float) topSegments;
	}

	private void createBaseCylinder() {
		CylinderCreator creator = new CylinderCreator();
		creator.setVertices(rotationSegments);
		creator.setBottomRadius(radius);
		creator.setTopRadius(radius);
		creator.setHeight(getBottomSegmentHeight());
		creator.setTopCapFillType(FillType.N_GON);
		creator.setBottomCapFillType(FillType.N_GON);
		mesh = creator.create();
		mesh.translateY(-getBottomSegmentHeight() * 0.5f);
	}

	private void initSelectionAndselectTopFace() {
		selection = new FaceSelection(mesh);
		selection.selectTopFaces();
	}

	private void createBottom() {
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < bottomSegments - 1; i++) {
				Mesh3DUtil.extrudeFace(mesh, face, 0.9f, 0.0f);
				Mesh3DUtil.extrudeFace(mesh, face, 1.0f, getBottomSegmentHeight());
			}
		}
	}

	private void createCenter() {
		for (Face3D face : selection.getFaces()) {
			Mesh3DUtil.extrudeFace(mesh, face, 0.9f, 0.0f);
			Mesh3DUtil.extrudeFace(mesh, face, 1.0f, centerHeight);
		}
	}

	private void createTop() {
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < topSegments; i++) {
				Mesh3DUtil.extrudeFace(mesh, face, 1.1f, 0.0f);
				Mesh3DUtil.extrudeFace(mesh, face, 1.0f, getTopSegmentHeight());
			}
		}
	}

	@Override
	public Mesh3D create() {
		createBaseCylinder();
		initSelectionAndselectTopFace();
		createBottom();
		createCenter();
		createTop();
		processCaps();
		return mesh;
	}

	public int getRotationSegments() {
		return rotationSegments;
	}

	public void setRotationSegments(int rotationSegments) {
		this.rotationSegments = rotationSegments;
	}

	public int getTopSegments() {
		return topSegments;
	}

	public void setTopSegments(int topSegments) {
		this.topSegments = topSegments;
	}

	public int getBottomSegments() {
		return bottomSegments;
	}

	public void setBottomSegments(int bottomSegments) {
		this.bottomSegments = bottomSegments;
	}

	public float getTopHeight() {
		return topHeight;
	}

	public void setTopHeight(float topHeight) {
		this.topHeight = topHeight;
	}

	public float getBottomHeight() {
		return bottomHeight;
	}

	public void setBottomHeight(float bottomHeight) {
		this.bottomHeight = bottomHeight;
	}

	public float getCenterHeight() {
		return centerHeight;
	}

	public void setCenterHeight(float centerHeight) {
		this.centerHeight = centerHeight;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public FillType getCapFillType() {
		return capFillType;
	}

	public void setCapFillType(FillType capFillType) {
		this.capFillType = capFillType;
	}

}

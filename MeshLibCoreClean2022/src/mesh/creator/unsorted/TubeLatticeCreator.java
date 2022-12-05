package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.SegmentedTubeCreator;
import mesh.modifier.SolidifyModifier;
import mesh.operators.ExtrudeIndividualFacesOperator;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class TubeLatticeCreator implements IMeshCreator {

	private int segments;
	private int vertices;
	private float outerRadius;
	private float innerRadius;
	private float height;
	private float scaleExtrude;
	private float thickness;
	private Mesh3D mesh;

	public TubeLatticeCreator() {
		segments = 10;
		vertices = 32;
		outerRadius = 1f;
		innerRadius = 0.9f;
		height = 2f;
		scaleExtrude = 0.5f;
		thickness = 0.1f;
	}
	
	private SegmentedTubeCreator createSegmentedTubeCreator() {
		SegmentedTubeCreator creator = new SegmentedTubeCreator();
		creator.setSegments(segments);
		creator.setVertices(vertices);
		creator.setInnerRadius(innerRadius);
		creator.setOuterRadius(outerRadius);
		creator.setHeight(height);
		return creator;
	}
	
	private void createBaseSegmentedTube() {
		mesh = createSegmentedTubeCreator().create();
	}

	@Override
	public Mesh3D create() {
		createBaseSegmentedTube();
		createHoles();
		flipFaceNormals();
		solidify();
		return mesh;
	}
	
	private FaceSelection select() {
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectTopFaces();
		selection.selectBottomFaces();
		selection.invert();
		return selection;
	}
	
	private void createHoles() {
		ExtrudeIndividualFacesOperator operator = new ExtrudeIndividualFacesOperator(mesh);
		operator.setScale(scaleExtrude);
		operator.setRemoveFace(true);
		operator.apply(select().getFaces());
	}
	
	private void solidify() {
		new SolidifyModifier(thickness).modify(mesh);
	}
	
	private void flipFaceNormals() {
		Mesh3DUtil.flipDirection(mesh);
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
	}

	public float getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getScaleExtrude() {
		return scaleExtrude;
	}

	public void setScaleExtrude(float scaleExtrude) {
		this.scaleExtrude = scaleExtrude;
	}

}

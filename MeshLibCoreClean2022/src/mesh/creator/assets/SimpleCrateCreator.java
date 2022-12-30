package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.InsetModifier;
import mesh.selection.FaceSelection;

public class SimpleCrateCreator implements IMeshCreator {

	private float inset;
	private float extrudeAmount;
	private Mesh3D mesh;
	private FaceSelection faceSelection;

	public SimpleCrateCreator() {
		inset = 0.3f;
		extrudeAmount = 0.1f;
	}

	@Override
	public Mesh3D create() {
		createCube();
		initializeFaceSelection();
		selectAllFaces();
		insetSelectedFaces();
		extrudeSelectedFaces();
		snapToGround();
		return mesh;
	}

	private void insetSelectedFaces() {
		new InsetModifier(inset).modify(mesh, faceSelection.getFaces());
	}

	private void extrudeSelectedFaces() {
		new ExtrudeModifier(1, -extrudeAmount).modify(mesh, faceSelection.getFaces());
	}

	private void selectAllFaces() {
		faceSelection.selectAll();
	}

	private void initializeFaceSelection() {
		faceSelection = new FaceSelection(mesh);
	}

	private void createCube() {
		CubeCreator creator = new CubeCreator();
		mesh = creator.create();
	}

	private void snapToGround() {
		mesh.translateY(-1);
	}

	public float getInset() {
		return inset;
	}

	public void setInset(float inset) {
		this.inset = inset;
	}

	public float getExtrudeAmount() {
		return extrudeAmount;
	}

	public void setExtrudeAmount(float extrudeAmount) {
		this.extrudeAmount = extrudeAmount;
	}

}

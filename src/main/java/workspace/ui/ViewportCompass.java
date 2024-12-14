package workspace.ui;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.ConeCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.RotateXModifier;
import mesh.modifier.RotateZModifier;
import mesh.modifier.ScaleModifier;
import mesh.modifier.TranslateModifier;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;

public class ViewGizmo extends UiComponent {

	private static final float DEFAULT_HEIGHT = 2;

	private static final float DEFAULT_SIZE = 10;

	private float height;

	private float size;

	private float rotationX;

	private float rotationY;

	private float rotationZ;

	private Mesh3D cube;

	private Mesh3D coneX;

	private Mesh3D coneY;

	private Mesh3D coneZ;

	public ViewGizmo() {
		this.height = DEFAULT_HEIGHT;
		this.size = DEFAULT_SIZE;
		createMeshes();
	}

	@Override
	public void render(Graphics g) {
		g.pushMatrix();
		g.translate(x, y);
		g.rotateX(rotationX);
		g.rotateY(rotationY);
		g.rotateZ(rotationZ);

		renderMesh(g, cube, UiConstants.KEY_GIZMO_CENTER_COLOR);
		renderMesh(g, coneX, UiConstants.KEY_GIZMO_AXIS_X_COLOR);
		renderMesh(g, coneY, UiConstants.KEY_GIZMO_AXIS_Y_COLOR);
		renderMesh(g, coneZ, UiConstants.KEY_GIZMO_AXIS_Z_COLOR);

		g.popMatrix();
	}

	private void renderMesh(Graphics g, Mesh3D mesh, String colorKey) {
		g.setColor(UiValues.getColor(colorKey));
		g.fillFaces(mesh);
	}

	private void createMeshes() {
		createCube();
		createConeX();
		createConeY();
		createConeZ();
	}

	private void createConeX() {
		coneX = createCone();
		coneX.apply(new RotateZModifier(-Mathf.HALF_PI));
		coneX.apply(new TranslateModifier(height * size, 0, 0));
	}

	private void createConeY() {
		coneY = createCone();
		coneY.apply(new TranslateModifier(0, height * size, 0));
	}

	private void createConeZ() {
		coneZ = createCone();
		coneZ.apply(new RotateXModifier(Mathf.HALF_PI));
		coneZ.apply(new TranslateModifier(0, 0, height * size));
	}

	private Mesh3D createCone() {
		Mesh3D cone = new ConeCreator().create();
		cone.apply(new ScaleModifier(size));
		return cone;
	}

	private void createCube() {
		cube = new CubeCreator(size).create();
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

}

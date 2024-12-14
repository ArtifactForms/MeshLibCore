package workspace.ui;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.ConeCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.RotateXModifier;
import mesh.modifier.RotateZModifier;
import mesh.modifier.ScaleModifier;
import mesh.modifier.TranslateModifier;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;

public class ViewportCompass extends UiComponent {

	private static final float DEFAULT_HEIGHT = 2;

	private static final float DEFAULT_SIZE = 10;

	private float height;

	private float size;

	private Vector3f rotation;

	private Mesh3D cube;

	private Mesh3D coneX;

	private Mesh3D coneY;

	private Mesh3D coneZ;

	public ViewportCompass() {
		this.height = DEFAULT_HEIGHT;
		this.size = DEFAULT_SIZE;
		this.rotation = new Vector3f();
		createMeshes();
	}

	@Override
	public void render(Graphics g) {
		g.pushMatrix();
		g.translate(x, y);
		g.rotateX(rotation.x);
		g.rotateY(rotation.y);
		g.rotateZ(rotation.z);

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

	public void setRotation(Vector3f rotation) {
		this.rotation.setX(Mathf.clamp(rotation.x, -Mathf.PI, Mathf.PI));
		this.rotation.setY(Mathf.clamp(rotation.y, -Mathf.PI, Mathf.PI));
		this.rotation.setZ(Mathf.clamp(rotation.z, -Mathf.PI, Mathf.PI));
	}

}

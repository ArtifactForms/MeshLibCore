package workspace.ui.elements;

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
import workspace.ui.Graphics;
import workspace.ui.UiComponent;

/**
 * Represents a simple viewport compass in the upper-right corner of the mesh viewer UI. The compass
 * visualizes the current rotation/orientation of the viewport in 3D space. This component is
 * non-interactive and purely for visualization purposes.
 */
public class ViewportCompass extends UiComponent {

  private static final float DEFAULT_HEIGHT = 2;

  private static final float DEFAULT_SIZE = 10;

  private float size;

  private float height;

  private Vector3f rotation;

  private Mesh3D cube;

  private Mesh3D coneX;

  private Mesh3D coneY;

  private Mesh3D coneZ;

  /**
   * Default constructor initializes the compass with default height, size, and creates all required
   * meshes for rendering the compass.
   */
  public ViewportCompass() {
    this.size = DEFAULT_SIZE;
    this.height = DEFAULT_HEIGHT;
    this.rotation = new Vector3f();
    createMeshes();
  }

  /**
   * Renders the viewport compass in the UI's graphics context. This method sets the translation,
   * applies rotations, and renders the gizmo axes and center mesh.
   *
   * @param g The graphics context for rendering.
   */
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

  /**
   * Helper method to render a given mesh with its associated color key.
   *
   * @param g The graphics context for rendering.
   * @param mesh The mesh to render.
   * @param colorKey The key identifying the color to use from UiValues.
   */
  private void renderMesh(Graphics g, Mesh3D mesh, String colorKey) {
    g.setColor(UiValues.getColor(colorKey));
    g.fillFaces(mesh);
  }

  /** Creates and initializes all necessary meshes for the compass visualization. */
  private void createMeshes() {
    createCube();
    createConeX();
    createConeY();
    createConeZ();
  }

  /** Creates the X-axis cone and applies necessary transformations to position and rotate it. */
  private void createConeX() {
    coneX = createCone();
    coneX.apply(new RotateZModifier(-Mathf.HALF_PI));
    coneX.apply(new TranslateModifier(height * size, 0, 0));
  }

  /**
   * Creates the Y-axis cone and applies necessary transformations to position it in the Y
   * direction.
   */
  private void createConeY() {
    coneY = createCone();
    coneY.apply(new TranslateModifier(0, height * size, 0));
  }

  /**
   * Creates the Z-axis cone and applies transformations to rotate and position it in the Z
   * direction.
   */
  private void createConeZ() {
    coneZ = createCone();
    coneZ.apply(new RotateXModifier(Mathf.HALF_PI));
    coneZ.apply(new TranslateModifier(0, 0, height * size));
  }

  /**
   * Creates a cone mesh, scales it appropriately, and prepares it for rendering.
   *
   * @return The scaled and ready-to-render cone mesh.
   */
  private Mesh3D createCone() {
    Mesh3D cone = new ConeCreator().create();
    cone.apply(new ScaleModifier(size));
    return cone;
  }

  /** Creates the central cube for visualization. */
  private void createCube() {
    cube = new CubeCreator(size).create();
  }

  /**
   * Sets the viewport's rotation.
   *
   * @param rotation The desired rotation vector.
   */
  public void setRotation(Vector3f rotation) {
    this.rotation.setX(rotation.x);
    this.rotation.setY(rotation.y);
    this.rotation.setZ(rotation.z);
  }
}

package workspace.ui;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.ConeCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.ScaleModifier;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;

public class ViewGizmo extends UiComponent {

    private float height = 2;

    private float size = 10;

    private float rotationX;

    private float rotationY;

    private float rotationZ;

    private Mesh3D cube;

    private Mesh3D coneX;

    private Mesh3D coneY;

    private Mesh3D coneZ;

    public ViewGizmo() {
        createMeshes();
    }

    public void draw(Graphics g) {
        g.pushMatrix();
        g.translate(x, y);
        g.rotateX(rotationX);
        g.rotateY(rotationY);
        g.rotateZ(rotationZ);
        g.setColor(UiValues.getColor(UiConstants.KEY_GIZMO_CENTER_COLOR));
        g.fillFaces(cube);
        g.setColor(UiValues.getColor(UiConstants.KEY_GIZMO_AXIS_X_COLOR));
        g.fillFaces(coneX);
        g.setColor(UiValues.getColor(UiConstants.KEY_GIZMO_AXIS_Y_COLOR));
        g.fillFaces(coneY);
        g.setColor(UiValues.getColor(UiConstants.KEY_GIZMO_AXIS_Z_COLOR));
        g.fillFaces(coneZ);
        g.popMatrix();
    }

    private void createMeshes() {
        createCube();
        createConeX();
        createConeY();
        createConeZ();
    }

    private void createConeX() {
        coneX = new ConeCreator().create();
        coneX.apply(new ScaleModifier(size));
        coneX.rotateZ(-Mathf.HALF_PI);
        coneX.translateX(height * size);
    }

    private void createConeY() {
        coneY = new ConeCreator().create();
        coneY.apply(new ScaleModifier(size));
        coneY.translateY(height * size);
    }

    private void createConeZ() {
        coneZ = new ConeCreator().create();
        coneZ.apply(new ScaleModifier(size));
        coneZ.rotateX(Mathf.HALF_PI);
        coneZ.translateZ(height * size);
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

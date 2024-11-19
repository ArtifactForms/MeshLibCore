package workspace;

import mesh.Mesh3D;
import workspace.ui.Color;

public class SceneObject {

    private String name;

    private Color fillColor;

    private Mesh3D mesh;

    public SceneObject(Mesh3D mesh) {
        this.mesh = mesh;
        fillColor = Color.WHITE;
    }

    public SceneObject() {
        this(null);
    }

    public Mesh3D getMesh() {
        return mesh;
    }

    public void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setFillColor(int r, int g, int b) {
        fillColor = new Color(r, g, b);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

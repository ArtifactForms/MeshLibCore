package workspace.examples;

import mesh.Mesh3D;
import mesh.creator.primitives.SegmentedCubeCreator;
import processing.core.PApplet;
import workspace.Workspace;

public class HelloWorkspace extends PApplet {

    public static void main(String[] args) {
        PApplet.main(HelloWorkspace.class.getName());
    }

    Mesh3D mesh;

    Workspace workspace;

    @Override
    public void settings() {
        size(1000, 1000, P3D);
        smooth(8);
    }

    @Override
    public void setup() {
        workspace = new Workspace(this);
        workspace.setGridVisible(true);
        workspace.setUiVisible(true);
        createMesh();
    }

    @Override
    public void draw() {
        workspace.draw(mesh);
    }

    public void createMesh() {
        SegmentedCubeCreator creator = new SegmentedCubeCreator();
        mesh = creator.create();
    }

}

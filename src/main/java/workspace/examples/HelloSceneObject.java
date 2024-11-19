package workspace.examples;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import processing.core.PApplet;
import workspace.SceneObject;
import workspace.Workspace;
import workspace.render.Mesh3DRenderer;
import workspace.ui.Color;

public class HelloSceneObject extends PApplet {

    public static void main(String[] args) {
        PApplet.main(HelloSceneObject.class.getName());
    }

    Mesh3D mesh;

    Mesh3DRenderer renderer;

    Workspace workspace;

    @Override
    public void settings() {
        size(1000, 1000, P3D);
        smooth(8);
    }

    @Override
    public void setup() {
        renderer = new Mesh3DRenderer(this);
        workspace = new Workspace(this);
        workspace.setGridVisible(true);
        createMesh();
    }

    @Override
    public void draw() {
        workspace.draw(mesh);
    }

    public void createMesh() {
        CubeCreator creator = new CubeCreator();
        mesh = creator.create();

        SceneObject sceneObject = new SceneObject();
        sceneObject.setName("Object-1");
        sceneObject.setMesh(mesh);
        sceneObject.setFillColor(new Color(255, 255, 0));
        workspace.addSceneObject(sceneObject);
    }

}

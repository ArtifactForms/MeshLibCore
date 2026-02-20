package workspace.examples;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import processing.core.PApplet;
import workspace.Workspace;

public class WorkspaceTemplate extends PApplet {

	public static void main(String[] args) {
		PApplet.main(WorkspaceTemplate.class.getName());
	}

	private Mesh3D mesh;

	private Workspace workspace;

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
		CubeCreator creator = new CubeCreator();
		mesh = creator.create();
	}

}

package workspace.examples;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import processing.core.PApplet;
import workspace.Workspace;

public class WS_FirstMesh extends PApplet {

	public static void main(String[] args) {
		PApplet.main(WS_FirstMesh.class.getName());
	}

	private Mesh3D mesh;

	private Workspace workspace;

	@Override
	public void settings() {
		size(1000, 1000, P3D);
	}

	@Override
	public void setup() {
		workspace = new Workspace(this);
		mesh = new CubeCreator().create();
	}

	@Override
	public void draw() {
		workspace.draw(mesh);
	}

}

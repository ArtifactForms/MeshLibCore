package mesh.creator.unsorted;

import java.io.File;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.io.SimpleObjectReader;

public class FromObjectCreator implements IMeshCreator {

	private float scale;
	private String path;
	private Mesh3D mesh;

	public FromObjectCreator() {
		scale = 1.0f;
		path = "";
	}

	@Override
	public Mesh3D create() {
		readMeshFromFile();
		scaleMesh();
		return mesh;
	}
	
	private void readMeshFromFile() {
		File file = new File(path);
		SimpleObjectReader in = new SimpleObjectReader();
		mesh = in.read(file);
	}
	
	private void scaleMesh() {
		mesh.scale(scale);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}

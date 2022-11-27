package mesh.creator.unsorted;

import java.io.File;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.io.SimpleObjectReader;

public class FromObjectCreator implements IMeshCreator {

	private float scale;
	private String path;

	public FromObjectCreator() {
		this.scale = 1.0f;
		this.path = "";
	}

	@Override
	public Mesh3D create() {
		File file = new File(path);
		SimpleObjectReader in = new SimpleObjectReader();
		Mesh3D mesh = in.read(file);
		mesh.scale(scale);
		return mesh;
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

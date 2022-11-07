package mesh.creator.unsorted;

import java.io.File;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.io.SimpleObjectReader;

public class FromObjectCreator implements IMeshCreator {

	private float scale;
	private String path;

	public FromObjectCreator(String path) {
		this(1.0f, path);
	}

	public FromObjectCreator(float scale, String path) {
		this.scale = scale;
		this.path = path;
	}

	@Override
	public Mesh3D create() {
		File file = new File(path);
		SimpleObjectReader in = new SimpleObjectReader();
		Mesh3D mesh = in.read(file);
		mesh.scale(scale);
		return mesh;
	}

}

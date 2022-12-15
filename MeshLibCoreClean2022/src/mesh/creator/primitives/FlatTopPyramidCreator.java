package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.util.Mesh3DUtil;

public class FlatTopPyramidCreator implements IMeshCreator {

	private float size;
	
	private float topScale;

	public FlatTopPyramidCreator() {
		this.size = 1f;
		this.topScale = 0.5f;
	}
	
	public FlatTopPyramidCreator(float size, float topScale) {
		this.size = size;
		this.topScale = topScale;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new CubeCreator(size).create();
		Mesh3DUtil.scaleFaceAt(mesh, 0, topScale);
		return mesh;
	}
	
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getTopScale() {
		return topScale;
	}

	public void setTopScale(float topScale) {
		this.topScale = topScale;
	}
	
}

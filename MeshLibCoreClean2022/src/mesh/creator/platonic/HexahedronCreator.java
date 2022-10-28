package mesh.creator.platonic;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;

public class HexahedronCreator implements IMeshCreator {

	private float radius;

	public HexahedronCreator() {
		this(1);
	}
	
	public HexahedronCreator(float radius) {
		this.radius = radius;
	}
	
	@Override
	public Mesh3D create() {
		CubeCreator creator = new CubeCreator(radius);
		return creator.create();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}

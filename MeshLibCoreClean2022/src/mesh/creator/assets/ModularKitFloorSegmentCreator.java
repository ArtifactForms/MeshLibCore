package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.SolidifyModifier;

public class ModularKitFloorSegmentCreator implements IMeshCreator {

	private float floorWidth;
	private float floorDepth;
	private float floorHeight;

	public ModularKitFloorSegmentCreator() {
		this(4, 4, 0);
	}

	public ModularKitFloorSegmentCreator(float floorWidth, float floorDepth, float floorHeight) {
		this.floorWidth = floorWidth;
		this.floorDepth = floorDepth;
		this.floorHeight = floorHeight;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new PlaneCreator(0.5f).create();
		mesh.scale(floorWidth, 1, floorDepth);
		if (floorHeight > 0) {
			new SolidifyModifier(floorHeight).modify(mesh);
			mesh.translateY(-floorHeight);
		}
		return mesh;
	}

	public float getFloorWidth() {
		return floorWidth;
	}

	public void setFloorWidth(float floorWidth) {
		this.floorWidth = floorWidth;
	}

	public float getFloorDepth() {
		return floorDepth;
	}

	public void setFloorDepth(float floorDepth) {
		this.floorDepth = floorDepth;
	}

	public float getFloorHeight() {
		return floorHeight;
	}

	public void setFloorHeight(float floorHeight) {
		this.floorHeight = floorHeight;
	}

}

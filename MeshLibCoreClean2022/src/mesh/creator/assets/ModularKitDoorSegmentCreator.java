package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.SolidifyModifier;

public class ModularKitDoorSegmentCreator implements IMeshCreator {

	private float doorWidth;
	private float doorHeight;
	private float segmentWidth;
	private float segmentHeight;
	private float segmentDepth;

	public ModularKitDoorSegmentCreator() {
		this(1.0f, 2.0f, 2.0f, 3.0f, 0.0f);
	}
	
	public ModularKitDoorSegmentCreator(float doorWidth, float doorHeight, float segmentWidth, float segmentHeight,
			float segmentDepth) {
		this.doorWidth = doorWidth;
		this.doorHeight = doorHeight;
		this.segmentWidth = segmentWidth;
		this.segmentHeight = segmentHeight;
		this.segmentDepth = segmentDepth;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		float doorWidthHalf = doorWidth * 0.5f;
		float segmentWidthHalf = segmentWidth * 0.5f;

		mesh.addVertex(-segmentWidthHalf, 0, 0);
		mesh.addVertex(-doorWidthHalf, 0, 0);
		mesh.addVertex(doorWidthHalf, 0, 0);
		mesh.addVertex(segmentWidthHalf, 0, 0);

		mesh.addVertex(-segmentWidthHalf, -doorHeight, 0);
		mesh.addVertex(-doorWidthHalf, -doorHeight, 0);
		mesh.addVertex(doorWidthHalf, -doorHeight, 0);
		mesh.addVertex(segmentWidthHalf, -doorHeight, 0);

		mesh.addVertex(-segmentWidthHalf, -segmentHeight, 0);
		mesh.addVertex(-doorWidthHalf, -segmentHeight, 0);
		mesh.addVertex(doorWidthHalf, -segmentHeight, 0);
		mesh.addVertex(segmentWidthHalf, -segmentHeight, 0);

		mesh.addFace(8, 9, 5, 4);
		mesh.addFace(9, 10, 6, 5);
		mesh.addFace(10, 11, 7, 6);
		mesh.addFace(4, 5, 1, 0);
		mesh.addFace(6, 7, 3, 2);

		if (segmentDepth > 0) {
			new SolidifyModifier(segmentDepth).modify(mesh);
			mesh.translateZ(segmentDepth);
		}
		
		return mesh;
	}

}

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
	private float doorWidthHalf;
	private float segmentWidthHalf;
	private Mesh3D mesh;

	public ModularKitDoorSegmentCreator() {
		doorWidth = 1.0f;
		doorHeight = 2.0f;
		segmentWidth = 2.0f;
		segmentHeight = 3.0f;
		segmentDepth = 0.0f;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		refreshHelperVariables();
		createVertices();
		createFaces();
		solidify();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void refreshHelperVariables() {
		doorWidthHalf = doorWidth * 0.5f;
		segmentWidthHalf = segmentWidth * 0.5f;
	}
	
	private void createVertices() {
		createVerticesAtGroundLevel();
		createVerticesAtDoorHeightLevel();
		createVeticesAtSegmentHeightLevel();
	}
	
	private void createVerticesAtGroundLevel() {
		mesh.addVertex(-segmentWidthHalf, 0, 0);
		mesh.addVertex(-doorWidthHalf, 0, 0);
		mesh.addVertex(doorWidthHalf, 0, 0);
		mesh.addVertex(segmentWidthHalf, 0, 0);
	}
	
	private void createVerticesAtDoorHeightLevel() {
		mesh.addVertex(-segmentWidthHalf, -doorHeight, 0);
		mesh.addVertex(-doorWidthHalf, -doorHeight, 0);
		mesh.addVertex(doorWidthHalf, -doorHeight, 0);
		mesh.addVertex(segmentWidthHalf, -doorHeight, 0);
	}
	
	private void createVeticesAtSegmentHeightLevel() {
		mesh.addVertex(-segmentWidthHalf, -segmentHeight, 0);
		mesh.addVertex(-doorWidthHalf, -segmentHeight, 0);
		mesh.addVertex(doorWidthHalf, -segmentHeight, 0);
		mesh.addVertex(segmentWidthHalf, -segmentHeight, 0);
	}
	
	private void createFaces() {
		mesh.addFace(8, 9, 5, 4);
		mesh.addFace(9, 10, 6, 5);
		mesh.addFace(10, 11, 7, 6);
		mesh.addFace(4, 5, 1, 0);
		mesh.addFace(6, 7, 3, 2);
	}
	
	private void solidify() {
		if (segmentDepth <= 0)
			return;
		new SolidifyModifier(segmentDepth).modify(mesh);
		mesh.translateZ(segmentDepth);
	}

	public float getDoorWidth() {
		return doorWidth;
	}

	public void setDoorWidth(float doorWidth) {
		this.doorWidth = doorWidth;
	}

	public float getDoorHeight() {
		return doorHeight;
	}

	public void setDoorHeight(float doorHeight) {
		this.doorHeight = doorHeight;
	}

	public float getSegmentWidth() {
		return segmentWidth;
	}

	public void setSegmentWidth(float segmentWidth) {
		this.segmentWidth = segmentWidth;
	}

	public float getSegmentHeight() {
		return segmentHeight;
	}

	public void setSegmentHeight(float segmentHeight) {
		this.segmentHeight = segmentHeight;
	}

	public float getSegmentDepth() {
		return segmentDepth;
	}

	public void setSegmentDepth(float segmentDepth) {
		this.segmentDepth = segmentDepth;
	}

}

package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SegmentedBox implements IMeshCreator {

	private int segmentsX;
	private int segmentsY;
	private int segmentsZ;
	private float width;
	private float height;
	private float depth;
	private Mesh3D mesh;

	public SegmentedBox() {
		segmentsX = 10;
		segmentsY = 10;
		segmentsZ = 10;
		width = 2;
		height = 2;
		depth = 2;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createFront();
		createBack();
		createLeft();
		createRight();
		createTop();
		createBottom();
		removeDoubles();
		return mesh;
	}

	private void removeDoubles() {
		for (Vector3f v : mesh.vertices)
			v.roundLocalDecimalPlaces(4);
		mesh.removeDoubles();
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createFront() {
		float segmentSizeX = width / (float) segmentsX;
		float segmentSizeY = height / (float) segmentsY;
		GridCreator creator = new GridCreator(segmentsX, segmentsY, segmentSizeX, segmentSizeY);
		Mesh3D front = creator.create();
		front.rotateX(-Mathf.HALF_PI);
		front.translateZ(depth / 2f);
		mesh.append(front);
	}

	private void createBack() {
		float segmentSizeX = width / (float) segmentsX;
		float segmentSizeY = height / (float) segmentsY;
		GridCreator creator = new GridCreator(segmentsX, segmentsY, segmentSizeX, segmentSizeY);
		Mesh3D back = creator.create();
		back.rotateX(Mathf.HALF_PI);
		back.translateZ(-depth / 2f);
		mesh.append(back);
	}

	private void createLeft() {
		float segmentSizeZ = depth / (float) segmentsZ;
		float segmentSizeY = height / (float) segmentsY;
		GridCreator creator = new GridCreator(segmentsZ, segmentsY, segmentSizeZ, segmentSizeY);
		Mesh3D left = creator.create();
		left.rotateX(Mathf.HALF_PI);
		left.rotateY(Mathf.HALF_PI);
		left.translateX(-width / 2f);
		mesh.append(left);
	}

	private void createRight() {
		float segmentSizeZ = depth / (float) segmentsZ;
		float segmentSizeY = height / (float) segmentsY;
		GridCreator creator = new GridCreator(segmentsZ, segmentsY, segmentSizeZ, segmentSizeY);
		Mesh3D right = creator.create();
		right.rotateX(Mathf.HALF_PI);
		right.rotateY(-Mathf.HALF_PI);
		right.translateX(width / 2f);
		mesh.append(right);
	}

	private void createTop() {
		float segmentSizeX = width / (float) segmentsX;
		float segmentSizeZ = depth / (float) segmentsZ;
		GridCreator creator = new GridCreator(segmentsX, segmentsZ, segmentSizeX, segmentSizeZ);
		Mesh3D top = creator.create();
		top.translateY(-height / 2f);
		mesh.append(top);
	}

	private void createBottom() {
		float segmentSizeX = width / (float) segmentsX;
		float segmentSizeZ = depth / (float) segmentsZ;
		GridCreator creator = new GridCreator(segmentsX, segmentsZ, segmentSizeX, segmentSizeZ);
		Mesh3D right = creator.create();
		right.rotateX(-Mathf.PI);
		right.translateY(height / 2f);
		mesh.append(right);
	}

	public int getSegmentsX() {
		return segmentsX;
	}

	public void setSegmentsX(int segmentsX) {
		this.segmentsX = segmentsX;
	}

	public int getSegmentsY() {
		return segmentsY;
	}

	public void setSegmentsY(int segmentsY) {
		this.segmentsY = segmentsY;
	}

	public int getSegmentsZ() {
		return segmentsZ;
	}

	public void setSegmentsZ(int segmentsZ) {
		this.segmentsZ = segmentsZ;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

}

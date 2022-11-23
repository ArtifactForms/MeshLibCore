package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SegmentedCubeCreator implements IMeshCreator {

	private int segments;
	private float size;
	private float creationSize;
	private Mesh3D mesh;

	public SegmentedCubeCreator() {
		this.segments = 10;
		this.size = 1f;
		this.creationSize = (float) segments;
	}

	public SegmentedCubeCreator(int segments, float size) {
		this.segments = segments;
		this.size = size;
		this.creationSize = (float) segments;
	}

	private void createTop() {
		Mesh3D top = new GridCreator(segments, segments, creationSize).create();
		top.translateY(-creationSize);
		mesh.append(top);
	}

	private void createBottom() {
		Mesh3D bottom = new GridCreator(segments, segments, creationSize).create();
		bottom.rotateX(Mathf.toRadians(180));
		bottom.translateY(creationSize);
		mesh.append(bottom);
	}

	private void createFront() {
		Mesh3D front = new GridCreator(segments, segments, creationSize).create();
		front.rotateX(Mathf.HALF_PI);
		front.translateZ(-creationSize);
		mesh.append(front);
	}

	private void createBack() {
		Mesh3D front = new GridCreator(segments, segments, creationSize).create();
		front.rotateX(-Mathf.HALF_PI);
		front.translateZ(creationSize);
		mesh.append(front);
	}

	private void createLeft() {
		Mesh3D front = new GridCreator(segments, segments, creationSize).create();
		front.rotateZ(-Mathf.HALF_PI);
		front.translateX(-creationSize);
		mesh.append(front);
	}

	private void createRight() {
		Mesh3D front = new GridCreator(segments, segments, creationSize).create();
		front.rotateZ(Mathf.HALF_PI);
		front.translateX(creationSize);
		mesh.append(front);
	}

	private void removeDoubles() {
		mesh.removeDoubles(2);
	}

	private void scale() {
		mesh.scale(1.0f / creationSize, 1.0f / creationSize, 1.0f / creationSize);
		mesh.scale(size);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createTop();
		createBottom();
		createFront();
		createBack();
		createLeft();
		createRight();
		removeDoubles();
		scale();
		return mesh;
	}
	
	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

}

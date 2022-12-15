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
		this(10, 1);
	}

	public SegmentedCubeCreator(int segments, float size) {
		this.segments = segments;
		this.size = size;
		this.creationSize = (float) segments;
	}
	
	private Mesh3D createSide() {
		return new GridCreator(segments, segments, creationSize).create();
	}
	
	private void append(Mesh3D mesh) {
		this.mesh.append(mesh);
	}

	private void createTop() {
		Mesh3D top = createSide();
		top.translateY(-creationSize);
		append(top);
	}

	private void createBottom() {
		Mesh3D bottom = createSide();
		bottom.rotateX(Mathf.PI);
		bottom.translateY(creationSize);
		append(bottom);
	}

	private void createFront() {
		Mesh3D front = createSide();
		front.rotateX(Mathf.HALF_PI);
		front.translateZ(-creationSize);
		append(front);
	}

	private void createBack() {
		Mesh3D back = createSide();
		back.rotateX(-Mathf.HALF_PI);
		back.translateZ(creationSize);
		append(back);
	}

	private void createLeft() {
		Mesh3D left = createSide();
		left.rotateZ(-Mathf.HALF_PI);
		left.translateX(-creationSize);
		append(left);
	}

	private void createRight() {
		Mesh3D right = createSide();
		right.rotateZ(Mathf.HALF_PI);
		right.translateX(creationSize);
		append(right);
	}

	private void removeDoubles() {
		mesh.removeDoubles(2);
	}

	private void scale() {
		mesh.scale(1.0f / creationSize, 1.0f / creationSize, 1.0f / creationSize);
		mesh.scale(size);
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
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

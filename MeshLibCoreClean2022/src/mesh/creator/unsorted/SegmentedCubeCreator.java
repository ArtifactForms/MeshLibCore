package mesh.creator.unsorted;

import java.util.HashSet;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;

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

	private void roundVertices() {
		for (Vector3f v : mesh.vertices) {
			v.set(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
		}
	}

	private void removeDoubles() {
		Mesh3D m = new Mesh3D();
		HashSet<Vector3f> vertexSet = new HashSet<Vector3f>();
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				vertexSet.add(v);
			}
		}
		m.vertices.addAll(vertexSet);
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				int index = m.vertices.indexOf(v);
				f.indices[i] = index;
			}
			m.add(f);
		}
		this.mesh = m;
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
		roundVertices();
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

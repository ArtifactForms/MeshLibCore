package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SolidArc implements IMeshCreator {
	
	private int index;
	private int vertices;
	private float angle;
	private float outerRadius;
	private float innerRadius;
	private float height;
	private boolean capStart;
	private boolean capEnd;
	private Mesh3D mesh;
	
	private ArcCreator creator;
	private Mesh3D outerArc;
	private Mesh3D innerArc;
	
	public SolidArc() {
		vertices = 17;
		angle = Mathf.HALF_PI;
		outerRadius = 3;
		innerRadius = 2;
		height = 1;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		initializeCreator();
		createArcs();
		createVertices();
		createFaces();
		capStart();
		capEnd();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void initializeCreator() {
		creator = new ArcCreator();
		creator.setStartAngle(0);
		creator.setEndAngle(angle);
		creator.setVertices(vertices);
	}
	
	private void createArcs() {
		creator.setRadius(outerRadius);
		outerArc = creator.create();
		creator.setRadius(innerRadius);
		innerArc = creator.create();
	}
	
	private void createVertices() {
		for (int i = 0; i < vertices; i++) {
			mesh.add(outerArc.getVertexAt(i).add(0, -height / 2.0f, 0));
			mesh.add(innerArc.getVertexAt(i).add(0, -height / 2.0f, 0));
			mesh.add(innerArc.getVertexAt(i).add(0, height / 2.0f, 0));
			mesh.add(outerArc.getVertexAt(i).add(0, height / 2.0f, 0));
		}
	}
	
	private void capStart() {
		if (!capStart)
			return;
		mesh.addFace(0, 1, 2, 3);
	}
	
	private void capEnd() {
		if (!capEnd)
			return;
		int index = mesh.vertices.size();
		mesh.addFace(index - 1, index - 2, index - 3, index - 4);
	}
	 
	private void createFaces() {
		for (int i = 0; i < (vertices) * 4 - 4; i+=4) {
			setIndex(i);
			addTopFaceAtIndex();
			addOuterFaceAtIndex();
			addBottomFaceAtIndex();
			addInnerFaceAtIndex();
		}
	}
	
	private void addTopFaceAtIndex() {
		addFace(1, 0, 4, 5);
	}
	
	private void addOuterFaceAtIndex() {
		addFace(4, 0, 3, 7);
	}
	
	private void addBottomFaceAtIndex() {
		addFace(2, 6, 7, 3);
	}
	
	private void addInnerFaceAtIndex() {
		addFace(6, 2, 1, 5);
	}
	
	private void addFace(int... indices) {
		int[] indices1 = new int[indices.length];
		for (int i = 0; i < indices.length; i++) {
			indices1[i] = index + indices[i];
		}
		mesh.addFace(indices1);
	}
	
	private void setIndex(int index) {
		this.index = index;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
	}

	public int getRotationSegments() {
		return vertices;
	}

	public void setRotationSegments(int rotationSegments) {
		this.vertices = rotationSegments + 1;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isCapStart() {
		return capStart;
	}

	public void setCapStart(boolean capStart) {
		this.capStart = capStart;
	}

	public boolean isCapEnd() {
		return capEnd;
	}

	public void setCapEnd(boolean capEnd) {
		this.capEnd = capEnd;
	}

}

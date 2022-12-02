package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class CapsuleCreator implements IMeshCreator {

	private float topRadius;
	private float bottomRadius;
	private float cylinderHeight;
	private float topCapHeight;
	private float bottomCapHeight;
	private int topCapSegments;
	private int bottomCapSegments;
	private int cylinderSegments;
	private int rotationSegments;
	private Mesh3D mesh;

	public CapsuleCreator() {
		this.topRadius = 1;
		this.bottomRadius = 1;
		this.cylinderHeight = 2;
		this.topCapHeight = 1;
		this.bottomCapHeight = 1;
		this.topCapSegments = 16;
		this.bottomCapSegments = 16;
		this.cylinderSegments = 8;
		this.rotationSegments = 32;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createTopCapVertices();
		createCylinderVertices();
		createBottomCapVertices();
		createQuadFaces();
		createTriangleFaces();
		return mesh;
	}
	
	private void createTopCapVertices() {
		createCapVertices(-1, topRadius, topCapHeight, topCapSegments, -cylinderHeight / 2);
	}
	
	private void createBottomCapVertices() {
		createCapVertices(1, bottomRadius, bottomCapHeight, bottomCapSegments, cylinderHeight / 2);
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createCapVertices(int a, float radius, float height, int segments, float yOffset) {
		float stepTheta = Mathf.HALF_PI / segments;
		float stepPhi = Mathf.TWO_PI / rotationSegments;
		float thetaA = segments * stepTheta;
		for (int i = 1; i < segments; i++) {
			float theta = a == 1 ? thetaA - (i * stepTheta) : i * stepTheta;
			for (int j = 0; j < rotationSegments; j++) {
				float phi = j * stepPhi;
				float x = radius * Mathf.cos(phi) * Mathf.sin(theta);
				float y = height * a * Mathf.cos(theta) + yOffset;
				float z = radius * Mathf.sin(phi) * Mathf.sin(theta);
				mesh.addVertex(x, y, z);
			}
		}
	}

	private void createCylinderVertices() {
		float radiusStep = (topRadius - bottomRadius) / cylinderSegments;
		float segmentHeight = cylinderHeight / cylinderSegments;
		float angle = Mathf.TWO_PI / rotationSegments;
		for (int i = 0; i <= cylinderSegments; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				float x = (topRadius - (i * radiusStep)) * (Mathf.cos(j * angle));
				float y = i * segmentHeight - (cylinderHeight / 2f);
				float z = (topRadius - (i * radiusStep)) * (Mathf.sin(j * angle));
				mesh.addVertex(x, y, z);
			}
		}
	}

	private void createQuadFaces() {
		for (int i = 0; i < getSegmentsCount() - 2; i++)
			for (int j = 0; j < rotationSegments; j++)
				addFace(i, j);
	}

	private void addFace(int i, int j) {
		int idx0 = toOneDimensionalIndex(i, j);
		int idx1 = toOneDimensionalIndex(i + 1, j);
		int idx2 = toOneDimensionalIndex(i + 1, (j + 1) % rotationSegments);
		int idx3 = toOneDimensionalIndex(i, (j + 1) % rotationSegments);
		mesh.addFace(idx0, idx1, idx2, idx3);
	}

	private int toOneDimensionalIndex(int i, int j) {
		return Mathf.toOneDimensionalIndex(i, j, rotationSegments);
	}

	private void createTriangleFaces() {
		int offset = (getSegmentsCount() - 2) * rotationSegments;
		triangleFan(0, -cylinderHeight / 2 - topCapHeight);
		triangleFan(offset, cylinderHeight / 2f + bottomCapHeight);
	}

	private void triangleFan(int offset, float y) {
		int idx = mesh.vertices.size();
		mesh.addVertex(0, y, 0);
		for (int i = 0; i < rotationSegments; i++) {
			int idx0 = i + offset;
			int idx1 = (i + 1) % rotationSegments + offset;
			if (offset == 0) {
				mesh.addFace(idx0, idx1, idx);
			} else {
				mesh.addFace(idx1, idx0, idx);
			}
		}
	}

	private int getSegmentsCount() {
		return topCapSegments + cylinderSegments + bottomCapSegments;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(float topRadius) {
		this.topRadius = topRadius;
	}

	public float getBottomRadius() {
		return bottomRadius;
	}

	public void setBottomRadius(float bottomRadius) {
		this.bottomRadius = bottomRadius;
	}

	public float getCylinderHeight() {
		return cylinderHeight;
	}

	public void setCylinderHeight(float cylinderHeight) {
		this.cylinderHeight = cylinderHeight;
	}

	public float getTopCapHeight() {
		return topCapHeight;
	}

	public void setTopCapHeight(float topCapHeight) {
		this.topCapHeight = topCapHeight;
	}

	public float getBottomCapHeight() {
		return bottomCapHeight;
	}

	public void setBottomCapHeight(float bottomCapHeight) {
		this.bottomCapHeight = bottomCapHeight;
	}

	public int getTopCapSegments() {
		return topCapSegments;
	}

	public void setTopCapSegments(int topCapSegments) {
		this.topCapSegments = topCapSegments;
	}

	public int getBottomCapSegments() {
		return bottomCapSegments;
	}

	public void setBottomCapSegments(int bottomCapSegments) {
		this.bottomCapSegments = bottomCapSegments;
	}

	public int getCylinderSegments() {
		return cylinderSegments;
	}

	public void setCylinderSegments(int cylinderSegments) {
		this.cylinderSegments = cylinderSegments;
	}

	public int getRotationSegments() {
		return rotationSegments;
	}

	public void setRotationSegments(int rotationSegments) {
		this.rotationSegments = rotationSegments;
	}

}

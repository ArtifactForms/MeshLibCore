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
		this(1, 2, 1, 16, 8, 32);
	}

	public CapsuleCreator(float radius, float cylinderHeight, float capHeight, int capSegments, int cylinderSegments,
			int rotationSegments) {
		this(radius, radius, cylinderHeight, capHeight, capHeight, capSegments, capSegments, cylinderSegments,
				rotationSegments);
	}

	public CapsuleCreator(float topRadius, float bottomRadius, float cylinderHeight, float topCapHeight,
			float bottomCapHeight, int topCapSegments, int bottomCapSegments, int cylinderSegments,
			int rotationSegments) {
		this.topRadius = topRadius;
		this.bottomRadius = bottomRadius;
		this.cylinderHeight = cylinderHeight;
		this.topCapHeight = topCapHeight;
		this.bottomCapHeight = bottomCapHeight;
		this.topCapSegments = topCapSegments;
		this.bottomCapSegments = bottomCapSegments;
		this.cylinderSegments = cylinderSegments;
		this.rotationSegments = rotationSegments;
	}

	private void addFace(int i, int j) {
		int idx0 = Mathf.toOneDimensionalIndex(i, j, rotationSegments);
		int idx1 = Mathf.toOneDimensionalIndex(i + 1, j, rotationSegments);
		int idx2 = Mathf.toOneDimensionalIndex(i + 1, (j + 1) % rotationSegments, rotationSegments);
		int idx3 = Mathf.toOneDimensionalIndex(i, (j + 1) % rotationSegments, rotationSegments);
		mesh.addFace(idx0, idx1, idx2, idx3);
	}

	private void createCapVertices(int a, float radius, float height, int segments, float offset) {
		float stepTheta = Mathf.HALF_PI / segments;
		float stepPhi = Mathf.TWO_PI / rotationSegments;
		float thetaA = segments * stepTheta;
		for (int i = 1; i < segments; i++) {
			float theta = a == 1 ? thetaA - (i * stepTheta) : i * stepTheta;
			for (int j = 0; j < rotationSegments; j++) {
				float phi = j * stepPhi;
				float x = radius * Mathf.cos(phi) * Mathf.sin(theta);
				float y = height * a * Mathf.cos(theta) + offset;
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
		for (int i = 0; i < getSegmentsCount() - 2; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				addFace(i, j);
			}
		}
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

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createCapVertices(-1, topRadius, topCapHeight, topCapSegments, -cylinderHeight / 2);
		createCylinderVertices();
		createCapVertices(1, bottomRadius, bottomCapHeight, bottomCapSegments, cylinderHeight / 2);
		createQuadFaces();
		createTriangleFaces();
		return mesh;
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

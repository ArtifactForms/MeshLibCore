package mesh.creator.primitives;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.wip.Mesh3DUtil;

public class ConeCreator implements IMeshCreator {

	private int rotationSegments;
	private int heightSegments;
	private float topRadius;
	private float bottomRadius;
	private float height;

	private Mesh3D mesh;

	public ConeCreator() {
		this(32, 10, 0, 1, 2);
	}

	public ConeCreator(int rotationSegments, int heightSegments, float topRadius, float bottomRadius, float height) {
		this.rotationSegments = rotationSegments;
		this.heightSegments = heightSegments;
		this.topRadius = topRadius;
		this.bottomRadius = bottomRadius;
		this.height = height;
	}

	private float getSegmentHeight() {
		return height / heightSegments;
	}

	private float getRadius(int i) {
		float radius = Mathf.map(i, 0, heightSegments, topRadius, bottomRadius);
		return radius;
	}

	private void createVertices() {
		int start = topRadius > 0 ? 0 : 1;
		int end = bottomRadius > 0 ? 0 : 1;
		float angle = Mathf.TWO_PI / rotationSegments;
		for (int i = start; i <= heightSegments - end; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				float x = getRadius(i) * Mathf.cos(angle * j);
				float y = getSegmentHeight() * i - (height * 0.5f);
				float z = getRadius(i) * Mathf.sin(angle * j);
				mesh.addVertex(x, y, z);
			}
		}
	}

	private void addFace(int i, int j) {
		int idx0 = Mathf.toOneDimensionalIndex(i, j, rotationSegments);
		int idx1 = Mathf.toOneDimensionalIndex(i + 1, j, rotationSegments);
		int idx2 = Mathf.toOneDimensionalIndex(i + 1, (j + 1) % rotationSegments, rotationSegments);
		int idx3 = Mathf.toOneDimensionalIndex(i, (j + 1) % rotationSegments, rotationSegments);
		mesh.addFace(idx0, idx1, idx2, idx3);
	}

	private void createQuadFaces() {
		int end = topRadius > 0 ? 0 : 1;
		end += bottomRadius > 0 ? 0 : 1;
		for (int i = 0; i < heightSegments - end; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				addFace(i, j);
			}
		}
	}

	private void createTopCap() {
		int[] indices = new int[rotationSegments];

		for (int i = 0; i < indices.length; i++)
			indices[i] = i;

		mesh.addFace(indices);
	}

	private void createBottomCap() {
		int index = mesh.getVertexCount() - 1;
		int[] indices = new int[rotationSegments];

		for (int i = 0; i < indices.length; i++)
			indices[i] = index - i;

		mesh.addFace(indices);
	}

	private void splitTop() {
		if (topRadius > 0)
			return;

		splitFace(mesh.getFaceCount() - 2, -getSegmentHeight());
	}

	private void splitBottom() {
		if (bottomRadius > 0)
			return;

		splitFace(mesh.getFaceCount() - 1, getSegmentHeight());
	}

	private void splitFace(int faceIndex, float offsetY) {
		int index = mesh.vertices.size();
		Face3D face = mesh.getFaceAt(faceIndex);

		Mesh3DUtil.centerSplit(mesh, face);
		mesh.getVertexAt(index).addLocal(0, offsetY, 0);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();

		if (parametersAreZero())
			return mesh;

		createVertices();
		createQuadFaces();
		createTopCap();
		createBottomCap();
		splitTop();
		splitBottom();

		return mesh;
	}
	
	private boolean parametersAreZero() {
		return ((topRadius == 0 && bottomRadius == 0) || height == 0 || rotationSegments == 0 || heightSegments == 0);
	}

	public int getRotationSegments() {
		return rotationSegments;
	}

	public void setRotationSegments(int rotationSegments) {
		this.rotationSegments = rotationSegments;
	}

	public int getHeightSegments() {
		return heightSegments;
	}

	public void setHeightSegments(int heightSegments) {
		this.heightSegments = heightSegments;
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

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}

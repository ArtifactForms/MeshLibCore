package mesh.creator.special;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;

public class VariableCylinderCreator implements IMeshCreator {

	private Mesh3D mesh;
	private float lastY;
	private int rotationSegments;
	private FillType capBottomFillType;
	private FillType capTopFillType;
	private List<Float> radii;
	private List<Float> yCoordinates;
	
	public VariableCylinderCreator() {
		radii = new ArrayList<Float>();
		rotationSegments = 16;
		yCoordinates = new ArrayList<Float>();
		capBottomFillType = FillType.N_GON;
		capTopFillType = FillType.N_GON;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		capEnds();
		removeDoubles();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void capEnds() {
		capBottom();
		capTop();
	}
	
	private void removeDoubles() {
		for (Vector3f v : mesh.vertices)
			v.roundLocalDecimalPlaces(4);
		mesh.removeDoubles();
	}
	
	private void capBottom() {
		if (capBottomFillType == FillType.NOTHING)
			return;
		CircleCreator creator = new CircleCreator();
		creator.setFillType(capBottomFillType);
		creator.setRadius(radii.get(0));
		creator.setVertices(rotationSegments);
		Mesh3D bottom = creator.create();
		bottom.rotateZ(Mathf.PI);
		bottom.translateY(yCoordinates.get(0));
		mesh.append(bottom);
	}
	
	private void capTop() {
		if (capTopFillType == FillType.NOTHING)
			return;
		CircleCreator creator = new CircleCreator();
		creator.setFillType(capTopFillType);
		creator.setRadius(radii.get(radii.size() - 1));
		creator.setVertices(rotationSegments);
		Mesh3D top = creator.create();
		top.translateY(yCoordinates.get(yCoordinates.size() - 1));
		mesh.append(top);
	}
	
	private void createVertices() {
		for (int i = 0; i < radii.size(); i++) {
			float y = yCoordinates.get(i);
			float radius = radii.get(i);
			Mesh3D circle;
			CircleCreator circleCreator = new CircleCreator();
			circleCreator.setVertices(rotationSegments);
			circleCreator.setRadius(radius);
			circle = circleCreator.create();
			circle.translateY(y);
			mesh.append(circle);
		}
	}
	
	private void createFaces() {
		createQuadFaces();
	}
	
	private void createQuadFaces() {
		for (int i = 0; i < yCoordinates.size() - 1; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				addFace(i, j);
			}
		}
	}
	
	private void addFace(int i, int j)  {
		int idx0 = getOneDimensionalIndex(i, j);
		int idx1 = getOneDimensionalIndex(i + 1, j);
		int idx2 = getOneDimensionalIndex(i + 1, (j + 1) % rotationSegments);
		int idx3 = getOneDimensionalIndex(i, (j + 1) % rotationSegments);
		mesh.addFace(idx3, idx2, idx1, idx0);
	}
	
	private int getOneDimensionalIndex(int i, int j) {
		return Mathf.toOneDimensionalIndex(i, j, rotationSegments);
	}
	
	public void add(float radius, float height) {
		float y1 = -Mathf.abs(height);
		radii.add(radius);
		yCoordinates.add(y1 + lastY);
		lastY = y1 + lastY;
	}
	
	public void clear() {
		radii.clear();
		yCoordinates.clear();
	}

	public int getRotationSegments() {
		return rotationSegments;
	}

	public void setRotationSegments(int rotationSegments) {
		this.rotationSegments = rotationSegments;
	}

	public FillType getCapBottomFillType() {
		return capBottomFillType;
	}

	public void setCapBottomFillType(FillType capBottomFillType) {
		this.capBottomFillType = capBottomFillType;
	}

	public FillType getCapTopFillType() {
		return capTopFillType;
	}

	public void setCapTopFillType(FillType capTopFillType) {
		this.capTopFillType = capTopFillType;
	}

}

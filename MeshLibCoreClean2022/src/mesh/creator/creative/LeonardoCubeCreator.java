package mesh.creator.creative;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class LeonardoCubeCreator implements IMeshCreator {

    private int subdivisions;
    private float innerRadius;
    private float outerRadius;
    private float connectorRadius;
    private Mesh3D mesh;

    public LeonardoCubeCreator() {
	subdivisions = 0;
	innerRadius = 0.9f;
	outerRadius = 1.0f;
    }

    @Override
    public Mesh3D create() {
	initializeConnectorRadius();
	initializeMesh();
	createConnectors();
	removeDoubles();
	subdivide();
	return mesh;
    }

    private void initializeConnectorRadius() {
	connectorRadius = calculateConnectorRadius();
    }

    private void subdivide() {
	new CatmullClarkModifier(subdivisions).modify(mesh);
    }

    private void initializeMesh() {
	mesh = new Mesh3D();
    }

    private void removeDoubles() {
	removeDoubleVertices();
	removeDoubleFaces();
    }

    private void removeDoubleVertices() {
	mesh.removeDoubles(2);
    }

    private void removeDoubleFaces() {
	FaceSelection selection = new FaceSelection(mesh);
	selection.selectDoubles();
	mesh.faces.removeAll(selection.getFaces());
    }

    private void createTopConnector() {
	Mesh3D top = createConnector(true, false);
	top.translateY(-outerRadius + connectorRadius);
	mesh.append(top);
    }

    private void createBottomConnector() {
	Mesh3D bottom = createConnector(true, false);
	bottom.translateY(outerRadius - connectorRadius);
	mesh.append(bottom);
    }

    private void createLeftConnector() {
	Mesh3D left = createConnector(false, false);
	left.rotateZ(-Mathf.HALF_PI);
	left.translateX(-outerRadius + connectorRadius);
	mesh.append(left);
    }

    private void createRightConnector() {
	Mesh3D right = createConnector(false, false);
	right.rotateZ(Mathf.HALF_PI);
	right.translateX(outerRadius - connectorRadius);
	mesh.append(right);
    }

    private void createBackConnector() {
	Mesh3D back = createConnector(true, true);
	back.rotateX(-Mathf.HALF_PI);
	back.translateZ(-outerRadius + connectorRadius);
	mesh.append(back);
    }

    private void createFrontConnector() {
	Mesh3D front = createConnector(true, true);
	front.rotateX(-Mathf.HALF_PI);
	front.translateZ(outerRadius - connectorRadius);
	mesh.append(front);
    }

    private void createConnectors() {
	createTopConnector();
	createBottomConnector();
	createLeftConnector();
	createRightConnector();
	createBackConnector();
	createFrontConnector();
    }

    private Mesh3D createConnector(boolean extrudeEnds0, boolean extrudeEnds1) {
	CubeCreator creator = new CubeCreator(connectorRadius);
	Mesh3D m = creator.create();

	FaceSelection selection0 = new FaceSelection(m);
	selection0.selectLeftFaces();
	selection0.selectRightFaces();

	FaceSelection selection1 = new FaceSelection(m);
	selection1.selectBackFaces();
	selection1.selectFrontFaces();

	for (Face3D f : selection0.getFaces()) {
	    Mesh3DUtil.extrudeFace(m, f, 1, outerRadius - (3 * connectorRadius));
	    if (extrudeEnds0)
		Mesh3DUtil.extrudeFace(m, f, 1, (2 * connectorRadius));
	}

	for (Face3D f : selection1.getFaces()) {
	    Mesh3DUtil.extrudeFace(m, f, 1, outerRadius - (3 * connectorRadius));
	    if (extrudeEnds1)
		Mesh3DUtil.extrudeFace(m, f, 1, (2 * connectorRadius));
	}

	return m;
    }

    public int getSubdivisions() {
	return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
	this.subdivisions = subdivisions;
    }

    private float calculateConnectorRadius() {
	return (outerRadius - innerRadius) * 0.5f;
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

}

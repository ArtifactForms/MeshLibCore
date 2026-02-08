package mesh.creator.creative;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.RotateXModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class LeonardoCubeCreator implements IMeshCreator {

    private float innerRadius;

    private float outerRadius;

    private float connectorRadius;

    private Mesh3D mesh;

    public LeonardoCubeCreator() {
        innerRadius = 0.9f;
        outerRadius = 1.0f;
    }

    @Override
    public Mesh3D create() {
        initializeConnectorRadius();
        initializeMesh();
        createConnectors();
        removeDoubles();
        return mesh;
    }

    private void initializeConnectorRadius() {
        connectorRadius = calculateConnectorRadius();
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
        back.apply(new RotateXModifier(-Mathf.HALF_PI));
        back.translateZ(-outerRadius + connectorRadius);
        mesh.append(back);
    }

    private void createFrontConnector() {
        Mesh3D front = createConnector(true, true);
        front.apply(new RotateXModifier(-Mathf.HALF_PI));
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
        Mesh3D mesh = creator.create();
        FaceSelection selection0 = selectLeftRight(mesh);
        FaceSelection selection1 = selectBackFront(mesh);
        extrude(selection0, extrudeEnds0);
        extrude(selection1, extrudeEnds1);
        return mesh;
    }

    private FaceSelection selectLeftRight(Mesh3D mesh) {
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectLeftFaces();
        selection.selectRightFaces();
        return selection;
    }

    private FaceSelection selectBackFront(Mesh3D mesh) {
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectBackFaces();
        selection.selectFrontFaces();
        return selection;
    }

    private void extrude(FaceSelection selection, boolean extrudeEnds) {
        Mesh3D mesh = selection.getMesh();
        for (Face3D face : selection.getFaces()) {
            Mesh3DUtil.extrudeFace(mesh, face, 1,
                    outerRadius - (3 * connectorRadius));
            if (extrudeEnds)
                Mesh3DUtil.extrudeFace(mesh, face, 1, (2 * connectorRadius));
        }
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

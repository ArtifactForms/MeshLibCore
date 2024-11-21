package mesh.creator.creative;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.SegmentedCubeCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.ScaleModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.selection.FaceSelection;

public class PortedCubeCreator implements IMeshCreator {

    private int subdivisions;

    private float radius;

    private float thickness;

    private boolean removeCorners;

    private Mesh3D mesh;

    private List<Face3D> centerFaces;

    public PortedCubeCreator() {
        subdivisions = 1;
        radius = 1;
        thickness = 0.1f;
        removeCorners = true;
        centerFaces = new ArrayList<>();
    }

    @Override
    public Mesh3D create() {
        clearCenterFacesList();
        createSegmentedCube();
        removeCornerFaces();
        selectCenterFaces();
        extrudeCenterFaces();
        removeDoubles();
        solidify();
        subdivide();
        scale();
        return mesh;
    }

    private void clearCenterFacesList() {
        centerFaces.clear();
    }

    private void createSegmentedCube() {
        mesh = new SegmentedCubeCreator(3, 1.5f).create();
    }

    private void scale() {
        mesh.apply(new ScaleModifier(Mathf.ONE_THIRD));
        mesh.apply(new ScaleModifier(radius * 2));
    }

    private void removeCornerFaces() {
        if (!removeCorners)
            return;

        FaceSelection selection = new FaceSelection(mesh);
        Mesh3D cube = new CubeCreator(1.5f).create();
        for (Vector3f v : cube.vertices)
            selection.selectByVertex(v);

        mesh.removeFaces(selection.getFaces());
    }

    private void selectCenterFaces() {
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectCenterDistanceLessThan(Vector3f.ZERO, 1.6f);
        centerFaces.addAll(selection.getFaces());
    }

    private void extrudeCenterFaces() {
        ExtrudeModifier modifier = new ExtrudeModifier();
        modifier.setScale(1);
        modifier.setAmount(-1);
        modifier.modify(mesh, centerFaces);
        mesh.faces.removeAll(centerFaces);
    }

    private void removeDoubles() {
        mesh.removeDoubles();
    }

    private void solidify() {
        new SolidifyModifier(thickness).modify(mesh);
    }

    private void subdivide() {
        new CatmullClarkModifier(subdivisions).modify(mesh);
    }

    public int getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
        this.subdivisions = subdivisions;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public boolean isRemoveCorners() {
        return removeCorners;
    }

    public void setRemoveCorners(boolean removeCorners) {
        this.removeCorners = removeCorners;
    }

}

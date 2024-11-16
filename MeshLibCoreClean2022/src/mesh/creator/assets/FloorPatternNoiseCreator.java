package mesh.creator.assets;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.SolidifyModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

/**
 * Inspiration: https://www.youtube.com/watch?v=aFkBHigEOdE
 */
public class FloorPatternNoiseCreator implements IMeshCreator {

    private float height;

    private float radius;

    private int subdivisions;

    private Mesh3D mesh;

    private FaceSelection faceSelection;

    public FloorPatternNoiseCreator() {
        this(0.2f, 2, 4);
    }

    public FloorPatternNoiseCreator(float height, float radius,
            int subdivisions) {
        this.height = height;
        this.radius = radius;
        this.subdivisions = subdivisions;
    }

    @Override
    public Mesh3D create() {
        createGrid();
        initializeFaceSelection();
        selectAllFaces();
        solidify();
        extrude();
        snapToGround();
        rotateFaces();
        return mesh;
    }

    private void rotateFaces() {
        for (Face3D face : faceSelection.getFaces()) {
            Mesh3DUtil.rotateFaceZ(mesh, face, randomAngle());
            Mesh3DUtil.rotateFaceX(mesh, face, randomAngle());
        }
    }

    private float randomAngle() {
        return Mathf.toRadians(Mathf.random(0, 2));
    }

    private void snapToGround() {
        mesh.translateY(-height * 0.5f);
    }

    private void extrude() {
        new ExtrudeModifier(0.9f, height * 0.5f).modify(mesh,
                faceSelection.getFaces());
    }

    private void solidify() {
        new SolidifyModifier(height * 0.5f).modify(mesh);
    }

    private void initializeFaceSelection() {
        faceSelection = new FaceSelection(mesh);
    }

    private void selectAllFaces() {
        faceSelection.selectAll();
    }

    private void createGrid() {
        mesh = new GridCreator(subdivisions, subdivisions, radius).create();
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
        this.subdivisions = subdivisions;
    }

}

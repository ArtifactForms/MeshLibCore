package mesh.creator.assets;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.BoxCreator;

public class StairsCreator implements IMeshCreator {

    private int nextIndex;

    private Mesh3D mesh;

    private int numSteps;

    private float width;

    private float stepDepth;

    private float stepHeight;

    private boolean floating;

    public StairsCreator() {
        setNumSteps(20);
        setWidth(4);
        setStepDepth(0.5f);
        setStepHeight(0.5f);
        setFloating(false);
    }

    @Override
    public Mesh3D create() {
        mesh = new Mesh3D();

        if (floating)
            createFloatingStairs();
        else
            createStairs();

        return mesh;
    }

    private void createFloatingStairs() {
        BoxCreator creator = new BoxCreator();
        creator.setDepth(stepDepth);
        creator.setHeight(stepHeight);
        creator.setWidth(width);

        for (int i = 0; i < numSteps; i++) {
            Mesh3D step = creator.create();
            step.translateZ(-stepDepth / 2.0f);
            step.translateZ(-i * stepDepth);
            step.translateY(-stepHeight / 2.0f);
            step.translateY(-i * stepHeight);
            mesh.append(step);
        }

        mesh.removeDoubles();
    }

    private void createStairs() {
        for (int i = 0; i < numSteps + 1; i++) {
            nextIndex = mesh.getVertexCount();
            createVerticesAt(i);
            createFacesAt(i);
        }
        addBottomFace();
        addBackFace();
        snapLastVerticesToGround();
    }

    private void snapLastVerticesToGround() {
        mesh.getVertexAt(getLastIndex()).setY(0);
        mesh.getVertexAt(getLastIndex() - 1).setY(0);
    }

    private void createFacesAt(int i) {
        if (i == numSteps)
            return;
        addVerticalQuad();
        addHorizontalQuad();
        addLeftTriangle();
        addRightTriangle();
        addLargeTriangleLeft();
        addLargeTriangleRight();
    }

    private void createVerticesAt(int i) {
        float halfWidth = width / 2.0f;

        Vector3f bottomRight = new Vector3f(halfWidth, -i * stepHeight, -i * stepDepth);
        Vector3f bottomLeft = new Vector3f(-halfWidth, -i * stepHeight, -i * stepDepth);
        Vector3f topLeft = new Vector3f(-halfWidth, -(i + 1) * stepHeight, -i * stepDepth);
        Vector3f topRight = new Vector3f(halfWidth, -(i + 1) * stepHeight, -i * stepDepth);

        mesh.add(bottomRight);
        mesh.add(bottomLeft);
        mesh.add(topLeft);
        mesh.add(topRight);
    }

    private void addTriangle(int a, int b, int c) {
        mesh.addFace(nextIndex + a, nextIndex + b, nextIndex + c);
    }

    private void addQuad(int a, int b, int c, int d) {
        mesh.addFace(nextIndex + a, nextIndex + b, nextIndex + c, nextIndex + d);
    }

    private void addBottomFace() {
        mesh.addFace(1, 0, getLastIndex(), getLastIndex() - 1);
    }

    private void addBackFace() {
        int index = getLastIndex();
        mesh.addFace(index - 1, index, index - 3, index - 2);
    }

    private void addVerticalQuad() {
        addQuad(0, 1, 2, 3);
    }

    private void addHorizontalQuad() {
        addQuad(3, 2, 5, 4);
    }

    private void addLeftTriangle() {
        addTriangle(1, 5, 2);
    }

    private void addRightTriangle() {
        addTriangle(0, 3, 4);
    }

    private void addLargeTriangleLeft() {
        mesh.addFace(nextIndex + 1, getLastIndex() - 1, nextIndex + 5);
    }

    private void addLargeTriangleRight() {
        mesh.addFace(nextIndex + 4, getLastIndex(), nextIndex + 0);
    }

    private int getLastIndex() {
        return ((numSteps + 1) * 4) - 1;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    public float getStepDepth() {
        return stepDepth;
    }

    public void setStepDepth(float stepDepth) {
        this.stepDepth = stepDepth;
    }

    public float getStepHeight() {
        return stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }

}

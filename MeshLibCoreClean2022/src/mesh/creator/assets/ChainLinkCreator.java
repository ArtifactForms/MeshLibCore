package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class ChainLinkCreator implements IMeshCreator {

    private float centerPieceSize;

    private float majorRadius;

    private float minorRadius;

    private int majorSegments;

    private int minorSegments;

    private Mesh3D partOne;

    private Mesh3D partTwo;

    private Mesh3D mesh;

    public ChainLinkCreator() {
        centerPieceSize = 1.5f;
        majorRadius = 1f;
        minorRadius = 0.25f;
        majorSegments = 12;
        minorSegments = 12;
    }

    private void createVertices() {
        float u = 0;
        float v = 0;
        float stepU = Mathf.TWO_PI / minorSegments;
        float stepV = Mathf.TWO_PI / (majorSegments * 2);

        for (int i = 0; i <= majorSegments; i++) {
            for (int j = 0; j < minorSegments; j++) {
                float x = (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.cos(v);
                float y = minorRadius * Mathf.sin(u);
                float z = (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.sin(v);
                mesh.add(new Vector3f(x, y, z));
                u += stepU;
            }
            u = 0;
            v += stepV;
        }
    }

    private void createFaces() {
        for (int j = 0; j < (majorSegments * 2) + 2; j++) {
            for (int i = 0; i < minorSegments; i++) {
                int[] k = new int[] { j % (majorSegments * 2 + 2), (j + 1) % (majorSegments * 2 + 2), i % minorSegments,
                        (i + 1) % minorSegments };
                int index0 = k[1] * minorSegments + k[2];
                int index1 = k[0] * minorSegments + k[2];
                int index2 = k[1] * minorSegments + k[3];
                int index3 = k[0] * minorSegments + k[3];
                Face3D f = new Face3D(index0, index1, index3, index2);
                mesh.add(f);
            }
        }
    }

    private void createPartOne() {
        partOne = new Mesh3D();
        mesh = partOne;
    }

    private void createPartTwo() {
        partTwo = partOne.copy();
        partTwo.rotateY(Mathf.PI);
    }

    private void translateParts() {
        partOne.translateZ(centerPieceSize * 0.5f);
        partTwo.translateZ(-centerPieceSize * 0.5f);
    }

    private void appendParts() {
        mesh.append(partTwo);
    }

    @Override
    public Mesh3D create() {
        createPartOne();
        createVertices();
        createPartTwo();
        translateParts();
        appendParts();
        createFaces();
        return mesh;
    }

    public float getCenterPieceSize() {
        return centerPieceSize;
    }

    public void setCenterPieceSize(float centerPieceSize) {
        this.centerPieceSize = centerPieceSize;
    }

    public float getMajorRadius() {
        return majorRadius;
    }

    public void setMajorRadius(float majorRadius) {
        this.majorRadius = majorRadius;
    }

    public float getMinorRadius() {
        return minorRadius;
    }

    public void setMinorRadius(float minorRadius) {
        this.minorRadius = minorRadius;
    }

    public int getMajorSegments() {
        return majorSegments;
    }

    public void setMajorSegments(int majorSegments) {
        this.majorSegments = majorSegments;
    }

    public int getMinorSegments() {
        return minorSegments;
    }

    public void setMinorSegments(int minorSegments) {
        this.minorSegments = minorSegments;
    }

}

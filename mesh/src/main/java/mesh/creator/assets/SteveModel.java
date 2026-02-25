package mesh.creator.assets;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.TransformAxis;

public class SteveModel {

    private float unit = 1.0f / 16.0f;

    private Vector3f rotationHead;

    private Vector3f rotationLeftArm;

    private Vector3f rotationRightArm;

    private Mesh3D head;

    private Mesh3D torso;

    private Mesh3D leftArm;

    private Mesh3D rightArm;

    private Mesh3D leftLeg;

    private Mesh3D rightLeg;

    Mesh3D mesh;

    public SteveModel() {
        rotationHead = new Vector3f();
        rotationLeftArm = new Vector3f();
        rotationRightArm = new Vector3f();
        create();
    }

    public void rotateHead(float x, float y, float z) {
        head.translateY(28 * unit);

        new RotateModifier(-rotationHead.getZ(), TransformAxis.Z).modify(head);
        head.rotateY(-rotationHead.getY());
        new RotateXModifier(-rotationHead.getX()).modify(head);
        

        new RotateXModifier(x).modify(head);
        head.rotateY(y);
        new RotateModifier(z, TransformAxis.Z).modify(head);

        head.translateY(-28 * unit);

        rotationHead.set(x, y, z);
    }

    public void rotateLeftArm(float x, float y, float z) {
        leftArm.translateY(24 * unit);

        new RotateXModifier(-rotationLeftArm.getZ()).modify(leftArm);
        leftArm.rotateY(-rotationLeftArm.getY());
        new RotateXModifier(-rotationLeftArm.getX()).modify(leftArm);

        new RotateXModifier(x).modify(leftArm);
        leftArm.rotateY(y);
        leftArm.rotateY(z);

        leftArm.translateY(-24 * unit);

        rotationLeftArm.set(x, y, z);
    }

    public void rotateRightArm(float x, float y, float z) {
        rightArm.translateY(24 * unit);

        new RotateXModifier(-rotationRightArm.getZ()).modify(rightArm);
        rightArm.rotateY(-rotationRightArm.getY());
        new RotateXModifier(-rotationRightArm.getX()).modify(rightArm);

        new RotateXModifier(x).modify(rightArm);
        rightArm.rotateY(y);
        rightArm.rotateY(z);

        rightArm.translateY(-24 * unit);

        rotationRightArm.set(x, y, z);
    }

    private void createHead() {
        head = new CubeCreator(4 * unit).create();
        head.translateY(4 * unit);
        mesh.append(head);
    }

    private void createTorso() {
        torso = new BoxCreator(8 * unit, 12 * unit, 4 * unit).create();
        torso.translateY(14 * unit);
        mesh.append(torso);
    }

    private void createLeftArm() {
        leftArm = new BoxCreator(4 * unit, 12 * unit, 4 * unit).create();
        leftArm.translateX(-6 * unit);
        leftArm.translateY(14 * unit);
        mesh.append(leftArm);
    }

    private void createRightArm() {
        rightArm = new BoxCreator(4 * unit, 12 * unit, 4 * unit).create();
        rightArm.translateX(6 * unit);
        rightArm.translateY(14 * unit);
        mesh.append(rightArm);
    }

    private void createLeftLeg() {
        leftLeg = new BoxCreator(4 * unit, 12 * unit, 4 * unit).create();
        leftLeg.translateX(-2 * unit);
        leftLeg.translateY(26 * unit);
        mesh.append(leftLeg);
    }

    private void createRightLeg() {
        rightLeg = new BoxCreator(4 * unit, 12 * unit, 4 * unit).create();
        rightLeg.translateX(2 * unit);
        rightLeg.translateY(26 * unit);
        mesh.append(rightLeg);
    }

    private Mesh3D create() {
        mesh = new Mesh3D();
        createHead();
        createTorso();
        createLeftArm();
        createRightArm();
        createLeftLeg();
        createRightLeg();
        mesh.translateY(-32 * unit);
        return mesh;
    }

    public Mesh3D getMesh() {
        return mesh;
    }

}

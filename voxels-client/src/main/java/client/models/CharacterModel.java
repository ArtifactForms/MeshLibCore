package client.models;

import engine.components.Geometry;
import engine.rendering.Material;
import engine.scene.SceneNode;
import math.Color;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.ScaleModifier;

public class CharacterModel extends SceneNode {

  private SceneNode head;
  private SceneNode torso;
  private SceneNode leftArm;
  private SceneNode rightArm;
  private SceneNode leftLeg;
  private SceneNode rightLeg;

  private Color legColor = Color.getColorFromInt(101, 59, 195);
  private Color torsoColor = Color.getColorFromInt(226, 94, 195);
  private Color armColor = Color.getColorFromInt(101, 59, 195);

  public CharacterModel() {
    build();
  }

  private void build() {

    // -------------------------
    // DIMENSIONS
    // -------------------------
    float legH = 0.4f;
    float torsoH = 0.5f;
    float headH = 0.5f;

    float legHalf = legH / 2f;
    float torsoHalf = torsoH / 2f;
    float headHalf = headH / 2f;

    float torsoW = 0.4f;
    float armH = 0.4f;
    float armW = 0.12f;

    // -------------------------
    // LEGS (Root = Feet Level!)
    // -------------------------
    leftLeg = createPart("LeftLeg", 0.15f, legH, 0.15f, legColor);
    rightLeg = createPart("RightLeg", 0.15f, legH, 0.15f, legColor);

    leftLeg.getTransform().translate(-0.12f, -legHalf, 0);
    rightLeg.getTransform().translate(0.12f, -legHalf, 0);

    addChild(leftLeg);
    addChild(rightLeg);

    // -------------------------
    // TORSO
    // -------------------------
    torso = createPart("Torso", torsoW, torsoH, 0.25f, torsoColor);
    torso.getTransform().translate(0, -(legH + torsoHalf), 0);

    addChild(torso);

    // -------------------------
    // ARMS
    // -------------------------
    leftArm = createPart("LeftArm", armW, armH, armW, armColor);
    rightArm = createPart("RightArm", armW, armH, armW, armColor);

    float armOffsetX = (torsoW / 2f) + (armW / 2f);

    leftArm.getTransform().translate(-armOffsetX, 0f, 0);
    rightArm.getTransform().translate(armOffsetX, 0f, 0);

    torso.addChild(leftArm);
    torso.addChild(rightArm);

    // -------------------------
    // HEAD
    // -------------------------
    head = createPart("Head", 0.6f, headH, 0.6f, Color.getColorFromInt(236, 216, 185));

    head.getTransform().translate(0, -(torsoHalf + headHalf), 0);

    torso.addChild(head);
  }

  private SceneNode createPart(String name, float sx, float sy, float sz, Color color) {

    Mesh3D mesh = new CubeCreator(0.5f).create();
    new ScaleModifier(sx, sy, sz).modify(mesh);

    Material material = new Material(color);
    Geometry geometry = new Geometry(mesh, material);

    return new SceneNode(name, geometry);
  }

  public SceneNode getHead() {
    return head;
  }

  public SceneNode getLeftArm() {
    return leftArm;
  }

  public SceneNode getRightArm() {
    return rightArm;
  }

  public SceneNode getLeftLeg() {
    return leftLeg;
  }

  public SceneNode getRightLeg() {
    return rightLeg;
  }

  public SceneNode getTorso() {
    return torso;
  }
}

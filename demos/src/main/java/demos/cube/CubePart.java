package demos.cube;

import engine.components.StaticGeometry;
import engine.render.Material;
import engine.scene.SceneNode;
import math.Color;
import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.RotateZModifier;
import mesh.modifier.transform.TranslateModifier;

public class CubePart extends SceneNode {

  private float radius;

  private SceneNode top;
  private SceneNode bottom;
  private SceneNode left;
  private SceneNode right;
  private SceneNode front;
  private SceneNode back;

  private Direction direction;

  private static final Material greenMaterial;
  private static final Material redMaterial;
  private static final Material blueMaterial;
  private static final Material orangeMaterial;
  private static final Material yellowMaterial;
  private static final Material whiteMaterial;
  private static final Material blackMaterial;

  static {
    greenMaterial = new Material(Color.getColorFromInt(0, 156, 70));
    redMaterial = new Material(Color.RED);
    blueMaterial = new Material(Color.BLUE);
    orangeMaterial = new Material(Color.getColorFromInt(255, 180, 0));
    yellowMaterial = new Material(Color.YELLOW);
    whiteMaterial = new Material(Color.WHITE);
    blackMaterial = new Material(Color.BLACK);
  }

  public CubePart(float radius, Vector3f position, Direction direction) {
    this.radius = radius;
    this.direction = direction;
    createTop();
    createBottom();
    createLeft();
    createRight();
    createFront();
    createBack();
    getTransform().setPosition(position);
  }

  // -Y
  private void createTop() {
    Mesh3D mesh = new PlaneCreator(radius).create();
    new TranslateModifier(0, -radius, 0).modify(mesh);
    Material material = direction == Direction.TOP ? greenMaterial : blackMaterial;
    StaticGeometry geometry = new StaticGeometry(mesh, material);
    top = new SceneNode("Top", geometry);
    addChild(top);
  }

  // +Y
  private void createBottom() {
    Mesh3D mesh = new PlaneCreator(radius).create();
    new RotateXModifier(-Mathf.PI).modify(mesh);
    new TranslateModifier(0, radius, 0).modify(mesh);
    Material material = direction == Direction.BOTTOM ? greenMaterial : blackMaterial;
    StaticGeometry geometry = new StaticGeometry(mesh, material);
    bottom = new SceneNode("Bottom", geometry);
    addChild(bottom);
  }

  // -X
  private void createLeft() {
    Mesh3D mesh = new PlaneCreator(radius).create();
    new RotateZModifier(-Mathf.HALF_PI).modify(mesh);
    new TranslateModifier(-radius, 0, 0).modify(mesh);
    Material material = direction == Direction.LEFT ? greenMaterial : blackMaterial;
    StaticGeometry geometry = new StaticGeometry(mesh, material);
    left = new SceneNode("Left", geometry);
    addChild(left);
  }

  // +X
  private void createRight() {
    Mesh3D mesh = new PlaneCreator(radius).create();
    new RotateZModifier(Mathf.HALF_PI).modify(mesh);
    new TranslateModifier(radius, 0, 0).modify(mesh);
    Material material = direction == Direction.RIGHT ? greenMaterial : blackMaterial;
    StaticGeometry geometry = new StaticGeometry(mesh, material);
    right = new SceneNode("Right", geometry);
    addChild(right);
  }

  // -Z
  private void createFront() {
    Mesh3D mesh = new PlaneCreator(radius).create();
    new RotateXModifier(-Mathf.HALF_PI).modify(mesh);
    new TranslateModifier(0, 0, -radius).modify(mesh);
    Material material = direction == Direction.FRONT ? greenMaterial : blackMaterial;
    StaticGeometry geometry = new StaticGeometry(mesh, material);
    front = new SceneNode("Front", geometry);
    addChild(front);
  }

  // +Z
  private void createBack() {
    Mesh3D mesh = new PlaneCreator(radius).create();
    new RotateXModifier(Mathf.HALF_PI).modify(mesh);
    new TranslateModifier(0, 0, radius).modify(mesh);
    Material material = direction == Direction.BACK ? greenMaterial : blackMaterial;
    StaticGeometry geometry = new StaticGeometry(mesh, material);
    back = new SceneNode("Back", geometry);
    addChild(back);
  }
}

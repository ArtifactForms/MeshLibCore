package demos.collision.scene;

import demos.collision.GrayBox;
import engine.scene.SceneNode;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.assets.PillarCreator;
import mesh.creator.assets.rock.BoulderCreator;
import mesh.creator.primitives.BoxCreator;
import mesh.creator.primitives.CylinderCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.modifier.transform.SnapToGroundModifier;

public class EnvironmentFactory2 {

  private GrayBox grayBox;

  public SceneNode create() {
    grayBox = new GrayBox(1);

    SceneNode root = new SceneNode();

    root.addChild(createSpawnPlatform());
//    root.addChild(createFloor());
    root.addChild(createPillars());
    root.addChild(createBoulder());

//    root.addChild(new SkyBox(2000));

    return root;
  }

  private SceneNode createSpawnPlatform() {
    float radius = 3;
    float height = 1f;
    CylinderCreator creator = new CylinderCreator();
    creator.setBottomCapFillType(FillType.N_GON);
    creator.setTopCapFillType(FillType.N_GON);
    creator.setTopRadius(radius);
    creator.setBottomRadius(radius);
    creator.setHeight(height);
    Mesh3D mesh = creator.create();
    new SnapToGroundModifier(0).modify(mesh);
    new PlanarVertexCenterModifier().modify(mesh);
    SceneNode platform = grayBox.createGrayBox("Spawn", mesh);
    return platform;
  }

  private SceneNode createFloor() {
    Mesh3D mesh = new BoxCreator(500, 0.1f, 500).create();
    SceneNode floor = grayBox.createGrayBox("Floor", mesh);
    floor.getTransform().setPosition(0, 0.05f, 0);
    return floor;
  }

  private SceneNode createPillars() {
    SceneNode pillars = new SceneNode();
    int num = 20;
    float radius = 20;
    float angleStep = Mathf.TWO_PI / (float) num;
    for (int i = 0; i < num; i++) {
      float x = radius * Mathf.sin(angleStep * i);
      float z = radius * Mathf.cos(angleStep * i);
      SceneNode pillar = createPillar();
      pillar.getTransform().setPosition(x, 0, z);
      pillars.addChild(pillar);
    }
    return pillars;
  }

  private SceneNode createPillar() {
    Mesh3D mesh = new PillarCreator().create();
    SceneNode pillar = grayBox.createGrayBox("Pillar", mesh);
    return pillar;
  }

  private SceneNode createBoulder() {
    Mesh3D mesh = new BoulderCreator().create();
    SceneNode boulderNode = grayBox.createGrayBox("Boulder", mesh);
    boulderNode.getTransform().translate(30, 0, 0);
    return boulderNode;
  }
}

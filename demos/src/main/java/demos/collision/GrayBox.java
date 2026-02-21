package demos.collision;

import java.awt.image.BufferedImage;

import engine.components.Geometry;
import engine.physics.collision.collider.AABBCollider;
import engine.physics.collision.component.ColliderComponent;
import engine.render.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.resources.TextureWrapMode;
import engine.scene.SceneNode;
import math.Bounds;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.assets.StairsCreator;
import mesh.creator.primitives.BoxCreator;
import mesh.modifier.transform.SnapToGroundModifier;
import mesh.modifier.uv.BoxUVModifier;
import mesh.modifier.uv.FlipVModifier;

public class GrayBox {

  private float scale;

  private static Texture texture;
  private static Material debugMaterial;

  static {
    int halfTextureSize = 256;
    int width = halfTextureSize * 2;
    int height = halfTextureSize * 2;

    PrototypeGridTexture pgt = new PrototypeGridTexture(width, height);
    //    pgt.setBackgroundColor(java.awt.Color.WHITE);
    BufferedImage image = pgt.createImage();

    texture = TextureManager.getInstance().createTexture(image);

    if (texture != null) {
      texture.setTextureWrapMode(TextureWrapMode.REPEAT);
      texture.setFilterMode(FilterMode.TRILINEAR);
      debugMaterial = createDebugMaterial();
    } else {
      debugMaterial = new Material();
    }
  }

  public GrayBox(float scale) {
    this.scale = scale;
  }

  public SceneNode blockOutBox(String name, Vector3f position, float size) {
    return blockOutBox(name, position, new Vector3f(size, size, size));
  }

  public SceneNode blockOutBox(String name, Vector3f position, Vector3f size) {
    Mesh3D boxMesh = new BoxCreator(size.x, size.y, size.z).create();
    new SnapToGroundModifier().modify(boxMesh);
    SceneNode node = createGrayBox(name, boxMesh);
    node.getTransform().setPosition(position);
    return node;
  }

  public SceneNode blockOutStrairs(Vector3f position) {
    int steps = 20;
    float stepDepth = 1.0f;
    float stepHeight = 0.3f;
    float width = 10;

    StairsCreator creator = new StairsCreator();
    creator.setFloating(false);
    creator.setNumSteps(steps);
    creator.setStepDepth(stepDepth);
    creator.setStepHeight(stepHeight);
    creator.setWidth(width);

    Mesh3D mesh = creator.create();

    new BoxUVModifier(1).modify(mesh);

    SceneNode node = new SceneNode();
    node.addComponent(new Geometry(mesh, debugMaterial));

    for (int i = 0; i < steps; i++) {
      AABBCollider collider =
          new AABBCollider(new Vector3f(width * 0.5f, stepHeight * 0.5f, stepDepth * 0.5f));
      collider.setLocalOffset(
          new Vector3f(
              0, -(stepHeight * i) - (stepHeight * 0.5f), -stepDepth * i - (stepDepth * 0.5f)));
      ColliderComponent component = new ColliderComponent(collider);
      node.addComponent(component);
    }

    node.getTransform().setPosition(position);

    return node;
  }

  public SceneNode createGrayBox(String name, Mesh3D mesh) {
    new BoxUVModifier(scale).modify(mesh);
    new FlipVModifier().modify(mesh);

    Geometry geometry = new Geometry(mesh, debugMaterial);
    Bounds bounds = geometry.getLocalBounds();
    Vector3f center = bounds.getCenter();

    SceneNode node = new SceneNode(name, geometry);

    AABBCollider collider = new AABBCollider(bounds.getSize().mult(0.5f));
    collider.setLocalOffset(center);
    node.addComponent(new ColliderComponent(collider));

    return node;
  }

  private static Material createDebugMaterial() {
    Material material = new Material();
    material.setDiffuseTexture(texture);
    return material;
  }
}

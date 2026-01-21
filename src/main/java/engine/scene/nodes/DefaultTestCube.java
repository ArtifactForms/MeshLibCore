package engine.scene.nodes;

import engine.components.StaticGeometry;
import engine.render.Material;
import engine.resources.Resources;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.scene.SceneNode;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreatorUV;
import mesh.modifier.SnapToGroundModifier;

public class DefaultTestCube extends SceneNode {

  private Material material;

  public DefaultTestCube(float radius) {
    Mesh3D cubeMesh = new CubeCreatorUV(radius).create();
    cubeMesh.apply(new SnapToGroundModifier());
    Texture texture = TextureManager.getInstance().loadTexture(Resources.DEFAULT_TEST_CUBE_TEXTURE);
    material = new Material();
    material.setUseLighting(false);
    material.setDiffuseTexture(texture);
    addComponent(new StaticGeometry(cubeMesh, material));
  }

  public void setUseLighting(boolean useLighting) {
    material.setUseLighting(useLighting);
  }
}

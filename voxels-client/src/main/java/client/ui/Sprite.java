package client.ui;

import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.scene.SceneNode;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;

public class Sprite extends SceneNode {

  private StaticGeometry geometry;

  public Sprite(String path) {
    Texture texture = TextureManager.getInstance().loadTexture(path);
    int width = texture.getWidth();
    int height = texture.getHeight();
    Material material = new Material();
    material.setDiffuseTexture(texture);  
    Mesh3D mesh = new PlaneCreatorUV(0.5f).create();
    
    new RotateModifier(-Mathf.HALF_PI, TransformAxis.X).modify(mesh);
    new ScaleModifier(width, height, 1).modify(mesh);
    new TranslateModifier(width * 0.5f, height * 0.5f, 0).modify(mesh);
    geometry = new StaticGeometry(mesh, material);
    addComponent(geometry);
  }
}

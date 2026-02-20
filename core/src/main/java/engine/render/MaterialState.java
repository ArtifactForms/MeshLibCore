package engine.render;

import engine.resources.Texture;
import math.Color;

public final class MaterialState {
  public float ambientR, ambientG, ambientB;
  public float diffuseR, diffuseG, diffuseB;
  public float specularR, specularG, specularB;
  public float shininess;
  public boolean useLighting;
  public boolean depthTest;
  public boolean receiveShadows;
  public boolean smoothShading;
  public Texture diffuseTexture;
  public Color baseColor;
}

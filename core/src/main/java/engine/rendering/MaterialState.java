package engine.rendering;

import engine.resources.Texture;
import math.Color;

public final class MaterialState {

  public float ambientR;

  public float ambientG;

  public float ambientB;

  public float diffuseR;

  public float diffuseG;

  public float diffuseB;

  public float specularR;

  public float specularG;

  public float specularB;

  public float shininess;

  public boolean useLighting;

  public boolean depthTest;

  public boolean receiveShadows;

  public boolean smoothShading;

  public Texture diffuseTexture;

  public Color baseColor;
}

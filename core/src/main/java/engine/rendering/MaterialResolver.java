package engine.rendering;

import math.Color;

public final class MaterialResolver {

  public static MaterialState resolve(Material m) {
    MaterialState s = new MaterialState();

    Color c = m.getColor();
    s.baseColor = c != null ? c : Color.WHITE;

    float[] a = safe3(m.getAmbient(), 0.2f);
    float[] d = safe3(m.getDiffuse(), 1.0f);
    float[] sp = safe3(m.getSpecular(), 1.0f);

    s.ambientR = a[0];
    s.ambientG = a[1];
    s.ambientB = a[2];

    s.diffuseR = d[0];
    s.diffuseG = d[1];
    s.diffuseB = d[2];

    s.specularR = sp[0];
    s.specularG = sp[1];
    s.specularB = sp[2];

    s.shininess = m.getShininess();
    s.useLighting = m.isUseLighting();
    s.depthTest = m.isDepthTest();
    s.receiveShadows = m.isReceiveShadows();
    s.diffuseTexture = m.getDiffuseTexture();

    s.smoothShading = m.getShading() == Shading.SMOOTH;

    return s;
  }

  private static float[] safe3(float[] v, float fallback) {
    if (v == null || v.length < 3) {
      return new float[] {fallback, fallback, fallback};
    }

    return new float[] {v[0], v[1], v[2]};
  }
}

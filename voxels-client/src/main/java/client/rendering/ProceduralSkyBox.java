package client.rendering;

import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import engine.rendering.Material;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.topology.FlipFacesModifier;
import mesh.modifier.transform.ScaleModifier;

public class ProceduralSkyBox {

  private final Vector3f sunDir = new Vector3f();
  private final Vector3f moonDir = new Vector3f();

  private final Vector3f dayColor = new Vector3f(1.0f, 0.95f, 0.8f);
  private final Vector3f nightColor = new Vector3f(0.2f, 0.3f, 0.5f);
  private final Vector3f skyDayColor = new Vector3f(0.6f, 0.75f, 1.0f);
  private final Vector3f skyNightColor = new Vector3f(0.05f, 0.08f, 0.15f);

  private final Vector3f currentSkyColor = new Vector3f();
  private final Vector3f currentLightDir = new Vector3f();
  private final Vector3f currentLightColor = new Vector3f();

  private float currentAmbient;

  private final Vector3f tempVec = new Vector3f();

  private final StaticGeometry skyGeo;

  public ProceduralSkyBox(float size) {
    Mesh3D cube = new CubeCreator(0.5f).create();
    new ScaleModifier(size).modify(cube);
    new FlipFacesModifier().modify(cube);
    skyGeo = new StaticGeometry(cube, new Material(new Color(0, 0, 0, 0)));
  }

  public void update(float timeOfDay) {
	float angle = timeOfDay * (float) Math.PI * 2f - (float) Math.PI / 2f;
    sunDir.set((float) Math.sin(angle), (float) -Math.cos(angle), 0.2f).normalizeLocal();
    moonDir.set(sunDir).negateLocal();

    float sunHeight = Math.max(0f, Math.min(1f, -sunDir.y * 0.5f + 0.5f));
    float dayFactor = sunHeight;
    float nightFactor = 1.0f - dayFactor;

    // --- Light-Direction Mix ---
    currentLightDir.set(sunDir).multLocal(dayFactor);
    tempVec.set(moonDir).multLocal(nightFactor);
    currentLightDir.addLocal(tempVec).normalizeLocal();

    // --- Light-Color Mix ---
    currentLightColor.set(dayColor).multLocal(dayFactor);
    tempVec.set(nightColor).multLocal(nightFactor);
    currentLightColor.addLocal(tempVec);

    currentAmbient = 0.08f + 0.5f * dayFactor;

    // --- Sky Color Interpolation ---
    float blend = sunHeight * sunHeight * (3f - 2f * sunHeight);
    currentSkyColor.set(skyNightColor).multLocal(1f - blend);
    tempVec.set(skyDayColor).multLocal(blend);
    currentSkyColor.addLocal(tempVec);
  }

  public void render(Graphics g, float timeOfDay) {
    g.setShader("sky.vert", "sky.frag");
    g.pushMatrix();
    g.setUniform("u_timeOfDay", timeOfDay);
    g.disableDepthMask();
    skyGeo.render(g);
    g.enableDepthMask();
    g.popMatrix();
    g.resetShader();
  }

  public Vector3f getLightDir() {
    return currentLightDir;
  }

  public Vector3f getLightColor() {
    return currentLightColor;
  }

  public Vector3f getSkyColor() {
    return currentSkyColor;
  }

  public float getAmbient() {
    return currentAmbient;
  }
}

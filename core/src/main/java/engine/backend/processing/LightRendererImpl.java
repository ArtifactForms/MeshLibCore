package engine.backend.processing;

import java.util.ArrayList;
import java.util.List;

import engine.rendering.Graphics;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import engine.scene.light.Light;
import engine.scene.light.LightRenderer;
import engine.scene.light.LightType;
import engine.scene.light.PointLight;
import engine.scene.light.SpotLight;
import math.Color;
import math.Mathf;
import processing.core.PApplet;

public class LightRendererImpl implements LightRenderer {

  private boolean debug;

  private PApplet p;

  private Graphics g;

  private List<Light> rendered;

  private boolean lightsOff;

  public LightRendererImpl(PApplet p) {
    p.registerMethod("post", this);
    p.registerMethod("pre", this);
    rendered = new ArrayList<Light>();
    this.p = p;
  }

  @Override
  public void setGraphics(Graphics g) {
    this.g = g;
  }

  @Override
  public void render(Light light) {
    if (lightsOff) {
      p.noLights();
      return;
    }

    switch (light.getType()) {
      case SPOT:
        render((SpotLight) light);
        break;
      case POINT:
        render((PointLight) light);
        break;
      case DIRECTIONAL:
        render((DirectionalLight) light);
        break;
      case AMBIENT:
        render((AmbientLight) light);
        break;
      default:
        logUnexpectedLightType(light.getType());
    }
  }

  public void render(SpotLight light) {
    if (lightsOff) return;
    store(light);
    renderCommon(light.getColor(), light.getConcentration());
    p.spotLight(
        light.getColor().getRed(),
        light.getColor().getGreen(),
        light.getColor().getBlue(),
        light.getPosition().getX(),
        light.getPosition().getY(),
        light.getPosition().getZ(),
        light.getDirection().getX(),
        light.getDirection().getY(),
        light.getDirection().getZ(),
        light.getAngle(),
        light.getConcentration());
  }

  public void render(PointLight light) {
    if (lightsOff) return;
    store(light);
    renderCommon(light.getColor(), light.getIntensity());

    float intensity = light.getIntensity();

    // Retrieve light color
    float red = light.getColor().getRed(); // Gives a value from 0 to 1
    float green = light.getColor().getGreen(); // Same for green
    float blue = light.getColor().getBlue(); // Same for blue

    // Apply intensity to each color component
    red = Mathf.clamp01(red * intensity);
    green = Mathf.clamp01(green * intensity);
    blue = Mathf.clamp01(blue * intensity);

    p.pointLight(
        red,
        green,
        blue,
        light.getPosition().getX(),
        light.getPosition().getY(),
        light.getPosition().getZ());
  }

  public void render(DirectionalLight light) {
    if (lightsOff) return;
    store(light);
    renderCommon(light.getColor(), light.getIntensity());
    p.directionalLight(
        light.getColor().getRed(),
        light.getColor().getGreen(),
        light.getColor().getBlue(),
        light.getDirection().getX(),
        light.getDirection().getY(),
        light.getDirection().getZ());
  }

  @Override
  public void render(AmbientLight light) {
    if (lightsOff) return;
    store(light);
    renderCommon(light.getColor(), 1);
    Color c = light.getColor();
    p.ambientLight(c.getRed(), c.getGreen(), c.getBlue());
  }

  private void store(Light light) {
    rendered.add(light);
  }

  public void pre() {
    lightsOff = false;
  }

  public void post() {
    rendered.clear();
  }

  public void off() {
    if (lightsOff) return;
    lightsOff = true;
    p.g.noLights();
  }

  public void on() {
    if (!lightsOff) return;
    lightsOff = false;
    // Re-render light
    // Processing does not provide this by default
    List<Light> toRerender = new ArrayList<Light>(rendered);
    rendered.clear();
    for (Light light : toRerender) {
      light.render(this);
    }
  }

  private void renderCommon(Color color, float intensity) {
    if (!debug) return;
    float scaledRed = color.getRed() * intensity;
    float scaledGreen = color.getGreen() * intensity;
    float scaledBlue = color.getBlue() * intensity;

    System.out.println(
        "Rendering light with values - R: "
            + scaledRed
            + ", G: "
            + scaledGreen
            + ", B: "
            + scaledBlue);
  }

  private void logUnexpectedLightType(LightType lightType) {
    System.err.println("Unexpected LightType: " + lightType);
    throw new IllegalArgumentException("Unexpected value: " + lightType);
  }
}

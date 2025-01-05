package engine.processing;

import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import engine.scene.light.Light;
import engine.scene.light.LightRenderer;
import engine.scene.light.LightType;
import engine.scene.light.PointLight;
import engine.scene.light.SpotLight;
import math.Color;
import processing.core.PApplet;
import workspace.ui.Graphics;

public class LightRendererImpl implements LightRenderer {

  private boolean debug;

  private PApplet p;

  private Graphics g;

  public LightRendererImpl(PApplet p) {
    this.p = p;
  }

  @Override
  public void setGraphics(Graphics g) {
    this.g = g;
  }

  @Override
  public void render(Light light) {
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
    renderCommon(light.getColor(), light.getConcentration());
    p.spotLight(
        light.getColor().getRedInt(),
        light.getColor().getGreenInt(),
        light.getColor().getBlueInt(),
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
    renderCommon(light.getColor(), light.getIntensity());
    p.pointLight(
        light.getColor().getRedInt(),
        light.getColor().getGreenInt(),
        light.getColor().getBlueInt(),
        light.getPosition().getX(),
        light.getPosition().getY(),
        light.getPosition().getZ());

    float intensity = light.getIntensity();

    // Retrieve light color
    float red = light.getColor().getRed(); // Gives a value from 0 to 1
    float green = light.getColor().getGreen(); // Same for green
    float blue = light.getColor().getBlue(); // Same for blue

    // Apply intensity to each color component
    red *= intensity;
    green *= intensity;
    blue *= intensity;

    // Call pointLight() with adjusted color
    p.pointLight(
        red * 255, // Scale back to 0-255 range for Processing's pointLight
        green * 255, // Same for green
        blue * 255, // Same for blue
        light.getPosition().getX(),
        light.getPosition().getY(),
        light.getPosition().getZ());
  }

  public void render(DirectionalLight light) {
    renderCommon(light.getColor(), light.getIntensity());
    p.directionalLight(
        light.getColor().getRedInt(),
        light.getColor().getGreenInt(),
        light.getColor().getBlueInt(),
        light.getDirection().getX(),
        light.getDirection().getY(),
        light.getDirection().getZ());
  }

  @Override
  public void render(AmbientLight light) {
    renderCommon(light.getColor(), 1);
    g.setAmbientColor(light.getColor());
  }

  private void renderCommon(Color color, float intensity) {
    if (!debug) return;
    float scaledRed = color.getRedInt() * intensity;
    float scaledGreen = color.getGreenInt() * intensity;
    float scaledBlue = color.getBlueInt() * intensity;

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

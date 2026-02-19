package engine.processing;

import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import engine.scene.light.Light;
import engine.scene.light.LightRenderer;
import engine.scene.light.LightType;
import engine.scene.light.PointLight;
import engine.scene.light.SpotLight;
import math.Mathf;
import math.Vector3f;
import processing.core.PApplet;
import workspace.ui.Graphics;

public class LightGizmoRenderer implements LightRenderer {

  private float radius = 3f;

  private float size = 0.1f;

  private PApplet p;

  private Graphics g;

  public LightGizmoRenderer(PApplet p) {
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

  @Override
  public void render(AmbientLight light) {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(SpotLight light) {
    render(light.getPosition());
  }

  @Override
  public void render(PointLight light) {
    render(light.getPosition());
  }

  private void render(Vector3f position) {
    p.pushStyle();
    p.pushMatrix();
    p.translate(position.x, position.y, position.z);

    p.fill(128);
    p.noStroke();
    p.box(size);

    p.noFill();

    p.stroke(Laf.LIGHT_GIZMO_X.getRGBA());
    p.ellipse(0, 0, radius, radius);

    p.rotateX(Mathf.HALF_PI);
    p.stroke(Laf.LIGHT_GIZMO_Y.getRGBA());
    p.ellipse(0, 0, radius, radius);

    p.rotateY(Mathf.HALF_PI);
    p.stroke(Laf.LIGHT_GIZMO_Z.getRGBA());
    p.ellipse(0, 0, radius, radius);

    p.popMatrix();
    p.popStyle();
  }

  @Override
  public void render(DirectionalLight light) {
    p.pushStyle();
    p.pushMatrix();

    // Set up the starting point (you can choose the origin or a fixed spot to render from)
    p.translate(0, 0, 0); // Visualize the light emanating from the origin

    // Visualize the direction as a line extending in the light's direction vector
    Vector3f direction = light.getDirection();

    // Normalize the direction to ensure it renders consistently
    float length = radius; // Set the visualization length
    float dirX = direction.x * length;
    float dirY = direction.y * length;
    float dirZ = direction.z * length;

    p.stroke(Laf.LIGHT_GIZMO_DIRECTIONAL.getRGBA());
    p.line(0, 0, 0, dirX, dirY, dirZ);

    p.popMatrix();
    p.popStyle();
  }

  private void logUnexpectedLightType(LightType lightType) {
    System.err.println("Unexpected LightType: " + lightType);
    throw new IllegalArgumentException("Unexpected value: " + lightType);
  }
}

package engine.physics.ray;

import engine.components.Transform;
import engine.runtime.input.Input;
import engine.scene.camera.Camera;
import math.Ray3f;
import math.Vector3f;

public class Raycaster {

  public static Ray3f crossHairRay(Camera camera) {
    Transform t = camera.getTransform();

    Vector3f origin = t.getPosition();
    Vector3f direction = t.getForward();

    return new Ray3f(origin, direction);
  }

  public static Ray3f screenPointToRay(
      Camera camera, float mouseX, float mouseY, float width, float height) {
    float nx = ((2f * mouseX) / width) - 1f;
    float ny = 1f - ((2f * mouseY) / height); // 1.0 at top, -1.0 at bottom

    float tanFovY = (float) Math.tan(camera.getFieldOfView() * 0.5f);
    float tanFovX = tanFovY * camera.getAspectRatio();

    Transform t = camera.getTransform();

    Vector3f direction =
        new Vector3f(t.getForward())
            .addLocal(t.getRight().mult(nx * tanFovX))
            .addLocal(t.getUp().mult(ny * tanFovY))
            .normalizeLocal();

    return new Ray3f(t.getPosition(), direction);
  }

  public static Ray3f screenPointToRay(Camera camera, Input input) {
    float mouseX = input.getMouseX();
    float mouseY = input.getLastMouseY();
    float width = input.getScreenWidth();
    float height = input.getScreenHeight();
    return screenPointToRay(camera, mouseX, mouseY, width, height);
  }
}

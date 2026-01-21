package engine.demos.ray;

import engine.components.Transform;
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
  
}

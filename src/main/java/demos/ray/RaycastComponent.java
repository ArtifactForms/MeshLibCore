package demos.ray;

import engine.components.Component;
import math.Ray3f;

public interface RaycastComponent extends Component {

  /**
   * Tests the given ray against this component.
   *
   * @param ray the ray in world space
   * @return a RaycastHit if an intersection occurred, otherwise null
   */
  RaycastHit raycast(Ray3f ray);
}

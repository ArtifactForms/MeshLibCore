package engine.demos.jam26.world;

import engine.demos.ray.RaycastHit;
import math.Ray3f;

public interface Collider {
  RaycastHit raycast(Ray3f ray);
}

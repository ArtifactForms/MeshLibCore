package demos.jam26port.world;

import demos.ray.RaycastHit;
import math.Ray3f;

public interface Collider {
  RaycastHit raycast(Ray3f ray);
}

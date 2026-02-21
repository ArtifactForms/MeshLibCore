package demos.jam26port.combat;

import demos.jam26port.world.SphereCollider;
import math.Ray3f;
import math.Vector3f;

public class RaySphereIntersection {

  public static float intersect(Ray3f ray, SphereCollider sphere) {

    Vector3f origin = ray.getOrigin();
    Vector3f dir = ray.getDirection();
    Vector3f center = sphere.getCenter();
    float radius = sphere.getRadius();

    // Vector from ray origin to sphere center
    Vector3f L = center.subtract(origin);

    // Project onto ray direction
    float tca = L.dot(dir);

    // If sphere is behind ray
    if (tca < 0) return -1;

    // Distance from center to ray
    float d2 = L.dot(L) - tca * tca;
    float r2 = radius * radius;

    // Miss
    if (d2 > r2) return -1;

    // Distance from closest approach to intersection
    float thc = (float) Math.sqrt(r2 - d2);

    // Nearest hit distance
    float t0 = tca - thc;

    return t0 >= 0 ? t0 : -1;
  }
}

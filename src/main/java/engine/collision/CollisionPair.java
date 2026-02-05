package engine.collision;

import engine.collision.component.ColliderComponent;

public final class CollisionPair {

  private final ColliderComponent a;
  private final ColliderComponent b;

  public CollisionPair(ColliderComponent a, ColliderComponent b) {
    if (System.identityHashCode(a) <= System.identityHashCode(b)) {
      this.a = a;
      this.b = b;
    } else {
      this.a = b;
      this.b = a;
    }
  }

  public ColliderComponent a() {
    return a;
  }

  public ColliderComponent b() {
    return b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CollisionPair)) return false;
    CollisionPair other = (CollisionPair) o;
    return a == other.a && b == other.b;
  }

  @Override
  public int hashCode() {
    return System.identityHashCode(a) * 31 + System.identityHashCode(b);
  }
}

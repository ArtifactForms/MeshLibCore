package engine.collision.component;

import java.util.ArrayList;
import java.util.List;

import engine.collision.CollisionListener;
import engine.collision.collider.Collider;
import engine.collision.contact.Contact;
import engine.components.AbstractComponent;
import engine.components.Transform;
import math.Vector3f;

public class ColliderComponent extends AbstractComponent {

  private final Collider collider;
  private boolean isTrigger = false;

  private final List<CollisionListener> listeners = new ArrayList<>();

  public ColliderComponent(Collider collider) {
    if (collider == null) {
      throw new IllegalArgumentException("Collider cannot be null.");
    }
    this.collider = collider;
  }

  public Transform getTransform() {
    if (getOwner() == null) {
      throw new IllegalStateException("ColliderComponent has no owner.");
    }
    return getOwner().getTransform();
  }

  public Vector3f getWorldPosition() {
    if (getOwner() == null) {
      throw new IllegalStateException("ColliderComponent has no owner.");
    }
    return getOwner().getWorldPosition().add(collider.getLocalOffset());
  }

  public Collider getCollider() {
    return collider;
  }

  public boolean isTrigger() {
    return isTrigger;
  }

  public void setTrigger(boolean trigger) {
    this.isTrigger = trigger;
  }

  public void addListener(CollisionListener listener) {
    listeners.add(listener);
  }

  public void removeListener(CollisionListener listener) {
    listeners.remove(listener);
  }

  public void notifyEnter(ColliderComponent other, Contact contact) {
    for (CollisionListener l : listeners) {
      l.onCollisionEnter(this, other, contact);
    }
  }

  public void notifyStay(ColliderComponent other, Contact contact) {
    for (CollisionListener l : listeners) {
      l.onCollisionStay(this, other, contact);
    }
  }

  public void notifyExit(ColliderComponent other) {
    for (CollisionListener l : listeners) {
      l.onCollisionExit(this, other);
    }
  }
}

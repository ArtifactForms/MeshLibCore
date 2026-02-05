package engine.collision;

import engine.collision.component.ColliderComponent;
import engine.collision.contact.Contact;

public interface CollisionListener {

  void onCollisionEnter(ColliderComponent self, ColliderComponent other, Contact contact);

  void onCollisionStay(ColliderComponent self, ColliderComponent other, Contact contact);

  void onCollisionExit(ColliderComponent self, ColliderComponent other);
}

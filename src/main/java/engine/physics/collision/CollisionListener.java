package engine.physics.collision;

import engine.physics.collision.component.ColliderComponent;
import engine.physics.collision.contact.Contact;

public interface CollisionListener {

  void onCollisionEnter(ColliderComponent self, ColliderComponent other, Contact contact);

  void onCollisionStay(ColliderComponent self, ColliderComponent other, Contact contact);

  void onCollisionExit(ColliderComponent self, ColliderComponent other);
}

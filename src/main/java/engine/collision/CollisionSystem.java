package engine.collision;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.collision.component.ColliderComponent;
import engine.collision.contact.Contact;
import engine.collision.narrowphase.CollisionTests;
import engine.scene.Scene;
import engine.scene.SceneNode;

public class CollisionSystem {

  private final Set<CollisionPair> previousPairs = new HashSet<>();
  private final Set<CollisionPair> currentPairs = new HashSet<>();

  // tmp buffer per frame
  private final List<ColliderComponent> colliders = new ArrayList<>();

  public void update(Scene scene) {

    collectColliders(scene);
    currentPairs.clear();

    for (int i = 0; i < colliders.size(); i++) {
      for (int j = i + 1; j < colliders.size(); j++) {

        ColliderComponent a = colliders.get(i);
        ColliderComponent b = colliders.get(j);

        Contact contact = CollisionTests.test(a, b);
        if (contact == null) continue;

        CollisionPair pair = new CollisionPair(a, b);
        currentPairs.add(pair);

        if (!previousPairs.contains(pair)) {
          onCollisionEnter(a, b, contact);
        } else {
          onCollisionStay(a, b, contact);
        }
      }
    }

    for (CollisionPair pair : previousPairs) {
      if (!currentPairs.contains(pair)) {
        onCollisionExit(pair.a(), pair.b());
      }
    }

    swapBuffers();
  }

  /* =======================
  Scene Traversal
  ======================= */

  private void collectColliders(Scene scene) {
    colliders.clear();

    scene.visitRootNodes(
        (SceneNode node) -> {
          if (!node.isActive()) return;
          colliders.addAll(node.getComponents(ColliderComponent.class));
        });
  }

  private void swapBuffers() {
    previousPairs.clear();
    previousPairs.addAll(currentPairs);
  }

  /* =======================
  Event Hooks (Contact-aware)
  ======================= */

  private void onCollisionEnter(ColliderComponent a, ColliderComponent b, Contact c) {

    a.notifyEnter(b, c);
    b.notifyEnter(a, c.inverted());
  }

  private void onCollisionStay(ColliderComponent a, ColliderComponent b, Contact c) {

    a.notifyStay(b, c);
    b.notifyStay(a, c.inverted());
  }

  private void onCollisionExit(ColliderComponent a, ColliderComponent b) {

    a.notifyExit(b);
    b.notifyExit(a);
  }
}

package engine.physics;

import java.util.ArrayList;
import java.util.List;

import engine.collision.component.ColliderComponent;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.system.AbstractSystem;
import math.Vector3f;

public class PhysicsQuerySystem extends AbstractSystem {

  private final List<ColliderComponent> colliders = new ArrayList<>();

  @Override
  public void update(float deltaTime) {}

  public SweepResult sweepCapsuleAtPosition(
      Scene scene, ColliderComponent capsuleComponent, Vector3f startPos, Vector3f delta) {

    collectColliders();

    SweepResult best = SweepResult.noHit();

    for (ColliderComponent other : colliders) {

      if (other == capsuleComponent) continue;

      SweepResult hit =
          SweepTests.sweepCapsuleVsColliderAtPosition(capsuleComponent, startPos, other, delta);

      if (!hit.hasHit()) continue;

      if (!best.hasHit() || hit.getTOI() < best.getTOI()) best = hit;
    }

    return best;
  }

  public SweepResult sweepCapsule(ColliderComponent capsule, Vector3f delta) {

    collectColliders();

    float closestTOI = 1f;
    SweepResult bestResult = SweepResult.noHit();

    for (ColliderComponent other : colliders) {

      if (other == capsule) continue;
      if (!other.isActive()) continue;

      SweepResult result = SweepTests.sweepCapsuleVsCollider(capsule, other, delta);

      if (!result.hasHit()) continue;

      if (result.getTOI() < closestTOI) {
        closestTOI = result.getTOI();
        bestResult = result;
      }
    }

    return bestResult;
  }

  /* ========================================================= */

  private void collectColliders() {

    colliders.clear();

    getScene()
        .visitRootNodes(
            (SceneNode node) -> {
              if (!node.isActive()) return;

              colliders.addAll(node.getComponents(ColliderComponent.class));
            });
  }
}

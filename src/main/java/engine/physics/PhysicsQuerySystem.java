package engine.physics;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import engine.physics.collision.component.ColliderComponent;
import engine.scene.SceneNode;
import engine.system.AbstractSceneSystem;
import engine.system.UpdatePhase;
import math.Vector3f;

/**
 * Provides physics query functionality (sweep tests).
 *
 * <p>During the PHYSICS phase, all active colliders in the scene are collected and cached. Query
 * operations then operate on this cached list for the remainder of the frame.
 *
 * <p>This ensures deterministic and efficient physics queries.
 */
public class PhysicsQuerySystem extends AbstractSceneSystem {

  private final List<ColliderComponent> colliders = new ArrayList<>();

  private final List<PhysicsQueryListener> listeners = new ArrayList<PhysicsQueryListener>();

  @Override
  public EnumSet<UpdatePhase> getPhases() {
    // Prepare collider cache during PHYSICS phase
    return EnumSet.of(UpdatePhase.PHYSICS);
  }

  @Override
  public void update(UpdatePhase phase, float deltaTime) {

    if (phase == UpdatePhase.PHYSICS) {
      collectColliders();
    }
  }

  /**
   * Performs a capsule sweep against all cached colliders.
   *
   * @param capsule The capsule collider performing the sweep.
   * @param delta The movement vector.
   * @return The closest sweep result.
   */
  public SweepResult sweepCapsule(ColliderComponent capsule, Vector3f delta) {

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

    notifyListeners(new PhysicsQueryEvent(capsule, delta, bestResult));

    return bestResult;
  }

  /* ========================================================= */

  /** Collects all active collider components from the scene. Called once per PHYSICS phase. */
  private void collectColliders() {

    colliders.clear();

    getScene()
        .visitRootNodes(
            (SceneNode node) -> {
              if (!node.isActive()) return;

              colliders.addAll(node.getComponents(ColliderComponent.class));
            });
  }

  protected void notifyListeners(PhysicsQueryEvent e) {
    for (PhysicsQueryListener l : listeners) l.onSweepCapsule(e);
  }

  public void addListener(PhysicsQueryListener listener) {
    if (listener == null) throw new IllegalArgumentException("Listener cannot be null.");
    listeners.add(listener);
  }

  public void removeListener(PhysicsQueryListener listener) {
    listeners.remove(listener);
  }
}

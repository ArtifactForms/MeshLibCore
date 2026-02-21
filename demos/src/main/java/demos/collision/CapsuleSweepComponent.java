package demos.collision;

import engine.components.AbstractComponent;
import engine.physics.PhysicsQuerySystem;
import engine.physics.SweepResult;
import engine.physics.collision.component.ColliderComponent;
import math.Vector3f;

public class CapsuleSweepComponent extends AbstractComponent {

  private Vector3f previousPos;
  private static final float EPSILON = 0.0001f;
  private ColliderComponent colliderComponent;

  public CapsuleSweepComponent(ColliderComponent colliderComponent) {
    this.colliderComponent = colliderComponent;
  }

  @Override
  public void onAttach() {
    previousPos = getOwner().getWorldPosition();
    //    physics = new PhysicsQuerySystem(); // besser später als Scene-System
  }

  @Override
  public void onUpdate(float tpf) {
    PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);

    if (physics == null) {
      throw new IllegalStateException(
          "CapsuleSweepComponent requires PhysicsQuerySystem to be registered in the Scene.");
    }
    
    Vector3f currentPos = getOwner().getWorldPosition();

    // delta = current - previous
    Vector3f delta = currentPos.subtract(previousPos);

    if (delta.lengthSquared() == 0f) {
      return;
    }

    SweepResult result = physics.sweepCapsule(colliderComponent, delta);

    if (result.hasHit() && result.getTOI() < 1f) {

      float safeT = Math.max(0f, result.getTOI() - EPSILON);

      Vector3f safeMove = delta.mult(safeT);
      Vector3f correctedPos = previousPos.add(safeMove);

      getOwner().getTransform().setPosition(correctedPos);
    }

    previousPos = getOwner().getWorldPosition();
  }
}

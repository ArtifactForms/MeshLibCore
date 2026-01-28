package engine.demos.jam26.enemy;

import engine.components.AbstractComponent;
import engine.scene.camera.Camera;
import math.Vector3f;

public class EnemyChaseComponent extends AbstractComponent {

  private float moveSpeed = 64f;
  private float stopDistance = 20f;
  private float sightDistance = 300f; // Aggro-Range

  @Override
  public void onUpdate(float tpf) {

    Camera cam = getOwner().getScene().getActiveCamera();
    if (cam == null) return;

    Vector3f enemyPos = getOwner().getTransform().getPosition();
    Vector3f camPos = cam.getTransform().getPosition();

    Vector3f toPlayer = camPos.subtract(enemyPos);

    // Just XZ
    toPlayer.y = 0;

    float distance = toPlayer.length();

    // ❌ out ouf sight → do nothing
    if (distance > sightDistance) return;

    // ❌ too near or exact
    if (distance < stopDistance || distance == 0f) return;

    toPlayer.normalizeLocal();

    Vector3f delta = toPlayer.mult(moveSpeed * tpf);
    getOwner().getTransform().translate(delta);
  }

  public void setMoveSpeed(float moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  public void setStopDistance(float stopDistance) {
    this.stopDistance = stopDistance;
  }

  public void setSightDistance(float sightDistance) {
    this.sightDistance = sightDistance;
  }
}

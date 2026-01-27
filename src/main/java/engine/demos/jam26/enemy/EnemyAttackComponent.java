package engine.demos.jam26.enemy;

import engine.components.AbstractComponent;
import engine.demos.jam26.assets.AssetRefs;
import engine.demos.jam26.player.PlayerHealthComponent;
import engine.scene.audio.SoundManager;
import engine.scene.camera.Camera;
import math.Vector3f;

public class EnemyAttackComponent extends AbstractComponent {

  private float attackRange;
  private float damage;
  private float attackCooldown;

  private PlayerHealthComponent health;

  private float cooldownTimer = 0f;

  public EnemyAttackComponent(PlayerHealthComponent health) {
    this.attackRange = 48;
    this.damage = 10;
    this.attackCooldown = 1.0f;
    this.health = health;
  }

  @Override
  public void onUpdate(float tpf) {
    cooldownTimer -= tpf;
    if (cooldownTimer > 0) return;

    Camera cam = getOwner().getScene().getActiveCamera();
    if (cam == null) return;

    Vector3f enemyPos = getOwner().getTransform().getPosition();
    Vector3f playerPos = cam.getTransform().getPosition();

    if (enemyPos.distanceSquared(playerPos) <= attackRange * attackRange) {
      attack(playerPos);
      cooldownTimer = attackCooldown;
    }
  }

  private void attack(Vector3f playerPos) {
    if (health != null) {
      SoundManager.playEffect(AssetRefs.SOUND_PLAYER_HIT_KEY);
      health.damage(damage);
    }
  }
}

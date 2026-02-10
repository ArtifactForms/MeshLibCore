package demos.jam26port.enemy;

import demos.jam26port.game.event.PlayerDamageEvent;
import demos.jam26port.game.world.WorldContext;
import engine.components.AbstractComponent;
import math.Vector3f;

public class EnemyAttackComponent extends AbstractComponent {

  private float attackRange;
  private float damage;
  private float attackCooldown;
  private float cooldownTimer = 0f;

  private WorldContext world;

  public EnemyAttackComponent(WorldContext world) {
    this.attackRange = 64;
    this.damage = 10;
    this.attackCooldown = 1.0f;
    this.world = world;
  }

  @Override
  public void onUpdate(float tpf) {
    cooldownTimer -= tpf;
    if (cooldownTimer > 0) return;

    Vector3f enemyPos = getOwner().getTransform().getPosition();
    Vector3f playerPos = world.getPlayer().getPosition();

    if (enemyPos.distanceSquared(playerPos) <= attackRange * attackRange) {
      attackPlayer(this.damage);
      cooldownTimer = attackCooldown;
    }
  }

  private void attackPlayer(float damage) {
    PlayerDamageEvent event = new PlayerDamageEvent(damage);
    world.perform(event);
  }
}

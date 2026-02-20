package demos.jam26port.player;

import demos.jam26port.ui.HitFlashComponent;
import engine.components.AbstractComponent;

public class PlayerHealthComponent extends AbstractComponent {

  private float maxHealth = 100f;
  private float health = 100f;

  private boolean dead = false;

  public void damage(float amount) {
    if (dead) return;

    health -= amount;
    if (health <= 0f) {
      health = 0f;
      die();
    }
    
    HitFlashComponent hitFlash = getOwner().getComponent(HitFlashComponent.class);
    if (hitFlash != null) {
	hitFlash.trigger(1);
    }
  }

  public void heal(float amount) {
    if (dead) return;

    health += amount;
    if (health > maxHealth) {
      health = maxHealth;
    }
  }

  private void die() {
    if (dead) return;
    dead = true;
    onDeath();
  }

  /**
   * Override or hook into this from outside (GameState, Demo logic). Jam-friendly extension point.
   */
  protected void onDeath() {
    // default: do nothing
  }

  public boolean isDead() {
    return dead;
  }

  public float getHealth() {
    return health;
  }

  public float getMaxHealth() {
    return maxHealth;
  }

  public float getHealth01() {
    return maxHealth <= 0f ? 0f : health / maxHealth;
  }

  /** Optional utility for restart / respawn. */
  public void reset() {
    health = maxHealth;
    dead = false;
  }
}

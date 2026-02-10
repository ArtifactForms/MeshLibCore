package demos.jam26port.player;

import engine.components.AbstractComponent;
import engine.game.lifecycle.Health;
import engine.game.lifecycle.HealthListener;

public class HealthComponent extends AbstractComponent {

  private final Health health;

  public HealthComponent(float maxHealth) {
    this.health = new Health(maxHealth);
  }

  @Override
  public void onAttach() {
    health.addListener(
        new HealthListener() {

          @Override
          public void onHealed(float amount, float newHealth) {
            // TODO Auto-generated method stub

          }

          @Override
          public void onDeath() {
            onPlayerDeath();
          }

          @Override
          public void onDamaged(float amount, float newHealth) {
            onPlayerDamaged();
          }
        });
  }

  protected void onPlayerDeath() {}

  protected void onPlayerDamaged() {}

  public void applyDamage(float amount) {
    health.applyDamage(amount);
  }

  public void heal(float amount) {
    health.heal(amount);
  }

  public boolean isDead() {
    return health.isDead();
  }

  public float getCurrentHealth() {
    return health.getCurrent();
  }

  public float getMaxHealth() {
    return health.getMax();
  }

  public float getHealth01() {
    return health.get01();
  }
}

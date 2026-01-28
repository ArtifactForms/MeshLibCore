package engine.demos.jam26.pickup;

import engine.demos.jam26.assets.AssetRefs;
import engine.demos.jam26.player.PlayerHealthComponent;
import engine.scene.audio.SoundManager;

public class HealthPickupComponent extends PickupComponent {

  private int amount;
  private PlayerHealthComponent health;

  public HealthPickupComponent(PlayerHealthComponent health, int amount) {
    this.amount = amount;
    this.health = health;
  }

  @Override
  protected void applyEffect() {
    SoundManager.playEffect(AssetRefs.SOUND_HEALTH_PICK_UP_KEY);
    health.heal(amount);
  }

  @Override
  protected boolean canPickUp() {
    return health.getHealth() < health.getMaxHealth();
  }
}

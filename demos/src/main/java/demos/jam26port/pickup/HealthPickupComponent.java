package demos.jam26port.pickup;

import demos.jam26port.assets.AssetRefs;
import demos.jam26port.player.PlayerContext;
import engine.scene.audio.SoundManager;

public class HealthPickupComponent extends PickupComponent {

  private int amount;

  public HealthPickupComponent(PlayerContext player, int amount) {
    super(player);
    this.amount = amount;
  }

  @Override
  protected void applyEffect() {
    player.getHealth().heal(amount);
    SoundManager.playEffect(AssetRefs.SOUND_HEALTH_PICK_UP_KEY);
  }

  @Override
  protected boolean canPickUp() {
    /*
     * Why NOT: return player.getHealth().getCurrentHealth() < player.getHealth().getMaxHealth();
     *
     * and why this matters Pickups shouldn’t care why health is capped
     *
     * Later we might add:
     *  - overheal rules
     *  - difficulty modifiers
     *  - status effects
     *  And nothing in pickup code changes.
     */
    return player.canReceiveHealth();
  }
}

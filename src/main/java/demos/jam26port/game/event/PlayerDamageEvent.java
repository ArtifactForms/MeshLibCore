package demos.jam26port.game.event;

public class PlayerDamageEvent extends GameEvent {

  private float amount;

  public PlayerDamageEvent(float amount) {
    this.amount = amount;
  }

  public float getAmount() {
    return amount;
  }
}

package engine.components.time;

import engine.components.AbstractComponent;
import engine.game.time.GameClock;

/** Engine adapter that updates a {@link GameClock} each frame. */
public final class GameClockComponent extends AbstractComponent {

  private final GameClock clock;

  public GameClockComponent(GameClock clock) {
    this.clock = clock;
  }

  @Override
  public void onUpdate(float tpf) {
    clock.update(tpf);
  }

  public GameClock getClock() {
    return clock;
  }
}

package server.events.events.world;

/**
 * Event fired when the world time changes.
 *
 * <p>This event is triggered whenever the server updates the global world time.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>React to time changes (e.g. day/night transitions)
 *   <li>Trigger scheduled gameplay events
 *   <li>Synchronize client-side systems
 * </ul>
 *
 * <p>This event is not cancellable.
 */
public class WorldTimeChangedEvent extends WorldEvent {

  /** The new world time value. */
  private final long time;

  /**
   * Creates a new {@code WorldTimeChangedEevent}.
   *
   * @param time The updated world time.
   */
  public WorldTimeChangedEvent(long time) {
    this.time = time;
  }

  /**
   * Returns the current world time.
   *
   * @return The world time.
   */
  public long getTime() {
    return time;
  }
}

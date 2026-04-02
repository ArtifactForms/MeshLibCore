package server.events;

/**
 * Base class for events that can be cancelled.
 *
 * <p>This class provides a default implementation of the {@link Cancellable} interface and is
 * intended to be extended by events that support cancellation.
 *
 * <p>When an event is cancelled, the system that triggered the event is expected to stop further
 * processing and skip any default behavior associated with the event.
 *
 * <p>Typical examples include:
 *
 * <ul>
 *   <li>Preventing a player from joining ({@code PlayerPreJoinEvent})
 *   <li>Blocking chat messages
 *   <li>Stopping command execution
 * </ul>
 *
 * <p>Note: The effect of cancellation depends on the event dispatcher and the calling system. This
 * class only provides the state and contract for cancellation.
 */
public abstract class CancellableEvent extends GameEvent implements Cancellable {

  /**
   * Indicates whether this event has been cancelled.
   *
   * <p>Defaults to {@code false}.
   */
  private boolean cancelled = false;

  /**
   * Returns whether this event has been cancelled.
   *
   * @return {@code true} if the event is cancelled, {@code false} otherwise.
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * Sets whether this event is cancelled.
   *
   * <p>Setting this to {@code true} signals that the event should not be processed further.
   *
   * @param cancelled {@code true} to cancel the event, {@code false} otherwise.
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}

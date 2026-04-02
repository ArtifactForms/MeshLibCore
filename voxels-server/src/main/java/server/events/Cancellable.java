package server.events;

/**
 * Represents an event that can be cancelled.
 *
 * <p>Implementations of this interface allow listeners to prevent further processing of an event.
 * If an event is marked as cancelled, the calling system is expected to stop executing the default
 * behavior associated with that event.
 *
 * <p>Typical use cases include:
 *
 * <ul>
 *   <li>Preventing player actions (e.g. movement, chat, interaction)
 *   <li>Blocking access (e.g. login, commands)
 *   <li>Overriding default system behavior
 * </ul>
 *
 * <p>Note: The exact effect of cancellation depends on the event dispatcher and the system
 * triggering the event.
 */
public interface Cancellable {

  /**
   * Returns whether this event has been cancelled.
   *
   * @return {@code true} if the event is cancelled, {@code false} otherwise.
   */
  boolean isCancelled();

  /**
   * Sets whether this event is cancelled.
   *
   * <p>Setting this to {@code true} signals that the event should not be processed further.
   *
   * @param cancelled {@code true} to cancel the event, {@code false} to allow it.
   */
  void setCancelled(boolean cancelled);
}

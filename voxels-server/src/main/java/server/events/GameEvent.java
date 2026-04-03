package server.events;

/**
 * Base class for all events in the system.
 *
 * <p>{@code GameEvent} serves as the root type for the event hierarchy and is used by the event
 * system (e.g. {@link EventBus}) to dispatch and handle events.
 *
 * <p>All events must extend this class in order to be recognized and processed by the event
 * infrastructure.
 *
 * <p>Events represent occurrences within the system and are typically used to:
 *
 * <ul>
 *   <li>Notify listeners about state changes (e.g. player join/quit)
 *   <li>Allow extension points for custom logic
 *   <li>Decouple systems through an event-driven architecture
 * </ul>
 *
 * <p>Events may be either:
 *
 * <ul>
 *   <li><b>Non-cancellable</b> – extending {@code GameEvent} directly
 *   <li><b>Cancellable</b> – extending {@link CancellableEvent}
 * </ul>
 *
 * <p>Note: This class does not define any behavior by itself. It acts purely as a common type and
 * marker for all events.
 */
public abstract class GameEvent {}

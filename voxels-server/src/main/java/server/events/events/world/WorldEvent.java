package server.events.events.world;

import server.events.GameEvent;

/**
 * Base class for all world-related events.
 *
 * <p>{@code WorldEvent} serves as a common superclass for events that are associated with changes
 * or updates in the world state.
 *
 * <p>This class can be used to group and categorize world-specific events, allowing listeners to
 * organize logic around world interactions.
 *
 * <p>Typical examples include:
 *
 * <ul>
 *   <li>Time updates
 *   <li>Weather changes
 *   <li>Block or chunk updates
 * </ul>
 *
 * <p>Note: This class does not contain any world reference yet. If multiple worlds are introduced
 * in the future, this class can be extended to include a world identifier or reference.
 */
public class WorldEvent extends GameEvent {}

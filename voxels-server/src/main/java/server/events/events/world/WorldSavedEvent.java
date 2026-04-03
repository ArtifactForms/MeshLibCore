package server.events.events.world;

/**
 * Event fired after the world has been saved.
 *
 * <p>This event is triggered once the server completes a save operation for the world, including
 * all modified chunks.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Log save operations
 *   <li>Trigger backup systems
 *   <li>Monitor world persistence performance
 * </ul>
 *
 * <p>This event is not cancellable.
 *
 * <p>Note: This event represents a completed save operation. It does not provide access to
 * individual chunk data or allow modification of the save process.
 */
public class WorldSavedEvent extends WorldEvent {

  /** The number of chunks that were saved during this operation. */
  private final int savedChunksCount;

  /**
   * Creates a new {@code WorldSavedEvent}.
   *
   * @param savedChunksCount The number of chunks that were saved.
   */
  public WorldSavedEvent(int savedChunksCount) {
    this.savedChunksCount = savedChunksCount;
  }

  /**
   * Returns the number of chunks that were saved.
   *
   * @return The saved chunk count.
   */
  public int getSavedChunksCount() {
    return savedChunksCount;
  }
}

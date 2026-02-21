package engine.scene.loading;

/**
 * A {@code LoadTask} represents an executable loading operation that can report its progress.
 *
 * <p>Load tasks are typically executed on a background thread and expose their current progress as
 * a value in the range {@code [0, 1]}.
 *
 * <p>The progress value is expected to be:
 *
 * <ul>
 *   <li>{@code 0} at the start of execution
 *   <li>monotonically increasing
 *   <li>{@code 1} when the task has completed
 * </ul>
 *
 * <p>Implementations must ensure that {@link #getProgress()} can be safely called from other
 * threads.
 */
public interface LoadTask extends Runnable {

  /**
   * Returns the current progress of this task.
   *
   * @return the progress value in the range {@code [0, 1]}
   */
  float getProgress(); // 0..1
}

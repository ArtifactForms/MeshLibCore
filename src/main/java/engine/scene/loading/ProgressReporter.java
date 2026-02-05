package engine.scene.loading;

/**
 * Reports progress information for a running {@link LoadTask}.
 *
 * <p>A {@code ProgressReporter} is typically provided to loading code (e.g. a {@link SceneFactory})
 * to allow incremental progress updates during long-running operations.
 *
 * <p>The reported progress value must be in the range {@code [0, 1]} and should be monotonically
 * increasing.
 *
 * <p>Implementations are expected to be lightweight and thread-safe, as progress updates may occur
 * from background threads.
 */
@FunctionalInterface
public interface ProgressReporter {

  /**
   * Reports the current progress of an operation.
   *
   * @param progress the progress value in the range {@code [0, 1]}
   */
  void report(float progress); // 0..1
}

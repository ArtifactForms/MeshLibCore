package engine.scene.loading;

import engine.scene.Scene;

/**
 * A {@code SceneFactory} is responsible for constructing a {@link Scene} during the loading
 * process.
 *
 * <p>The factory is executed by a {@link SceneLoadJob} on a background thread and is expected to
 * report its loading progress via the provided {@link ProgressReporter}.
 *
 * <p>Implementations should:
 *
 * <ul>
 *   <li>build the complete scene graph
 *   <li>initialize all required scene resources
 *   <li>report progress in the range {@code [0, 1]}
 * </ul>
 *
 * <p>The factory must not perform rendering operations and should avoid interacting with systems
 * that require execution on the main thread.
 *
 * <p>Progress reporting is cooperative: the factory decides when and how often progress updates are
 * reported.
 */
@FunctionalInterface
public interface SceneFactory {

  /**
   * Builds and returns a fully initialized {@link Scene}.
   *
   * <p>The provided {@link ProgressReporter} should be used to report loading progress as the scene
   * is being constructed.
   *
   * @param progress a callback used to report loading progress
   * @return the constructed scene
   */
  Scene build(ProgressReporter progress);
}

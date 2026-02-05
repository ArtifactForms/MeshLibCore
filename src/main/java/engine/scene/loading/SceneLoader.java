package engine.scene.loading;

import java.util.function.Consumer;
import engine.scene.Scene;

/**
 * The {@code SceneLoader} is responsible for asynchronously loading scenes on a background thread.
 *
 * <p>It encapsulates the lifecycle of a single {@link SceneLoadJob} and provides a simple API to:
 *
 * <ul>
 *   <li>start loading a scene
 *   <li>query loading progress
 *   <li>receive the finished {@link Scene} on completion
 * </ul>
 *
 * <p>The loader is intentionally minimal and supports only one active load job at a time. Scene
 * construction is delegated to a {@link SceneFactory}, which reports progress back to the loader.
 *
 * <p>Threading model:
 *
 * <ul>
 *   <li>Scene loading runs on a dedicated worker thread
 *   <li>The {@code onFinished} callback is invoked on the worker thread
 * </ul>
 *
 * <p>Higher-level systems are expected to transfer the loaded scene back to the main thread if
 * required.
 */
public class SceneLoader {

  private SceneLoadJob currentJob;
  private Thread worker;

  /**
   * Starts loading a scene asynchronously using the given factory.
   *
   * <p>The provided {@link SceneFactory} is executed on a background thread. Once loading is
   * complete, the {@code onFinished} callback is invoked with the resulting {@link Scene}.
   *
   * @param factory the factory responsible for building the scene
   * @param onFinished callback invoked when scene loading has completed
   * @throws IllegalArgumentException if {@code factory} or {@code onFinished} is null
   */
  public void load(SceneFactory factory, Consumer<Scene> onFinished) {
    currentJob = new SceneLoadJob(factory);

    worker =
        new Thread(
            () -> {
              currentJob.run();
              onFinished.accept(currentJob.getResult());
            });

    worker.start();
  }

  /**
   * Returns whether a scene is currently being loaded.
   *
   * @return {@code true} if a load job is active and not yet finished
   */
  public boolean isLoading() {
    return currentJob != null && currentJob.getProgress() < 1f;
  }

  /**
   * Returns the current loading progress.
   *
   * <p>The returned value is clamped to the range {@code [0, 1]}. If no load job is active, this
   * method returns {@code 1}.
   *
   * @return the current loading progress
   */
  public float getProgress() {
    return currentJob != null ? currentJob.getProgress() : 1f;
  }
}

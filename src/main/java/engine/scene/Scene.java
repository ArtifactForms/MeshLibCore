package engine.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import engine.components.Transform;
import engine.scene.audio.AudioListener;
import engine.scene.audio.AudioSystem;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import math.Color;
import workspace.ui.Graphics;

/**
 * The {@code Scene} class manages a hierarchy of {@code SceneNode}s for rendering and updating. It
 * handles root-level scene nodes, lighting, and a thread pool for parallel updates, offering a
 * high-level interface for managing and rendering complex 3D scenes.
 */
public class Scene {

  /** Default name assigned to a newly created scene if no name is provided. */
  private static final String DEFAULT_NAME = "Untitled-Scene";

  /** List of root-level nodes in the scene hierarchy for rendering and updates. */
  private final ConcurrentLinkedQueue<SceneNode> rootNodes = new ConcurrentLinkedQueue<>();

  /** List of lights in the scene that are used for lighting calculations. */
  private final List<Light> lights = new ArrayList<>();

  /** Name of the scene. Used for identification or debugging purposes. */
  private final String name;

  /** The background color of the scene. Defaults to black if not explicitly set. */
  private Color background;

  /** The currently active camera that determines the scene's view transformation. */
  private Camera activeCamera;

  /** The global transform of the scene for scaling, panning, etc. */
  private Transform transform;

  private AudioSystem audioSystem;

  private SceneNode uiRoot;

  /** Constructs a {@code Scene} with a default name. */
  public Scene() {
    this(DEFAULT_NAME);
  }

  /**
   * Constructs a {@code Scene} with the specified name.
   *
   * @param name The name of the scene.
   * @throws IllegalArgumentException if the name is {@code null}.
   */
  public Scene(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.name = name;
    this.background = new Color(0, 0, 0, 1);
    this.transform = new Transform();
    this.audioSystem = new AudioSystem();
    this.uiRoot = new SceneNode("UI-Root");
    this.uiRoot.setScene(this);
  }

  /**
   * Adds a SceneNode to the root level of the scene graph.
   *
   * @param node The node to add to the root level.
   */
  public void addNode(SceneNode node) {
    if (node == null) throw new IllegalArgumentException("Node cannot be null.");
    node.setScene(this);
    synchronized (rootNodes) {
      rootNodes.add(node);
    }
  }

  /**
   * Adds a light to the scene's list of lights for rendering and lighting calculations. Ensures
   * thread-safe addition by synchronizing on the `lights` list.
   *
   * @param light The Light instance to be added to the scene.
   * @throws IllegalArgumentException if the provided light is null.
   */
  public void addLight(Light light) {
    if (light == null) {
      throw new IllegalArgumentException("Light cannot be null.");
    }
    synchronized (lights) {
      lights.add(light);
    }
  }

  /**
   * Removes a SceneNode from the root level.
   *
   * @param node The node to remove from the root level.
   */
  public void removeNode(SceneNode node) {
    if (node == null) return;
    synchronized (rootNodes) {
      rootNodes.remove(node);
    }
  }

  /**
   * Updates all nodes in the scene graph.
   *
   * @param deltaTime The time step for simulation logic updates.
   */
  public void update(float deltaTime) {
    for (SceneNode node : rootNodes) {
      node.update(deltaTime);
    }
    updateAudio();
    updateUI(deltaTime);
  }

  private void updateUI(float deltaTime) {
    uiRoot.update(deltaTime);
  }

  /**
   * Updates the audio system for the current frame.
   *
   * <p>This method synchronizes the {@link AudioListener} with the active camera and propagates
   * audio updates to all root-level {@link SceneNode}s.
   *
   * <p>If no active camera is set, audio updates are skipped.
   */
  private void updateAudio() {
    if (activeCamera == null) return;

    AudioListener listener = new AudioListener();
    listener.setPosition(activeCamera.getTransform().getPosition());
    listener.setForward(activeCamera.getTransform().getForward());

    audioSystem.setListener(listener);
    for (SceneNode node : rootNodes) {
      node.updateAudio(audioSystem);
    }
  }

  /**
   * Renders the scene using the provided graphics context.
   *
   * <p>This includes applying the active camera, scene transform, clearing the background,
   * rendering lights, and finally rendering all root-level scene nodes.
   *
   * <p>Rendering is expected to run on the main thread due to graphics API constraints.
   *
   * @param g The graphics context used for rendering.
   */
  public void render(Graphics g) {

    g.clear(background);

    if (activeCamera != null) {
      g.applyCamera(activeCamera);
    }
    renderLights(g);

    if (transform != null) {
      transform.apply(g);
    }

    synchronized (rootNodes) {
      for (SceneNode node : rootNodes) {
        node.render(g);
      }
    }
  }

  /**
   * Called when this scene becomes the active scene.
   *
   * <p>This method is invoked automatically by the application when the scene is set as the current
   * active scene via {@code setActiveScene(Scene)}.
   *
   * <p>Typical use cases include:
   *
   * <ul>
   *   <li>Initializing runtime-only resources
   *   <li>Starting background music or ambient sounds
   *   <li>Resetting timers or stateful components
   *   <li>Enabling input handling specific to this scene
   * </ul>
   *
   * <p>This method is guaranteed to be called exactly once per activation and always before the
   * first {@link #update(float)} call.
   *
   * <p>Subclasses may override {@link #onEnter()} to implement custom behavior.
   */
  public final void enter() {
    onEnter();
  }

  /**
   * Scene-specific enter hook.
   *
   * <p>This method is intended to be overridden by subclasses to implement scene-specific
   * activation logic.
   *
   * <p>The default implementation performs no action.
   */
  public void onEnter() {
    // Do nothing
  }

  /**
   * Hook method called when this scene is no longer the active scene.
   *
   * <p>This method is invoked automatically before another scene becomes active or when the
   * application is shutting down.
   *
   * <p>Typical use cases include:
   *
   * <ul>
   *   <li>Stopping audio playback
   *   <li>Releasing or pausing transient resources
   *   <li>Disabling input listeners
   *   <li>Saving scene-specific state
   * </ul>
   *
   * <p>This method is guaranteed to be called exactly once per deactivation and always after the
   * final {@link #update(float)} call.
   *
   * <p>Subclasses may override {@link #onExit()} to implement custom behavior.
   */
  public final void exit() {
    onExit();
  }

  /**
   * Scene-specific exit hook.
   *
   * <p>This method is intended to be overridden by subclasses to implement scene-specific
   * deactivation logic.
   *
   * <p>The default implementation performs no action.
   */
  public void onExit() {
    // Do nothing
  }

  /**
   * Renders all lights in the scene safely by synchronizing access to the lights list. This ensures
   * thread-safe iteration and rendering, especially when lights are added or removed concurrently.
   *
   * @param g The graphics context used for rendering the lights.
   */
  private void renderLights(Graphics g) {
    synchronized (lights) {
      for (Light light : lights) {
        g.render(light);
      }
    }
  }

  /** Cleans up resources and shuts down the executor safely. */
  public void cleanup() {
    synchronized (rootNodes) {
      for (SceneNode node : rootNodes) {
        node.cleanup();
      }
      rootNodes.clear();
    }
    uiRoot.cleanup();
  }

  /**
   * Retrieves the number of root nodes in the scene.
   *
   * @return The count of root nodes currently present in the scene graph.
   */
  public int getRootCount() {
    synchronized (rootNodes) {
      return rootNodes.size();
    }
  }

  /**
   * Performs a complete cleanup of all lights and nodes in the scene, in addition to shutting down
   * worker threads. This ensures no memory leaks or dangling references persist after the scene is
   * no longer needed.
   */
  public void cleanupAllResources() {
    synchronized (lights) {
      lights.clear();
    }
    synchronized (rootNodes) {
      for (SceneNode node : rootNodes) {
        node.cleanup();
      }
      rootNodes.clear();
    }
  }

  /**
   * Retrieves all lights that are currently active in the scene. This allows querying of lights for
   * dynamic light management features.
   *
   * @return A thread-safe copy of the current lights list.
   */
  public List<Light> getAllLights() {
    synchronized (lights) {
      return new ArrayList<>(lights);
    }
  }

  /**
   * Fetches all the nodes at the root level in a thread-safe way. Useful for debugging,
   * visualization, or debugging purposes to monitor scene nodes in real-time.
   *
   * @return A list of root SceneNodes currently in the scene graph.
   */
  public List<SceneNode> getRootNodes() {
    synchronized (rootNodes) {
      return new ArrayList<>(rootNodes);
    }
  }

  /**
   * Applies the given {@link SceneNodeVisitor} to all root nodes of the scene.
   *
   * <p>This method serves as the entry point for traversing the entire scene graph. Each root node
   * accepts the visitor and recursively propagates it to its descendants.
   *
   * @param visitor the visitor to apply to the scene graph
   * @throws IllegalArgumentException if {@code visitor} is {@code null}
   */
  public void visitRootNodes(SceneNodeVisitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor cannot be null.");
    }

    synchronized (rootNodes) {
      for (SceneNode node : rootNodes) {
        node.accept(visitor);
      }
    }
  }

  /**
   * Finds and removes all SceneNodes matching a particular condition. For example, it can remove
   * nodes based on type or other predicates.
   *
   * @param predicate The condition to test nodes against.
   * @return The number of nodes removed.
   */
  public int removeNodesIf(java.util.function.Predicate<SceneNode> predicate) {
    int count = 0;
    synchronized (rootNodes) {
      for (SceneNode node : new ArrayList<>(rootNodes)) {
        if (predicate.test(node)) {
          node.cleanup();
          rootNodes.remove(node);
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Sets the currently active camera for the scene. The active camera determines the view and
   * projection matrices used during rendering. If no active camera is set, rendering will proceed
   * without camera transformations.
   *
   * @param camera The camera to set as the active camera. May be null to disable camera-based
   *     rendering logic.
   */
  public void setActiveCamera(Camera camera) {
    this.activeCamera = camera;
  }

  /**
   * Retrieves the currently active camera used for rendering the scene. The active camera's view
   * and projection matrices define the perspective and viewport used during rendering.
   *
   * @return The currently active camera, or {@code null} if no active camera has been set.
   */
  public Camera getActiveCamera() {
    return this.activeCamera;
  }

  /**
   * Retrieves the number of lights currently managed by the scene.
   *
   * <p>This method provides the total count of lights that are part of the scene's lighting system.
   * It operates in a thread-safe manner by synchronizing access to the shared lights list to ensure
   * accurate and safe read operations, even when lights are being added or removed concurrently.
   *
   * @return The total number of lights currently in the scene.
   */
  public int getLightCount() {
    synchronized (lights) {
      return lights.size();
    }
  }

  /**
   * Retrieves the name of the scene.
   *
   * @return The name of the scene.
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the background color of the scene.
   *
   * <p>The background color is used to clear the rendering surface before drawing the scene.
   *
   * @return The current background color of the scene.
   */
  public Color getBackground() {
    return background;
  }

  /**
   * Sets the background color of the scene.
   *
   * <p>The background color is used to clear the rendering surface before drawing the scene.
   *
   * @param background The new background color for the scene. Must not be {@code null}.
   * @throws IllegalArgumentException if the provided background color is {@code null}.
   */
  public void setBackground(Color background) {
    if (background == null) {
      throw new IllegalArgumentException("Background color cannot be null.");
    }
    this.background = background;
  }

  /**
   * Retrieves the transform of the scene.
   *
   * @return The current global transform of the scene.
   */
  public Transform getTransform() {
    return transform;
  }

  /**
   * Sets the transform of the scene.
   *
   * @param transform The global transform to apply to the scene.
   */
  public void setTransform(Transform transform) {
    if (transform == null) {
      throw new IllegalArgumentException("Transform cannot be null.");
    }
    this.transform = transform;
  }

  /**
   * Renders the UI layer of the scene.
   *
   * <p>This method renders all UI-related {@link SceneNode}s that are attached to the scene's
   * dedicated UI root. UI rendering is performed separately from the main 3D scene graph and is
   * typically executed with depth testing disabled.
   *
   * <p>The UI root is rendered after the main scene and is not affected by the scene's active
   * camera or lighting setup.
   *
   * @param g the graphics context used for rendering UI elements
   */
  public void renderUI(Graphics g) {
    uiRoot.render(g);
  }

  /**
   * Returns the root {@link SceneNode} for all UI elements in this scene.
   *
   * <p>The UI root acts as a separate scene graph for user interface components such as HUDs,
   * menus, cursors, and overlays. Nodes attached to this root are updated and rendered
   * independently from the main 3D scene graph.
   *
   * <p>This node is always present and is automatically managed by the scene.
   *
   * @return the root node of the UI scene graph
   */
  public SceneNode getUIRoot() {
    return uiRoot;
  }
}

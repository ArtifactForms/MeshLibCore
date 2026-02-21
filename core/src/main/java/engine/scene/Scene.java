package engine.scene;

import java.util.ArrayList;
import java.util.List;

import engine.components.Transform;
import engine.rendering.Graphics;
import engine.scene.audio.AudioListener;
import engine.scene.audio.AudioSystem;
import engine.scene.camera.Camera;
import engine.scene.light.Light;
import engine.system.SceneSystem;
import engine.system.SceneSystemManager;
import engine.system.UpdatePhase;
import math.Color;

/**
 * The {@code Scene} class manages a hierarchy of {@code SceneNode}s for rendering and updating. It
 * handles root-level scene nodes, lighting, and a thread pool for parallel updates, offering a
 * high-level interface for managing and rendering complex 3D scenes.
 */
public class Scene {

  /** Default name assigned to a newly created scene if no name is provided. */
  private static final String DEFAULT_NAME = "Untitled-Scene";

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

  private SceneNode worldRoot;

  private SceneSystemManager systemManager;

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
    this.worldRoot = new SceneNode();
    this.worldRoot.setScene(this);
    this.systemManager = new SceneSystemManager();
  }

  /**
   * Adds a SceneNode to the root level of the scene graph.
   *
   * @param node The node to add to the root level.
   */
  public void addNode(SceneNode node) {
    if (node == null) throw new IllegalArgumentException("Node cannot be null.");
    node.setScene(this);
    worldRoot.addChild(node);
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
    worldRoot.removeChild(node);
  }

  /**
   * Executes a full frame update using the phased engine pipeline.
   *
   * <p>The update is performed in deterministic phases defined by {@link UpdatePhase}. For each
   * phase: 1. All registered systems for that phase are updated. 2. Engine-level operations (world
   * update, audio, UI) are executed in their designated phases.
   *
   * <p>This ensures a clear separation between simulation, world logic, and presentation updates.
   *
   * @param deltaTime The frame time in seconds.
   */
  public void update(float deltaTime) {

    for (UpdatePhase phase : UpdatePhase.values()) {

      // Update all systems registered for the current phase
      systemManager.updatePhase(phase, deltaTime);

      // Update the scene graph during the WORLD_UPDATE phase
      if (phase == UpdatePhase.WORLD_UPDATE) {
        worldRoot.update(deltaTime);
        worldRoot.pruneDestroyedChildren();

        // UI is updated after world logic to reflect final transforms/state
        updateUI(deltaTime);
      }

      // Synchronize audio after world updates are finalized
      if (phase == UpdatePhase.POST_WORLD) {
        updateAudio();
      }
    }
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
    worldRoot.updateAudio(audioSystem);
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

    worldRoot.render(g);
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
    worldRoot.cleanup();
    uiRoot.cleanup();
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
    worldRoot.cleanup();
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

    worldRoot.accept(visitor);
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

  /**
   * Registers a {@link SceneSystem} with this scene.
   *
   * <p>The system will be attached to this scene via {@link SceneSystem#onAttach(Scene)} and
   * becomes part of the scene's update lifecycle.
   *
   * <p>After registration, the system will automatically receive update calls when {@link
   * #update(float)} is invoked on this scene.
   *
   * <p>Systems are scene-scoped. A system instance should not be added to multiple scenes
   * simultaneously.
   *
   * @param system the system to register; must not be null
   * @throws IllegalArgumentException if the system is null
   */
  public void addSystem(SceneSystem system) {
    if (system == null) throw new IllegalArgumentException("System cannot be null.");
    systemManager.addSystem(system, this);
  }

  /**
   * Returns the system instance of the specified type that is registered with this scene.
   *
   * <p>Systems are scene-scoped and must have been previously registered via {@link
   * #addSystem(SceneSystem)}. If no system of the given type is registered, this method returns
   * {@code null}.
   *
   * <p>Typical usage:
   *
   * <pre>
   * PhysicsQuerySystem physics =
   *     scene.getSystem(PhysicsQuerySystem.class);
   * </pre>
   *
   * @param <T> the concrete system type
   * @param type the class object representing the requested system type; must not be {@code null}
   * @return the registered system instance of the given type, or {@code null} if none is found
   * @throws IllegalArgumentException if {@code type} is {@code null}
   */
  public <T extends SceneSystem> T getSystem(Class<T> type) {
    if (type == null) {
      throw new IllegalArgumentException("System type must not be null");
    }
    return systemManager.getSystem(type);
  }
}

package engine.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import engine.scene.camera.Camera;
import engine.scene.light.Light;
import workspace.ui.Graphics;

/**
 * The {@code Scene} class manages a hierarchy of {@code SceneNode}s for
 * rendering and updating. It handles root-level scene nodes, lighting, and a
 * thread pool for parallel updates, offering a high-level interface for
 * managing and rendering complex 3D scenes.
 */
public class Scene {

	/** Default name assigned to a newly created scene if no name is provided. */
	private static final String DEFAULT_NAME = "Untitled-Scene";

	/**
	 * List of root-level nodes in the scene hierarchy for rendering and updates.
	 */
	private final List<SceneNode> rootNodes = new ArrayList<>();

	/** List of lights in the scene that are used for lighting calculations. */
	private final List<Light> lights = new ArrayList<>();

	/** Thread pool used to parallelize updates for performance optimization. */
	private final ExecutorService updateExecutor = Executors
	    .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/** Flag indicating whether the scene is rendered in wireframe mode. */
	private boolean wireframeMode;

	/** Name of the scene. Used for identification or debugging purposes. */
	private final String name;

	/**
	 * The currently active camera that determines the scene's view
	 * transformation.
	 */
	private Camera activeCamera;

	/**
	 * Constructs a {@code Scene} with a default name.
	 */
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
	}

	/**
	 * Adds a SceneNode to the root level of the scene graph.
	 * 
	 * @param node The node to add to the root level.
	 */
	public void addNode(SceneNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Node cannot be null.");
		}
		synchronized (rootNodes) {
			rootNodes.add(node);
		}
	}

	/**
	 * Adds a light to the scene's list of lights for rendering and lighting
	 * calculations. Ensures thread-safe addition by synchronizing on the `lights`
	 * list.
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
		if (node == null)
			return;
		synchronized (rootNodes) {
			rootNodes.remove(node);
		}
	}

	/**
	 * Perform parallel updates on all nodes in the scene graph.
	 * 
	 * @param deltaTime The time step for simulation logic updates.
	 */
	public void update(float deltaTime) {
		List<SceneNode> nodesCopy;
		synchronized (rootNodes) {
			nodesCopy = new ArrayList<>(rootNodes);
		}

		// Submit updates to worker threads
		for (SceneNode node : nodesCopy) {
			updateExecutor.submit(() -> node.update(deltaTime));
		}
	}

	/**
	 * Render lights and nodes concurrently. However, rendering must still run on
	 * the main thread for compatibility with most rendering APIs.
	 */
	public void render(Graphics g) {

		if (activeCamera != null) {
//			Vector3f position = activeCamera.getTransform().getPosition();
//			g.applyMatrix(activeCamera.getProjectionMatrix());
//			g.applyMatrix(activeCamera.getViewMatrix());
			g.applyCamera(activeCamera);
//			if (position != null)
//			g.translate(position.x, position.y, position.z);
//			System.out.println(position);
		}

		g.setWireframeMode(wireframeMode);
		renderLights(g);

		synchronized (rootNodes) {

			for (SceneNode node : rootNodes) {
				node.render(g);
			}
		}

	}

	/**
	 * Renders all lights in the scene safely by synchronizing access to the
	 * lights list. This ensures thread-safe iteration and rendering, especially
	 * when lights are added or removed concurrently.
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

	/**
	 * Cleans up resources and shuts down the executor safely.
	 */
	public void cleanup() {
		// Shutdown thread pool properly
		updateExecutor.shutdown();
		synchronized (rootNodes) {
			for (SceneNode node : rootNodes) {
				node.cleanup();
			}
			rootNodes.clear();
		}
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
	 * Performs a complete cleanup of all lights and nodes in the scene, in
	 * addition to shutting down worker threads. This ensures no memory leaks or
	 * dangling references persist after the scene is no longer needed.
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
		updateExecutor.shutdown();
	}

	/**
	 * Retrieves all lights that are currently active in the scene. This allows
	 * querying of lights for dynamic light management features.
	 *
	 * @return A thread-safe copy of the current lights list.
	 */
	public List<Light> getAllLights() {
		synchronized (lights) {
			return new ArrayList<>(lights);
		}
	}

	/**
	 * Fetches all the nodes at the root level in a thread-safe way. Useful for
	 * debugging, visualization, or debugging purposes to monitor scene nodes in
	 * real-time.
	 * 
	 * @return A list of root SceneNodes currently in the scene graph.
	 */
	public List<SceneNode> getRootNodes() {
		synchronized (rootNodes) {
			return new ArrayList<>(rootNodes);
		}
	}

	/**
	 * Finds and removes all SceneNodes matching a particular condition. For
	 * example, it can remove nodes based on type or other predicates.
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
	 * Sets the currently active camera for the scene. The active camera
	 * determines the view and projection matrices used during rendering. If no
	 * active camera is set, rendering will proceed without camera
	 * transformations.
	 *
	 * @param camera The camera to set as the active camera. May be null to
	 *               disable camera-based rendering logic.
	 */
	public void setActiveCamera(Camera camera) {
		this.activeCamera = camera;
	}

	/**
	 * Retrieves the currently active camera used for rendering the scene. The
	 * active camera's view and projection matrices define the perspective and
	 * viewport used during rendering.
	 *
	 * @return The currently active camera, or {@code null} if no active camera
	 *         has been set.
	 */
	public Camera getActiveCamera() {
		return this.activeCamera;
	}

	/**
	 * Retrieves the number of lights currently managed by the scene.
	 * <p>
	 * This method provides the total count of lights that are part of the scene's
	 * lighting system. It operates in a thread-safe manner by synchronizing
	 * access to the shared lights list to ensure accurate and safe read
	 * operations, even when lights are being added or removed concurrently.
	 * </p>
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
	 * Checks if the scene is in wireframe rendering mode.
	 *
	 * @return {@code true} if wireframe mode is enabled, {@code false} otherwise.
	 */
	public boolean isWireframeMode() {
		return wireframeMode;
	}

	/**
	 * Enables or disables wireframe rendering mode for the scene.
	 *
	 * @param wireframeMode {@code true} to enable wireframe mode, {@code false}
	 *                      to disable it.
	 */
	public void setWireframeMode(boolean wireframeMode) {
		this.wireframeMode = wireframeMode;
	}

}
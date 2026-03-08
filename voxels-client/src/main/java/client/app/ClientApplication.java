package client.app;

import client.network.ClientNetwork;
import client.world.ChunkManager;
import client.world.ClientWorld;
import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.rendering.Graphics;
import engine.scene.Scene;
import engine.scene.audio.SoundManager;

/**
 * The main entry point for the Voxel Client. Manages the high-level lifecycle of the application,
 * including network synchronization, scene management, and resource preloading.
 */
public class ClientApplication extends BasicApplication {

  /** Network client instance handling communication with the game server */
  private ClientNetwork network;

  public static void main(String[] args) {
    ClientApplication application = new ClientApplication();
    // Launching with default settings; fullscreen enabled by default
    application.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  @Override
  public void onInitialize() {
    // Pre-allocate audio buffers
    preloadResources();

    // Initialize global application context for easy cross-referencing
    ApplicationContext.application = this;
    ApplicationContext.chunkManager = new ChunkManager();
    ApplicationContext.clientWorld = new ClientWorld(ApplicationContext.chunkManager);

    // Initialize Network connection
    try {
      network = new ClientNetwork();
      ApplicationContext.network = network;
    } catch (Exception e) {
      System.err.println("[Client] Failed to initialize network: " + e.getMessage());
      e.printStackTrace();
    }

    // Set the initial scene
    Scene startScene = new StartScene(input);
    setActiveScene(startScene);
  }

  /** Loads essential audio resources into memory to prevent lag during gameplay. */
  public void preloadResources() {
    SoundManager.addSound(Resources.BACKGROUND_MUSIC_KEY, Resources.BACKGROUND_MUSIC);
    SoundManager.addEffect(Resources.BLOCK_BREAK_FX_KEY, Resources.BLOCK_BREAK_FX_PATH, 5);
    SoundManager.addEffect(Resources.BLOCK_PLACE_FX_KEY, Resources.BLOCK_PLACE_FX_PATH, 5);
  }

  @Override
  public void onUpdate(float tpf) {
    // --- NETWORK UPDATE ---
    // Crucial: Process incoming packets on the main thread before updating scene logic.
    // This ensures the scene is working with the most recent server data.
    if (network != null && network.isRunning()) {
      network.update();
    }
  }

  @Override
  public void onRender(Graphics g) {}

  @Override
  public void onCleanup() {
    // Gracefully disconnect from the server and release network resources
    if (network != null) {
      System.out.println("[Client] Closing network connection...");
      network.close();
    }
  }
}

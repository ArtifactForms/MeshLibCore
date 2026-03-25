package client.app;

import client.network.ClientNetwork;
import client.player.PlayerNetworkSync;
import client.scene.StartScene;
import client.usecases.loadresources.LoadResourcesUseCase;
import client.world.ChunkStreamingSystem;
import common.game.ItemRegistry;
import common.game.block.BlockLoader;
import common.game.block.BlockRegistry;
import common.game.block.Blocks;
import common.game.crafting.GameRecipes;
import engine.application.BasicApplication;
import engine.rendering.Graphics;

/**
 * The main entry point for the Voxel Client. Manages the high-level lifecycle of the application,
 * including network synchronization, scene management, and resource preloading.
 */
public class ClientApplication extends BasicApplication {

  /** Network client instance handling communication with the game server */
  private ClientNetwork network;

  private PlayerNetworkSync playerNetworkSync;
  private ChunkStreamingSystem chunkStreamingSystem;
  private GameClient client;

  @Override
  public void onInitialize() {
    setDisplayInfo(true);


    Blocks.initialize();
    BlockLoader.load();
    BlockRegistry.freeze();
    ItemRegistry.init();
    GameRecipes.init();

    client = new GameClient(this);
    network = client.getNetwork();
    playerNetworkSync = new PlayerNetworkSync(client.getPlayer(), client.getNetwork());
    chunkStreamingSystem = new ChunkStreamingSystem(client.getPlayer(), client.getChunkManager());

    preloadResources();
    setupStartScene(client);
  }

  private void setupStartScene(GameClient client) {
    setActiveScene(new StartScene(input, client));
  }

  /** Loads essential audio resources into memory to prevent lag during gameplay. */
  public void preloadResources() {
    new LoadResourcesUseCase().execute();
  }

  @Override
  public void onUpdate(float tpf) {
    playerNetworkSync.update();
    chunkStreamingSystem.update();
    // --- NETWORK UPDATE ---
    // Crucial: Process incoming packets on the main thread before updating scene logic.
    // This ensures the scene is working with the most recent server data.
    if (network != null && network.isRunning()) {
      network.update();
    }
    client.getWorld().update(tpf);
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

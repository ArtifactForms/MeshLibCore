package client.app;

import java.util.UUID;

import client.entity.ClientEntityManager;
import client.network.ClientNetwork;
import client.network.PingTracker;
import client.player.ClientPlayer;
import client.ray.RaycastMode;
import client.scene.SceneManager;
import client.ui.ClientView;
import client.ui.View;
import client.world.ChunkManager;
import client.world.ClientWorld;
import common.network.NetworkPackets;
import engine.application.BasicApplication;
import engine.scene.camera.Camera;
import engine.scene.camera.PerspectiveCamera;

public class GameClient {

  private final PingTracker pingTracker = new PingTracker();

  private ClientNetwork network;
  private ClientView view;
  private ClientWorld world;

  private ClientPlayer player;
  private Camera playerCamera;
  private RaycastMode raycastMode = RaycastMode.CROSS_HAIR;

  private ChunkManager chunkManager;
  private SceneManager sceneManager;
  private ClientEntityManager entityManager;

  public GameClient(BasicApplication application) {
    NetworkPackets.register();
    setupCamera();
    initNetwork();

    this.view = new View();
    this.player = new ClientPlayer(UUID.randomUUID(), "Test-Player");
    this.sceneManager = new SceneManager(application);

    this.world = new ClientWorld(); // Important init before manager
    this.chunkManager = new ChunkManager(this);
    world.setChunkManager(chunkManager);
    this.entityManager = new ClientEntityManager(world);
  }

  private void setupCamera() {
    playerCamera = new PerspectiveCamera();
    playerCamera.setNearPlane(0.1f);
    playerCamera.setFarPlane(600);
  }

  private void initNetwork() {
    // Initialize Network connection
    try {
      network = new ClientNetwork(this);
    } catch (Exception e) {
      System.err.println("[Client] Failed to initialize network: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void onConnectionClosed() {
//    sceneManager.setActiveScene(new ConnectionLostScene(this));
  }

  public ClientNetwork getNetwork() {
    return network;
  }

  public ClientEntityManager getEntityManager() {
    return entityManager;
  }

  public ChunkManager getChunkManager() {
    return chunkManager;
  }

  public ClientWorld getWorld() {
    return world;
  }

  public ClientPlayer getPlayer() {
    return player;
  }

  public ClientView getView() {
    return view;
  }

  public Camera getPlayerCamera() {
    return playerCamera;
  }

  public RaycastMode getRaycastMode() {
    return raycastMode;
  }

  public void setRaycastMode(RaycastMode raycastMode) {
    this.raycastMode = raycastMode;
  }

  public SceneManager getSceneManager() {
    return sceneManager;
  }

  public PingTracker getPingTracker() {
    return pingTracker;
  }
}

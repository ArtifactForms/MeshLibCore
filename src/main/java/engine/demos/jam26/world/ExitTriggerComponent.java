package engine.demos.jam26.world;

import engine.components.AbstractComponent;
import engine.demos.jam26.assets.AssetRefs;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import engine.scene.camera.Camera;
import math.Vector3f;

public class ExitTriggerComponent extends AbstractComponent {

  private float triggerRadius;
  private boolean triggered;
  private SceneNode title;
  private Vector3f lobbySpawn;

  public ExitTriggerComponent(SceneNode title, Vector3f lobbySpawn) {
    this.triggerRadius = 64;
    this.triggered = false;
    this.title = title;
    this.lobbySpawn = lobbySpawn;
  }

  @Override
  public void onUpdate(float tpf) {
    if (triggered) return;

    Camera cam = getOwner().getScene().getActiveCamera();
    if (cam == null) return;

    Vector3f exitPos = getOwner().getTransform().getPosition();
    Vector3f playerPos = cam.getTransform().getPosition();

    float distSq = exitPos.distanceSquared(playerPos);
    if (distSq <= triggerRadius * triggerRadius) {
      triggered = true;
      onExit();
    }
  }

  private void onExit() {
    SoundManager.stopSound(AssetRefs.SOUND_BACKGROUND_KEY);
    SoundManager.playSound(AssetRefs.SOUND_EXIT_KEY);
    displayLevelCompleteTitle();
    sendPlayerToLobby();
  }

  private void displayLevelCompleteTitle() {
    title.setActive(true);
  }

  private void sendPlayerToLobby() {
    getOwner().getScene().getActiveCamera().getTransform().setPosition(lobbySpawn);
  }
}

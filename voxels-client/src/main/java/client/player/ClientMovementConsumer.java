package client.player;

import client.app.ApplicationContext;
import common.network.packets.PlayerMovePacket;
import engine.components.MovementInputConsumer;
import engine.scene.SceneNode;
import math.Mathf;
import math.Vector3f;

/**
 * Handles player movement input on the client side. Implements Client-Side Prediction for terrain
 * following and Linear Interpolation (Lerp) for smooth visual movement.
 */
public class ClientMovementConsumer implements MovementInputConsumer {

  /** The "logical" target position in the game world. */
  private Vector3f position = new Vector3f();

  private float speed = 0.2f;

  /**
   * * Determines how quickly the camera glides to the target position. Values between 0.1 and 0.3
   * provide a smooth experience.
   */
  private float lerpFactor = 0.25f;

  private SceneNode player;

  public ClientMovementConsumer(SceneNode player) {
    this.player = player;
  }

  @Override
  public void addMovementInput(Vector3f direction) {
    // 1. Calculate horizontal movement (X/Z)
    position.x += direction.x * speed;
    position.z += direction.z * speed;

    // 2. CLIENT-SIDE PREDICTION: Snap-to-Ground
    // We query the local ChunkManager for the terrain height to prevent "jittering"
    // while waiting for a server response.
    if (ApplicationContext.chunkManager != null) {
      float groundHeight = ApplicationContext.chunkManager.getTerrainHeight(position.x, position.z);
      // Convert to internal rendering coordinate system (-Y logic)
      position.y = -groundHeight;
    }

    // 3. NETWORK: Send intended position to the Server
    if (ApplicationContext.network != null) {
      float yaw = 0, pitch = 0;
      if (ApplicationContext.activeScene != null
          && ApplicationContext.activeScene.getActiveCamera() != null) {
        Vector3f rot =
            ApplicationContext.activeScene.getActiveCamera().getTransform().getRotation();
        pitch = rot.x;
        yaw = rot.y;
      }

      // IMPORTANT: We send positive Y coordinates to the server,
      // as the server logic is agnostic of the client's -Y rendering.
      ApplicationContext.network.send(
          new PlayerMovePacket(position.x, -position.y, position.z, yaw, pitch));
    }

    // 4. VISUAL SMOOTHING (Interpolation)
    // Instead of snapping to the position immediately, we interpolate (Lerp)
    // to smooth out server corrections or uneven terrain transitions.
    Vector3f currentVisualPos = player.getTransform().getPosition();

    currentVisualPos.x = Mathf.lerp(currentVisualPos.x, position.x, lerpFactor);
    currentVisualPos.y = Mathf.lerp(currentVisualPos.y, position.y, lerpFactor);
    currentVisualPos.z = Mathf.lerp(currentVisualPos.z, position.z, lerpFactor);

    // Apply the smoothed transformation
    player.getTransform().setPosition(currentVisualPos);
  }

  /**
   * * Called by the ClientPacketHandler when the server issues a mandatory position correction.
   * Updates the logical position while letting the Lerp handle the visual transition.
   */
  public void applyServerCorrection(float x, float y, float z) {
    this.position.x = x;
    this.position.y = -y; // Convert back to -Y rendering
    this.position.z = z;
    // Note: We do NOT snap the visual position here; Lerp will pull the camera to the new spot.
  }

  public Vector3f getPosition() {
    return position;
  }

  @Override
  public void jump() {
    // Future implementation: Add upward velocity/force
  }
}

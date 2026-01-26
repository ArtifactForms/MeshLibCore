package engine.components;

import engine.scene.Scene;
import engine.scene.camera.Camera;
import math.Vector3f;

/**
 * BillboardComponent makes a SceneNode always face the specified camera. Rotation is constrained to
 * the Y axis (upright billboard).
 */
public class BillboardComponent extends AbstractComponent {

  @Override
  public void onUpdate(float tpf) {
    Scene scene = getOwner().getScene();
    Camera camera = scene.getActiveCamera();

    Vector3f pos = getOwner().getTransform().getPosition();
    Vector3f camPos = camera.getTransform().getPosition();

    // Direction from billboard to camera
    Vector3f dir = camPos.subtract(pos, new Vector3f());

    // Constrain to Y-axis rotation only
    dir.y = 0;

    if (dir.lengthSquared() < 0.0001f) return;

    dir.normalizeLocal();

    // FIX: flip X to correct mirrored yaw
    dir.x = -dir.x;

    getOwner().getTransform().setForward(dir);
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}

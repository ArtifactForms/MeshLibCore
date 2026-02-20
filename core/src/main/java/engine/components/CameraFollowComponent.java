package engine.components;

import engine.scene.SceneNode;
import engine.scene.camera.Camera;
import math.Mathf;
import math.Vector3f;

/**
 * CameraFollowComponent makes a camera follow a target SceneNode using a fixed offset. Optionally
 * supports smooth interpolation for softer camera movement.
 *
 * <p>Coordinate system assumption: Y axis points down (-Y is up).
 */
public class CameraFollowComponent extends AbstractComponent {

  private final Camera camera;
  private final SceneNode target;

  private Vector3f offset;
  private float smoothness = 0f; // 0 = no smoothing (instant)

  /**
   * Creates a camera follow component.
   *
   * @param camera The camera to move
   * @param target The scene node to follow
   * @param offset The positional offset relative to the target
   */
  public CameraFollowComponent(Camera camera, SceneNode target, Vector3f offset) {
    if (camera == null || target == null || offset == null) {
      throw new IllegalArgumentException("Camera, target and offset must not be null");
    }
    this.camera = camera;
    this.target = target;
    this.offset = new Vector3f(offset);
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f targetPos = target.getTransform().getPosition();
    Vector3f desiredPos = new Vector3f(targetPos).addLocal(offset);

    Vector3f currentPos = camera.getTransform().getPosition();

    camera.setTarget(targetPos);

    if (smoothness > 0f) {
      float alpha = Mathf.clamp(tpf * smoothness, 0f, 1f);
      currentPos.lerpLocal(desiredPos, alpha);
      camera.getTransform().setPosition(currentPos);
    } else {
      camera.getTransform().setPosition(desiredPos);
    }
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}

  /** Sets the follow offset. */
  public void setOffset(Vector3f offset) {
    this.offset.set(offset);
  }

  /**
   * Sets camera smoothness.
   *
   * @param smoothness Higher values = faster interpolation. 0 disables smoothing.
   */
  public void setSmoothness(float smoothness) {
    this.smoothness = Math.max(0f, smoothness);
  }
}

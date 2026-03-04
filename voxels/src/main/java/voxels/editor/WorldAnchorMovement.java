package voxels.editor;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.scene.camera.Camera;
import math.Color;
import math.Vector3f;

public class WorldAnchorMovement extends AbstractComponent implements RenderableComponent {

  private Key leftKey = Key.ARROW_LEFT;
  private Key rightKey = Key.ARROW_RIGHT;
  private Key forwardKey = Key.ARROW_UP;
  private Key backwardKey = Key.ARROW_DOWN;

  private float speed = 64;
  private Input input;
  private WorldAnchor anchor;

  public WorldAnchorMovement(Input input, WorldAnchor anchor) {
    this.input = input;
    this.anchor = anchor;
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f direction = new Vector3f();

    if (input.isKeyPressed(forwardKey)) direction.addLocal(0, 0, -1);
    if (input.isKeyPressed(leftKey)) direction.addLocal(-1, 0, 0);
    if (input.isKeyPressed(backwardKey)) direction.addLocal(0, 0, 1);
    if (input.isKeyPressed(rightKey)) direction.addLocal(1, 0, 0);

    if (direction.lengthSquared() > 0f) {
      direction.normalizeLocal();
    }

    anchor.move(direction.x * speed * tpf, direction.z * speed * tpf);

    if (direction.length() > 0) {
      Camera camera = getOwner().getScene().getActiveCamera();
      camera.setTarget(new Vector3f(anchor.getX(), 0, anchor.getZ()));
    }
  }

  @Override
  public void render(Graphics g) {
    float x = anchor.getX();
    float z = anchor.getZ();
    g.setColor(Color.YELLOW);
    g.drawLine(x, 100, z, x, -200, z);
  }
}
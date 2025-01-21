package engine.demos.voxels;

import math.Vector3f;

public class Player {

  private Vector3f position;

  public Player() {
    position = new Vector3f();
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }
}

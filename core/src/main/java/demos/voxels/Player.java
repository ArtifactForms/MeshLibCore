package demos.voxels;

import math.Vector3f;

public class Player {

  private Vector3f position;
  private Vector3f velocity;
  private float width = 0.6f;
  private float height = 1.8f;
  private boolean grounded;

  public Player() {
    position = new Vector3f();
    velocity = new Vector3f();
    grounded = false;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public Vector3f getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector3f velocity) {
    this.velocity = velocity;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public boolean isGrounded() {
    return grounded;
  }

  public void setGrounded(boolean grounded) {
    this.grounded = grounded;
  }
}

package engine.scene.audio;

import math.Vector3f;

public class AudioListener {
  private Vector3f position;
  private Vector3f forward;

  public AudioListener() {
    this.position = new Vector3f(0, 0, 0);
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public Vector3f getForward() {
    return forward;
  }

  public void setForward(Vector3f forward) {
    this.forward = forward;
  }
}

package demos.collision;

public class Settings {

  private float stepHeight = 0.3f;
  private float radius = 0.5f;
  private float halfHeight = 1.0f; // Half-height of the cylindrical section
  private float speed = 12f;
  private boolean cameraFollowEnabled = false;

  public float getCapsuleRadius() {
    return radius;
  }

  public float getStepHeight() {
    return stepHeight;
  }

  public float getCapsuleHeight() {
    return (halfHeight * 2) + (radius * 2);
  }

  public float getHalfHeight() {
    return halfHeight;
  }

  public float getSpeed() {
    return speed;
  }

  public float getRadius() {
    return radius;
  }

  public boolean isCameraFollowEnabled() {
    return cameraFollowEnabled;
  }
}

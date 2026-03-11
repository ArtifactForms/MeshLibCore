package engine.scene.camera;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;

public class CameraTest {

  @Test
  void testPointInFrontOfCameraIsVisible() {
    PerspectiveCamera camera = new PerspectiveCamera();

    camera.getTransform().setPosition(0, 0, 0);
    camera.getTransform().setRotation(0, 0, 0);

    Matrix4f vp = camera.getViewProjectionMatrix();

    // Point is 5 units in front of the camera (-Z direction)
    Vector4f point = new Vector4f(0, 0, -5, 1);

    Vector4f clip = vp.mult(point);

    // Calculate Normalized Device Coordinates (NDC)
    float ndcX = clip.getX() / clip.getW();
    float ndcY = clip.getY() / clip.getW();
    float ndcZ = clip.getZ() / clip.getW();

    // Check if the point is within the visible unit cube [-1, 1]
    assertTrue(Math.abs(ndcX) <= 1);
    assertTrue(Math.abs(ndcY) <= 1);
    assertTrue(Math.abs(ndcZ) <= 1);
  }

  @Test
  void testPointBehindCameraIsNotVisible() {
    PerspectiveCamera camera = new PerspectiveCamera();

    Matrix4f vp = camera.getViewProjectionMatrix();

    // Point is 5 units behind the camera (+Z direction)
    Vector4f point = new Vector4f(0, 0, 5, 1);

    Vector4f clip = vp.mult(point);

    float ndcZ = clip.getZ() / clip.getW();

    // Point should be outside the NDC range [-1, 1]
    assertTrue(ndcZ > 1 || ndcZ < -1);
  }

  @Test
  void testFrustumFromCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    camera.getTransform().setPosition(0, 0, 0);

    Matrix4f vp = camera.getViewProjectionMatrix();

    Frustum frustum = new Frustum();
    frustum.update(vp);

    Vector3f center = new Vector3f(0, 0, -5);

    // The sphere should be inside the frustum
    assertTrue(frustum.intersectsSphere(center, 1));
  }

  @Test
  void testFrustumWithCameraRotation() {
    PerspectiveCamera camera = new PerspectiveCamera();

    // Rotate camera 90 degrees around Y axis (looking towards +X)
    camera.getTransform().setRotation(0, (float) Math.toRadians(90), 0);

    Matrix4f vp = camera.getViewProjectionMatrix();

    Frustum frustum = new Frustum();
    frustum.update(vp);

    // Point at X=5 should now be in front of the camera
    Vector3f point = new Vector3f(5, 0, 0);

    assertTrue(frustum.intersectsSphere(point, 1));
  }

  @Test
  void testFrustumWithCameraTranslation() {
    PerspectiveCamera camera = new PerspectiveCamera();
    Frustum frustum = new Frustum();

    // Move camera far away (e.g., X=100)
    camera.getTransform().setPosition(100, 0, 0);
    camera.getTransform().setRotation(0, 0, 0); // Still looking forward (-Z)

    Matrix4f vp = camera.getViewProjectionMatrix();
    frustum.update(vp);

    // Point directly in front of the camera at its new position
    // Position (100, 0, 0) + Forward (0, 0, -5) = (100, 0, -5)
    Vector3f pointInFront = new Vector3f(100, 0, -5);

    // Point at the old origin (0, 0, -5), which should now be far outside
    Vector3f pointOldOrigin = new Vector3f(0, 0, -5);

    assertTrue(
        frustum.intersectsSphere(pointInFront, 1),
        "Point in front of the translated camera should be visible");
    assertTrue(
        !frustum.intersectsSphere(pointOldOrigin, 1),
        "Point at the origin should not be visible after translation");
  }

  @Test
  void testFrustumWithMovementAndRotation() {
    PerspectiveCamera camera = new PerspectiveCamera();
    Frustum frustum = new Frustum();

    // Move camera and rotate 180 degrees (now looking towards +Z)
    camera.getTransform().setPosition(0, 0, 10);
    camera.getTransform().setRotation(0, (float) Math.toRadians(180), 0);

    frustum.update(camera.getViewProjectionMatrix());

    // Point at Z=15 (in front of camera, as it is at Z=10 looking backwards)
    Vector3f pointInFront = new Vector3f(0, 0, 15);
    // Point at Z=5 (now behind the camera)
    Vector3f pointBehind = new Vector3f(0, 0, 5);

    assertTrue(
        frustum.intersectsSphere(pointInFront, 1),
        "Point in front of the rotated camera (Z=15) should be visible");
    assertTrue(
        !frustum.intersectsSphere(pointBehind, 1),
        "Point behind the camera (Z=5) should be culled");
  }

  @Test
  void testFrustumHugeTranslation() {
    PerspectiveCamera camera = new PerspectiveCamera();
    Frustum frustum = new Frustum();

    // Test with extreme values to check floating point precision and matrix indices
    float hugeX = 5000.0f;
    camera.getTransform().setPosition(hugeX, 0, 0);

    frustum.update(camera.getViewProjectionMatrix());

    Vector3f point = new Vector3f(hugeX, 0, -10);

    assertTrue(
        frustum.intersectsSphere(point, 1), "Culling should work even with large coordinates");
  }
}

package math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Vector3fTest {

  @Test
  public void angleReturnsPiOverTwoForOrthogonalVectors() {
    Vector3f xAxis = new Vector3f(1.0f, 0.0f, 0.0f);
    Vector3f yAxis = new Vector3f(0.0f, 1.0f, 0.0f);

    float angle = xAxis.angle(yAxis);

    assertEquals((float) (Math.PI / 2.0), angle, 1e-6f);
  }

  @Test
  public void angleThrowsForZeroLengthVector() {
    Vector3f zero = new Vector3f();
    Vector3f xAxis = new Vector3f(1.0f, 0.0f, 0.0f);

    assertThrows(IllegalArgumentException.class, () -> zero.angle(xAxis));
    assertThrows(IllegalArgumentException.class, () -> xAxis.angle(zero));
  }

  @Test
  public void crossLocalWithComponentsUsesOriginalValues() {
    Vector3f vector = new Vector3f(1.0f, 2.0f, 3.0f);

    Vector3f result = vector.crossLocal(4.0f, 5.0f, 6.0f);

    assertSame(vector, result);
    assertEquals(-3.0f, vector.x, 1e-6f);
    assertEquals(6.0f, vector.y, 1e-6f);
    assertEquals(-3.0f, vector.z, 1e-6f);
  }

  @Test
  public void crossLocalWithVectorUsesOriginalValues() {
    Vector3f vector = new Vector3f(1.0f, 2.0f, 3.0f);

    Vector3f result = vector.crossLocal(new Vector3f(4.0f, 5.0f, 6.0f));

    assertSame(vector, result);
    assertEquals(-3.0f, vector.x, 1e-6f);
    assertEquals(6.0f, vector.y, 1e-6f);
    assertEquals(-3.0f, vector.z, 1e-6f);
  }

  @Test
  public void clampLengthDoesNotGrowShortVector() {
    Vector3f vector = new Vector3f(3.0f, 4.0f, 0.0f);

    Vector3f clamped = vector.clampLength(10.0f);

    assertNotEquals(vector, clamped);
    assertEquals(3.0f, clamped.x, 1e-6f);
    assertEquals(4.0f, clamped.y, 1e-6f);
    assertEquals(0.0f, clamped.z, 1e-6f);
    assertEquals(5.0f, clamped.length(), 1e-6f);
  }

  @Test
  public void clampLengthShortensLongVector() {
    Vector3f vector = new Vector3f(3.0f, 4.0f, 0.0f);

    Vector3f clamped = vector.clampLength(2.0f);

    assertEquals(2.0f, clamped.length(), 1e-6f);
    assertEquals(1.2f, clamped.x, 1e-6f);
    assertEquals(1.6f, clamped.y, 1e-6f);
  }

  @Test
  public void clampLengthLocalOnlyClampsWhenNeeded() {
    Vector3f shortVector = new Vector3f(1.0f, 1.0f, 1.0f);
    Vector3f longVector = new Vector3f(3.0f, 4.0f, 0.0f);

    shortVector.clampLengthLocal(10.0f);
    longVector.clampLengthLocal(2.0f);

    assertEquals((float) Math.sqrt(3.0), shortVector.length(), 1e-6f);
    assertEquals(2.0f, longVector.length(), 1e-6f);
  }

  @Test
  public void projectOnPlaneRemovesNormalComponent() {
    Vector3f vector = new Vector3f(2.0f, 3.0f, 4.0f);

    Vector3f projected = vector.projectOnPlane(new Vector3f(0.0f, 1.0f, 0.0f));

    assertEquals(2.0f, projected.x, 1e-6f);
    assertEquals(0.0f, projected.y, 1e-6f);
    assertEquals(4.0f, projected.z, 1e-6f);
    assertTrue(Math.abs(projected.dot(new Vector3f(0.0f, 1.0f, 0.0f))) < 1e-6f);
  }
}

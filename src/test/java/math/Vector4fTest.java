package math;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Vector4fTest {

  @Test
  public void testDefaultConstructor() {
    Vector4f vector = new Vector4f();
    assertEquals(0, vector.getX(), "Default X value should be 0.");
    assertEquals(0, vector.getY(), "Default Y value should be 0.");
    assertEquals(0, vector.getZ(), "Default Z value should be 0.");
    assertEquals(0, vector.getW(), "Default W value should be 0.");
  }

  @Test
  public void testParameterizedConstructor() {
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    assertEquals(1.0f, vector.getX(), "X value should match the constructor argument.");
    assertEquals(2.0f, vector.getY(), "Y value should match the constructor argument.");
    assertEquals(3.0f, vector.getZ(), "Z value should match the constructor argument.");
    assertEquals(4.0f, vector.getW(), "W value should match the constructor argument.");
  }

  @Test
  public void testCopyConstructor() {
    Vector4f original = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f copy = new Vector4f(original);

    assertEquals(original.getX(), copy.getX(), "X value of copy should match the original.");
    assertEquals(original.getY(), copy.getY(), "Y value of copy should match the original.");
    assertEquals(original.getZ(), copy.getZ(), "Z value of copy should match the original.");
    assertEquals(original.getW(), copy.getW(), "W value of copy should match the original.");
  }

  @Test
  public void testCopyConstructorIndependence() {
    Vector4f original = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f copy = new Vector4f(original);

    // Modify the original vector
    original = new Vector4f(0, 0, 0, 0);

    // Ensure the copy remains unchanged
    assertEquals(1.0f, copy.getX(), "Copy X value should remain unchanged.");
    assertEquals(2.0f, copy.getY(), "Copy Y value should remain unchanged.");
    assertEquals(3.0f, copy.getZ(), "Copy Z value should remain unchanged.");
    assertEquals(4.0f, copy.getW(), "Copy W value should remain unchanged.");
  }

  // ----------------------------------------------------------------------------------------------
  // Add
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testAddValidVector() {
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);

    Vector4f result = vector1.add(vector2);

    assertEquals(5.0f, result.getX());
    assertEquals(5.0f, result.getY());
    assertEquals(5.0f, result.getZ());
    assertEquals(5.0f, result.getW());
  }

  @Test
  public void testAddNullVector() {
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    assertThrows(IllegalArgumentException.class, () -> vector1.add(null));
  }

  @Test
  public void testAddZeroVector() {
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f zeroVector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    Vector4f result = vector1.add(zeroVector);

    assertEquals(1.0f, result.getX());
    assertEquals(2.0f, result.getY());
    assertEquals(3.0f, result.getZ());
    assertEquals(4.0f, result.getW());
  }

  @Test
  public void testAddShouldNotModifyOriginalVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vectorToAdd = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);

    // Act
    originalVector.add(vectorToAdd);

    // Assert that original vector remains unchanged
    assertEquals(1.0f, originalVector.getX());
    assertEquals(2.0f, originalVector.getY());
    assertEquals(3.0f, originalVector.getZ());
    assertEquals(4.0f, originalVector.getW());

    // Assert that the other vector remains unchanged
    assertEquals(4.0f, vectorToAdd.getX());
    assertEquals(3.0f, vectorToAdd.getY());
    assertEquals(2.0f, vectorToAdd.getZ());
    assertEquals(1.0f, vectorToAdd.getW());
  }

  @Test
  public void testAddShouldNotModifyOriginalVectorWhenAddingZeroVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f zeroVector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    originalVector.add(zeroVector);

    // Assert that original vector remains unchanged
    assertEquals(1.0f, originalVector.getX());
    assertEquals(2.0f, originalVector.getY());
    assertEquals(3.0f, originalVector.getZ());
    assertEquals(4.0f, originalVector.getW());

    // Assert that zero vector remains unchanged
    assertEquals(0.0f, zeroVector.getX());
    assertEquals(0.0f, zeroVector.getY());
    assertEquals(0.0f, zeroVector.getZ());
    assertEquals(0.0f, zeroVector.getW());
  }

  @Test
  public void testAddShouldNotModifyOtherVectorWhenAddingNonZeroVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vectorToAdd = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    // Act
    originalVector.add(vectorToAdd);

    // Assert that other vector (vectorToAdd) remains unchanged
    assertEquals(1.0f, vectorToAdd.getX());
    assertEquals(1.0f, vectorToAdd.getY());
    assertEquals(1.0f, vectorToAdd.getZ());
    assertEquals(1.0f, vectorToAdd.getW());
  }

  // ----------------------------------------------------------------------------------------------
  // Add Local
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testAddLocalValidVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vectorToAdd = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);

    // Act
    Vector4f result = originalVector.addLocal(vectorToAdd);

    // Assert that the original vector is updated correctly
    assertEquals(5.0f, originalVector.getX());
    assertEquals(5.0f, originalVector.getY());
    assertEquals(5.0f, originalVector.getZ());
    assertEquals(5.0f, originalVector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(originalVector, result);
  }

  @Test
  public void testAddLocalNullVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act and Assert: ensure IllegalArgumentException is thrown when adding null
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          originalVector.addLocal(null);
        });
  }

  @Test
  public void testAddLocalZeroVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f zeroVector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    Vector4f result = originalVector.addLocal(zeroVector);

    // Assert that the original vector remains the same (adding zero doesn't change it)
    assertEquals(1.0f, originalVector.getX());
    assertEquals(2.0f, originalVector.getY());
    assertEquals(3.0f, originalVector.getZ());
    assertEquals(4.0f, originalVector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(originalVector, result);
  }

  @Test
  public void testAddLocalShouldNotModifyOtherVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vectorToAdd = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);

    // Act
    originalVector.addLocal(vectorToAdd);

    // Assert that the other vector (vectorToAdd) remains unchanged
    assertEquals(4.0f, vectorToAdd.getX());
    assertEquals(3.0f, vectorToAdd.getY());
    assertEquals(2.0f, vectorToAdd.getZ());
    assertEquals(1.0f, vectorToAdd.getW());
  }

  // ----------------------------------------------------------------------------------------------
  // Subtract
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testSubtractValidVector() {
    // Arrange
    Vector4f vector1 = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);
    Vector4f vector2 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    Vector4f result = vector1.subtract(vector2);

    // Assert
    assertEquals(3.0f, result.getX());
    assertEquals(1.0f, result.getY());
    assertEquals(-1.0f, result.getZ());
    assertEquals(-3.0f, result.getW());
  }

  @Test
  public void testSubtractNullVector() {
    Vector4f vector1 = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);

    assertThrows(IllegalArgumentException.class, () -> vector1.subtract(null));
  }

  @Test
  public void testSubtractZeroVector() {
    Vector4f vector1 = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);
    Vector4f zeroVector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    Vector4f result = vector1.subtract(zeroVector);

    assertEquals(4.0f, result.getX());
    assertEquals(3.0f, result.getY());
    assertEquals(2.0f, result.getZ());
    assertEquals(1.0f, result.getW());
  }

  @Test
  public void testSubtractShouldNotModifyOriginalVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);
    Vector4f vectorToSubtract = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    originalVector.subtract(vectorToSubtract);

    // Assert that original vector remains unchanged
    assertEquals(4.0f, originalVector.getX());
    assertEquals(3.0f, originalVector.getY());
    assertEquals(2.0f, originalVector.getZ());
    assertEquals(1.0f, originalVector.getW());

    // Assert that the other vector remains unchanged
    assertEquals(1.0f, vectorToSubtract.getX());
    assertEquals(2.0f, vectorToSubtract.getY());
    assertEquals(3.0f, vectorToSubtract.getZ());
    assertEquals(4.0f, vectorToSubtract.getW());
  }

  @Test
  public void testSubtractShouldNotModifyOriginalVectorWhenSubtractingZeroVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);
    Vector4f zeroVector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    originalVector.subtract(zeroVector);

    // Assert that original vector remains unchanged
    assertEquals(4.0f, originalVector.getX());
    assertEquals(3.0f, originalVector.getY());
    assertEquals(2.0f, originalVector.getZ());
    assertEquals(1.0f, originalVector.getW());

    // Assert that zero vector remains unchanged
    assertEquals(0.0f, zeroVector.getX());
    assertEquals(0.0f, zeroVector.getY());
    assertEquals(0.0f, zeroVector.getZ());
    assertEquals(0.0f, zeroVector.getW());
  }

  @Test
  public void testSubtractShouldNotModifyOtherVectorWhenSubtractingNonZeroVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(4.0f, 3.0f, 2.0f, 1.0f);
    Vector4f vectorToSubtract = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    // Act
    originalVector.subtract(vectorToSubtract);

    // Assert that other vector (vectorToSubtract) remains unchanged
    assertEquals(1.0f, vectorToSubtract.getX());
    assertEquals(1.0f, vectorToSubtract.getY());
    assertEquals(1.0f, vectorToSubtract.getZ());
    assertEquals(1.0f, vectorToSubtract.getW());
  }

  // ----------------------------------------------------------------------------------------------
  // Subtract Local
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testSubtractLocalValidVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(5.0f, 5.0f, 5.0f, 5.0f);
    Vector4f vectorToSubtract = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    Vector4f result = originalVector.subtractLocal(vectorToSubtract);

    // Assert that the original vector is updated correctly
    assertEquals(4.0f, originalVector.getX());
    assertEquals(3.0f, originalVector.getY());
    assertEquals(2.0f, originalVector.getZ());
    assertEquals(1.0f, originalVector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(originalVector, result);
  }

  @Test
  public void testSubtractLocalNullVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(5.0f, 5.0f, 5.0f, 5.0f);

    // Act and Assert: ensure IllegalArgumentException is thrown when subtracting null
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          originalVector.subtractLocal(null);
        });
  }

  @Test
  public void testSubtractLocalZeroVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(5.0f, 5.0f, 5.0f, 5.0f);
    Vector4f zeroVector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    Vector4f result = originalVector.subtractLocal(zeroVector);

    // Assert that the original vector remains the same (subtracting zero doesn't change it)
    assertEquals(5.0f, originalVector.getX());
    assertEquals(5.0f, originalVector.getY());
    assertEquals(5.0f, originalVector.getZ());
    assertEquals(5.0f, originalVector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(originalVector, result);
  }

  @Test
  public void testSubtractLocalShouldNotModifyOtherVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(5.0f, 5.0f, 5.0f, 5.0f);
    Vector4f vectorToSubtract = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    originalVector.subtractLocal(vectorToSubtract);

    // Assert that the other vector (vectorToSubtract) remains unchanged
    assertEquals(1.0f, vectorToSubtract.getX());
    assertEquals(2.0f, vectorToSubtract.getY());
    assertEquals(3.0f, vectorToSubtract.getZ());
    assertEquals(4.0f, vectorToSubtract.getW());
  }

  // ----------------------------------------------------------------------------------------------
  //  Scalar Multiplication
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testMultiplyValidScalar() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    float scalar = 2.0f;

    // Act
    Vector4f result = vector.multiply(scalar);

    // Assert that each component is multiplied correctly
    assertEquals(2.0f, result.getX());
    assertEquals(4.0f, result.getY());
    assertEquals(6.0f, result.getZ());
    assertEquals(8.0f, result.getW());
  }

  @Test
  public void testMultiplyByZero() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = 0.0f;

    // Act
    Vector4f result = vector.multiply(scalar);

    // This approach ensures that 0.0f and -0.0f are considered equal when performing
    // assertions, as they are numerically the same but may differ in representation.
    // Assert that each component is zero, allowing for floating-point precision issues
    assertEquals(0.0f, result.getX(), 1e-6f); // delta of 1e-6f
    assertEquals(0.0f, result.getY(), 1e-6f);
    assertEquals(0.0f, result.getZ(), 1e-6f);
    assertEquals(0.0f, result.getW(), 1e-6f);
  }

  @Test
  public void testMultiplyByNegativeScalar() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = -2.0f;

    // Act
    Vector4f result = vector.multiply(scalar);

    // Assert that each component is negated correctly
    assertEquals(-2.0f, result.getX());
    assertEquals(4.0f, result.getY());
    assertEquals(-6.0f, result.getZ());
    assertEquals(8.0f, result.getW());
  }

  @Test
  public void testMultiplyWithZeroVector() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    float scalar = 0.0f;

    // Act
    Vector4f result = vector.multiply(scalar);

    // Assert that result is zero vector
    assertEquals(0.0f, result.getX());
    assertEquals(0.0f, result.getY());
    assertEquals(0.0f, result.getZ());
    assertEquals(0.0f, result.getW());
  }

  @Test
  public void testMultiplyShouldNotModifyOriginalVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = 2.0f;

    // Act
    originalVector.multiply(scalar);

    // Assert that original vector remains unchanged
    assertEquals(1.0f, originalVector.getX());
    assertEquals(-2.0f, originalVector.getY());
    assertEquals(3.0f, originalVector.getZ());
    assertEquals(-4.0f, originalVector.getW());
  }

  // ----------------------------------------------------------------------------------------------
  //  Scalar Multiplication
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testMultiplyLocalByPositiveScalar() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = 2.0f;

    // Act
    Vector4f result = vector.multiplyLocal(scalar);

    // Assert: verify that the original vector is updated correctly
    assertEquals(2.0f, vector.getX());
    assertEquals(-4.0f, vector.getY());
    assertEquals(6.0f, vector.getZ());
    assertEquals(-8.0f, vector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(vector, result);
  }

  @Test
  public void testMultiplyLocalByNegativeScalar() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = -2.0f;

    // Act
    Vector4f result = vector.multiplyLocal(scalar);

    // Assert: verify that the original vector is updated correctly
    assertEquals(-2.0f, vector.getX());
    assertEquals(4.0f, vector.getY());
    assertEquals(-6.0f, vector.getZ());
    assertEquals(8.0f, vector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(vector, result);
  }

  @Test
  public void testMultiplyLocalByZero() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = 0.0f;

    // Act
    Vector4f result = vector.multiplyLocal(scalar);

    // Assert: verify that all components become zero
    assertEquals(0.0f, vector.getX(), 1e-6f);
    assertEquals(0.0f, vector.getY(), 1e-6f);
    assertEquals(0.0f, vector.getZ(), 1e-6f);
    assertEquals(0.0f, vector.getW(), 1e-6f);

    // Assert that the result is the same as the modified original vector
    assertSame(vector, result);
  }

  @Test
  public void testMultiplyLocalByOne() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = 1.0f;

    // Act
    Vector4f result = vector.multiplyLocal(scalar);

    // Assert: verify that the vector remains unchanged
    assertEquals(1.0f, vector.getX());
    assertEquals(-2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(-4.0f, vector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(vector, result);
  }

  @Test
  public void testMultiplyLocalByFraction() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, 4.0f, 6.0f, 8.0f);
    float scalar = 0.5f;

    // Act
    Vector4f result = vector.multiplyLocal(scalar);

    // Assert: verify that the vector components are halved
    assertEquals(1.0f, vector.getX());
    assertEquals(2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(4.0f, vector.getW());

    // Assert that the result is the same as the modified original vector
    assertSame(vector, result);
  }

  // ----------------------------------------------------------------------------------------------
  // Scalar Division
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testDivideByPositiveScalar() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = 2.0f;

    // Act
    Vector4f result = vector.divide(scalar);

    // Assert: verify that the division works correctly
    assertEquals(1.0f, result.getX());
    assertEquals(-2.0f, result.getY());
    assertEquals(3.0f, result.getZ());
    assertEquals(-4.0f, result.getW());
  }

  @Test
  public void testDivideByNegativeScalar() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = -2.0f;

    // Act
    Vector4f result = vector.divide(scalar);

    // Assert: verify that the division works correctly with negative scalar
    assertEquals(-1.0f, result.getX());
    assertEquals(2.0f, result.getY());
    assertEquals(-3.0f, result.getZ());
    assertEquals(4.0f, result.getW());
  }

  @Test
  public void testDivideByZero() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = 0.0f;

    // Act and Assert: ensure that dividing by zero throws an ArithmeticException
    assertThrows(
        ArithmeticException.class,
        () -> {
          vector.divide(scalar);
        });
  }

  @Test
  public void testDivideByOne() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = 1.0f;

    // Act
    Vector4f result = vector.divide(scalar);

    // Assert: verify that the vector remains unchanged when divided by one
    assertEquals(2.0f, result.getX());
    assertEquals(-4.0f, result.getY());
    assertEquals(6.0f, result.getZ());
    assertEquals(-8.0f, result.getW());
  }

  @Test
  public void testDivideByFraction() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, 4.0f, 6.0f, 8.0f);
    float scalar = 0.5f;

    // Act
    Vector4f result = vector.divide(scalar);

    // Assert: verify that the vector components are doubled when divided by a fraction
    assertEquals(4.0f, result.getX());
    assertEquals(8.0f, result.getY());
    assertEquals(12.0f, result.getZ());
    assertEquals(16.0f, result.getW());
  }

  @Test
  public void testDivideShouldNotModifyOriginalVector() {
    // Arrange
    Vector4f originalVector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    float scalar = 2.0f;

    // Act
    originalVector.divide(scalar);

    // Assert that original vector remains unchanged
    assertEquals(1.0f, originalVector.getX());
    assertEquals(-2.0f, originalVector.getY());
    assertEquals(3.0f, originalVector.getZ());
    assertEquals(-4.0f, originalVector.getW());
  }

  // ----------------------------------------------------------------------------------------------
  // Scalar Division Local
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testDivideLocalByPositiveScalar() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = 2.0f;

    // Act
    Vector4f result = vector.divideLocal(scalar);

    // Assert: verify that the division works correctly and the vector is updated in place
    assertEquals(1.0f, vector.getX());
    assertEquals(-2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(-4.0f, vector.getW());

    assertSame(vector, result);
  }

  @Test
  public void testDivideLocalByNegativeScalar() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = -2.0f;

    // Act
    Vector4f result = vector.divideLocal(scalar);

    // Assert: verify that the division works correctly with negative scalar and the vector is
    // updated
    assertEquals(-1.0f, vector.getX());
    assertEquals(2.0f, vector.getY());
    assertEquals(-3.0f, vector.getZ());
    assertEquals(4.0f, vector.getW());

    assertSame(vector, result);
  }

  @Test
  public void testDivideLocalByZero() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = 0.0f;

    // Act and Assert: ensure that dividing by zero throws an ArithmeticException
    assertThrows(
        ArithmeticException.class,
        () -> {
          vector.divideLocal(scalar);
        });
  }

  @Test
  public void testDivideLocalByOne() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = 1.0f;

    // Act
    Vector4f result = vector.divideLocal(scalar);

    // Assert: verify that the vector remains unchanged when divided by one
    assertEquals(2.0f, vector.getX());
    assertEquals(-4.0f, vector.getY());
    assertEquals(6.0f, vector.getZ());
    assertEquals(-8.0f, vector.getW());

    assertSame(vector, result);
  }

  @Test
  public void testDivideLocalByFraction() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, 4.0f, 6.0f, 8.0f);
    float scalar = 0.5f;

    // Act
    Vector4f result = vector.divideLocal(scalar);

    // Assert: verify that the vector components are doubled when divided by a fraction
    assertEquals(4.0f, vector.getX());
    assertEquals(8.0f, vector.getY());
    assertEquals(12.0f, vector.getZ());
    assertEquals(16.0f, vector.getW());

    assertSame(vector, result);
  }

  @Test
  public void testDivideLocalWithNegativeFraction() {
    // Arrange
    Vector4f vector = new Vector4f(2.0f, -4.0f, 6.0f, -8.0f);
    float scalar = -0.5f;

    // Act
    Vector4f result = vector.divideLocal(scalar);

    // Assert: verify that the vector components are halved and the signs are flipped
    assertEquals(-4.0f, vector.getX());
    assertEquals(8.0f, vector.getY());
    assertEquals(-12.0f, vector.getZ());
    assertEquals(16.0f, vector.getW());

    assertSame(vector, result);
  }

  // ----------------------------------------------------------------------------------------------
  // Negate
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testNegate() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act
    Vector4f negatedVector = vector.negate();

    // Assert: verify that the negated vector has the opposite components
    assertEquals(-1.0f, negatedVector.getX());
    assertEquals(2.0f, negatedVector.getY());
    assertEquals(-3.0f, negatedVector.getZ());
    assertEquals(4.0f, negatedVector.getW());
    // Verify that the original vector is unchanged
    assertEquals(1.0f, vector.getX());
    assertEquals(-2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(-4.0f, vector.getW());

    // Ensure new instance is returned
    assertNotSame(vector, negatedVector);
  }

  @Test
  public void testNegateWithZero() {
    // Arrange
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    Vector4f negatedVector = vector.negate();

    // Assert: verify that negating a zero vector still results in a zero vector
    assertEquals(0.0f, negatedVector.getX());
    assertEquals(0.0f, negatedVector.getY());
    assertEquals(0.0f, negatedVector.getZ());
    assertEquals(0.0f, negatedVector.getW());

    // Verify that the original zero vector is unchanged
    assertEquals(0.0f, vector.getX());
    assertEquals(0.0f, vector.getY());
    assertEquals(0.0f, vector.getZ());
    assertEquals(0.0f, vector.getW());

    // Ensure new instance is returned
    assertNotSame(vector, negatedVector);
  }

  @Test
  public void testNegateWithNegativeValues() {
    // Arrange
    Vector4f vector = new Vector4f(-1.0f, -2.0f, -3.0f, -4.0f);

    // Act
    Vector4f negatedVector = vector.negate();

    // Assert: verify that negating a vector with negative values results in a vector with positive
    // values
    assertEquals(1.0f, negatedVector.getX());
    assertEquals(2.0f, negatedVector.getY());
    assertEquals(3.0f, negatedVector.getZ());
    assertEquals(4.0f, negatedVector.getW());

    // Verify that the original vector is unchanged
    assertEquals(-1.0f, vector.getX());
    assertEquals(-2.0f, vector.getY());
    assertEquals(-3.0f, vector.getZ());
    assertEquals(-4.0f, vector.getW());

    // Ensure new instance is returned
    assertNotSame(vector, negatedVector);
  }

  @Test
  public void testNegateWithMixedValues() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -1.0f, 0.0f, 3.0f);

    // Act
    Vector4f negatedVector = vector.negate();

    // Assert: verify that mixed values are negated correctly in the new vector
    assertEquals(-1.0f, negatedVector.getX());
    assertEquals(1.0f, negatedVector.getY());
    assertEquals(0.0f, negatedVector.getZ());
    assertEquals(-3.0f, negatedVector.getW());

    // Verify that the original vector is unchanged
    assertEquals(1.0f, vector.getX());
    assertEquals(-1.0f, vector.getY());
    assertEquals(0.0f, vector.getZ());
    assertEquals(3.0f, vector.getW());

    // Ensure new instance is returned
    assertNotSame(vector, negatedVector);
  }

  @Test
  public void testNegateTwice() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act
    Vector4f negatedVectorOnce = vector.negate();
    Vector4f negatedVectorTwice = negatedVectorOnce.negate();

    // Assert: verify that negating twice returns the original vector
    assertEquals(1.0f, negatedVectorTwice.getX());
    assertEquals(-2.0f, negatedVectorTwice.getY());
    assertEquals(3.0f, negatedVectorTwice.getZ());
    assertEquals(-4.0f, negatedVectorTwice.getW());

    // Ensure new instance is returned
    assertNotSame(vector, negatedVectorOnce);
    assertNotSame(vector, negatedVectorTwice);
    assertNotSame(negatedVectorOnce, negatedVectorTwice);
  }

  // ----------------------------------------------------------------------------------------------
  // Negate Local
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testNegateLocal() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act
    Vector4f result = vector.negateLocal();

    // Assert: verify that the vector components are negated correctly in place
    assertEquals(-1.0f, vector.getX());
    assertEquals(2.0f, vector.getY());
    assertEquals(-3.0f, vector.getZ());
    assertEquals(4.0f, vector.getW());

    // Ensure self reference is returned
    assertSame(vector, result);
  }

  @Test
  public void testNegateLocalWithZero() {
    // Arrange
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    Vector4f result = vector.negateLocal();

    // Assert: verify that negating a zero vector still results in a zero vector
    assertEquals(0.0f, vector.getX());
    assertEquals(0.0f, vector.getY());
    assertEquals(0.0f, vector.getZ());
    assertEquals(0.0f, vector.getW());

    // Ensure self reference is returned
    assertSame(vector, result);
  }

  @Test
  public void testNegateLocalWithNegativeValues() {
    // Arrange
    Vector4f vector = new Vector4f(-1.0f, -2.0f, -3.0f, -4.0f);

    // Act
    Vector4f result = vector.negateLocal();

    // Assert: verify that the negation of negative values results in positive values
    assertEquals(1.0f, vector.getX());
    assertEquals(2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(4.0f, vector.getW());

    // Ensure self reference is returned
    assertSame(vector, result);
  }

  @Test
  public void testNegateLocalWithMixedValues() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -1.0f, 0.0f, 3.0f);

    // Act
    Vector4f result = vector.negateLocal();

    // Assert: verify that mixed values are negated correctly
    assertEquals(-1.0f, vector.getX());
    assertEquals(1.0f, vector.getY());
    assertEquals(0.0f, vector.getZ());
    assertEquals(-3.0f, vector.getW());

    // Ensure self reference is returned
    assertSame(vector, result);
  }

  @Test
  public void testNegateLocalTwice() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act
    vector.negateLocal();
    Vector4f result = vector.negateLocal(); // negate again

    // Assert: verify that negating twice brings the vector back to its original state
    assertEquals(1.0f, vector.getX());
    assertEquals(-2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(-4.0f, vector.getW());

    // Ensure self reference is returned
    assertSame(vector, result);
  }

  // ----------------------------------------------------------------------------------------------
  // Length
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testLength() {
    // Arrange
    Vector4f vector = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);

    // Act
    float length = vector.length();

    // Assert: Verify the length is calculated correctly (sqrt(3^2 + 4^2) = 5)
    assertEquals(5.0f, length, 0.0001f);
  }

  @Test
  public void testLengthZeroVector() {
    // Arrange
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    float length = vector.length();

    // Assert: The length of a zero vector should be 0
    assertEquals(0.0f, length, 0.0001f);
  }

  @Test
  public void testLengthNegativeValues() {
    // Arrange
    Vector4f vector = new Vector4f(-3.0f, -4.0f, 0.0f, 0.0f);

    // Act
    float length = vector.length();

    // Assert: The length is the same as for positive values, sqrt((-3)^2 + (-4)^2) = 5
    assertEquals(5.0f, length, 0.0001f);
  }

  @Test
  public void testLengthMixedValues() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act
    float length = vector.length();

    // Assert: The length is sqrt(1^2 + (-2)^2 + 3^2 + (-4)^2) = sqrt(1 + 4 + 9 + 16) = sqrt(30)
    assertEquals(Math.sqrt(30), length, 0.0001f);
  }

  @Test
  public void testLengthUnitVector() {
    // Arrange: A unit vector with length 1
    Vector4f vector = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);

    // Act
    float length = vector.length();

    // Assert: The length of a unit vector should be 1
    assertEquals(1.0f, length, 0.0001f);
  }

  @Test
  public void testLengthLargeValues() {
    // Arrange
    Vector4f vector = new Vector4f(1000.0f, 2000.0f, 3000.0f, 4000.0f);

    // Act
    float length = vector.length();

    // Assert: Verify that the length is computed correctly for large values
    assertEquals(
        Math.sqrt(1000.0f * 1000.0f + 2000.0f * 2000.0f + 3000.0f * 3000.0f + 4000.0f * 4000.0f),
        length,
        0.0001f);
  }

  // ----------------------------------------------------------------------------------------------
  // Length squared
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testLengthSquared() {
    // Arrange
    Vector4f vector = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);

    // Act
    float lengthSquared = vector.lengthSquared();

    // Assert: Verify the length squared is calculated correctly (3^2 + 4^2 = 9 + 16 = 25)
    assertEquals(25.0f, lengthSquared, 0.0001f);
  }

  @Test
  public void testLengthSquaredZeroVector() {
    // Arrange
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act
    float lengthSquared = vector.lengthSquared();

    // Assert: The length squared of a zero vector should be 0
    assertEquals(0.0f, lengthSquared, 0.0001f);
  }

  @Test
  public void testLengthSquaredNegativeValues() {
    // Arrange
    Vector4f vector = new Vector4f(-3.0f, -4.0f, 0.0f, 0.0f);

    // Act
    float lengthSquared = vector.lengthSquared();

    // Assert: The length squared is the same as for positive values (3^2 + 4^2 = 9 + 16 = 25)
    assertEquals(25.0f, lengthSquared, 0.0001f);
  }

  @Test
  public void testLengthSquaredMixedValues() {
    // Arrange
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act
    float lengthSquared = vector.lengthSquared();

    // Assert: The length squared is calculated as (1^2 + (-2)^2 + 3^2 + (-4)^2) = 1 + 4 + 9 + 16 =
    // 30
    assertEquals(30.0f, lengthSquared, 0.0001f);
  }

  @Test
  public void testLengthSquaredUnitVector() {
    // Arrange: A unit vector with length 1, so length squared should also be 1
    Vector4f vector = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);

    // Act
    float lengthSquared = vector.lengthSquared();

    // Assert: The length squared of a unit vector should be 1
    assertEquals(1.0f, lengthSquared, 0.0001f);
  }

  @Test
  public void testLengthSquaredLargeValues() {
    // Arrange
    Vector4f vector = new Vector4f(1000.0f, 2000.0f, 3000.0f, 4000.0f);

    // Act
    float lengthSquared = vector.lengthSquared();

    // Assert: Verify that the length squared is computed correctly for large values
    assertEquals(
        1000.0f * 1000.0f + 2000.0f * 2000.0f + 3000.0f * 3000.0f + 4000.0f * 4000.0f,
        lengthSquared,
        0.0001f);
  }

  // ----------------------------------------------------------------------------------------------
  // Normalize
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testNormalize() {
    // Arrange: Vector with a known magnitude
    Vector4f vector = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);

    // Act: Normalize the vector
    Vector4f normalizedVector = vector.normalize();

    // Assert: The length of the normalized vector should be 1
    assertEquals(1.0f, normalizedVector.length(), 0.0001f);

    // Assert: The normalized vector should still point in the same direction
    // The expected normalized vector is (3/5, 4/5, 0, 0) because length of (3, 4, 0, 0) = 5
    assertEquals(3.0f / 5.0f, normalizedVector.getX(), 0.0001f);
    assertEquals(4.0f / 5.0f, normalizedVector.getY(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getZ(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getW(), 0.0001f);

    assertNotSame(vector, normalizedVector);
  }

  @Test
  public void testNormalizeZeroVector() {
    // Arrange: A zero vector, which cannot be normalized
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act: Normalize the zero vector
    Vector4f normalizedVector = vector.normalize();

    // Assert: The result should be a zero vector since a zero vector cannot be normalized
    assertEquals(0.0f, normalizedVector.getX(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getY(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getZ(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getW(), 0.0001f);

    assertNotSame(vector, normalizedVector);
  }

  @Test
  public void testNormalizeUnitVector() {
    // Arrange: A unit vector (length = 1)
    Vector4f vector = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);

    // Act: Normalize the unit vector
    Vector4f normalizedVector = vector.normalize();

    // Assert: The normalized vector should be the same as the input vector
    assertEquals(1.0f, normalizedVector.getX(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getY(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getZ(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getW(), 0.0001f);

    assertNotSame(vector, normalizedVector);
  }

  @Test
  public void testNormalizeNegativeValues() {
    // Arrange: A vector with negative components
    Vector4f vector = new Vector4f(-3.0f, -4.0f, 0.0f, 0.0f);

    // Act: Normalize the vector
    Vector4f normalizedVector = vector.normalize();

    // Assert: The normalized vector should have a magnitude of 1 and the same direction
    assertEquals(1.0f, normalizedVector.length(), 0.0001f);

    // Assert: The normalized vector should be (-3/5, -4/5, 0, 0)
    assertEquals(-3.0f / 5.0f, normalizedVector.getX(), 0.0001f);
    assertEquals(-4.0f / 5.0f, normalizedVector.getY(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getZ(), 0.0001f);
    assertEquals(0.0f, normalizedVector.getW(), 0.0001f);

    assertNotSame(vector, normalizedVector);
  }

  @Test
  public void testNormalizeMixedValues() {
    // Arrange: A vector with mixed positive and negative components
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act: Normalize the vector
    Vector4f normalizedVector = vector.normalize();

    // Assert: The length of the normalized vector should be 1
    assertEquals(1.0f, normalizedVector.length(), 0.0001f);

    // Assert: The normalized vector should point in the same direction as the input vector
    float length =
        (float) Math.sqrt(1.0f * 1.0f + (-2.0f) * (-2.0f) + 3.0f * 3.0f + (-4.0f) * (-4.0f));
    float invLength = 1.0f / length;

    assertEquals(1.0f * invLength, normalizedVector.getX(), 0.0001f);
    assertEquals(-2.0f * invLength, normalizedVector.getY(), 0.0001f);
    assertEquals(3.0f * invLength, normalizedVector.getZ(), 0.0001f);
    assertEquals(-4.0f * invLength, normalizedVector.getW(), 0.0001f);

    assertNotSame(vector, normalizedVector);
  }

  @Test
  public void testNormalizeDoesNotAlterOriginalVector() {
    // Arrange: Create a non-zero vector
    Vector4f originalVector = new Vector4f(2.0f, 3.0f, 4.0f, 5.0f);
    // Store the original values
    float originalX = originalVector.getX();
    float originalY = originalVector.getY();
    float originalZ = originalVector.getZ();
    float originalW = originalVector.getW();

    // Act: Normalize the vector
    Vector4f normalizedVector = originalVector.normalize();

    // Assert: Ensure the original vector is not modified
    assertEquals(originalX, originalVector.getX(), 0.0001f);
    assertEquals(originalY, originalVector.getY(), 0.0001f);
    assertEquals(originalZ, originalVector.getZ(), 0.0001f);
    assertEquals(originalW, originalVector.getW(), 0.0001f);

    // Optionally assert that the normalized vector has a length of 1
    assertEquals(1.0f, normalizedVector.length(), 0.0001f);

    assertNotSame(originalVector, normalizedVector);
  }

  // ----------------------------------------------------------------------------------------------
  // Normalize Local
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testNormalizeLocal() {
    // Arrange: Vector with a known magnitude
    Vector4f vector = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);

    // Act: Normalize the vector using normalizeLocal
    Vector4f result = vector.normalizeLocal();

    // Assert: The length of the normalized vector should be 1
    assertEquals(1.0f, vector.length(), 0.0001f);

    // Assert: The normalized vector should still point in the same direction
    // The expected normalized vector is (3/5, 4/5, 0, 0) because length of (3, 4, 0, 0) = 5
    assertEquals(3.0f / 5.0f, vector.getX(), 0.0001f);
    assertEquals(4.0f / 5.0f, vector.getY(), 0.0001f);
    assertEquals(0.0f, vector.getZ(), 0.0001f);
    assertEquals(0.0f, vector.getW(), 0.0001f);

    // Assert: The original vector should have been modified in place (same reference)
    assertSame(vector, result);
  }

  @Test
  public void testNormalizeLocalZeroVector() {
    // Arrange: A zero vector, which cannot be normalized
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act: Normalize the zero vector using normalizeLocal
    Vector4f result = vector.normalizeLocal();

    // Assert: The result should still be a zero vector since a zero vector cannot be normalized
    assertEquals(0.0f, vector.getX(), 0.0001f);
    assertEquals(0.0f, vector.getY(), 0.0001f);
    assertEquals(0.0f, vector.getZ(), 0.0001f);
    assertEquals(0.0f, vector.getW(), 0.0001f);

    // Assert: The original vector should have been modified in place (same reference)
    assertSame(vector, result);
  }

  @Test
  public void testNormalizeLocalUnitVector() {
    // Arrange: A unit vector (length = 1)
    Vector4f vector = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);

    // Act: Normalize the unit vector using normalizeLocal
    Vector4f result = vector.normalizeLocal();

    // Assert: The normalized vector should be the same as the input vector
    assertEquals(1.0f, vector.getX(), 0.0001f);
    assertEquals(0.0f, vector.getY(), 0.0001f);
    assertEquals(0.0f, vector.getZ(), 0.0001f);
    assertEquals(0.0f, vector.getW(), 0.0001f);

    // Assert: The original vector should have been modified in place (same reference)
    assertSame(vector, result);
  }

  @Test
  public void testNormalizeLocalNegativeValues() {
    // Arrange: A vector with negative components
    Vector4f vector = new Vector4f(-3.0f, -4.0f, 0.0f, 0.0f);

    // Act: Normalize the vector using normalizeLocal
    Vector4f result = vector.normalizeLocal();

    // Assert: The normalized vector should have a magnitude of 1 and the same direction
    assertEquals(1.0f, vector.length(), 0.0001f);

    // Assert: The normalized vector should be (-3/5, -4/5, 0, 0)
    assertEquals(-3.0f / 5.0f, vector.getX(), 0.0001f);
    assertEquals(-4.0f / 5.0f, vector.getY(), 0.0001f);
    assertEquals(0.0f, vector.getZ(), 0.0001f);
    assertEquals(0.0f, vector.getW(), 0.0001f);

    // Assert: The original vector should have been modified in place (same reference)
    assertSame(vector, result);
  }

  @Test
  public void testNormalizeLocalMixedValues() {
    // Arrange: A vector with mixed positive and negative components
    Vector4f vector = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);

    // Act: Normalize the vector using normalizeLocal
    Vector4f result = vector.normalizeLocal();

    // Assert: The length of the normalized vector should be 1
    assertEquals(1.0f, vector.length(), 0.0001f);

    // Assert: The normalized vector should point in the same direction as the input vector
    float length =
        (float) Math.sqrt(1.0f * 1.0f + (-2.0f) * (-2.0f) + 3.0f * 3.0f + (-4.0f) * (-4.0f));
    float invLength = 1.0f / length;

    assertEquals(1.0f * invLength, vector.getX(), 0.0001f);
    assertEquals(-2.0f * invLength, vector.getY(), 0.0001f);
    assertEquals(3.0f * invLength, vector.getZ(), 0.0001f);
    assertEquals(-4.0f * invLength, vector.getW(), 0.0001f);

    // Assert: The original vector should have been modified in place (same reference)
    assertSame(vector, result);
  }

  // ----------------------------------------------------------------------------------------------
  // Is Zero
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testIsZeroWithZeroVector() {
    // Arrange: A zero vector (all components are zero)
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act: Check if the vector is zero
    boolean result = vector.isZero();

    // Assert: The result should be true for a zero vector
    assertTrue(result);
  }

  @Test
  public void testIsZeroWithNonZeroVector() {
    // Arrange: A vector with non-zero components
    Vector4f vector = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);

    // Act: Check if the vector is zero
    boolean result = vector.isZero();

    // Assert: The result should be false for a non-zero vector
    assertFalse(result);
  }

  @Test
  public void testIsZeroWithNegativeValues() {
    // Arrange: A vector with negative components
    Vector4f vector = new Vector4f(-1.0f, 0.0f, 0.0f, 0.0f);

    // Act: Check if the vector is zero
    boolean result = vector.isZero();

    // Assert: The result should be false for a vector with negative components
    assertFalse(result);
  }

  @Test
  public void testIsZeroWithMixedNonZeroValues() {
    // Arrange: A vector with mixed non-zero components
    Vector4f vector = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);

    // Act: Check if the vector is zero
    boolean result = vector.isZero();

    // Assert: The result should be false for a vector with mixed non-zero components
    assertFalse(result);
  }

  @Test
  public void testIsZeroWithAllNonZeroComponents() {
    // Arrange: A vector with all non-zero components
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act: Check if the vector is zero
    boolean result = vector.isZero();

    // Assert: The result should be false for a vector with all non-zero components
    assertFalse(result);
  }

  // ----------------------------------------------------------------------------------------------
  // Dot
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testDotProductWithZeroVector() {
    // Arrange: A vector and a zero vector
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // Act: Calculate the dot product
    float result = vector1.dot(vector2);

    // Assert: The dot product of any vector with a zero vector should be 0
    assertEquals(0.0f, result, 0.0001f);
  }

  @Test
  public void testDotProductWithSameVector() {
    // Arrange: A vector and itself
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act: Calculate the dot product of the vector with itself
    float result = vector1.dot(vector1);

    // Assert: The dot product of a vector with itself is the sum of the squares of its components
    // dot(vector1, vector1) = 1^2 + 2^2 + 3^2 + 4^2 = 30
    assertEquals(30.0f, result, 0.0001f);
  }

  @Test
  public void testDotProductWithOrthogonalVectors() {
    // Arrange: Two orthogonal vectors
    Vector4f vector1 = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);
    Vector4f vector2 = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);

    // Act: Calculate the dot product
    float result = vector1.dot(vector2);

    // Assert: The dot product of orthogonal vectors should be 0
    assertEquals(0.0f, result, 0.0001f);
  }

  @Test
  public void testDotProductWithParallelVectors() {
    // Arrange: Two parallel vectors
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(2.0f, 4.0f, 6.0f, 8.0f); // Scalar multiple of vector1

    // Act: Calculate the dot product
    float result = vector1.dot(vector2);

    // Assert: The dot product of parallel vectors is the product of their magnitudes and cosine of
    // the angle between them, which is 1 for parallel vectors
    // dot(vector1, vector2) = 1*2 + 2*4 + 3*6 + 4*8 = 70
    assertEquals(60.0f, result, 0.0001f);
  }

  @Test
  public void testDotProductWithNegativeValues() {
    // Arrange: A vector and another vector with negative components
    Vector4f vector1 = new Vector4f(1.0f, -2.0f, 3.0f, -4.0f);
    Vector4f vector2 = new Vector4f(-1.0f, 2.0f, -3.0f, 4.0f);

    // Act: Calculate the dot product
    float result = vector1.dot(vector2);

    // Assert: The result should be the sum of the products of corresponding components
    // dot(vector1, vector2) = (1*(-1)) + (-2*2) + (3*(-3)) + (-4*4)
    // dot(vector1, vector2) = -1 - 4 - 9 - 16 = -30
    assertEquals(-30.0f, result, 0.0001f);
  }

  @Test
  public void testDotProductWithDifferentMagnitudeVectors() {
    // Arrange: Two vectors with different magnitudes
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(2.0f, 1.0f, -1.0f, 3.0f);

    // Act: Calculate the dot product
    float result = vector1.dot(vector2);

    // Assert: The dot product should be computed as expected
    // dot(vector1, vector2) = (1*2) + (2*1) + (3*(-1)) + (4*3) = 2 + 2 - 3 + 12 = 13
    assertEquals(13.0f, result, 0.0001f);
  }

  @Test
  public void testDotProductWithNull() {
    // Arrange: Create a vector
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act & Assert: Calling dot with null should throw NullPointerException
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          vector1.dot(null);
        });
  }

  // ----------------------------------------------------------------------------------------------
  // Is Equal With Tolerance
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testIsEqualExactMatch() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act & Assert
    assertTrue(v1.isEqual(v2, 0.0f));
  }

  @Test
  public void testIsEqualWithinTolerance() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.01f, 2.01f, 3.01f, 4.01f);

    // Act & Assert
    assertTrue(v1.isEqual(v2, 0.02f));
  }

  @Test
  public void testIsEqualExceedsTolerance() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.05f, 2.05f, 3.05f, 4.05f);

    // Act & Assert
    assertFalse(v1.isEqual(v2, 0.02f));
  }

  @Test
  public void testIsEqualNegativeTolerance() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act & Assert
    assertFalse(v1.isEqual(v2, -0.1f));
  }

  @Test
  public void testIsEqualWithNullVector() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act & Assert
    assertFalse(v1.isEqual(null, 0.1f));
  }

  @Test
  public void testIsEqualWithSelf() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act & Assert
    assertTrue(v1.isEqual(v1, 0.0f));
  }

  @Test
  public void testIsEqualWithVerySmallTolerance() {
    // Arrange
    Vector4f v1 = new Vector4f(1.00001f, 2.00001f, 3.00001f, 4.00001f);
    Vector4f v2 = new Vector4f(1.00002f, 2.00002f, 3.00002f, 4.00002f);

    // Act & Assert
    assertTrue(v1.isEqual(v2, 0.0001f)); // Should be equal within the small tolerance
    assertFalse(v1.isEqual(v2, 0.000001f)); // Should not be equal with a stricter tolerance
  }

  @Test
  public void testIsEqualLargeTolerance() {
    // Arrange
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(10.0f, 20.0f, 30.0f, 40.0f);

    // Act & Assert
    assertTrue(v1.isEqual(v2, 100.0f));
  }

  @Test
  public void testIsEqualNaN() {
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.0f, Float.NaN, 3.0f, 4.0f);
    assertFalse(v1.isEqual(v2, 0.1f));
  }

  @Test
  public void testIsEqualPositiveInfinity() {
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.0f, Float.POSITIVE_INFINITY, 3.0f, 4.0f);
    assertFalse(v1.isEqual(v2, 0.1f));
  }

  @Test
  public void testIsEqualNegativeInfinity() {
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.0f, Float.NEGATIVE_INFINITY, 3.0f, 4.0f);
    assertFalse(v1.isEqual(v2, 0.1f));
  }

  // ----------------------------------------------------------------------------------------------
  // Lerp
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testLerpWithTZero() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f end = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // Act
    Vector4f result = start.lerp(end, 0.0f);

    // Assert
    assertEquals(start.getX(), result.getX(), 0.0001f);
    assertEquals(start.getY(), result.getY(), 0.0001f);
    assertEquals(start.getZ(), result.getZ(), 0.0001f);
    assertEquals(start.getW(), result.getW(), 0.0001f);
  }

  @Test
  public void testLerpWithTOne() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f end = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // Act
    Vector4f result = start.lerp(end, 1.0f);

    // Assert
    assertEquals(end.getX(), result.getX(), 0.0001f);
    assertEquals(end.getY(), result.getY(), 0.0001f);
    assertEquals(end.getZ(), result.getZ(), 0.0001f);
    assertEquals(end.getW(), result.getW(), 0.0001f);
  }

  @Test
  public void testLerpWithHalfwayT() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f end = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // Act
    Vector4f result = start.lerp(end, 0.5f);

    // Assert
    assertEquals(3.0f, result.getX(), 0.0001f);
    assertEquals(4.0f, result.getY(), 0.0001f);
    assertEquals(5.0f, result.getZ(), 0.0001f);
    assertEquals(6.0f, result.getW(), 0.0001f);
  }

  @Test
  public void testLerpWithNegativeT() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f end = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // Act
    Vector4f result = start.lerp(end, -0.5f);

    // Assert
    assertEquals(-1.0f, result.getX(), 0.0001f);
    assertEquals(0.0f, result.getY(), 0.0001f);
    assertEquals(1.0f, result.getZ(), 0.0001f);
    assertEquals(2.0f, result.getW(), 0.0001f);
  }

  @Test
  public void testLerpWithOverOneT() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f end = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // Act
    Vector4f result = start.lerp(end, 1.5f);

    // Assert
    assertEquals(7.0f, result.getX(), 0.0001f);
    assertEquals(8.0f, result.getY(), 0.0001f);
    assertEquals(9.0f, result.getZ(), 0.0001f);
    assertEquals(10.0f, result.getW(), 0.0001f);
  }

  @Test
  public void testLerpWithIdenticalStartAndEnd() {
    // Arrange
    Vector4f start = new Vector4f(3.0f, 3.0f, 3.0f, 3.0f);
    Vector4f end = new Vector4f(3.0f, 3.0f, 3.0f, 3.0f);

    // Act
    Vector4f result = start.lerp(end, 0.5f);

    // Assert
    assertEquals(3.0f, result.getX(), 0.0001f);
    assertEquals(3.0f, result.getY(), 0.0001f);
    assertEquals(3.0f, result.getZ(), 0.0001f);
    assertEquals(3.0f, result.getW(), 0.0001f);
  }

  @Test
  public void testLerpReturnsNewInstanceAndOriginalVectorsUntouched() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f end = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);
    Vector4f startCopy = start.clone();
    Vector4f endCopy = end.clone();

    // Act
    Vector4f result = start.lerp(end, 0.5f);

    // Assert
    // Verify that a new instance is returned
    assertNotSame(start, result);
    assertNotSame(end, result);

    // Verify that the original start vector is untouched
    assertEquals(startCopy.getX(), start.getX(), 0.0001f);
    assertEquals(startCopy.getY(), start.getY(), 0.0001f);
    assertEquals(startCopy.getZ(), start.getZ(), 0.0001f);
    assertEquals(startCopy.getW(), start.getW(), 0.0001f);

    // Verify that the original end vector is untouched
    assertEquals(endCopy.getX(), end.getX(), 0.0001f);
    assertEquals(endCopy.getY(), end.getY(), 0.0001f);
    assertEquals(endCopy.getZ(), end.getZ(), 0.0001f);
    assertEquals(endCopy.getW(), end.getW(), 0.0001f);
  }

  @Test
  public void testLerpWithNullThrowsException() {
    // Arrange
    Vector4f start = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f other = null;
    float t = 0.5f;

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          start.lerp(other, t);
        });
  }

  // ----------------------------------------------------------------------------------------------
  // Distance To Squared
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testDistanceSquaredToWithValidInput() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f v2 = new Vector4f(5, 6, 7, 8);
    float expected = 64.0f; // (5-1)^2 + (6-2)^2 + (7-3)^2 + (8-4)^2 = 16 + 16 + 16 + 16
    assertEquals(
        expected,
        v1.distanceSquaredTo(v2),
        0.0001,
        "Distance squared should be calculated correctly.");
  }

  @Test
  public void testDistanceSquaredToWithZeroVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f v2 = new Vector4f(0, 0, 0, 0);
    float expected = 30.0f; // 1^2 + 2^2 + 3^2 + 4^2 = 1 + 4 + 9 + 16
    assertEquals(
        expected,
        v1.distanceSquaredTo(v2),
        0.0001,
        "Distance squared to zero vector should be correct.");
  }

  @Test
  public void testDistanceSquaredToItself() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    float expected = 0.0f; // Distance to itself is always 0
    assertEquals(
        expected, v1.distanceSquaredTo(v1), 0.0001, "Distance squared to itself should be zero.");
  }

  @Test
  public void testDistanceSquaredToWithNegativeComponents() {
    Vector4f v1 = new Vector4f(-1, -2, -3, -4);
    Vector4f v2 = new Vector4f(-5, -6, -7, -8);
    float expected =
        64.0f; // (-5 - (-1))^2 + (-6 - (-2))^2 + (-7 - (-3))^2 + (-8 - (-4))^2 = 16 + 16 + 16 + 16
    assertEquals(
        expected,
        v1.distanceSquaredTo(v2),
        0.0001,
        "Distance squared with negative components should be correct.");
  }

  @Test
  public void testDistanceSquaredToWithNullOtherVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          v1.distanceSquaredTo(null);
        },
        "Should throw IllegalArgumentException if the other vector is null.");
  }

  // ----------------------------------------------------------------------------------------------
  // Distance To
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testDistanceToWithValidInput() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f v2 = new Vector4f(5, 6, 7, 8);
    float expected =
        (float) Math.sqrt(64.0); // sqrt((5-1)^2 + (6-2)^2 + (7-3)^2 + (8-4)^2) = sqrt(64)
    assertEquals(expected, v1.distanceTo(v2), 0.0001, "Distance should be calculated correctly.");
  }

  @Test
  public void testDistanceToWithZeroVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f v2 = new Vector4f(0, 0, 0, 0);
    float expected = (float) Math.sqrt(30.0); // sqrt(1^2 + 2^2 + 3^2 + 4^2) = sqrt(30)
    assertEquals(expected, v1.distanceTo(v2), 0.0001, "Distance to zero vector should be correct.");
  }

  @Test
  public void testDistanceToItself() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    float expected = 0.0f; // Distance to itself is always 0
    assertEquals(expected, v1.distanceTo(v1), 0.0001, "Distance to itself should be zero.");
  }

  @Test
  public void testDistanceToWithNegativeComponents() {
    Vector4f v1 = new Vector4f(-1, -2, -3, -4);
    Vector4f v2 = new Vector4f(-5, -6, -7, -8);
    float expected = (float) Math.sqrt(64.0); // sqrt((-5 - (-1))^2 + ...) = sqrt(64)
    assertEquals(
        expected,
        v1.distanceTo(v2),
        0.0001,
        "Distance with negative components should be correct.");
  }

  @Test
  public void testDistanceToWithNullOtherVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          v1.distanceTo(null);
        },
        "Should throw IllegalArgumentException if the other vector is null.");
  }

  // ----------------------------------------------------------------------------------------------
  // Angle Between
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testAngleBetweenWithValidVectors() {
    Vector4f v1 = new Vector4f(1, 0, 0, 0);
    Vector4f v2 = new Vector4f(0, 1, 0, 0);

    float expected = (float) (Math.PI / 2); // 90 degrees in radians
    assertEquals(
        expected,
        v1.angleBetween(v2),
        0.0001,
        "Angle between orthogonal vectors should be 90 degrees.");
  }

  @Test
  public void testAngleBetweenWithSameDirection() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f v2 = new Vector4f(1, 2, 3, 4); // Use identical vectors

    float expected = 0.0f; // Angle between identical vectors should be 0 degrees
    assertEquals(
        expected,
        v1.angleBetween(v2),
        0.001,
        "Angle between identical vectors should be 0 degrees.");
  }

  @Test
  public void testAngleBetweenWithOppositeDirection() {
    Vector4f v1 = new Vector4f(1, 0, 0, 0);
    Vector4f v2 = new Vector4f(-1, 0, 0, 0);

    float expected = (float) Math.PI; // 180 degrees in radians
    assertEquals(
        expected,
        v1.angleBetween(v2),
        0.0001,
        "Angle between opposite vectors should be 180 degrees.");
  }

  @Test
  public void testAngleBetweenWithZeroVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f v2 = new Vector4f(0, 0, 0, 0);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          v1.angleBetween(v2);
        },
        "Should throw IllegalArgumentException when the other vector is zero.");
  }

  @Test
  public void testAngleBetweenWithNullVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          v1.angleBetween(null);
        },
        "Should throw IllegalArgumentException when the other vector is null.");
  }

  @Test
  public void testAngleBetweenWithDifferentAngles() {
    Vector4f v1 = new Vector4f(1, 1, 0, 0);
    Vector4f v2 = new Vector4f(1, 0, 0, 0);

    float expected = (float) (Math.PI / 4); // 45 degrees in radians
    assertEquals(
        expected,
        v1.angleBetween(v2),
        0.0001,
        "Angle between vectors at 45 degrees should be correct.");
  }

  // ----------------------------------------------------------------------------------------------
  // Project Onto
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testProjectOntoSameDirection() {
    Vector4f vector1 = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);
    Vector4f vector2 = new Vector4f(6.0f, 8.0f, 0.0f, 0.0f); // Same direction as vector1

    Vector4f projection = vector1.projectOnto(vector2);

    // Dot product of vector1 and vector2: 3*6 + 4*8 = 18 + 32 = 50
    // Dot product of vector2 with itself: 6*6 + 8*8 = 36 + 64 = 100
    // proj_vector2(vector1) = (50 / 100) * vector2 = 0.5 * vector2 = (3, 4, 0, 0)
    Vector4f expected = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f);

    assertEquals(expected, projection, "The projection should match the expected vector.");
  }

  @Test
  public void testProjectOntoPerpendicularVectors() {
    Vector4f vector1 = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f); // Along x-axis
    Vector4f vector2 = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f); // Along y-axis

    Vector4f projection = vector1.projectOnto(vector2);

    // The projection of a vector onto a perpendicular vector should be zero
    Vector4f expected = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    assertEquals(
        expected, projection, "The projection of perpendicular vectors should be a zero vector.");
  }

  @Test
  public void testProjectOntoZeroVector() {
    Vector4f vector1 = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    Vector4f vector2 = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f); // Zero vector

    // Projecting onto a zero vector should throw an exception
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          vector1.projectOnto(vector2);
        },
        "Projecting onto a zero vector should throw an exception.");
  }

  @Test
  public void testProjectOntoNonZeroVector() {
    Vector4f vector1 = new Vector4f(2.0f, 3.0f, 0.0f, 0.0f);
    Vector4f vector2 = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f); // Along x-axis

    Vector4f projection = vector1.projectOnto(vector2);

    // The projection onto the x-axis is just the x-component of vector1
    Vector4f expected = new Vector4f(2.0f, 0.0f, 0.0f, 0.0f);

    assertEquals(expected, projection, "The projection should match the expected x-component.");
  }

  @Test
  public void testProjectOntoNonUnitVector() {
    Vector4f vector1 = new Vector4f(2.0f, 3.0f, 0.0f, 0.0f);
    Vector4f vector2 =
        new Vector4f(4.0f, 6.0f, 0.0f, 0.0f); // Not a unit vector, same direction as vector1

    Vector4f projection = vector1.projectOnto(vector2);

    // Dot product of vector1 and vector2: 2*4 + 3*6 = 8 + 18 = 26
    // Dot product of vector2 with itself: 4*4 + 6*6 = 16 + 36 = 52
    // proj_vector2(vector1) = (26 / 52) * vector2 = 0.5 * vector2 = (2, 3, 0, 0)
    Vector4f expected = new Vector4f(2.0f, 3.0f, 0.0f, 0.0f);

    assertEquals(expected, projection, "The projection should match the expected vector.");
  }

  @Test
  public void testProjectOntoNullThrowsIllegalArgumentException() {
    Vector4f vector1 = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Any non-zero vector

    // Projecting onto a null vector should throw an IllegalArgumentException
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          vector1.projectOnto(null);
        },
        "Projecting onto a null vector should throw an IllegalArgumentException.");
  }

  @Test
  public void testProjectOntoReturnsNewInstanceAndLeavesOriginalUntouched() {
    // Given: Two non-null vectors
    Vector4f vector1 = new Vector4f(3.0f, 4.0f, 0.0f, 0.0f); // Original vector
    Vector4f vector2 = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f); // Vector to project onto

    // When: Project vector1 onto vector2
    Vector4f projected = vector1.projectOnto(vector2);

    // Then: Verify that the returned vector is a new instance
    assertNotSame(vector1, projected);
    assertNotSame(vector2, projected);

    // Verify that the original vectors are untouched
    assertEquals(3.0f, vector1.getX(), "The original vector1's X component should be unchanged.");
    assertEquals(4.0f, vector1.getY(), "The original vector1's Y component should be unchanged.");
    assertEquals(0.0f, vector1.getZ(), "The original vector1's Z component should be unchanged.");
    assertEquals(0.0f, vector1.getW(), "The original vector1's W component should be unchanged.");

    assertEquals(1.0f, vector2.getX(), "The original vector2's X component should be unchanged.");
    assertEquals(0.0f, vector2.getY(), "The original vector2's Y component should be unchanged.");
    assertEquals(0.0f, vector2.getZ(), "The original vector2's Z component should be unchanged.");
    assertEquals(0.0f, vector2.getW(), "The original vector2's W component should be unchanged.");
  }

  // ----------------------------------------------------------------------------------------------
  // Reflect
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testReflectXAxis() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f normal = new Vector4f(1, 0, 0, 0); // Normal of the x-axis
    Vector4f expected = new Vector4f(-1, 2, 3, 4);
    assertEquals(expected, v1.reflect(normal));
  }

  @Test
  public void testReflectYAxis() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f normal = new Vector4f(0, 1, 0, 0); // Normal of the y-axis
    Vector4f expected = new Vector4f(1, -2, 3, 4);
    assertEquals(expected, v1.reflect(normal));
  }

  @Test
  public void testReflectZAxis() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f normal = new Vector4f(0, 0, 1, 0); // Normal of the z-axis
    Vector4f expected = new Vector4f(1, 2, -3, 4);
    assertEquals(expected, v1.reflect(normal));
  }

  @Test
  public void testReflectArbitraryNormal() {
    // Setup
    Vector4f v1 = new Vector4f(1, 1, 1, 1);
    Vector4f normal = new Vector4f(1, 1, 1, 0).normalize();

    // Expected reflection result calculated manually:
    // Reflection formula: v' = v - 2 * (v ⋅ n) * n
    // Dot product (v ⋅ n)
    float dot =
        v1.getX() * normal.getX()
            + v1.getY() * normal.getY()
            + v1.getZ() * normal.getZ()
            + v1.getW() * normal.getW();

    // Scale the normal by 2 * dot
    Vector4f scaledNormal =
        new Vector4f(
            2 * dot * normal.getX(),
            2 * dot * normal.getY(),
            2 * dot * normal.getZ(),
            2 * dot * normal.getW());

    // Subtract scaledNormal from v1 to get the reflected vector
    Vector4f expectedReflection =
        new Vector4f(
            v1.getX() - scaledNormal.getX(),
            v1.getY() - scaledNormal.getY(),
            v1.getZ() - scaledNormal.getZ(),
            v1.getW() - scaledNormal.getW());

    // Actual reflection
    Vector4f reflected = v1.reflect(normal);

    // Assert equality
    assertEquals(expectedReflection.getX(), reflected.getX(), 1e-6);
    assertEquals(expectedReflection.getY(), reflected.getY(), 1e-6);
    assertEquals(expectedReflection.getZ(), reflected.getZ(), 1e-6);
    assertEquals(expectedReflection.getW(), reflected.getW(), 1e-6);
  }

  @Test
  public void testReflectAlongItself() {
    Vector4f v1 = new Vector4f(1, 0, 0, 0);
    Vector4f normal = new Vector4f(1, 0, 0, 0);
    assertEquals(v1.negate(), v1.reflect(normal));
  }

  @Test
  public void testReflectAlongZeroVector() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    Vector4f normal = new Vector4f(0, 0, 0, 0);
    assertThrows(IllegalArgumentException.class, () -> v1.reflect(normal));
  }

  @Test
  public void testReflectAlongNullVectorThrowsIllegalArgumentException() {
    Vector4f v1 = new Vector4f(1, 2, 3, 4);
    assertThrows(IllegalArgumentException.class, () -> v1.reflect(null));
  }

  @Test
  public void testReflectParallelToNormal() {
    // Vector and normal are parallel
    Vector4f v1 = new Vector4f(2, 0, 0, 0);
    Vector4f normal = new Vector4f(1, 0, 0, 0).normalize();

    // Perform reflection
    Vector4f reflected = v1.reflect(normal);

    // Expected result: negation of the vector
    Vector4f expected = new Vector4f(-2, 0, 0, 0);

    // Assert the reflected vector is correct
    assertEquals(
        expected, reflected, "The reflected vector is incorrect when v1 is parallel to normal.");

    // Assert v1 and normal remain unchanged
    Vector4f originalV1 = new Vector4f(2, 0, 0, 0);
    Vector4f originalNormal = new Vector4f(1, 0, 0, 0).normalize();
    assertEquals(originalV1, v1, "The original vector should remain unchanged.");
    assertEquals(originalNormal, normal, "The normal vector should remain unchanged.");
  }

  @Test
  public void testReflectCreatesNewInstanceAndDoesNotChangeOriginalVectorAndInputNormal() {
    // Original vector
    Vector4f v1 = new Vector4f(3, -2, 1, 0);
    // Normal vector
    Vector4f normal = new Vector4f(0, 1, 0, 0).normalize();

    // Store copies of the original vectors for later comparison
    Vector4f originalV1 = new Vector4f(v1.getX(), v1.getY(), v1.getZ(), v1.getW());
    Vector4f originalNormal =
        new Vector4f(normal.getX(), normal.getY(), normal.getZ(), normal.getW());

    // Perform reflection
    Vector4f reflected = v1.reflect(normal);

    // Assert that the returned instance is a new object
    assertNotSame("The method should return a new Vector4f instance.", v1, reflected);

    // Assert the original vector remains unchanged
    assertEquals(originalV1, v1, "The original vector should remain unchanged.");

    // Assert the normal vector remains unchanged
    assertEquals(originalNormal, normal, "The normal vector should remain unchanged.");

    // Optional: Assert that the reflection result is correct
    Vector4f expected = new Vector4f(3, 2, 1, 0); // Reflection around Y-axis
    assertEquals(expected, reflected, "The reflected vector is incorrect.");
  }

  // ----------------------------------------------------------------------------------------------
  // Set Values
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testSetUpdatesVector() {
    // Given: A vector with initial values
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling set to update the vector components
    vector.set(5.0f, 6.0f, 7.0f, 8.0f);

    // Then: The vector's components should match the new values
    assertEquals(5.0f, vector.getX());
    assertEquals(6.0f, vector.getY());
    assertEquals(7.0f, vector.getZ());
    assertEquals(8.0f, vector.getW());
  }

  @Test
  public void testSetToZero() {
    // Given: A vector with non-zero values
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling set to update the vector components to zero
    vector.set(0.0f, 0.0f, 0.0f, 0.0f);

    // Then: The vector's components should be zero
    assertEquals(0.0f, vector.getX());
    assertEquals(0.0f, vector.getY());
    assertEquals(0.0f, vector.getZ());
    assertEquals(0.0f, vector.getW());
  }

  @Test
  public void testSetWithNegativeValues() {
    // Given: A vector with positive values
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling set to update the vector components with negative values
    vector.set(-1.0f, -2.0f, -3.0f, -4.0f);

    // Then: The vector's components should be the negative values
    assertEquals(-1.0f, vector.getX());
    assertEquals(-2.0f, vector.getY());
    assertEquals(-3.0f, vector.getZ());
    assertEquals(-4.0f, vector.getW());
  }

  @Test
  public void testSetSameValues() {
    // Given: A vector with some initial values
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling set with the same values
    vector.set(1.0f, 2.0f, 3.0f, 4.0f);

    // Then: The vector's components should remain unchanged
    assertEquals(1.0f, vector.getX());
    assertEquals(2.0f, vector.getY());
    assertEquals(3.0f, vector.getZ());
    assertEquals(4.0f, vector.getW());
  }

  @Test
  public void testSetWithLargeValues() {
    // Given: A vector with small values
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // When: Calling set to update the vector with large values
    vector.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

    // Then: The vector's components should match the large values
    assertEquals(Float.MAX_VALUE, vector.getX());
    assertEquals(Float.MAX_VALUE, vector.getY());
    assertEquals(Float.MAX_VALUE, vector.getZ());
    assertEquals(Float.MAX_VALUE, vector.getW());
  }

  // ----------------------------------------------------------------------------------------------
  // Clone
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testCloneCreatesIdenticalVector() {
    // Arrange
    Vector4f original = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    Vector4f clone = original.clone();

    // Assert
    assertEquals(original.getX(), clone.getX(), 0.0001f);
    assertEquals(original.getY(), clone.getY(), 0.0001f);
    assertEquals(original.getZ(), clone.getZ(), 0.0001f);
    assertEquals(original.getW(), clone.getW(), 0.0001f);
  }

  @Test
  public void testCloneCreatesNewInstance() {
    // Arrange
    Vector4f original = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    Vector4f clone = original.clone();

    // Assert
    assertNotNull(clone);
    assertNotSame(original, clone); // Ensure it's a new instance
  }

  @Test
  public void testCloneIsIndependentOfOriginal() {
    // Arrange
    Vector4f original = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // Act
    Vector4f clone = original.clone();
    clone.setX(10.0f);

    // Assert
    assertNotEquals(original.getX(), clone.getX()); // Ensure the original vector is not affected
    assertEquals(1.0f, original.getX(), 0.0001f); // Verify the original value remains unchanged
    assertEquals(10.0f, clone.getX(), 0.0001f); // Verify the clone was updated
  }

  // ----------------------------------------------------------------------------------------------
  // Hash Code
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testHashCodeWithSlightlyDifferentVectors() {
    Vector4f v1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f v2 = new Vector4f(1.0001f, 2.0f, 3.0f, 4.0f);

    // Slightly different vectors may or may not have equal hash codes (due to hash function
    // characteristics)
    // This test checks for potential hash code collisions, but doesn't guarantee they won't occur
    assertNotEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  public void testHashCodeConsistency() {
    // Given: A vector4 object
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling hashCode multiple times
    int hash1 = vector.hashCode();
    int hash2 = vector.hashCode();

    // Then: hashCode should return the same value each time for the same object
    assertEquals(hash1, hash2, "hashCode should be consistent for the same object.");
  }

  @Test
  public void testHashCodeEquality() {
    // Given: Two equal vectors
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling hashCode on both vectors
    int hash1 = vector1.hashCode();
    int hash2 = vector2.hashCode();

    // Then: Equal vectors should have the same hashCode
    assertEquals(hash1, hash2, "hashCode should be the same for equal objects.");
  }

  @Test
  public void testHashCodeInequality() {
    // Given: Two different vectors
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // When: Calling hashCode on both vectors
    int hash1 = vector1.hashCode();
    int hash2 = vector2.hashCode();

    // Then: Different vectors should likely have different hashCodes (though not guaranteed)
    assertNotEquals(hash1, hash2, "hashCode should be different for different objects.");
  }

  @Test
  public void testHashCodeWithNullValues() {
    // Given: A vector with zero values
    Vector4f vector1 = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    Vector4f vector2 = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // When: Calling hashCode on both vectors
    int hash1 = vector1.hashCode();
    int hash2 = vector2.hashCode();

    // Then: Two identical vectors should have the same hashCode
    assertEquals(hash1, hash2, "hashCode should be the same for identical vectors.");
  }

  // ----------------------------------------------------------------------------------------------
  // Equals
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testEqualsSameObject() {
    // Given: A vector4 object
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Comparing the vector to itself
    boolean result = vector.equals(vector);

    // Then: The vector should be equal to itself
    assertTrue(result, "A vector should be equal to itself.");
  }

  @Test
  public void testEqualsIdenticalVectors() {
    // Given: Two equal vectors
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Comparing the two vectors
    boolean result = vector1.equals(vector2);

    // Then: The vectors should be equal
    assertTrue(result, "Two vectors with the same components should be equal.");
  }

  @Test
  public void testEqualsDifferentTypes() {
    // Given: A vector4 object and an object of a different type
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    String otherObject = "Not a vector";

    // When: Comparing the vector to the other object
    boolean result = vector.equals((Object) otherObject);

    // Then: The result should be false, since the object is not a Vector4f
    assertFalse(result, "A vector should not be equal to an object of a different type.");
  }

  @Test
  public void testEqualsDifferentVectors() {
    // Given: Two different vectors
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(5.0f, 6.0f, 7.0f, 8.0f);

    // When: Comparing the two vectors
    boolean result = vector1.equals(vector2);

    // Then: The vectors should not be equal
    assertFalse(result, "Two vectors with different components should not be equal.");
  }

  @Test
  public void testEqualsNull() {
    // Given: A vector4 object
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Comparing the vector to null
    boolean result = vector.equals(null);

    // Then: The result should be false, as a vector cannot be equal to null
    assertFalse(result, "A vector should not be equal to null.");
  }

  @Test
  public void testEqualsWithEpsilonTolerance() {
    // Given: Two vectors that are close but slightly different due to precision issues
    Vector4f vector1 = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
    Vector4f vector2 = new Vector4f(1.0000001f, 2.0000001f, 3.0000001f, 4.0000001f);

    // When: Comparing the two vectors with a very small epsilon
    boolean result = vector1.equals(vector2);

    // Then: The result should be false since they are not exactly equal
    assertFalse(result, "Vectors that differ slightly should not be equal without tolerance.");
  }

  // ----------------------------------------------------------------------------------------------
  // To String
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testToStringBasic() {
    // Given: A Vector4f with some standard values
    Vector4f vector = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);

    // When: Calling toString on the vector
    String result = vector.toString();

    // Then: The result should be in the expected format
    assertEquals("Vector4f [x=1.0, y=2.0, z=3.0, w=4.0]", result);
  }

  @Test
  public void testToStringNegativeValues() {
    // Given: A Vector4f with negative values
    Vector4f vector = new Vector4f(-1.0f, -2.0f, -3.0f, -4.0f);

    // When: Calling toString on the vector
    String result = vector.toString();

    // Then: The result should be in the expected format with negative values
    assertEquals("Vector4f [x=-1.0, y=-2.0, z=-3.0, w=-4.0]", result);
  }

  @Test
  public void testToStringZeroValues() {
    // Given: A Vector4f with all zero values
    Vector4f vector = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    // When: Calling toString on the vector
    String result = vector.toString();

    // Then: The result should be in the expected format with zero values
    assertEquals("Vector4f [x=0.0, y=0.0, z=0.0, w=0.0]", result);
  }

  @Test
  public void testToStringLargeValues() {
    // Given: A Vector4f with very large values
    Vector4f vector =
        new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

    // When: Calling toString on the vector
    String result = vector.toString();

    // Then: The result should be in the expected format with large values
    assertEquals(
        "Vector4f [x="
            + Float.MAX_VALUE
            + ", y="
            + Float.MAX_VALUE
            + ", z="
            + Float.MAX_VALUE
            + ", w="
            + Float.MAX_VALUE
            + "]",
        result);
  }

  @Test
  public void testToStringSmallValues() {
    // Given: A Vector4f with very small values
    Vector4f vector =
        new Vector4f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);

    // When: Calling toString on the vector
    String result = vector.toString();

    // Then: The result should be in the expected format with small values
    assertEquals(
        "Vector4f [x="
            + Float.MIN_VALUE
            + ", y="
            + Float.MIN_VALUE
            + ", z="
            + Float.MIN_VALUE
            + ", w="
            + Float.MIN_VALUE
            + "]",
        result);
  }

  @Test
  public void testToStringEmptyVector() {
    // Given: A default Vector4f (all zero values)
    Vector4f vector = new Vector4f();

    // When: Calling toString on the vector
    String result = vector.toString();

    // Then: The result should be in the expected format with zero values
    assertEquals("Vector4f [x=0.0, y=0.0, z=0.0, w=0.0]", result);
  }

  // ----------------------------------------------------------------------------------------------
  // Getter Setter
  // ----------------------------------------------------------------------------------------------

  @Test
  public void testSetAndGetX() {
    Vector4f vector = new Vector4f();
    vector.setX(1.0f);
    assertEquals(1.0f, vector.getX(), 0.0001);

    vector.setX(2.0f);
    assertEquals(2.0f, vector.getX(), 0.0001);
  }

  @Test
  public void testSetAndGetY() {
    Vector4f vector = new Vector4f();
    vector.setY(3.0f);
    assertEquals(3.0f, vector.getY(), 0.0001);

    vector.setY(4.0f);
    assertEquals(4.0f, vector.getY(), 0.0001);
  }

  @Test
  public void testSetAndGetZ() {
    Vector4f vector = new Vector4f();
    vector.setZ(5.0f);
    assertEquals(5.0f, vector.getZ(), 0.0001);

    vector.setZ(6.0f);
    assertEquals(6.0f, vector.getZ(), 0.0001);
  }

  @Test
  public void testSetAndGetW() {
    Vector4f vector = new Vector4f();
    vector.setW(7.0f);
    assertEquals(7.0f, vector.getW(), 0.0001);

    vector.setW(8.0f);
    assertEquals(8.0f, vector.getW(), 0.0001);
  }
}

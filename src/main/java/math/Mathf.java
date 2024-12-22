package math;

import java.util.Random;

/**
 * This class provides a collection of mathematical utility functions that are commonly used in
 * various numerical computations and game development. It offers a range of functionalities,
 * including trigonometric functions, logarithmic functions, rounding, clamping, and random number
 * generation.
 */
public class Mathf {

  /** A random number generator used to generate random values. */
  private static Random random = new Random();

  /** A float representation of the golden ratio, approximately 1.618. */
  public static final float GOLDEN_RATIO = (1 + sqrt(5)) / 2.0f;

  /**
   * A float representation of the reciprocal of the golden ratio, , which is exactly 1 less than
   * the golden ratio itself; approximately 0.618.
   */
  public static final float GOLDEN_RATIO_RECIPROCAL = 2 / (1 + sqrt(5));

  /** Euler's number, the base of the natural logarithm, approximately 2.718. */
  public static final float E = (float) Math.E;

  /** A representation of negative infinity. */
  public static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;

  /** A representation of positive infinity. */
  public static final float POSITIVE_INFINITY = Float.POSITIVE_INFINITY;

  /** The smallest positive nonzero value representable as a float. */
  public static final float MIN_VALUE = Float.MIN_VALUE;

  /** The largest finite value representable as a float. */
  public static final float MAX_VALUE = Float.MAX_VALUE;

  /** A small value used for floating-point comparisons, approximately 2.22E-16. */
  public static final double DBL_EPSILON = 2.220446049250313E-16d;

  /** A small value used for floating-point comparisons, approximately 1.19E-7. */
  public static final float FLT_EPSILON = 1.1920928955078125E-7f;

  /** A small tolerance value for comparing floating-point numbers. */
  public static final float ZERO_TOLERANCE = 0.0001f;

  /** A float representation of one-third, approximately 0.33333334. */
  public static final float ONE_THIRD = 1f / 3f;

  /** The value of Pi, approximately 3.14159. */
  public static final float PI = (float) Math.PI;

  /** Twice the value of Pi, approximately 6.283185. */
  public static final float TWO_PI = 2.0f * PI;

  /** Half the value of Pi, approximately 1.570796. */
  public static final float HALF_PI = 0.5f * PI;

  /** A quarter of the value of Pi, approximately 0.785398. */
  public static final float QUARTER_PI = 0.25f * PI;

  /** The reciprocal of Pi, approximately 0.3183099. */
  public static final float INV_PI = 1.0f / PI;

  /** The reciprocal of two times Pi, approximately 0.1591549. */
  public static final float INV_TWO_PI = 1.0f / TWO_PI;

  /** A factor to convert degrees to radians, approximately 0.0174533. */
  public static final float DEG_TO_RAD = PI / 180.0f;

  /** A factor to convert radians to degrees, approximately 57.29578. */
  public static final float RAD_TO_DEG = 180.0f / PI;

  /**
   * The Tribonacci constant, often denoted as t, is the real root of the cubic equation x³ - x² - x
   * - 1 = 0. It is approximately equal to 1.83928675521416.
   */
  public static final float TRIBONACCI_CONSTANT = 1.83928675521416f;

  /**
   * Converts a 2D index (row, column) into a 1D index for a matrix or array.
   *
   * <p>This method is useful when working with matrices or arrays that are stored in a 1D array. It
   * calculates the 1D index corresponding to the specified row and column in a matrix with the
   * given number of columns.
   *
   * @param rowIndex The zero-based index of the row.
   * @param colIndex The zero-based index of the column.
   * @param numberOfColumns The total number of columns in the matrix.
   * @return The 1D index corresponding to the given row and column.
   * @throws IllegalArgumentException if `rowIndex` or `colIndex` is negative, or if
   *     `numberOfColumns` is less than or equal to zero.
   */
  public static int toOneDimensionalIndex(int rowIndex, int colIndex, int numberOfColumns) {
    if (rowIndex < 0 || colIndex < 0) throw new IllegalArgumentException();

    if (numberOfColumns <= 0) throw new IllegalArgumentException();

    return rowIndex * numberOfColumns + colIndex;
  }

  /**
   * Returns the smaller of two int values. That is, the result is the argument closer to {@link
   * Integer#MIN_VALUE}. If the arguments have the same value, the result is that same value.
   *
   * @param a The first integer.
   * @param b The second integer.
   * @return The smaller of `a` and `b`.
   */
  public static int min(int a, int b) {
    return Math.min(a, b);
  }

  /**
   * Returns the larger of two int values. That is, the result is the argument closer to {@link
   * Integer#MAX_VALUE}. If the arguments have the same value, the result is that same value.
   *
   * @param a The first integer.
   * @param b The second integer.
   * @return The larger of `a` and `b`.
   */
  public static int max(int a, int b) {
    return Math.max(a, b);
  }

  /**
   * Returns the minimum value in the given array.
   *
   * @param values The array of integers.
   * @return The minimum value in the array, or 0 if the array is empty.
   */
  public static int min(int[] values) {
    if (values.length == 0) return 0;

    int min = values[0];
    for (int i = 1; i < values.length; i++) min = Math.min(min, values[i]);
    return min;
  }

  /**
   * Returns the maximum value in the given array.
   *
   * @param values The array of integers.
   * @return The maximum value in the array, or 0 if the array is empty.
   */
  public static int max(int[] values) {
    if (values.length == 0) return 0;

    int max = values[0];
    for (int i = 1; i < values.length; i++) max = Math.max(max, values[i]);
    return max;
  }

  /**
   * Returns the larger of the two given float values.
   *
   * @param a The first float value.
   * @param b The second float value.
   * @return The larger of `a` and `b`.
   */
  public static float max(float a, float b) {
    return Math.max(a, b);
  }

  /**
   * Returns the smaller of the two given float values.
   *
   * @param a The first float value.
   * @param b The second float value.
   * @return The smaller of `a` and `b`.
   */
  public static float min(float a, float b) {
    return Math.min(a, b);
  }

  /**
   * Returns the maximum float value in the given array.
   *
   * @param values The array of float values.
   * @return The maximum value in the array, or {@link Float#NaN} if the array is empty.
   */
  public static float max(float... values) {
    if (values.length == 0) return Float.NaN;

    float max = values[0];
    for (int i = 1; i < values.length; i++) max = Math.max(max, values[i]);
    return max;
  }

  /**
   * Returns the minimum float value in the given array.
   *
   * @param values The array of float values.
   * @return The minimum value in the array, or {@link Float#NaN} if the array is empty.
   */
  public static float min(float... values) {
    if (values.length == 0) return Float.NaN;

    float min = values[0];
    for (int i = 1; i < values.length; i++) min = Math.min(min, values[i]);
    return min;
  }

  /**
   * Rounds a float value to the nearest integer, rounding ties towards positive infinity.
   *
   * @param a The float value to be rounded.
   * @return The rounded integer value.
   */
  public static int roundToInt(float a) {
    return Math.round(a);
  }

  /**
   * Rounds a float value to the nearest integer.
   *
   * <p>This method rounds the given float value to the nearest integer. If the fractional part is
   * 0.5 or greater, the value is rounded up. Otherwise, it is rounded down.
   *
   * @param a The float value to be rounded.
   * @return The rounded float value.
   */
  public static float round(float a) {
    return Math.round(a);
  }

  /**
   * Clamps a value between a minimum and maximum value.
   *
   * @param a The value to clamp.
   * @param min The minimum value.
   * @param max The maximum value-
   * @return The clamped value.
   */
  public static float clamp(float a, float min, float max) {
    return Math.max(min, Math.min(max, a));
  }

  /**
   * Clamps a between min and max and returns the clamped value.
   *
   * @param a The value to clamp
   * @param min The minimum for a.
   * @param max The maximum for a.
   * @return The clamped value.
   */
  public static int clampInt(int a, int min, int max) {
    return a < min ? min : (a > max ? max : a);
  }

  /**
   * Clamps the given float value to be between 0 and 1. This method is equivalent to {@link
   * #saturate(float)}.
   *
   * @param a The value to clamp.
   * @return A clamped value between 0 and 1-
   * @see #saturate(float)
   */
  public static float clamp01(float a) {
    return clamp(a, 0f, 1f);
  }

  /**
   * Converts an angle measured in degrees to an approximately equivalent angle measured in radians.
   * The conversion from degrees to radians is generally inexact; users should not expect
   * cos(toRadians(90.0)) to exactly equal 0.0.
   *
   * @param angdeg The angle, in degrees.
   * @return The angle in radians.
   */
  public static float toRadians(float angdeg) {
    return (float) Math.toRadians((double) angdeg);
  }

  /**
   * Converts an angle measured in radians to an approximately equivalent angle measured in degrees.
   * The conversion from radians to degreees is generally inexact; users should not expect
   * cos(toRadians(90.0)) to exactlyequal 0.0.
   *
   * @param angrad The angle, in radians.
   * @return The angle in degrees.
   */
  public static float toDegrees(float angrad) {
    return (float) Math.toDegrees((double) angrad);
  }

  /**
   * Returns a hash code for a float value; compatible with Float.hashCode().
   *
   * @param value The value to hash.
   * @return A hash code value for a float value.
   */
  public static int hashCode(float value) {
    return Float.hashCode(value);
  }

  /**
   * Returns the absolute value of a.
   *
   * @param a The argument whose absolute value is to be determined.
   * @return The absolute value of the argument.
   */
  public static float abs(float a) {
    return Math.abs(a);
  }

  /**
   * Returns the trigonometric tangent of an angle. Special cases:
   *
   * <ul>
   *   <li>If the argument is NaN or an infinity, then the result is NaN.
   *   <li>If the argument is zero, then the result is a zero with the same sign as the argument.
   *       The computed result must be within 1 ulp of the exact result. Results must be
   *       semi-monotonic.
   *
   * @param a An angle, in radians.
   * @return The tangent of the argument.
   */
  public static float tan(float a) {
    return (float) Math.tan((double) a);
  }

  /**
   * Returns the trigonometric cosine of an angle.
   *
   * <ul>
   *   <li>Special cases: If the argument is NaN or an infinity, then the result is NaN.
   * </ul>
   *
   * @param a An angle, in radians.
   * @return The cosine of the argument.
   */
  public static float cos(float a) {
    return (float) Math.cos((double) a);
  }

  /**
   * Returns the trigonometric sine of an angle. Special cases:
   *
   * <ul>
   *   <li>If the argument is NaN or an infinity, then the result is NaN.
   *   <li>If the argument is zero, then the result is a zero with the same sign as the argument.
   * </ul>
   *
   * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
   *
   * @param a An angle, in radians.
   * @return The sine of the argument.
   */
  public static float sin(float a) {
    return (float) Math.sin((double) a);
  }

  /**
   * Determines whether or not the given value a is in range of min and max.
   *
   * @param a The value to check.
   * @param min The minimum value for a.
   * @param max The maximum value for a.
   * @return true if the value is in range of [min,max], false otherwise.
   */
  public static boolean isInRange(float a, int min, int max) {
    return a >= min && a <= max;
  }

  /**
   * Returns the signum function of the argument; zero if the argument is zero, 1.0f if the argument
   * is greater than zero, -1.0f if the argument is less than zero. Special Cases:
   *
   * <ul>
   *   <li>If the argument is NaN, then the result is NaN.
   *   <li>If the argument is positive zero or negative zero, then the result is the same as the
   *       argument.
   * </ul>
   *
   * @param a The floating-point value whose signum is to be returned.
   * @return The signum function of the argument.
   */
  public static float sign(float a) {
    return Math.signum(a);
  }

  /**
   * Returns the correctly rounded positive square root of a float value. Special cases:
   *
   * <ul>
   *   <li>If the argument is NaN or less than zero, then the result is NaN.
   *   <li>If the argument is positive infinity, then the result is positive infinity.
   *   <li>If the argument is positive zero or negative zero, then the result is the same as the
   *       argument. <br>
   *       Otherwise, the result is the double value closest to the true mathematical square root of
   *       the argument value.
   * </ul>
   *
   * @param a A value.
   * @return The positive square root of a. If the argument is NaN or less than zero, the result is
   *     NaN.
   */
  public static float sqrt(float a) {
    return (float) Math.sqrt((double) a);
  }

  /**
   * Returns the largest (closest to positive infinity) float value that is less than or equal to
   * the argument and is equal to a mathematical integer. Special cases:
   *
   * <ul>
   *   <li>If the argument value is already equal to a mathematical integer, then the result is the
   *       same as the argument.
   *   <li>If the argument is NaN or an infinity or positive zero or negative zero, then the result
   *       is the same as the argument.
   * </ul>
   *
   * @param a A value.
   * @return The largest (closest to positive infinity) floating-point value that less than or equal
   *     to the argument and is equal to a mathematical integer.
   */
  public static float floor(float a) {
    return (float) Math.floor((double) a);
  }

  /**
   * Returns Euler's number e raised to the power of a float value. Special cases:
   *
   * <ul>
   *   <li>If the argument is NaN, the result is NaN.
   *   <li>If the argument is positive infinity, then the result is positive infinity.
   *   <li>If the argument is negative infinity, then the result is positive zero.
   * </ul>
   *
   * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
   *
   * @param a The exponent to raise e to.
   * @return The value e<sup>a</sup>, where e is the base of the natural logarithms.
   */
  public static float exp(float a) {
    return (float) Math.exp((double) a);
  }

  /**
   * Returns true if the argument is a finite floating-point value; returns false otherwise (for NaN
   * and infinity arguments).
   *
   * @param f The float value to be tested.
   * @return true if the argument is a finite floating-point value, false otherwise.
   */
  public static boolean isFinite(float f) {
    return Float.isFinite(f);
  }

  /**
   * Returns true if the specified number is infinitely large in magnitude, false otherwise.
   *
   * @param v The value to be tested.
   * @return true if the argument is positive infinity or negative infinity; false otherwise.
   */
  public static boolean isInfinite(float v) {
    return Float.isInfinite(v);
  }

  /**
   * Returns true if the specified number is a Not-a-Number (NaN) value, false otherwise.
   *
   * @param v The value to be tested.
   * @return true if the argument is NaN; false otherwise.
   */
  public static boolean isNaN(float v) {
    return Float.isNaN(v);
  }

  /**
   * Returns the arc cosine of a (the angle in radians whose cosine is a).
   *
   * @param a The value whose arc cosine is to be returned.
   * @return The arc cosine of a.
   */
  public static float acos(float a) {
    return (float) Math.acos((double) a);
  }

  /**
   * Returns the arc-sine of a - the angle in radians whose sine is a.
   *
   * @param a The value whose arc sine is to be returned.
   * @return The arc sine of the argument.
   */
  public static float asin(float a) {
    return (float) Math.asin((double) a);
  }

  /**
   * Returns the arc tangent of a (the angle in radians whose tangent is a).
   *
   * @param a The value whose arc tangent is to be returned.
   * @return The arc tangent of the argument.
   */
  public static float atan(float a) {
    return (float) Math.atan((double) a);
  }

  /**
   * Returns the angle in radians whose Tan is y/x.
   *
   * <p>Return value is the angle between the x-axis and a 2D vector starting at zero and
   * terminating at (x,y).
   *
   * @param y The ordinate coordinate.
   * @param x The abscissa coordinate.
   * @return The theta component of the point (r, theta) in polar coordinates that corresponds to
   *     the point (x, y) in Cartesian coordinates.
   */
  public static float atan2(float y, float x) {
    return (float) Math.atan2((double) y, (double) x);
  }

  /**
   * Returns the smallest mathematical integer greater to or equal to a.
   *
   * @param a value
   * @return The smallest (closest to negative infinity) floating-point value that is greater than
   *     or equal to the argument and is equal to a mathematical integer.
   */
  public static float ceil(float a) {
    return (float) Math.ceil((double) a);
  }

  /**
   * Returns the value of the first argument raised to the power of the second argument.
   *
   * @param a The base.
   * @param b The exponent.
   * @return The base raised to the power of b.
   * @see Math#pow(double, double)
   */
  public static float pow(float a, float b) {
    return (float) Math.pow((double) a, (double) b);
  }

  /**
   * Returns the base 10 logarithm of a double value. Special cases:
   *
   * <ul>
   *   <li>If the argument is NaN or less than zero, then the result is NaN.
   *   <li>If the argument is positive infinity, then the result is positive infinity.
   *   <li>If the argument is positive zero or negative zero, then the result is negative infinity.
   * </ul>
   *
   * @param a A value.
   * @return The base 10 logarithm of a.
   */
  public static float log10(float a) {
    return (float) Math.log10((double) a);
  }

  /**
   * Returns the natural logarithm (base e) of a float value. Special cases:
   *
   * <ul>
   *   <li>If the argument is NaN or less than zero, then the result is NaN.
   *   <li>If the argument is positive infinity, then the result is positive infinity.
   *   <li>If the argument is positive zero or negative zero, then the result is negative infinity.
   * </ul>
   *
   * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
   *
   * @param a A value.
   * @return The value ln a, the natural logarithm of a.
   */
  public static float log(float a) {
    return (float) Math.log((double) a);
  }

  /**
   * Returns the largest (closest to positive infinity) integer value that is less than or equal to
   * the argument.
   *
   * <ul>
   *   <li>If the argument value is already equal to a mathematical integer, then the result is the
   *       same as the argument.
   *   <li>If the argument is NaN or an infinity or positive zero or negative zero, then the result
   *       is the same as the argument.
   * </ul>
   *
   * @param a A value.
   * @return The largest (closest to positive infinity) integer value that is less than or equal to
   *     the argument.
   */
  public static int floorToInt(float a) {
    return (int) Math.floor((double) a);
  }

  /**
   * Returns the smallest mathematical integer greater to or equal to a.
   *
   * @param a The value to ceil.
   * @return The smallest (closest to negative infinity) integer value that is greater than or equal
   *     to the argument and is equal to a mathematical integer.
   */
  public static int ceilToInt(float a) {
    return (int) Math.ceil((double) a);
  }

  /**
   * Linearly interpolates between a and b by t. The parameter t is not clamped and values outside
   * the range [0, 1] will result in a return value outside the range [a, /b/].
   *
   * <pre>
   * When t = 0 returns a.
   * When t = 1 returns b.
   * When t = 0.5 returns the midpoint of a and b.
   * </pre>
   *
   * @param a The value to interpolate from.
   * @param b The value to interpolate to.
   * @param t The value to interpolate by.
   * @return The resultant interpolated value.
   */
  public static float lerpUnclamped(float a, float b, float t) {
    return a + (b - a) * t;
  }

  /**
   * Linearly interpolates between from and to by t. The parameter t is clamped to the range [0, 1].
   *
   * <pre>
   * When t = 0 returns a.
   * When t = 1 return b.
   * When t = 0.5 returns the midpoint of a and b.
   * </pre>
   *
   * @param from The value to interpolate from.
   * @param to The value to interpolate to.
   * @param t The value to interpolate by.
   * @return The resultant interpolated value.
   */
  public static float lerp(float from, float to, float t) {
    return from + (to - from) * clamp01(t);
  }

  /**
   * Returns the next power of two greater than or equal to the given value.
   *
   * <p>For example:
   *
   * <ul>
   *   <li>nextPowerOfTwo(1) returns 2
   *   <li>nextPowerOfTwo(2) returns 2
   *   <li>nextPowerOfTwo(3) returns 4
   *   <li>nextPowerOfTwo(16) returns 16
   *   <li>nextPowerOfTwo(17) returns 32
   * </ul>
   *
   * @param value the input value
   * @return the next power of two greater than or equal to the input value
   */
  public static int nextPowerOfTwo(int value) {
    return value > 0 ? Integer.highestOneBit(value - 1) << 1 : 1;
  }

  /**
   * Smoothly interpolates between two values. This function provides a smoother transition between
   * the two values compared to linear interpolation. It uses a cubic Hermite spline to achieve a
   * smooth curve.
   *
   * @param from The starting value.
   * @param to The ending value.
   * @param t The interpolation factor, clamped to the range [0, 1].
   * @return The interpolated value.
   */
  public static float smoothStep(float from, float to, float t) {
    t = clamp01(t);
    t = -2f * t * t * t + 3f * t * t;
    return to * t + from * (1f - t);
  }

  /**
   * Returns a random float value between the specified minimum and maximum values, inclusive.
   *
   * @param min The minimum value.
   * @param max The maximum value.
   * @return A random float value between min and max, inclusive.
   */
  public static float random(float min, float max) {
    return random.nextFloat() * (max - min) + min;
  }

  /**
   * Returns a random integer value between the specified minimum and maximum values, inclusive.
   *
   * @param min The minimum value.
   * @param max The maximum value.
   * @return A random integer value between min and max, inclusive.
   */
  public static int random(int min, int max) {
    return random.nextInt(max - min + 1) + min;
  }

  /**
   * Sets the seed for the random number generator. This allows for reproducible random sequences.
   *
   * @param seed The seed value.
   */
  public static void setSeed(long seed) {
    random.setSeed(seed);
  }

  /**
   * Returns a random float value between 0.0 (inclusive) and 1.0 (exclusive).
   *
   * <p>This method uses a random number generator with a specified seed to ensure reproducibility.
   *
   * @return A random float value between 0.0 (inclusive) and 1.0 (exclusive).
   */
  public static float randomFloat() {
    return random.nextFloat();
  }

  /**
   * Calculates a smooth, oscillating value between 0 and `length` over time `t`.
   *
   * <p>This function is commonly used in game development to create various effects, such as
   * character movement, object animations, camera effects, and particle systems.
   *
   * <p>The function works by repeating the input time `t` over an interval of `length * 2`, and
   * then calculating the distance between the repeated time and the midpoint `length`. This
   * distance is then subtracted from `length` to produce the final oscillating value.
   *
   * @param t The input time.
   * @param length The desired range of oscillation.
   * @return The calculated oscillating value.
   */
  public static float pingPong(float t, float length) {
    t = repeat(t, length * 2f);
    return length - abs(t - length);
  }

  /**
   * Normalizes an angle to a specific range centered around a given center angle.
   *
   * <p>This method ensures that the returned angle is within a specific range, typically between -π
   * and π or 0 and 2π.
   *
   * @param a The angle to be normalized.
   * @param center The center angle of the desired range.
   * @return The normalized angle.
   */
  public static float normalizeAngle(float a, float center) {
    return a - TWO_PI * floor((a + PI - center) / TWO_PI);
  }

  /**
   * Wraps a value cyclically within a specified range.
   *
   * <p>This method takes a value `t` and maps it to a value within the interval [0, length). The
   * value is repeatedly decreased by `length` until it becomes less than `length`. This creates a
   * cyclic effect, where the value continuously cycles from 0 to `length` and then back to 0.
   *
   * <p>**Example:** For `t = 12` and `length = 5`, the result is: - `floor(12 / 5) = 2` (number of
   * full cycles) - `2 * 5 = 10` (value exceeding the range) - `12 - 10 = 2` (the returned value)
   *
   * @param t The value to be wrapped.
   * @param length The length of the interval within which the value is wrapped.
   * @return The wrapped value within the interval [0, length).
   */
  public static float repeat(float t, float length) {
    return t - floor(t / length) * length;
  }

  /**
   * Determines if two floating-point numbers are approximately equal.
   *
   * <p>This method compares two floating-point numbers, `a` and `b`, considering the limited
   * precision of floating-point numbers. It accounts for both relative and absolute tolerances to
   * provide a robust comparison method.
   *
   * <p>**How it works:** 1. **Calculates absolute difference:** The absolute difference between `a`
   * and `b` is calculated. 2. **Determines relative tolerance:** The larger of the two absolute
   * values of `a` and `b` is multiplied by a small factor (e.g., 1e-6) to obtain a relative
   * tolerance. 3. **Determines absolute tolerance:** A small fixed value (e.g., `FLT_EPSILON * 8`)
   * is set as the absolute tolerance. 4. **Comparison:** The absolute difference is compared with
   * the larger of the two tolerances. If the difference is smaller, the numbers are considered
   * approximately equal.
   *
   * <p>**Why such a method is necessary:** Due to the limited precision of floating-point numbers,
   * small rounding errors can occur, causing two mathematically equal values to not be represented
   * exactly equal in a computer. This method allows ignoring such small differences.
   *
   * @param a The first floating-point number.
   * @param b The second floating-point number.
   * @return `true` if `a` and `b` are approximately equal, otherwise `false`.
   */
  public static boolean approximately(float a, float b) {
    return abs(b - a) < max(1E-06f * max(abs(a), abs(b)), FLT_EPSILON * 8f);
  }

  /**
   * Clamps the given float value to be between 0 and 1. This method is equivalent to {@link
   * #clamp01(float)}.
   *
   * @param a The value to clamp.
   * @return A clamped between 0 and 1.
   * @see #clamp01(float)
   */
  public static float saturate(float a) {
    return clamp(a, 0f, 1f);
  }

  /**
   * Returns the next power of two of the given value.
   *
   * <p>E.g. for a value of 100, this returns 128. Returns 1 for all numbers <= 1.
   *
   * @param value The number to obtain the power of two for.
   * @return The closest power of two.
   */
  public static int closestPowerOfTwo(int value) {
    value--;
    value |= value >> 1;
    value |= value >> 2;
    value |= value >> 4;
    value |= value >> 8;
    value |= value >> 16;
    value++;
    value += (value == 0) ? 1 : 0;
    return value;
  }

  /**
   * Maps a value from one range to another using linear interpolation.
   *
   * @param value The value to be mapped.
   * @param from0 The lower bound of the input range.
   * @param to0 The upper bound of the input range.
   * @param from1 The lower bound of the output range.
   * @param to1 The upper bound of the output range.
   * @return The mapped value.
   * @throws IllegalArgumentException if `from0 == to0` or `from1 == to1`.
   */
  public static float map(float value, float from0, float to0, float from1, float to1) {
    if (from0 == to0 || from1 == to1) {
      throw new IllegalArgumentException("Invalid input ranges");
    }

    float result = from1 + (to1 - from1) * ((value - from0) / (to0 - from0));

    if (Float.isNaN(result)) {
      throw new IllegalArgumentException("Result is NaN");
    } else if (Float.isInfinite(result)) {
      throw new IllegalArgumentException("Result is infinite");
    }

    return result;
  }

  /**
   * Calculates the floating-point remainder of dividing two values. This method works similarly to
   * the fmod function in other programming languages.
   *
   * @param a the dividend
   * @param b the divisor
   * @return the remainder when a is divided by b
   */
  public static float fmod(float a, float b) {
    return (float) (a - b * Math.floor(a / b));
  }

  /**
   * Normalizes the input angle to the range [0, 2π] in radians.
   *
   * <p>Small values close to zero (less than 1e-6) are snapped to zero to handle floating-point
   * precision issues.
   *
   * @param angle The input angle in radians.
   * @return The normalized angle in the range [0, 2π].
   */
  public static float normalizeAngle(float angle) {
    float smallAngleThreshold = 1e-6f;

    angle = angle % (2 * Mathf.PI);

    if (Mathf.abs(angle) < smallAngleThreshold) {
      angle = 0;
    }

    if (angle < 0) {
      angle += 2 * Mathf.PI;
    }

    return angle;
  }
}

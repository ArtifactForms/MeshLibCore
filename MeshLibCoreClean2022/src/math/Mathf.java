package math;

/**
 * A collection of common math functions. This math utility class has its focus
 * on float operations.
 * 
 * @author Simon Dietz
 * @version 0.6, 9 June 2016
 */
public class Mathf {
	
	/**
	 * A float representation of the golden ratio.
	 */
	public static final float GOLDEN_RATIO = (float) (1 + Math.sqrt(5)) / 2;

	/**
	 * A float representation of the "Euler's number".
	 */
	public static final float E = (float) Math.E;

	/**
	 * A representation of negative infinity.
	 */
	public static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;

	/**
	 * A representation of positive infinity.
	 */
	public static final float POSITIVE_INFINITY = Float.POSITIVE_INFINITY;

	/**
	 * The minimum float value.
	 */
	public static final float MIN_VALUE = Float.MIN_VALUE;

	/**
	 * The maximum float value.
	 */
	public static final float MAX_VALUE = Float.MAX_VALUE;

	/**
	 * A tiny double value. ("close to zero")
	 */
	public static final double DBL_EPSILON = 2.220446049250313E-16d;

	/**
	 * A tiny float value. ("close to zero")
	 */
	public static final float FLT_EPSILON = 1.1920928955078125E-7f;

	/**
	 * A float epsilon value ("close to zero").
	 * 
	 */
	public static final float ZERO_TOLERANCE = 0.0001f;

	/**
	 * A float representation of 1 / 3.
	 */
	public static final float ONE_THIRD = 1f / 3f;

	/**
	 * The value PI as a float. (180 degrees)
	 */
	public static final float PI = (float) Math.PI;

	/**
	 * The value 2PI as a float. (360 degrees)
	 */
	public static final float TWO_PI = 2.0f * PI;

	/**
	 * The value PI/2 as a float. (90 degrees)
	 */
	public static final float HALF_PI = 0.5f * PI;

	/**
	 * The value PI/4 as a float. (45 degrees)
	 */
	public static final float QUARTER_PI = 0.25f * PI;

	/**
	 * The value 1/PI as a float.
	 */
	public static final float INV_PI = 1.0f / PI;

	/**
	 * The value 1/(2PI) as a float.
	 */
	public static final float INV_TWO_PI = 1.0f / TWO_PI;

	/**
	 * A value to multiply a degree value by, to convert it to radians.
	 */
	public static final float DEG_TO_RAD = PI / 180.0f;

	/**
	 * A value to multiply a radian value by, to convert it to degrees.
	 */
	public static final float RAD_TO_DEG = 180.0f / PI;
	
	public static final float TRIBONACCI_CONSTANT = 1.8392868f;

	/**
	 * Converts a 2D index (row, column) into a 1D index for a matrix or array.
	 *
	 * <p>This method is useful when working with matrices or arrays that are stored
	 * in a 1D array. It calculates the 1D index corresponding to the specified
	 * row and column in a matrix with the given number of columns.
	 *
	 * @param rowIndex The zero-based index of the row.
	 * @param colIndex The zero-based index of the column.
	 * @param numberOfColumns The total number of columns in the matrix.
	 * @return The 1D index corresponding to the given row and column.
	 *
	 * @throws IllegalArgumentException if `rowIndex` or `colIndex` is negative, or if
	 *     `numberOfColumns` is less than or equal to zero.
	 */
	public static int toOneDimensionalIndex(int rowIndex, int colIndex, int numberOfColumns) {
		return rowIndex * numberOfColumns + colIndex;
	}
	
	/**
	 * Re-maps a float value from one range to another.
	 * 
	 * @param value
	 *            the value to be converted
	 * @param from0
	 *            lower bound of the value's current range
	 * @param to0
	 *            upper bound of the value's current range
	 * @param from1
	 *            lower bound of the value's target range
	 * @param to1
	 *            upper bound of the value's target range
	 * @return the re-mapped value
	 */
	static public final float map(float value, float from0, float to0,
			float from1, float to1) {
		float result = from1 + (to1 - from1)
				* ((value - from0) / (to0 - from0));
		if (result != result) {
			// TODO throw exception NaN
		} else if (result == Float.NEGATIVE_INFINITY
				|| result == Float.POSITIVE_INFINITY) {
			// TODO throw exception
		}
		return result;
	}

	// public static extern float GammaToLinearSpace(float value);
	// public static extern float LinearToGammaSpace(float value);
	// public static extern float PerlinNoise(float x, float y);

	public static int nextPowerOfTwo(int value) {
		// Taken from:
		// http://stackoverflow.com/questions/7685838/how-to-obtain-the-next-power-of-two-of-a-given-number
		return Integer.highestOneBit(value) << 1;
	}

	// TODO javadoc
	public static int nearestPowerOfTwoExponent(int value) {
		// Taken from:
		// http://stackoverflow.com/questions/5242533/fast-way-to-find-exponent-of-nearest-superior-power-of-2
		return value == 0 ? 0 : 32 - Integer.numberOfLeadingZeros(value - 1);
	}

	// TODO javadoc
	public static float smoothDamp(float current, float target,
			FloatValue currentVelocity, float smoothTime, float maxSpeed) {
		float deltaTime = Time.deltaTime;
		return Mathf.smoothDamp(current, target, currentVelocity, smoothTime,
				maxSpeed, deltaTime);
	}

	// TODO javadoc
	public static float smoothDamp(float current, float target,
			FloatValue currentVelocity, float smoothTime) {
		float deltaTime = Time.deltaTime;
		float maxSpeed = Float.POSITIVE_INFINITY;
		return Mathf.smoothDamp(current, target, currentVelocity, smoothTime,
				maxSpeed, deltaTime);
	}

	// TODO javadoc
	public static float smoothDamp(float current, float target,
			FloatValue currentVelocity, float smoothTime, float maxSpeed,
			float deltaTime) {
		smoothTime = Mathf.max(0.0001f, smoothTime);
		float num = 2f / smoothTime;
		float num2 = num * deltaTime;
		float num3 = 1f / (1f + num2 + 0.48f * num2 * num2 + 0.235f * num2
				* num2 * num2);
		float num4 = current - target;
		float num5 = target;
		float num6 = maxSpeed * smoothTime;
		num4 = Mathf.clamp(num4, -num6, num6);
		target = current - num4;
		float num7 = (currentVelocity.value + num * num4) * deltaTime;
		currentVelocity.value = (currentVelocity.value - num * num7) * num3;
		float num8 = target + (num4 + num7) * num3;
		if (num5 - current > 0f == num8 > num5) {
			num8 = num5;
			currentVelocity.value = (num8 - num5) / deltaTime;
		}
		return num8;
	}

	// TODO javadoc
	public static float smoothDampAngle(float current, float target,
			FloatValue currentVelocity, float smoothTime, float maxSpeed) {
		float deltaTime = Time.deltaTime;
		return Mathf.smoothDampAngle(current, target, currentVelocity,
				smoothTime, maxSpeed, deltaTime);
	}

	// TODO javadoc
	public static float smoothDampAngle(float current, float target,
			FloatValue currentVelocity, float smoothTime) {
		float deltaTime = Time.deltaTime;
		float maxSpeed = Float.POSITIVE_INFINITY;
		return Mathf.smoothDampAngle(current, target, currentVelocity,
				smoothTime, maxSpeed, deltaTime);
	}

	// TODO javadoc
	public static float smoothDampAngle(float current, float target,
			FloatValue currentVelocity, float smoothTime, float maxSpeed,
			float deltaTime) {
		target = current + Mathf.deltaAngle(current, target);
		return Mathf.smoothDamp(current, target, currentVelocity, smoothTime,
				maxSpeed, deltaTime);
	}

	// TODO javadoc
	public static float smoothStep(float from, float to, float t) {
		t = Mathf.clamp01(t);
		t = -2f * t * t * t + 3f * t * t;
		return to * t + from * (1f - t);
	}

	// TODO javadoc
	public static float gamma(float value, float absmax, float gamma) {
		boolean flag = false;
		if (value < 0f) {
			flag = true;
		}
		float num = abs(value);
		if (num > absmax) {
			return (!flag) ? num : (-num);
		}
		float num2 = pow(num / absmax, gamma) * absmax;
		return (!flag) ? num2 : (-num2);
	}

	// TODO javadoc
	public static float moveTowards(float current, float target, float maxDelta) {
		if (abs(target - current) <= maxDelta) {
			return target;
		}
		return current + sign(target - current) * maxDelta;
	}

	// TODO javadoc
	public static float moveTowardsAngle(float current, float target,
			float maxDelta) {
		target = current + deltaAngle(current, target);
		return moveTowards(current, target, maxDelta);
	}

	// TODO javadoc
	public static float inverseLerp(float from, float to, float value) {
		if (from < to) {
			if (value < from) {
				return 0f;
			}
			if (value > to) {
				return 1f;
			}
			value -= from;
			value /= to - from;
			return value;
		} else {
			if (from <= to) {
				return 0f;
			}
			if (value < to) {
				return 1f;
			}
			if (value > from) {
				return 0f;
			}
			return 1f - (value - to) / (from - to);
		}
	}

	// TODO javadoc
	public static float pingPong(float t, float length) {
		t = Mathf.repeat(t, length * 2f);
		return length - Mathf.abs(t - length);
	}

	// TODO javadoc
	public static float repeat(float t, float length) {
		return t - Mathf.floor(t / length) * length;
	}

	// TODO javadoc
	public static boolean approximately(float a, float b) {
		return abs(b - a) < max(1E-06f * max(abs(a), abs(b)), FLT_EPSILON * 8f);
	}

	/**
	 * Returns a hash code for a float value; compatible with Float.hashCode().
	 * 
	 * @param value
	 *            the value to hash
	 * @return a hash code value for a float value
	 */
	public static int hashCode(float value) {
		return Float.hashCode(value);
	}

	/**
	 * Returns true if the argument is a finite floating-point value; returns
	 * false otherwise (for NaN and infinity arguments).
	 * 
	 * @param f
	 *            the float value to be tested
	 * @return true if the argument is a finite floating-point value, false
	 *         otherwise
	 */
	public static boolean isFinite(float f) {
		return Float.isFinite(f);
	}

	/**
	 * Returns true if the specified number is infinitely large in magnitude,
	 * false otherwise.
	 * 
	 * @param v
	 *            the value to be tested
	 * @return true if the argument is positive infinity or negative infinity;
	 *         false otherwise
	 */
	public static boolean isInfinite(float v) {
		return Float.isInfinite(v);
	}

	/**
	 * Returns true if the specified number is a Not-a-Number (NaN) value, false
	 * otherwise.
	 * 
	 * @param v
	 *            the value to be tested
	 * @return true if the argument is NaN; false otherwise.
	 */
	public static boolean isNaN(float v) {
		return Float.isNaN(v);
	}

	/**
	 * Normalizes an angle between 0 and 2&pi; around a center.
	 * 
	 * @param a
	 *            the angle to normalize in radians
	 * @param center
	 *            the center of the desired interval [0,2&pi;]
	 * @return the normalized angle
	 */
	public static float normalizeAngle(float a, float center) {
		return a - TWO_PI * floor((a + PI - center) / TWO_PI);
	}

	/**
	 * Returns the absolute value of a.
	 * 
	 * @param a
	 *            the argument whose absolute value is to be determined
	 * @return the absolute value of the argument
	 */
	public static float abs(float a) {
		return Math.abs(a);
	}

	/**
	 * Returns the arc cosine of a (the angle in radians whose cosine is a).
	 * 
	 * @param a
	 *            the value whose arc cosine is to be returned
	 * @return the arc cosine of a
	 */
	public static float acos(float a) {
		return (float) Math.acos((double) a);
	}

	/**
	 * Returns the arc-sine of a - the angle in radians whose sine is a.
	 * 
	 * @param a
	 *            the value whose arc sine is to be returned
	 * @return the arc sine of the argument
	 */
	public static float asin(float a) {
		return (float) Math.asin((double) a);
	}

	/**
	 * Returns the arc tangent of a (the angle in radians whose tangent is a).
	 * 
	 * @param a
	 *            the value whose arc tangent is to be returned
	 * @return the arc tangent of the argument
	 */
	public static float atan(float a) {
		return (float) Math.atan((double) a);
	}

	/**
	 * Returns the angle in radians whose Tan is y/x.
	 * 
	 * Return value is the angle between the x-axis and a 2D vector starting at
	 * zero and terminating at (x,y).
	 * 
	 * @param y
	 *            the ordinate coordinate
	 * @param x
	 *            the abscissa coordinate
	 * @return the theta component of the point (r, theta) in polar coordinates
	 *         that corresponds to the point (x, y) in Cartesian coordinates
	 */
	public static float atan2(float y, float x) {
		return (float) Math.atan2((double) y, (double) x);
	}

	/**
	 * Returns the smallest mathematical integer greater to or equal to a.
	 * 
	 * @param a
	 *            value
	 * @return the smallest (closest to negative infinity) floating-point value
	 *         that is greater than or equal to the argument and is equal to a
	 *         mathematical integer
	 */
	public static float ceil(float a) {
		return (float) Math.ceil((double) a);
	}

	/**
	 * Returns the smallest mathematical integer greater to or equal to a.
	 * 
	 * @param a
	 *            value
	 * @return the smallest (closest to negative infinity) integer value that is
	 *         greater than or equal to the argument and is equal to a
	 *         mathematical integer
	 */
	public static int ceilToInt(float a) {
		return (int) Math.ceil((double) a);
	}

	/**
	 * Clamps a between min and max and returns the clamped value.
	 * 
	 * @param a
	 *            the value to clamp
	 * @param min
	 *            the minimum for a
	 * @param max
	 *            the maximum for a
	 * @return the clamped value
	 */
	public static float clamp(float a, float min, float max) {
		a = a < min ? min : (a > max ? max : a);
		return a;
	}

	/**
	 * Clamps a between min and max and returns the clamped value.
	 * 
	 * @param a
	 *            the value to clamp
	 * @param min
	 *            the minimum for a
	 * @param max
	 *            the maximum for a
	 * @return the clamped value
	 */
	public static int clampInt(int a, int min, int max) {
		a = a < min ? min : (a > max ? max : a);
		return a;
	}

	/**
	 * Clamps the given float value to be between 0 and 1. This method is
	 * equivalent to {@link #saturate(float)}.
	 * 
	 * @param a
	 *            the value to clamp
	 * @return a clamped between 0 and 1
	 * @see #saturate(float)
	 */
	public static float clamp01(float a) {
		return clamp(a, 0f, 1f);
	}

	/**
	 * Clamps the given float value to be between 0 and 1. This method is
	 * equivalent to {@link #clamp01(float)}.
	 * 
	 * @param a
	 *            the value to clamp
	 * @return a clamped between 0 and 1
	 * @see #clamp01(float)
	 */
	public static float saturate(float a) {
		return clamp(a, 0f, 1f);
	}

	/**
	 * Returns the next power of two of the given value.
	 * 
	 * E.g. for a value of 100, this returns 128. Returns 1 for all numbers <=
	 * 1.
	 * 
	 * @param value
	 *            the number to obtain the power of two for
	 * @return the next power of two
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
	 * Converts an angle measured in degrees to an approximately equivalent
	 * angle measured in radians. The conversion from degrees to radians is
	 * generally inexact.
	 * 
	 * @param angdeg
	 *            the angle, in degrees
	 * @return the angle in radians
	 */
	public static float toRadians(float angdeg) {
		return (float) Math.toRadians((double) angdeg);
	}

	/**
	 * Converts an angle measured in radians to an approximately equivalent
	 * angle measured in degrees. The conversion from radians to degreees is
	 * generally inexact.
	 * 
	 * @param angrad
	 *            the angle, in radians
	 * @return the angle in degrees
	 */
	public static float toDegrees(float angrad) {
		return (float) Math.toDegrees((double) angrad);
	}

	/**
	 * Returns the trigonometric cosine of an angle.
	 * <ul>
	 * <li>Special cases: If the argument is NaN or an infinity, then the result
	 * is NaN.</li>
	 * </ul>
	 * 
	 * @param a
	 *            an angle, in radians
	 * @return the cosine of the argument
	 */
	public static float cos(float a) {
		return (float) Math.cos((double) a);
	}

	/**
	 * Calculates the shortest difference between two given angles given in
	 * degrees.
	 * 
	 * @param current
	 *            the source angle in degrees
	 * @param target
	 *            the target angle in degrees
	 * @return the shortest difference between the given angles
	 */
	public static float deltaAngle(float current, float target) {
		// Taken from:
		// http://stackoverflow.com/questions/1878907/the-smallest-difference-between-2-angles
		// float a = target - current;
		// a -= a > 180 ? 360 : 0;
		// a += a < -180 ? 360 : 0;
		// return a;
		// FIXED new version
		float num = Mathf.repeat(target - current, 360f);
		if (num > 180f) {
			num -= 360f;
		}
		return num;
	}

	/**
	 * Returns Euler's number e raised to the power of a float value. Special
	 * cases:
	 * <ul>
	 * <li>If the argument is NaN, the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive
	 * infinity.</li>
	 * <li>If the argument is negative infinity, then the result is positive
	 * zero.</li>
	 * </ul>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param a
	 *            the exponent to raise e to
	 * @return the value e<sup>a</sup>, where e is the base of the natural
	 *         logarithms
	 */
	public static float exp(float a) {
		return (float) Math.exp((double) a);
	}

	/**
	 * Returns the largest (closest to positive infinity) float value that is
	 * less than or equal to the argument and is equal to a mathematical
	 * integer. Special cases:
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer,
	 * then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative
	 * zero, then the result is the same as the argument.</li>
	 * </ul>
	 * 
	 * @param a
	 *            a value
	 * @return the largest (closest to positive infinity) floating-point value
	 *         that less than or equal to the argument and is equal to a
	 *         mathematical integer
	 */
	public static float floor(float a) {
		return (float) Math.floor((double) a);
	}

	/**
	 * Returns the largest (closest to positive infinity) integer value that is
	 * less than or equal to the argument.
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer,
	 * then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative
	 * zero, then the result is the same as the argument.</li>
	 * </ul>
	 * 
	 * @param a
	 *            a value
	 * @return the largest (closest to positive infinity) integer value that is
	 *         less than or equal to the argument
	 */
	public static int floorToInt(float a) {
		return (int) Math.floor((double) a);
	}

	/**
	 * Returns true if the number is a power of 2 (2, 4, 8, 16, ...). This
	 * method considers that the given number is positive.
	 * 
	 * @param number
	 *            the number to test
	 * @return true if the number is a power of two, false otherwise
	 */
	public static boolean isPowerOfTwo(int number) {
		// Using the fast algorithm to check if a positive number is a power of
		// two. Taken from:
		// http://stackoverflow.com/questions/600293/how-to-check-if-a-number-is-a-power-of-2
		return (number != 0) && (number & (number - 1)) == 0;
	}
	
	public static float lerp(float[] array, float step) {
		int sz = array.length;

		if (sz == 1 || step <= 0) {
			return array[0];
		} else if (step >= 1) {
			return array[sz - 1];
		}

		float scaledStep = step * (sz - 1f);
		int i = (int) (scaledStep);

		return Mathf.lerp(array[i], array[i + 1], scaledStep - i);
	}

	/**
	 * Linearly interpolates between from and to by t. The parameter t is
	 * clamped to the range [0, 1].
	 * 
	 * <pre>
	 * When t = 0 returns a. 
	 * When t = 1 return b. 
	 * When t = 0.5 returns the midpoint of a and b.
	 * </pre>
	 * 
	 * @param from
	 *            the value to interpolate from
	 * @param to
	 *            the value to interpolate to
	 * @param t
	 *            the value to interpolate by
	 * @return the resultant interpolated value
	 */
	public static float lerp(float from, float to, float t) {
		return from + (to - from) * clamp01(t);
	}

	/**
	 * Same as {@link #lerp(float, float, float)} but makes sure the values
	 * interpolate correctly when they wrap around 360 degrees. The parameter t
	 * is clamped to the range [0, 1]. Variables a and b are assumed to be in
	 * degrees.
	 * 
	 * @param a
	 *            the start angle in degrees
	 * @param b
	 *            the end angle in degrees
	 * @param t
	 *            the amount
	 * @return the resultant interpolated value
	 */
	public static float lerpAngle(float a, float b, float t) {
		// Also known as lerpDegrees
		// Taken from:
		// http://stackoverflow.com/questions/2708476/rotation-interpolation
		// float shortestAngle = ((((b - a) % 360) + 540) % 360) - 180;
		// return shortestAngle * clamp01(t);
		// New version
		float num = repeat(b - a, 360f);
		if (num > 180f) {
			num -= 360f;
		}
		return a + num * clamp01(t);
	}

	/**
	 * Linearly interpolates between a and b by t. The parameter t is not
	 * clamped and values outside the range [0, 1] will result in a return value
	 * outside the range [a, /b/].
	 * 
	 * <pre>
	 * When t = 0 returns a.
	 * When t = 1 returns b.
	 * When t = 0.5 returns the midpoint of a and b.
	 * </pre>
	 * 
	 * @param a
	 *            the value to interpolate from
	 * @param b
	 *            the value to interpolate to
	 * @param t
	 *            the value to interpolate by
	 * @return the resultant interpolated value
	 */
	public static float lerpUnclamped(float a, float b, float t) {
		return a + (a - b) * t;
	}

	/**
	 * Returns the natural logarithm (base e) of a float value. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive
	 * infinity.</li>
	 * <li>If the argument is positive zero or negative zero, then the result is
	 * negative infinity.</li>
	 * </ul>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param a
	 *            a value
	 * @return the value ln a, the natural logarithm of a
	 */
	public static float log(float a) {
		return (float) Math.log((double) a);
	}

	/**
	 * Returns the base 10 logarithm of a double value. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive
	 * infinity.</li>
	 * <li>If the argument is positive zero or negative zero, then the result is
	 * negative infinity.</li< <If the argument is equal to 10n for integer n,
	 * then the result is n.</li>
	 * </ul>
	 * 
	 * @param a
	 *            a value
	 * @return the base 10 logarithm of a
	 */
	public static float log10(float a) {
		return (float) Math.log10((double) a);
	}

	// TODO javadoc
	public static int min(int a, int b) {
		return Math.min(a, b);
	}

	// TODO javadoc
	public static int max(int a, int b) {
		return Math.max(a, b);
	}

	// TODO javadoc
	public static int min(int[] values) {
		if (values.length == 0)
			return 0;
		int min = values[0];
		for (int i = 1; i < values.length; i++) {
			min = Math.min(min, values[i]);
		}
		return min;
	}

	// TODO javadoc
	public static int max(int[] values) {
		if (values.length == 0)
			return 0;
		int max = values[0];
		for (int i = 1; i < values.length; i++) {
			max = Math.max(max, values[i]);
		}
		return max;
	}

	/**
	 * Returns the greater of two float values. That is, the result is the
	 * argument closer to positive infinity. If the arguments have the same
	 * value, the result is that same value. If either value is NaN, then the
	 * result is NaN. Unlike the numerical comparison operators, this method
	 * considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the other negative zero, the result is
	 * positive zero.
	 * 
	 * @param a
	 *            an argument
	 * @param b
	 *            another argument
	 * @return the larger of a and b
	 */
	public static float max(float a, float b) {
		return Math.max(a, b);
	}

	/**
	 * Returns the smaller of two float values. That is, the result is the value
	 * closer to negative infinity. If the arguments have the same value, the
	 * result is that same value. If either value is NaN, then the result is
	 * NaN. Unlike the numerical comparison operators, this method considers
	 * negative zero to be strictly smaller than positive zero. If one argument
	 * is positive zero and the other is negative zero, the result is negative
	 * zero.
	 * 
	 * @param a
	 *            an argument
	 * @param b
	 *            another argument
	 * @return the smaller of a and b
	 */
	public static float min(float a, float b) {
		return Math.min(a, b);
	}

	/**
	 * Returns the maximum float value out of values. This method considers that
	 * positive zero is larger than negative zero.
	 * 
	 * @param values
	 *            the values to get the maximum of
	 * @return the maximum value
	 * @see #max(float, float)
	 */
	public static float max(float... values) {
		if (values.length == 0)
			return 0;
		float max = values[0];
		for (int i = 1; i < values.length; i++) {
			max = Math.max(max, values[i]);
		}
		return max;
	}

	/**
	 * Returns the minimum float value out of values. This method considers that
	 * negative zero is smaller than positive zero.
	 * 
	 * @param values
	 *            the values to get the minimum of
	 * @return the minimum value
	 * @see #min(float, float)
	 */
	public static float min(float... values) {
		if (values.length == 0)
			return 0;
		float min = values[0];
		for (int i = 1; i < values.length; i++) {
			min = Math.min(min, values[i]);
		}
		return min;
	}

	/**
	 * Returns the value of the first argument raised to the power of the second
	 * argument.
	 * 
	 * @param a
	 *            the base
	 * @param b
	 *            the exponent
	 * @return the base raised to the power of b
	 * @see Math#pow(double, double)
	 */
	public static float pow(float a, float b) {
		return (float) Math.pow((double) a, (double) b);
	}

	/**
	 * Returns the closest int to the argument, with ties rounding to positive
	 * infinity.
	 * 
	 * @param a
	 *            a floating-point value to be rounded to an integer.
	 * @return the value of the argument rounded to the nearest integer value.
	 * @see Math#round(float)
	 */
	public static float round(float a) {
		return Math.round(a);
	}

	/**
	 * Returns the closest int to the argument, with ties rounding to positive
	 * infinity.
	 * 
	 * @param a
	 *            a floating-point value to be rounded to an integer.
	 * @return the value of the argument rounded to the nearest integer value.
	 * @see Math#round(float)
	 */
	public static int roundToInt(float a) {
		return (int) Math.round(a);
	}

	/**
	 * Returns the signum function of the argument; zero if the argument is
	 * zero, 1.0f if the argument is greater than zero, -1.0f if the argument is
	 * less than zero. Special Cases:
	 * <ul>
	 * <li>If the argument is NaN, then the result is NaN.</li>
	 * <li>If the argument is positive zero or negative zero, then the result is
	 * the same as the argument.</li>
	 * </ul>
	 * 
	 * @param a
	 *            the floating-point value whose signum is to be returned
	 * @return the signum function of the argument
	 */
	public static float sign(float a) {
		// return (a < 0f) ? -1f : 1f;
		return Math.signum(a);
	}

	/**
	 * Returns the trigonometric sine of an angle. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
	 * <li>If the argument is zero, then the result is a zero with the same sign
	 * as the argument.</li>
	 * </ul>
	 * The computed result must be within 1 ulp of the exact result. Results
	 * must be semi-monotonic.
	 * 
	 * @param a
	 *            an angle, in radians
	 * @return the sine of the argument
	 */
	public static float sin(float a) {
		return (float) Math.sin((double) a);
	}

	/**
	 * Returns the correctly rounded positive square root of a float value.
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive
	 * infinity.</li>
	 * <li>If the argument is positive zero or negative zero, then the result is
	 * the same as the argument.</li> <br />
	 * Otherwise, the result is the double value closest to the true
	 * mathematical square root of the argument value.
	 * </ul>
	 * 
	 * @param a
	 *            a value
	 * @return the positive square root of a. If the argument is NaN or less
	 *         than zero, the result is NaN
	 */
	public static float sqrt(float a) {
		return (float) Math.sqrt((double) a);
	}

	/**
	 * Returns the trigonometric tangent of an angle. Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
	 * <li>If the argument is zero, then the result is a zero with the same sign
	 * as the argument.</li> The computed result must be within 1 ulp of the
	 * exact result. Results must be semi-monotonic.
	 * 
	 * @param a
	 *            an angle, in radians
	 * @return the tangent of the argument
	 */
	public static float tan(float a) {
		return (float) Math.tan((double) a);
	}

	/**
	 * Determines whether or not the given value a is in range of min and max.
	 * 
	 * @param a
	 *            the value to check
	 * @param min
	 *            the minimum value for a
	 * @param max
	 *            the maximum value for a
	 * @return true if the value is in range of [min,max], false otherwise
	 */
	public static boolean isInRange(float a, int min, int max) {
		return a >= min && a <= max;
	}

	/**
	 * Returns a random float between min and max.
	 * 
	 * @param min
	 *            the minimum value of the random float
	 * @param max
	 *            the maximum value of the random float
	 * @return the random float
	 */
	public static float random(float min, float max) {
		return (float) (Math.random() * (max - min)) + min;
	}

	/**
	 * Returns a random int between min and max.
	 * 
	 * @param min
	 *            the minimum value of the random float
	 * @param max
	 *            the maximum value of the random float
	 * @return the random float
	 */
	public static int random(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}

}

package math;

public class Mathf {

	public static final float PI = (float) Math.PI;
	public static final float HALF_PI = PI * 0.5f;
	public static final float TWO_PI = PI + PI;
	public static final float ONE_THIRD = 1f / 3f;
	public static final float QUARTER_PI = PI / 4f;
	public static final float ZERO_TOLERANCE = 0.00001f;;

	public static float abs(float a) {
		return (float) Math.abs(a);
	}

	public static float clamp(float value, float min, float max) {
		float clampedValue = value;
		clampedValue = clampedValue < min ? min : clampedValue;
		clampedValue = clampedValue > max ? max : clampedValue;
		return clampedValue;
	}
	
	public static float atan(float a) {
		return (float) Math.atan(a);
	}

	public static float cos(float a) {
		return (float) Math.cos(a);
	}

	public static float map(float value, float from0, float to0, float from1, float to1) {
		return from1 + (to1 - from1) * ((value - from0) / (to0 - from0));
	}

	public static int clampInt(int a, int min, int max) {
		a = a < min ? min : (a > max ? max : a);
		return a;
	}

	public static float clamp01(float a) {
		return clamp(a, 0f, 1f);
	}

	public static float max(float... values) {
		if (values.length == 0)
			return 0;
		float max = values[0];
		for (int i = 1; i < values.length; i++) {
			max = Math.max(max, values[i]);
		}
		return max;
	}

	public static float min(float... values) {
		if (values.length == 0)
			return 0;
		float min = values[0];
		for (int i = 1; i < values.length; i++) {
			min = Math.min(min, values[i]);
		}
		return min;
	}

	public static float pow(float a, float b) {
		return (float) Math.pow(a, b);
	}

	public static int random(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}

	public static float random(float min, float max) {
		return (float) (Math.random() * (max - min)) + min;
	}

	public static float round(float a) {
		return (float) Math.round(a);
	}

	public static int roundToInt(float a) {
		return Math.round(a);
	}

	public static float sin(float a) {
		return (float) Math.sin(a);
	}

	public static float sqrt(float a) {
		return (float) Math.sqrt(a);
	}

	public static int toOneDimensionalIndex(int rowIndex, int colIndex, int numberOfColumns) {
		return rowIndex * numberOfColumns + colIndex;
	}

	public static float toRadians(float angDeg) {
		return (float) Math.toRadians(angDeg);
	}

}

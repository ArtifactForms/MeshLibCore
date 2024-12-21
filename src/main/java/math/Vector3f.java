package math;

public class Vector3f {

	public static final Vector3f BACK = new Vector3f(0, 0, -1);

	public static final Vector3f DOWN = new Vector3f(0, -1, 0);

	public static final Vector3f FORWARD = new Vector3f(0, 0, 1);

	public static final Vector3f LEFT = new Vector3f(-1, 0, 0);

	public static final Vector3f MAX = new Vector3f(Float.MAX_VALUE,
	    Float.MAX_VALUE, Float.MAX_VALUE);

	public static final Vector3f MIN = new Vector3f(Float.MIN_VALUE,
	    Float.MIN_VALUE, Float.MIN_VALUE);

	public static final Vector3f NAN = new Vector3f(Float.NaN, Float.NaN,
	    Float.NaN);

	public static final Vector3f NEGATIVE_INFINITY = new Vector3f(
	    Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
	    Float.NEGATIVE_INFINITY);

	public static final Vector3f ONE = new Vector3f(1, 1, 1);

	public static final Vector3f POSITIVE_INFINITY = new Vector3f(
	    Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
	    Float.POSITIVE_INFINITY);

	public static final Vector3f RIGHT = new Vector3f(1, 0, 0);

	public static final Vector3f UP = new Vector3f(0, 1, 0);

	public static final Vector3f ZERO = new Vector3f(0, 0, 0);

	public float x;

	public float y;

	public float z;

	public Vector3f() {
		x = y = z = 0;
	}

	public Vector3f(float value) {
		x = y = z = value;
	}

	public Vector3f(float x, float y) {
		this(x, y, 0);
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(float[] values) {
		x = values[0];
		y = values[1];
		z = values[2];
	}

	public Vector3f(Vector3f v) {
		set(v);
	}

	/**
	 * Rounds the x, y, and z components of this vector to the specified number of
	 * decimal places.
	 *
	 * @param decimalPlaces The number of decimal places to round to.
	 */
	public void roundLocalDecimalPlaces(int decimalPlaces) {
		float factor = Mathf.pow(10, decimalPlaces);
		x = Mathf.round(x * factor) / factor;
		y = Mathf.round(y * factor) / factor;
		z = Mathf.round(z * factor) / factor;
	}

	public boolean approximatelyEquals(Vector3f v, float threshold) {
		if (threshold < 0.0f)
			throw new IllegalArgumentException(
			    "Threshold must be greater or equal to 0.0f.");

		float diffX = Math.abs(x - v.x);
		float diffY = Math.abs(y - v.y);
		float diffZ = Math.abs(z - v.z);

		return (diffX <= threshold && diffY <= threshold && diffZ <= threshold);
	}

	public float angle(Vector3f v) {
		return (float) Math.acos(dot(v));
	}

	public float signedAngle(Vector3f v, Vector3f normal) {
		float unsignedAngle = (float) Math.acos(dot(v));
		return unsignedAngle * Math.signum(normal.dot(cross(v)));
	}

	public Vector3f project(Vector3f v) {
		float scalar = dot(v) / v.lengthSquared();
		return v.mult(scalar);
	}

	public Vector3f projectLocal(Vector3f v) {
		float scalar = dot(v) / v.lengthSquared();
		set(v.mult(scalar));
		return this;
	}

	public Vector3f projectOnPlane(Vector3f planeNormal) {
		// FIXME Check if this implementation is correct.
		float scalar = dot(planeNormal) / planeNormal.lengthSquared();
		return subtract(planeNormal.mult(scalar));
	}

	public Vector3f projectOnPlaneLocal(Vector3f planeNormal) {
		// FIXME Check if this implementation is correct.
		float scalar = dot(planeNormal) / planeNormal.lengthSquared();
		subtractLocal(planeNormal.mult(scalar));
		return this;
	}

	public Vector3f clampLength(float maxLength) {
		return normalize().mult(maxLength);
	}

	public Vector3f clampLengthLocal(float maxLength) {
		normalizeLocal();
		multLocal(maxLength);
		return this;
	}

	public float lengthSquared() {
		return (x * x) + (y * y) + (z * z);
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float distanceSquared(float x, float y, float z) {
		float dx = this.x - x;
		float dy = this.y - y;
		float dz = this.z - z;
		return (dx * dx) + (dy * dy) + (dz * dz);
	}

	public float distance(float x, float y, float z) {
		return (float) Math.sqrt(distanceSquared(x, y, z));
	}

	public float distanceSquared(Vector3f v) {
		return distanceSquared(v.x, v.y, v.z);
	}

	public float distance(Vector3f v) {
		return (float) Math.sqrt(distanceSquared(v));
	}

	public Vector3f normalize() {
		float length = length();
		if (length != 0) {
			return divide(length);
		}
		return divide(1);
	}

	public Vector3f normalizeLocal() {
		float length = length();
		if (length != 0) {
			return divideLocal(length);
		}
		return divideLocal(1);
	}

	public float dot(Vector3f v) {
		return (x * v.x) + (y * v.y) + z * (v.z);
	}

	public Vector3f cross(float x, float y, float z) {
		return new Vector3f((this.y * z) - (this.z * y),
		    (this.z * x) - (this.x * z), (this.x * y) - (this.y * x));
	}

	public Vector3f crossLocal(float x, float y, float z) {
		this.x = (this.y * z) - (this.z * y);
		this.y = (this.z * x) - (this.x * z);
		this.z = (this.x * y) - (this.y * x);
		return this;
	}

	public Vector3f cross(Vector3f v) {
		return new Vector3f((y * v.z) - (z * v.y), (z * v.x) - (x * v.z),
		    (x * v.y) - (y * v.x));
	}

	public Vector3f crossLocal(Vector3f v) {
		x = (y * v.z) - (z * v.y);
		y = (z * v.x) - (x * v.z);
		z = (x * v.y) - (y * v.x);
		return this;
	}

	public Vector3f cross(Vector3f v, Vector3f result) {
		if (result == null)
			result = new Vector3f();
		return result.set(cross(v));
	}

	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f negateLocal() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public Vector3f add(float x, float y, float z) {
		return new Vector3f(this.x + x, this.y + y, this.z + z);
	}

	public Vector3f addLocal(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3f add(Vector3f v) {
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}

	public Vector3f addLocal(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public Vector3f add(Vector3f v, Vector3f result) {
		if (result == null)
			result = new Vector3f();
		return result.set(add(v));
	}

	public Vector3f subtract(float x, float y, float z) {
		return new Vector3f(this.x - x, this.y - y, this.z - z);
	}

	public Vector3f subtractLocal(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vector3f subtract(Vector3f v) {
		return new Vector3f(x - v.x, y - v.y, z - v.z);
	}

	public Vector3f subtractLocal(Vector3f v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vector3f subtract(Vector3f v, Vector3f result) {
		if (result == null)
			result = new Vector3f();
		return result.set(subtract(v));
	}

	public Vector3f mult(float x, float y, float z) {
		return new Vector3f(this.x * x, this.y * y, this.z * z);
	}

	public Vector3f multLocal(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vector3f mult(Vector3f v) {
		return new Vector3f(x * v.x, y * v.y, z * v.z);
	}

	public Vector3f multLocal(Vector3f v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	public Vector3f mult(Vector3f v, Vector3f result) {
		if (result == null)
			result = new Vector3f();
		return result.set(mult(v));
	}

	public Vector3f divide(float x, float y, float z) {
		return new Vector3f(this.x / x, this.y / y, this.z / z);
	}

	public Vector3f divideLocal(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}

	public Vector3f divide(Vector3f v) {
		return new Vector3f(x / v.x, y / v.y, z / v.z);
	}

	public Vector3f divideLocal(Vector3f v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}

	public Vector3f mult(float scalar) {
		return new Vector3f(x * scalar, y * scalar, z * scalar);
	}

	public Vector3f multLocal(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}

	public Vector3f mult(Matrix3f m) {
		float x0 = m.values[0] * x + m.values[1] * y + m.values[2] * z;
		float y0 = m.values[3] * x + m.values[4] * y + m.values[5] * z;
		float z0 = m.values[6] * x + m.values[7] * y + m.values[8] * z;
		return new Vector3f(x0, y0, z0);
	}

	public Vector3f multLocal(Matrix3f m) {
		float x0 = m.values[0] * x + m.values[1] * y + m.values[2] * z;
		float y0 = m.values[3] * x + m.values[4] * y + m.values[5] * z;
		float z0 = m.values[6] * x + m.values[7] * y + m.values[8] * z;
		set(x0, y0, z0);
		return this;
	}

	public Vector3f divide(float scalar) {
		return new Vector3f(x / scalar, y / scalar, z / scalar);
	}

	public Vector3f divideLocal(float scalar) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
		return this;
	}

	public Vector3f divide(Vector3f v, Vector3f result) {
		if (result == null)
			result = new Vector3f();
		return result.set(divide(v));
	}

	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public Vector3f absLocal() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}

	public Vector3f min(Vector3f v) {
		return new Vector3f(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z));
	}

	public Vector3f minLocal(Vector3f v) {
		x = Math.min(x, v.x);
		y = Math.min(y, v.y);
		z = Math.min(z, v.z);
		return this;
	}

	public Vector3f minLocal(Vector3f a, Vector3f b) {
		x = Math.min(a.x, b.x);
		y = Math.min(a.y, b.y);
		z = Math.min(a.z, b.z);
		return this;
	}

	public Vector3f max(Vector3f v) {
		return new Vector3f(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z));
	}

	public Vector3f maxLocal(Vector3f v) {
		x = Math.max(x, v.x);
		y = Math.max(y, v.y);
		z = Math.max(z, v.z);
		return this;
	}

	public Vector3f maxLocal(Vector3f a, Vector3f b) {
		x = Math.max(a.x, b.x);
		y = Math.max(a.y, b.y);
		z = Math.max(a.z, b.z);
		return this;
	}

	public Vector3f reciprocal() {
		return new Vector3f((x != 0 ? 1.0f / x : Float.POSITIVE_INFINITY),
		    (y != 0 ? 1.0f / y : Float.POSITIVE_INFINITY),
		    (z != 0 ? 1.0f / z : Float.POSITIVE_INFINITY));
	}

	public Vector3f lerpLocal(Vector3f finalVec, float changeAmnt) {
		if (changeAmnt == 0) {
			return this;
		}
		if (changeAmnt == 1) {
			this.x = finalVec.x;
			this.y = finalVec.y;
			this.z = finalVec.z;
			return this;
		}
		this.x = (1 - changeAmnt) * this.x + changeAmnt * finalVec.x;
		this.y = (1 - changeAmnt) * this.y + changeAmnt * finalVec.y;
		this.z = (1 - changeAmnt) * this.z + changeAmnt * finalVec.z;
		return this;
	}

	public Vector3f lerpLocal(Vector3f beginVec, Vector3f finalVec,
	    float changeAmnt) {
		if (changeAmnt == 0) {
			this.x = beginVec.x;
			this.y = beginVec.y;
			this.z = beginVec.z;
			return this;
		}
		if (changeAmnt == 1) {
			this.x = finalVec.x;
			this.y = finalVec.y;
			this.z = finalVec.z;
			return this;
		}
		this.x = (1 - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
		this.y = (1 - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
		this.z = (1 - changeAmnt) * beginVec.z + changeAmnt * finalVec.z;
		return this;
	}

	public static boolean isValid(Vector3f v) {
		if (v == null)
			return false;
		if (Float.isNaN(v.x) || Float.isNaN(v.y) || Float.isNaN(v.z))
			return false;
		if (Float.isInfinite(v.x) || Float.isInfinite(v.y) || Float.isInfinite(v.z))
			return false;
		return true;
	}

	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f set(Vector3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	public Vector3f set(float[] values) {
		x = values[0];
		y = values[1];
		z = values[2];
		return this;
	}

	public float getX() {
		return x;
	}

	public Vector3f setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}

	public float getZ() {
		return z;
	}

	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

}

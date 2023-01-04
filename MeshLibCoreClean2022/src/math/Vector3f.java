package math;

public class Vector3f {

	public static final Vector3f ZERO = new Vector3f(0, 0, 0);

	private float x;
	private float y;
	private float z;

	public Vector3f() {
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
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
		return (x * v.x) + (y * v.y) + (z * v.z);
	}

	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
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
		if (v == null)
			return new Vector3f();
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}

	public Vector3f addLocal(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
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

	public Vector3f multLocal(Vector3f v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
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

	public Vector3f lerpLocal(Vector3f finalVec, float changeAmnt) {
		this.x = (1 - changeAmnt) * this.x + changeAmnt * finalVec.x;
		this.y = (1 - changeAmnt) * this.y + changeAmnt * finalVec.y;
		this.z = (1 - changeAmnt) * this.z + changeAmnt * finalVec.z;
		return this;
	}

	public void roundLocalDecimalPlaces(int places) {
		float a = (float) Math.pow(10, places);
		x = Math.round(x * a) / a;
		y = Math.round(y * a) / a;
		z = Math.round(z * a) / a;
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

	public void setZ(float z) {
		this.z = z;
	}

}

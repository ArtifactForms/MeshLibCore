package math;

/**
 * Representation of 2D vectors and points.
 * 
 * @author Simon
 * @version 0.3, 11 June 2016
 * 
 */
public class Vector2f {

	/**
	 * Shorthand for writing Vector2f(0, -1).
	 */
	public static final Vector2f DOWN = new Vector2f(0, -1);

	/**
	 * Shorthand for writing Vector2f(-1, 0).
	 */
	public static final Vector2f LEFT = new Vector2f(-1, 0);

	/**
	 * Shorthand for writing Vector2f(1, 0).
	 */
	public static final Vector2f RIGHT = new Vector2f(1, 0);

	/**
	 * Shorthand for writing Vector2f(0, 1).
	 */
	public static final Vector2f UP = new Vector2f(0, 1);

	/**
	 * Shorthand for writing Vector2f(1, 1).
	 */
	public static final Vector2f ONE = new Vector2f(1, 1);

	/**
	 * Shorthand for writing Vector2f(0, 0).
	 */
	public static final Vector2f ZERO = new Vector2f(0, 0);

	/**
	 * Shorthand for writing Vector2f(Float.MIN_VALUE, Float.MIN_VALUE).
	 */
	public static final Vector2f MIN = new Vector2f(Float.MIN_VALUE, Float.MIN_VALUE);

	/**
	 * Shorthand for writing Vector2f(Float.MAX_VALUE, Float.MAX_VALUE).
	 */
	public static final Vector2f MAX = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);

	/**
	 * Shorthand for writing Vector2f(Float.POSITIVE_INFINITY,
	 * Float.POSITIVE_INFINITY).
	 */
	public static final Vector2f POSITIVE_INFINITY = new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

	/**
	 * Shorthand for writing Vector2f(Float.NEGATIVE_INFINITY,
	 * Float.NEGATIVE_INFINITY).
	 */
	public static final Vector2f NEGATIVE_INFINITY = new Vector2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	/**
	 * The x component of the vector.
	 */
	public float x;

	/**
	 * The y component of the vector.
	 */
	public float y;

	/**
	 * Constructs a new instance of this {@link Vector2f} with x and y set to 0.
	 * Equivalent to Vector2f(0, 0).
	 */
	public Vector2f() {
		x = y = 0;
	}

	/**
	 * Constructs a new instance of the {@link Vector2f} with the given initial x
	 * and y values.
	 * 
	 * @param x the x value of this {@link Vector2f}
	 * @param y the y value of this {@link Vector2f}
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a new instance of this {@link Vector2f} that contains the passed
	 * vector's information.
	 * 
	 * @param v the vector to copy
	 */
	public Vector2f(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * Returns a new {@link Vector2f} containing the absolute x,y values of this
	 * vector. The values of this vector remain untouched.
	 * 
	 * @return the newly created vector
	 */
	public Vector2f abs() {
		return new Vector2f(Mathf.abs(x), Mathf.abs(y));
	}

	/**
	 * Sets the x,y components of this vector to their absolute values internally.
	 * 
	 * @return this
	 */
	public Vector2f absLocal() {
		this.x = Mathf.abs(x);
		this.y = Mathf.abs(y);
		return this;
	}

	/**
	 * Returns a resultant vector that is made from the smallest components of this
	 * vector and the provided vector v. If the provided vector is null, null is
	 * returned. The values of this vector remain untouched.
	 * 
	 * @param v the provided vector v
	 * @return the resultant vector or null if the provided vector is null
	 */
	public Vector2f min(Vector2f v) {
		if (v == null) {
			return null;
		}
		return new Vector2f(Mathf.min(x, v.x), Mathf.min(y, v.y));
	}

	/**
	 * Returns a resultant vector that is made from the largest components of this
	 * vector and the provided vector v. If the provided vector is null, null is
	 * returned. The values of this vector remain untouched.
	 * 
	 * @param v the provided vector v
	 * @return the resultant vector or null if the provided vector is null
	 */
	public Vector2f max(Vector2f v) {
		if (v == null) {
			return null;
		}
		return new Vector2f(Mathf.max(x, v.x), Mathf.max(y, v.y));
	}

	/**
	 * Clamps the x,y components of this vector between 0 and 1 creating a resultant
	 * vector which is returned. The values of this vector remain untouched.
	 * 
	 * @return the resultant vector
	 */
	public Vector2f clamp01() {
		return new Vector2f(Mathf.clamp01(x), Mathf.clamp01(y));
	}

	/**
	 * Clamps the x,y components of this vector between 0 and 1 internally, and
	 * returns a handle to this vector for easy chaining of calls.
	 * 
	 * @return this
	 */
	public Vector2f clamp01Local() {
		x = Mathf.clamp01(x);
		y = Mathf.clamp01(y);
		return this;
	}

	/**
	 * Clamps the x,y components of this vector between min and max creating a
	 * resultant vector which is returned. The values of this vector remain
	 * untouched.
	 * 
	 * @param min the minimum value for x and y
	 * @param max the maximum value for x and y
	 * @return the resultant vector
	 */
	public Vector2f clamp(float min, float max) {
		return new Vector2f(Mathf.clamp(x, min, max), Mathf.clamp(y, min, max));
	}

	/**
	 * Clamps the x,y components of this vector between min and max internally, and
	 * returns a handle to this vector for easy chaining of calls.
	 * 
	 * @param min the minimum value for x and y
	 * @param max the maximum value for x and y
	 * @return this
	 */
	public Vector2f clampLocal(float min, float max) {
		x = Mathf.clamp(x, min, max);
		y = Mathf.clamp(y, min, max);
		return this;
	}

	/**
	 * Clamps the x,y components of this vector between the given values creating a
	 * resultant vector which is returned. The values of this vector remain
	 * untouched.
	 * 
	 * @param minX the minimum value for x
	 * @param minY the minimum value for y
	 * @param maxX the maximum value for x
	 * @param maxY the maximum value for y
	 * @return the resultant vector
	 */
	public Vector2f clamp(float minX, float minY, float maxX, float maxY) {
		return new Vector2f(Mathf.clamp(x, minX, maxX), Mathf.clamp(y, minY, maxY));
	}

	/**
	 * Clamps the x,y components of this vector between the given values internally,
	 * and returns a handle to this vector for easy chaining of calls.
	 * 
	 * @param minX the minimum value for x
	 * @param minY the minimum value for y
	 * @param maxX the maximum value for x
	 * @param maxY the maximum value for y
	 * @return this
	 */
	public Vector2f clampLocal(float minX, float minY, float maxX, float maxY) {
		x = Mathf.clamp(x, minX, maxX);
		y = Mathf.clamp(y, minY, maxY);
		return this;
	}

	/**
	 * Converts the given angle (in radians) to this vector.
	 * 
	 * @param angrad the angle to convert this vector into
	 * @return this
	 */
	public Vector2f setToAngleRadLocal(float angrad) {
		this.x = Mathf.cos(angrad);
		this.y = Mathf.sin(angrad);
		return this;
	}

	/**
	 * Converts the given angle (in degrees) to this vector.
	 * 
	 * @param angdeg the angle to convert this vector into
	 * @return this
	 */
	public Vector2f setToAngleDegLocal(float angdeg) {
		this.x = Mathf.cos(Mathf.toRadians(angdeg));
		this.y = Mathf.sin(Mathf.toRadians(angdeg));
		return this;
	}

	/**
	 * Set the components of this vector to the values provided by the float array
	 * (values[0] = x, values[1] = y).
	 * 
	 * @param values the values to copy from
	 * @return this
	 * @throws IndexOutOfBoundsException if values.length < 0 || values.length < 2
	 */
	public Vector2f set(float[] values) {
		return set(values[0], values[1]);
	}

	/**
	 * Sets the x,y components of this vector to the given values.
	 * 
	 * @param x the x component of the vector
	 * @param y the y component of the vector
	 * @return this
	 */
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Sets the x,y components of this vector to the values provided by the given
	 * vector v.
	 * 
	 * @param v the vector to copy from
	 * @return this
	 */
	public Vector2f set(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	/**
	 * Adds the provided vector v to this vector creating a resultant vector which
	 * is returned. The values of this vector remain untouched. If the provided
	 * vector is null, null is returned.
	 * 
	 * @param v the vector to add to this vector
	 * @return the resultant vector
	 */
	public Vector2f add(Vector2f v) {
		if (null == v) {
			return null;
		}
		return new Vector2f(x + v.x, y + v.y);
	}

	/**
	 * Adds the provided vector v to this vector internally, and returns a handle to
	 * this vector for easy chaining of calls. If the provided vector is null, null
	 * is returned.
	 * 
	 * @param v the vector to add to this vector
	 * @return this
	 */
	public Vector2f addLocal(Vector2f v) {
		if (null == v) {
			return null;
		}
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Adds the provided values to this vector internally, and returns a handle to
	 * this vector for easy chaining of calls.
	 * 
	 * @param x value to add to x
	 * @param y value to add to y
	 * @return this
	 */
	public Vector2f addLocal(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Adds this vector to the given vector v and stores the result in the provided
	 * result vector. The values of this vector remain untouched. If the provided
	 * vector v is null, null is returned. If the provided result vector is null a
	 * new vector is created to store the result in.
	 * 
	 * @param v      the vector to add
	 * @param result the vector to store the result in
	 * @return the result vector, after adding
	 */
	public Vector2f add(Vector2f v, Vector2f result) {
		if (null == v) {
			return null;
		}
		if (result == null)
			result = new Vector2f();
		result.x = x + v.x;
		result.y = y + v.y;
		return result;
	}

	public Vector2f add(float x, float y) {
		return new Vector2f(this.x + x, this.y + y);
	}

	/**
	 * Calculates the dot product of this vector and the provided vector v. If the
	 * provided vector is null, 0 is returned.
	 * 
	 * @param v the vector to dot with this vector
	 * @return the resultant dot product of this vector and a given vector
	 */
	public float dot(Vector2f v) {
		if (null == v) {
			return 0;
		}
		return x * v.x + y * v.y;
	}

	/**
	 * Calculates the cross product of this vector with a parameter vector v.
	 * 
	 * @param v the vector to take the cross product of with this
	 * @return the cross product vector.
	 */
	public Vector3f cross(Vector2f v) {
		return new Vector3f(0, 0, determinant(v));
	}

	/**
	 * Calculates the determinant of this vector and the given vector v.
	 * 
	 * @param v the given vector v
	 * @return the resulting determinant
	 */
	public float determinant(Vector2f v) {
		return (x * v.y) - (y * v.x);
	}

	/**
	 * Sets this vector to the interpolation by changeAmnt from this to the finalVec
	 * this=(1-changeAmnt)*this + changeAmnt * finalVec
	 * 
	 * @param finalVec   the final vector to interpolate towards
	 * @param changeAmnt an amount between 0.0 - 1.0 representing a percentage
	 *                   change from this towards finalVec
	 */
	public Vector2f interpolateLocal(Vector2f finalVec, float changeAmnt) {
		this.x = (1 - changeAmnt) * this.x + changeAmnt * finalVec.x;
		this.y = (1 - changeAmnt) * this.y + changeAmnt * finalVec.y;
		return this;
	}

	/**
	 * Sets this vector to the interpolation by changeAmnt from beginVec to finalVec
	 * this=(1-changeAmnt)*beginVec + changeAmnt * finalVec
	 * 
	 * @param beginVec   the beginning vector (delta=0)
	 * @param finalVec   the final vector to interpolate towards (delta=1)
	 * @param changeAmnt an amount between 0.0 - 1.0 representing a percentage
	 *                   change from beginVec towards finalVec
	 */
	public Vector2f interpolateLocal(Vector2f beginVec, Vector2f finalVec, float changeAmnt) {
		this.x = (1 - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
		this.y = (1 - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
		return this;
	}

	/**
	 * Checks if the given vector v is valid. A vector is not valid if it is null or
	 * its floats are NaN or infinite, return false. Else return true.
	 * 
	 * @param v the vector to check
	 * @return true or false as stated above
	 */
	public static boolean isValidVector(Vector2f v) {
		if (v == null)
			return false;
		if (Float.isNaN(v.x) || Float.isNaN(v.y))
			return false;
		if (Float.isInfinite(v.x) || Float.isInfinite(v.y))
			return false;
		return true;
	}

	/**
	 * Calculates the magnitude of this vector.
	 * 
	 * @return the length (magnitude) of this vector
	 */
	public float length() {
		return Mathf.sqrt(lengthSquared());
	}

	/**
	 * Calculates the squared value of the magnitude of this vector.
	 * 
	 * @return the magnitude squared of this vector
	 */
	public float lengthSquared() {
		return x * x + y * y;
	}

	/**
	 * Performs a linear interpolation between this vector and another.
	 *
	 * Linear interpolation calculates a new vector that is a portion of the way
	 * between two given vectors. The parameter `t` determines how far along this
	 * line the new vector will be.
	 *
	 * Mathematically, the interpolation is calculated as follows: result = this + t
	 * * (other - this)
	 *
	 * Where: - `this` is the current vector - `other` is the target vector - `t` is
	 * the interpolation factor (between 0 and 1)
	 *
	 * @param other The vector to interpolate towards.
	 * @param t     The interpolation factor. A value of 0 returns this vector, a
	 *              value of 1 returns the other vector, and values between 0 and 1
	 *              return a vector between the two.
	 * @return A new vector representing the interpolated result.
	 */
	public Vector2f lerp(Vector2f other, float t) {
		return new Vector2f(this.x + t * (other.x - this.x), this.y + t * (other.y - this.y));
	}

	/**
	 * Calculates the distance squared between this vector and the given vector v.
	 * 
	 * @param v the second vector to determine the distance squared
	 * @return the distance squared between the two vectors
	 */
	public float distanceSquared(Vector2f v) {
		float dx = x - v.x;
		float dy = y - v.y;
		return dx * dx + dy * dy;
	}

	/**
	 * Calculates the distance squared between this vector and the given vector v.
	 * 
	 * @param x the x coordinate of the v vector
	 * @param y the y coordinate of the v vector
	 * @return the distance squared between the two vectors
	 */
	public float distanceSquared(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return dx * dx + dy * dy;
	}

	/**
	 * Calculates the distance between this vector and vector v.
	 * 
	 * @param v the second vector to determine the distance
	 * @return the distance between the two vectors
	 */
	public float distance(Vector2f v) {
		return Mathf.sqrt(distanceSquared(v));
	}

	/**
	 * Multiplies this vector by a scalar and resultant vector is returned. The
	 * values of this vector remain untouched.
	 * 
	 * @param scalar the value to multiply this vector by
	 * @return the resultant vector
	 */
	public Vector2f mult(float scalar) {
		return new Vector2f(x * scalar, y * scalar);
	}

	/**
	 * Project this vector onto the given vector b. If the provided vector v is
	 * null, null is returned. If the provided result vector is null a new vector is
	 * created to store the result in.
	 * 
	 * @param v      the vector to project onto
	 * @param result the projected vector
	 * @param result
	 */
	public Vector2f projectOntoUnit(Vector2f v, Vector2f result) {
		if (v == null) {
			return null;
		}
		if (result == null)
			result = new Vector2f();
		float dot = v.dot(this);
		result.x = dot * v.getX();
		result.y = dot * v.getY();
		return result;
	}

	/**
	 * Multiplies this vector by a scalar internally, and returns a handle to this
	 * vector for easy chaining of calls.
	 * 
	 * @param scalar the value to multiply this vector by
	 * @return this
	 */
	public Vector2f multLocal(float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	/**
	 * Multiplies a provided vector to this vector internally, and returns a handle
	 * to this vector for easy chaining of calls. If the provided vector is null,
	 * null is returned.
	 * 
	 * @param v the vector to multiply with this vector
	 * @return this
	 */
	public Vector2f multLocal(Vector2f v) {
		if (null == v) {
			return null;
		}
		x *= v.x;
		y *= v.y;
		return this;
	}
	
	public Vector2f mult(Vector2f v) {
		if (null == v) {
			return null;
		}
		return new Vector2f(x * v.x, y * v.y);
	}

	/**
	 * Multiplies the x and y of this vector by the scalar and stores the result in
	 * product. The values of this vector remain untouched. The result is returned
	 * for chaining.
	 * 
	 * @param scalar  the scalar to multiply by
	 * @param product the vector to store the result in
	 * @return product, after multiplication.
	 */
	public Vector2f mult(float scalar, Vector2f product) {
		if (null == product) {
			product = new Vector2f();
		}
		product.x = x * scalar;
		product.y = y * scalar;
		return product;
	}

	/**
	 * Divides the values of this vector by a scalar and returns the result. The
	 * values of this vector remain untouched. Dividing by zero will result in an
	 * exception.
	 * 
	 * @param scalar the value to divide this vectors attributes by
	 * @return the result vector
	 */
	public Vector2f divide(float scalar) {
		return new Vector2f(x / scalar, y / scalar);
	}

	/**
	 * Divides this vector by a scalar internally, and returns a handle to this
	 * vector for easy chaining of calls. Dividing by zero will result in an
	 * exception.
	 * 
	 * @param scalar the value to divide this vector by
	 * @return this
	 */
	public Vector2f divideLocal(float scalar) {
		x /= scalar;
		y /= scalar;
		return this;
	}

	/**
	 * Divides each component of this vector by the corresponding component of the
	 * given vector v internally.
	 * 
	 * @param v the vector providing the two divisors
	 * @return this
	 */
	public Vector2f divideLocal(Vector2f v) {
		x /= v.x;
		y /= v.y;
		return this;
	}

	/**
	 * Returns the negative of this vector. All values are negated and set to a new
	 * vector. The values of this vector remain untouched.
	 * 
	 * @return the negated vector
	 */
	public Vector2f negate() {
		return new Vector2f(-x, -y);
	}

	/**
	 * Negates the values of this vector internally.
	 * 
	 * @return this
	 */
	public Vector2f negateLocal() {
		x = -x;
		y = -y;
		return this;
	}

	/**
	 * Subtracts the values of a given vector from those of this vector creating a
	 * new vector object. If the provided vector is null, an exception is thrown.
	 * 
	 * @param v the vector to subtract from this vector
	 * @return the resultant vector
	 */
	public Vector2f subtract(Vector2f v) {
		return subtract(v, null);
	}

	/**
	 * Subtracts the values of a given vector from those of this vector storing the
	 * result in the given vector. If the provided vector v is null, an exception is
	 * thrown. If the provided vector result is null, a new vector is created.
	 * 
	 * @param v      the vector to subtract from this vector
	 * @param result the vector to store the result in
	 * @return the resultant vector
	 */
	public Vector2f subtract(Vector2f v, Vector2f result) {
		if (result == null)
			result = new Vector2f();
		result.x = x - v.x;
		result.y = y - v.y;
		return result;
	}

	/**
	 * Subtracts the given x,y values from those of this vector creating a new
	 * vector object.
	 * 
	 * @param x value to subtract from x
	 * @param y value to subtract from y
	 * @return the resultant vector
	 */
	public Vector2f subtract(float x, float y) {
		return new Vector2f(this.x - x, this.y - y);
	}

	/**
	 * Subtracts a provided vector to this vector internally, and returns a handle
	 * to this vector for easy chaining of calls. If the provided vector is null,
	 * null is returned.
	 * 
	 * @param v the vector to subtract
	 * @return this
	 */
	public Vector2f subtractLocal(Vector2f v) {
		if (null == v) {
			return null;
		}
		x -= v.x;
		y -= v.y;
		return this;
	}

	/**
	 * Subtracts the provided values from this vector internally, and returns a
	 * handle to this vector for easy chaining of calls.
	 * 
	 * @param x value to subtract from x
	 * @param y value to subtract from y
	 * @return this
	 */
	public Vector2f subtractLocal(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**
	 * Returns the unit vector of this vector. The values of this vector remain
	 * untouched.
	 * 
	 * @return the newly created unit vector of this vector
	 */
	public Vector2f normalize() {
		float length = length();
		if (length != 0) {
			return divide(length);
		}
		return divide(1);
	}

	/**
	 * Makes this vector into a unit vector of itself internally.
	 * 
	 * @return this
	 */
	public Vector2f normalizeLocal() {
		float length = length();
		if (length != 0) {
			return divideLocal(length);
		}
		return divideLocal(1);
	}

	/**
	 * Returns the minimum angle (in radians) between two vectors. It is assumed
	 * that both this vector and the given vector are unit vectors (iow,
	 * normalized).
	 * 
	 * @param otherVector a unit vector to find the angle against
	 * @return the angle in radians
	 */
	public float smallestAngleBetween(Vector2f otherVector) {
		float dotProduct = dot(otherVector);
		float angle = Mathf.acos(dotProduct);
		return angle;
	}

	/**
	 * Sets the closest int to the x and y components locally, with ties rounding to
	 * positive infinity.
	 * 
	 * @return this
	 */
	public Vector2f roundToIntLocal() {
		x = Mathf.roundToInt(x);
		y = Mathf.roundToInt(y);
		return this;
	}

	public Vector2f rotate(float[][] rotationMatrix) {
		float x0 = x * rotationMatrix[0][0] + y * rotationMatrix[0][1];
		float y0 = x * rotationMatrix[1][0] + y * rotationMatrix[1][1];
		this.x = x0;
		this.y = y0;
		return this;
	}

	public Vector2f rotate(float angle) {
		float cos = Mathf.cos(angle);
		float sin = Mathf.sin(angle);
		return new Vector2f(x * cos - y * sin, x * sin + y * cos);
	}

	/**
	 * Returns (in radians) the angle required to rotate a ray represented by this
	 * vector to lie colinear to a ray described by the given vector. It is assumed
	 * that both this vector and the given vector are unit vectors (iow,
	 * normalized).
	 * 
	 * @param otherVector the "destination" unit vector
	 * @return the angle in radians
	 */
	public float angleBetween(Vector2f otherVector) {
		float angle = Mathf.atan2(otherVector.y, otherVector.x) - Mathf.atan2(y, x);
		return angle;
	}

//	  /**
//     * Computes the angle (in radians) between the vector represented
//     * by this vector and the specified vector.
//     * @param x the X magnitude of the other vector
//     * @param y the Y magnitude of the other vector
//     * @return the angle between the two vectors measured in radians
//     */
//    public float angle(float x, float y) {
//        final float ax = getX();
//        final float ay = getY();
//
//        final float delta = (ax * x + ay * y) / Mathf.sqrt(
//                (ax * ax + ay * ay) * (x * x + y * y));
//
//        if (delta > 1.0) {
//        	return 0.0f;
//        }
//        if (delta < -1.0) {
//        	return Mathf.PI;
//        }
//
//        return Mathf.acos(delta);
//    }

	public Vector2f frac() {
		// TODO Check if this is the correct way
		return new Vector2f(x - (int) x, y - (int) y);
	}

	/**
	 * Returns the x component of this vector.
	 * 
	 * @return the x component of this vector
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x component of this vector to the specified new value, and returns a
	 * handle to this vector for easy chaining of calls.
	 * 
	 * @param x the specified new x component
	 * @return this
	 */
	public Vector2f setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Returns the y component of this vector.
	 * 
	 * @return the y component of this vector
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the y component of this vector to the specified new value, and returns a
	 * handle to this vector for easy chaining of calls.
	 * 
	 * @param y the specified new y component
	 * @return this
	 */
	public Vector2f setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * Returns the angle (in radians) represented by this vector as expressed by a
	 * conversion from rectangular coordinates ( <code>x</code>,&nbsp;
	 * <code>y</code>) to polar coordinates (r,&nbsp; <i>theta</i>).
	 * 
	 * @return the angle in radians. [-pi, pi)
	 */
	public float getAngle() {
		return Mathf.atan2(y, x);
	}

	/**
	 * Resets this vector's data to zero internally, and returns a handle to this
	 * vector for easy chaining of calls.
	 */
	public Vector2f zero() {
		x = y = 0;
		return this;
	}

	/**
	 * Returns the perpendicular vector to this vector.
	 * 
	 * @return the newly created perpendicular to this vector
	 */
	public Vector2f getPerpendicular() {
		return new Vector2f(-y, x);
	}

	/**
	 * Stores this vector into the given array.
	 * 
	 * @param floats the array to store this vector in, if null, a new float array
	 *               with the size of two is created
	 * @return the array, with x, y float values in that order
	 */
	public float[] toArray(float[] floats) {
		if (floats == null) {
			floats = new float[2];
		}
		floats[0] = x;
		floats[1] = y;
		return floats;
	}

	/**
	 * Stores this vector into a newly created float array with a size of two.
	 * 
	 * @return the array that stores the vector with x,y component in that order
	 */
	public float[] toArray() {
		return toArray(null);
	}

	/**
	 * Rotates this vector around the origin.
	 * 
	 * @param angle the angle to rotate (in radians)
	 * @param cw    true to rotate clockwise, false to rotate counterclockwise
	 */
	public Vector2f rotate(float angle, boolean cw) {
		if (cw)
			angle = -angle;
		float newX = Mathf.cos(angle) * x - Mathf.sin(angle) * y;
		float newY = Mathf.sin(angle) * x + Mathf.cos(angle) * y;
		return new Vector2f(newX, newY);
	}

	/**
	 * Returns a unique hash code for this vector object based on it's values. If
	 * two vectors are logically equivalent, they will return the same hash code
	 * value.
	 * 
	 * @return the hash code value of this vector
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/**
	 * Determines if this vector is equals to the given object obj.
	 * 
	 * @param obj the object to compare for equality
	 * @return true if they are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2f other = (Vector2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	/**
	 * Returns a string representation of this {@link Vector2f}.
	 * 
	 * @return a string representation of this {@link Vector2f}
	 */
	@Override
	public String toString() {
		return "Vector2f [x=" + x + ", y=" + y + "]";
	}

}
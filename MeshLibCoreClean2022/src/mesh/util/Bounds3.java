package mesh.util;

import math.Vector3f;

public class Bounds3 {

	private Vector3f min;
	private Vector3f max;

	public Bounds3() {
		this.min = new Vector3f();
		this.max = new Vector3f();
	}

	public Bounds3(Vector3f size) {
		this(new Vector3f(), size);
	}

	public Bounds3(Vector3f center, Vector3f size) {
		min = center.subtract(size.mult(0.5f));
		max = center.add(size.mult(0.5f));
	}

	public Bounds3(Bounds3 bounds) {
		this(new Vector3f(bounds.getCenter()), new Vector3f(bounds.getSize()));
	}

	public boolean containsPoint(Vector3f v) {
		return min.getX() <= v.getX() && min.getY() <= v.getY() && min.getZ() <= v.getZ() && v.getX() <= max.getX() && v.getY() <= max.getY() && v.getZ() <= max.getZ();
	}

	public boolean containsPoint(float x, float y, float z) {
		return min.getX() <= x && min.getY() <= y && min.getZ() <= z && x <= max.getX() && y <= max.getY() && z <= max.getZ();
	}

	public boolean contains(Bounds3 other) {
		return min.getX() <= other.min.getX() && min.getY() <= other.min.getY() && min.getZ() <= other.min.getZ() && other.max.getX() <= max.getX()
				&& other.max.getY() <= max.getY() && other.max.getZ() <= max.getZ();
	}

	public void encapsulate(Vector3f v) {
		float minX = v.getX() < min.getX() ? v.getX() : min.getX();
		float minY = v.getY() < min.getY() ? v.getY() : min.getY();
		float minZ = v.getZ() < min.getZ() ? v.getZ() : min.getZ();
		float maxX = v.getX() > max.getX() ? v.getX() : max.getX();
		float maxY = v.getY() > max.getY() ? v.getY() : max.getY();
		float maxZ = v.getZ() > max.getZ() ? v.getZ() : max.getZ();
		min.set(minX, minY, minZ);
		max.set(maxX, maxY, maxZ);
	}

	public void encapsulate(Bounds3 bounds) {
		float minX = bounds.min.getX() < min.getX() ? bounds.min.getX() : min.getX();
		float minY = bounds.min.getY() < min.getY() ? bounds.min.getY() : min.getY();
		float minZ = bounds.min.getZ() < min.getZ() ? bounds.min.getZ() : min.getZ();
		float maxX = bounds.max.getX() > max.getX() ? bounds.max.getX() : max.getX();
		float maxY = bounds.max.getY() > max.getY() ? bounds.max.getY() : max.getY(); 
		float maxZ = bounds.max.getZ() > max.getZ() ? bounds.max.getZ() : max.getZ();
		min.set(minX, minY, minZ);
		max.set(maxX, maxY, maxZ);
	}

	public void expand(float amount) {
		amount *= 0.5f;
		min.subtractLocal(amount, amount, amount);
		max.addLocal(amount, amount, amount);
	}

	public void expand(Vector3f amount) {
		Vector3f v = amount.mult(0.5f);
		min.subtractLocal(v);
		max.addLocal(v);
	}

	public boolean intersects(Bounds3 other) {
		if (min.getX() - other.max.getX() > 0.0f || min.getY() - other.max.getY() > 0.0f || min.getZ() - other.max.getZ() > 0.0f)
			return false;
		if (other.min.getX() - max.getX() > 0.0f || other.min.getY() - max.getY() > 0.0f || other.min.getZ() - max.getZ() > 0.0f)
			return false;
		return true;
	}

	public float volume() {
		return getWidth() * getHeight() * getDepth();
	}

	public Bounds3 copy() {
		return new Bounds3(this);
	}
	
	public Vector3f getExtents() {
		return max.subtract(min).mult(0.5f);
	}

	public void setExtents(Vector3f extents) {
		Vector3f center = getCenter();
		min.set(center.subtract(extents));
		max.set(center.add(extents));
	}

	public Vector3f getCenter() {
		return min.add(max).mult(0.5f);
	}

	public void setCenter(Vector3f center) {
		Vector3f extents = getExtents();
		min.set(center.subtract(extents));
		max.set(center.add(extents));
	}

	public void setCenter(float x, float y, float z) {
		Vector3f extents = getExtents();
		Vector3f center = new Vector3f(x, y, z);
		min.set(center.subtract(extents));
		max.set(center.add(extents));
	}

	public Vector3f getSize() {
		return max.subtract(min);
	}

	public float getCenterX() {
		return (min.getX() + max.getX()) * 0.5f;
	}

	public float getCenterY() {
		return (min.getY() + max.getY()) * 0.5f;
	}

	public float getCenterZ() {
		return (min.getZ() + max.getZ()) * 0.5f;
	}

	public Vector3f getMin() {
		return new Vector3f(min);
	}

	public Vector3f getMax() {
		return new Vector3f(max);
	}

	public float getMinX() {
		return min.getX();
	}

	public float getMaxX() {
		return max.getX();
	}

	public float getMinY() {
		return min.getY();
	}

	public float getMaxY() {
		return max.getY();
	}

	public float getMinZ() {
		return min.getZ();
	}

	public float getMaxZ() {
		return max.getZ();
	}

	public float getWidth() {
		return max.getX() - min.getX();
	}

	public float getHeight() {
		return max.getY() - min.getY();
	}

	public float getDepth() {
		return max.getZ() - min.getZ();
	}

	public Vector3f getMin(Vector3f store) {
		return store.set(min);
	}

	public Vector3f getMax(Vector3f store) {
		return store.set(max);
	}

	public void setMinMax(Vector3f min, Vector3f max) {
		this.min.set(min);
		this.max.set(max);
	}

	public void setMin(float minX, float minY, float minZ) {
		min.set(minX, minY, minZ);
	}

	public void setMax(float maxX, float maxY, float maxZ) {
		max.set(maxX, maxY, maxZ);
	}

	public void set(Bounds3 b) {
		min.set(b.min);
		max.set(b.max);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
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
		Bounds3 other = (Bounds3) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Bounds3 [min=" + min + ", max=" + max + "]";
	}

}

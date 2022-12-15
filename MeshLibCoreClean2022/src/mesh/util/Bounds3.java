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
	this.min = new Vector3f(center.x - (size.x * 0.5f), center.y - (size.y * 0.5f), center.z - (size.z * 0.5f));
	this.max = new Vector3f(center.x + (size.x * 0.5f), center.y + (size.y * 0.5f), center.z + (size.z * 0.5f));
    }

    public Bounds3(Bounds3 bounds) {
	this(new Vector3f(bounds.getCenter()), new Vector3f(bounds.getSize()));
    }

    public boolean containsPoint(Vector3f v) {
	return min.x <= v.x && min.y <= v.y && min.z <= v.z && v.x <= max.x && v.y <= max.y && v.z <= max.z;
    }

    public boolean containsPoint(float x, float y, float z) {
	return min.x <= x && min.y <= y && min.z <= z && x <= max.x && y <= max.y && z <= max.z;
    }

    public boolean contains(Bounds3 other) {
	return min.x <= other.min.x && min.y <= other.min.y && min.z <= other.min.z && other.max.x <= max.x
		&& other.max.y <= max.y && other.max.z <= max.z;
    }

    public void encapsulate(Vector3f v) {
	min.x = v.x < min.x ? v.x : min.x;
	min.y = v.y < min.y ? v.y : min.y;
	min.z = v.z < min.z ? v.z : min.z;

	max.x = v.x > max.x ? v.x : max.x;
	max.y = v.y > max.y ? v.y : max.y;
	max.z = v.z > max.z ? v.z : max.z;
    }

    public void encapsulate(Bounds3 bounds) {
	min.x = bounds.min.x < min.x ? bounds.min.x : min.x;
	min.y = bounds.min.y < min.y ? bounds.min.y : min.y;
	min.z = bounds.min.z < min.z ? bounds.min.z : min.z;

	max.x = bounds.max.x > max.x ? bounds.max.x : max.x;
	max.y = bounds.max.y > max.y ? bounds.max.y : max.y;
	max.z = bounds.max.z > max.z ? bounds.max.z : max.z;
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
	if (min.x - other.max.x > 0.0f || min.y - other.max.y > 0.0f || min.z - other.max.z > 0.0f)
	    return false;
	if (other.min.x - max.x > 0.0f || other.min.y - max.y > 0.0f || other.min.z - max.z > 0.0f)
	    return false;
	return true;
    }

    public float volume() {
	return getWidth() * getHeight() * getDepth();
    }

    public Bounds3 copy() {
	return new Bounds3(this);
    }

    public Bounds3 getBounds() {
	Bounds3 b = new Bounds3();
	b.min.x = min.x;
	b.min.y = min.y;
	b.min.z = min.z;
	b.max.x = max.x;
	b.max.y = max.y;
	b.max.z = max.z;
	return b;
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
	return (min.x + max.x) * 0.5f;
    }

    public float getCenterY() {
	return (min.y + max.y) * 0.5f;
    }

    public float getCenterZ() {
	return (min.z + max.z) * 0.5f;
    }

    public Vector3f getMin() {
	return new Vector3f(min);
    }

    public Vector3f getMax() {
	return new Vector3f(max);
    }

    public float getMinX() {
	return min.x;
    }

    public float getMaxX() {
	return max.x;
    }

    public float getMinY() {
	return min.y;
    }

    public float getMaxY() {
	return max.y;
    }

    public float getMinZ() {
	return min.z;
    }

    public float getMaxZ() {
	return max.z;
    }

    public float getWidth() {
	return max.x - min.x;
    }

    public float getHeight() {
	return max.y - min.y;
    }

    public float getDepth() {
	return max.z - min.z;
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

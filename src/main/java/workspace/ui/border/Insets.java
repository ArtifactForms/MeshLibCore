package workspace.ui.border;

/**
 * Represents a 2D Insets model, typically used for defining spacing around UI
 * elements (e.g., margins, padding, or borders).
 * 
 * <p>
 * Each side of the inset (top, left, bottom, right) can be configured
 * independently or uniformly. The class also provides utility methods for
 * computing total horizontal and vertical spacing, and supports operations like
 * adding insets or copying values.
 * </p>
 */
public class Insets {

	/**
	 * The inset value for the top side.
	 */
	private int top;

	/**
	 * The inset value for the left side.
	 */
	private int left;

	/**
	 * The inset value for the bottom side.
	 */
	private int bottom;

	/**
	 * The inset value for the right side.
	 */
	private int right;

	/**
	 * Creates a default Insets object with all sides set to zero.
	 */
	public Insets() {
		this(0, 0, 0, 0);
	}

	/**
	 * Creates a new Insets object as a copy of an existing one.
	 * 
	 * @param insets the Insets object to copy
	 * @throws NullPointerException if the provided insets object is null
	 */
	public Insets(Insets insets) {
		if (insets == null) {
			throw new NullPointerException("Insets to copy cannot be null.");
		}
		this.top = insets.top;
		this.left = insets.left;
		this.bottom = insets.bottom;
		this.right = insets.right;
	}

	/**
	 * Creates an Insets object with explicit values for each side.
	 * 
	 * @param top    the inset value for the top side
	 * @param left   the inset value for the left side
	 * @param bottom the inset value for the bottom side
	 * @param right  the inset value for the right side
	 * @throws IllegalArgumentException if any value is negative
	 */
	public Insets(int top, int left, int bottom, int right) {
		set(top, left, bottom, right);
	}

	/**
	 * Adds the values of another Insets object to this one.
	 * 
	 * @param other the Insets object whose values will be added
	 * @throws NullPointerException if the provided insets object is null
	 */
	public void add(Insets other) {
		if (other == null) {
			throw new NullPointerException("Insets to add cannot be null.");
		}
		this.top += other.top;
		this.left += other.left;
		this.bottom += other.bottom;
		this.right += other.right;
	}

	/**
	 * Sets the same value for all four sides.
	 * 
	 * @param size the uniform value for all sides
	 * @throws IllegalArgumentException if the value is negative
	 */
	public void set(int size) {
		set(size, size, size, size);
	}

	/**
	 * Sets the values for each side explicitly.
	 * 
	 * @param top    the inset value for the top side
	 * @param left   the inset value for the left side
	 * @param bottom the inset value for the bottom side
	 * @param right  the inset value for the right side
	 * @throws IllegalArgumentException if any value is negative
	 */
	public void set(int top, int left, int bottom, int right) {
		if (top < 0 || left < 0 || bottom < 0 || right < 0) {
			throw new IllegalArgumentException("Insets values cannot be negative.");
		}
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	/**
	 * Returns the total horizontal spacing (left + right).
	 * 
	 * @return the sum of the left and right inset values
	 */
	public int getHorizontalInsets() {
		return left + right;
	}

	/**
	 * Returns the total vertical spacing (top + bottom).
	 * 
	 * @return the sum of the top and bottom inset values
	 */
	public int getVerticalInsets() {
		return top + bottom;
	}

	/**
	 * Returns the inset value for the top side.
	 * 
	 * @return the top inset value
	 */
	public int getTop() {
		return top;
	}

	/**
	 * Returns the inset value for the left side.
	 * 
	 * @return the left inset value
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Returns the inset value for the bottom side.
	 * 
	 * @return the bottom inset value
	 */
	public int getBottom() {
		return bottom;
	}

	/**
	 * Returns the inset value for the right side.
	 * 
	 * @return the right inset value
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Checks if this Insets object is equal to another object.
	 * 
	 * @param obj the object to compare
	 * @return true if the other object is an Insets instance with identical
	 *         values; false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Insets insets = (Insets) obj;
		return top == insets.top && left == insets.left && bottom == insets.bottom
		    && right == insets.right;
	}

	/**
	 * Computes a hash code for this Insets object, consistent with the
	 * {@link #equals(Object)} method.
	 * 
	 * @return the hash code for this Insets object
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + top;
		result = 31 * result + left;
		result = 31 * result + bottom;
		result = 31 * result + right;
		return result;
	}

	/**
	 * Returns a string representation of the Insets object, useful for debugging.
	 * 
	 * @return a string describing the insets in the format: "Insets [top=...,
	 *         left=..., bottom=..., right=...]"
	 */
	@Override
	public String toString() {
		return "Insets [top=" + top + ", left=" + left + ", bottom=" + bottom
		    + ", right=" + right + "]";
	}

}
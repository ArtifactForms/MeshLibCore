package workspace.ui.border;

/**
 * TODO Explain me
 * 
 * @author - Simon Dietz
 * @version 0.1, 17 January 2011
 * 
 */
public class Insets implements Cloneable {

	/**
	 * The distance from top.
	 */
	public int top;

	/**
	 * The distance from left.
	 */
	public int left;

	/**
	 * The distance from bottom.
	 */
	public int bottom;

	/**
	 * The distance from right.
	 */
	public int right;

	/**
	 * Constructs a new instance of this insets with the top, left, bottom and right
	 * values set to zero.
	 */
	public Insets() {
		super();
	}

	/**
	 * Constructs a new instance of this insets with all distances set to the
	 * specified value.
	 * 
	 * @param i the value for the distance from top, left, bottom and right.
	 */
	public Insets(int i) {
		this(i, i, i, i);
	}

	/**
	 * Constructs a new instance of this insets with the specified top, left, bottom
	 * and right values.
	 * 
	 * @param top    the value for the distance from top
	 * @param left   the value for the distance from left
	 * @param bottom the value for the distance from bottom
	 * @param right  the value for the distance from right
	 */
	public Insets(int top, int left, int bottom, int right) {
		super();
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	/**
	 * Constructs a new instance of this insets with all distances same as the ones
	 * of the given insets object.
	 * 
	 * @param i the insets to copy the distance values from
	 */
	public Insets(Insets i) {
		this.top = i.top;
		this.left = i.left;
		this.bottom = i.bottom;
		this.right = i.right;
	}

	/**
	 * Adds the distances of the specified insets object to the distances of this
	 * insets.
	 * 
	 * @param i the insets being added
	 */
	public void add(Insets i) {
		this.top += i.top;
		this.left += i.left;
		this.bottom += i.bottom;
		this.right += i.right;
	}

	/**
	 * Subtracts the distances of the specified insets object from the distances of
	 * this insets.
	 * 
	 * @param i the insets being subtracted
	 */
	public void sub(Insets i) {
		this.top -= i.top;
		this.left -= i.left;
		this.bottom -= i.bottom;
		this.right -= i.right;
	}

	/**
	 * Returns the sum of <code>left</code> and <code>right</code>
	 * 
	 * @return the sum of <code>left</code> and <code>right</code>
	 */
	public int getWidth() {
		return left + right;
	}

	/**
	 * Returns the sum of <code>top</code> and <code>bottom</code>.
	 * 
	 * @return the sum of <code>top</code> and <code>bottom</code>
	 */
	public int getHeight() {
		return top + bottom;
	}

	/**
	 * Determines if all distances of this insets are equal to zero.
	 * 
	 * @return true if all distances of this insets are equal to zero
	 */
	public boolean isEmpty() {
		return top == 0 && left == 0 && bottom == 0 && right == 0;
	}

	public int getHorizontalInsets() {
		return left + right;
	}

	public int getVerticalInsets() {
		return top + bottom;
	}

	public void set(int size) {
		top = left = bottom = right = size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottom;
		result = prime * result + left;
		result = prime * result + right;
		result = prime * result + top;
		return result;
	}

	/**
	 * Checks if the specified object is equal to this insets. Two insets are equal
	 * if all of there distances (top, left, bottom, right) are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Insets other = (Insets) obj;
		if (bottom != other.bottom)
			return false;
		if (left != other.left)
			return false;
		if (right != other.right)
			return false;
		if (top != other.top)
			return false;
		return true;
	}

	/**
	 * Creates a copy of this insets.
	 * 
	 * @return a copy of this insets
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}

	/**
	 * Returns a string representation of this insets.
	 * 
	 * @return a string representation of this insets
	 */
	@Override
	public String toString() {
		return "Insets [top=" + top + ", left=" + left + ", bottom=" + bottom + ", right=" + right + "]";
	}

}

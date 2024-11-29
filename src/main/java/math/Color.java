package math;

/**
 * Representation of RGBA colors. The components (r,g,b) define a color in RGB
 * color space.
 * 
 * @author Simon
 * @version 0.3, 11 December 2017
 * 
 */
public class Color {

	/**
	 * Solid black. RGBA is (0, 0, 0, 1).
	 */
	public static final Color BLACK = new Color(0f, 0f, 0f, 1f);

	/**
	 * Solid blue. RGBA is (0, 0, 1, 1).
	 */
	public static final Color BLUE = new Color(0f, 0f, 1f, 1f);

	/**
	 * Completely transparent. RGBA is (0, 0, 0, 0).
	 */
	public static final Color CLEAR = new Color(0f, 0f, 0f, 0f);

	/**
	 * Cyan. RGBA is (0, 1, 1, 1).
	 */
	public static final Color CYAN = new Color(0f, 1f, 1f, 1f);

	/**
	 * Dark gray. RGBA is (0.25, 0.25, 0.25, 1).
	 */
	public static final Color DARK_GRAY = new Color(0.25f, 0.25f, 0.25f, 1.0f);

	/**
	 * Gray. RGBA is (0.5, 0.5, 0.5, 1).
	 */
	public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1f);

	/**
	 * Solid green. RGBA is (0, 1, 0, 1).
	 */
	public static final Color GREEN = new Color(0f, 1f, 0f, 1f);

	/**
	 * English spelling for gray. RGBA is the same (0.5, 0.5, 0.5, 1).
	 */
	public static final Color GREY = GRAY;

	/**
	 * Light gray. RGBA is (0.75f, 0.75f, 0.75f, 1f).
	 */
	public static final Color LIGHT_GRAY = new Color(0.75f, 0.75f, 0.75f, 1f);

	/**
	 * Magenta. RGBA is (1, 0, 1, 1).
	 */
	public static final Color MAGENTA = new Color(1f, 0f, 1f, 1f);

	/**
	 * Solid red. RGBA is (1, 0, 0, 1).
	 */
	public static final Color RED = new Color(1f, 0f, 0f, 1f);

	/**
	 * Solid white. RGBA is (1, 1, 1, 1).
	 */
	public static final Color WHITE = new Color(1f, 1f, 1f, 1f);

	/**
	 * Yellow. RGBA is (1, 1, 0, 1).
	 */
	public static final Color YELLOW = new Color(1f, 1f, 0f, 1f);

	/**
	 * The red component of the color.
	 */
	private float r;

	/**
	 * The green component of the color.
	 */
	private float g;

	/**
	 * The blue component of the color.
	 */
	private float b;

	/**
	 * The alpha component of the color.
	 */
	private float a;

	/**
	 * Constructs a new instance of this color with r,g,b,a components set to 0.
	 */
	public Color() {
		this(0, 0, 0, 0);
	}

	/**
	 * Constructs a new instance of this color with r,g,b,a components set to the
	 * values provided by color c.
	 * 
	 * @param c the color to copy from
	 */
	public Color(Color c) {
		this(c.r, c.g, c.b, c.a);
	}

	/**
	 * Constructs a new instance of this {@link Color} with the given r,g,b
	 * components and a alpha value set to 1f.
	 * 
	 * @param r the red component of this color
	 * @param g the green component of this color
	 * @param b the blue component of this color
	 */
	public Color(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}

	/**
	 * Constructs a new instance of this {@link Color} with the given r,g,b,a
	 * components.
	 * 
	 * 
	 * @param r the red component of this color
	 * @param g the green component of this color
	 * @param b the blue component of this color
	 * @param a the alpha component of this color
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * Returns a new color instance with the specified integer r,g,b values.
	 * 
	 * @param r the red component for the color (0 to 255)
	 * @param g the green component for the color (0 to 255)
	 * @param b the blue component for the color (0 to 255)
	 * 
	 * @return the newly created color
	 */
	public static Color getColorFromInt(int r, int g, int b) {
		r = Mathf.clampInt(r, 0, 255);
		g = Mathf.clampInt(g, 0, 255);
		b = Mathf.clampInt(b, 0, 255);
		return new Color(r / 255f, g / 255f, b / 255f);
	}

	/**
	 * Adds the components of a given color to those of this color creating a new
	 * color object. Each component is added separately. If the provided color is
	 * null, an exception is thrown.
	 * 
	 * @param color the color to add to this color
	 * @return the resultant color
	 */
	public Color add(Color color) {
		return subtract(color, null);
	}

	/**
	 * Adds the components of a given color to those of this color storing the
	 * result in the given result color. Each component is added separately. If the
	 * provided color c is null, an exception is thrown. If the provided result
	 * color is null, a new color is created.
	 * 
	 * @param color  the color to add to this color
	 * @param result the color to store the result in
	 * @return the resultant color
	 */
	public Color add(Color color, Color result) {
		if (result == null)
			result = new Color();
		result.r = this.r + color.r;
		result.g = this.g + color.g;
		result.b = this.b + color.b;
		result.a = this.a + color.a;
		return result;
	}

	/**
	 * Adds the given r,g,b,a components to those of this color creating a new color
	 * object. Each component is added separately.
	 * 
	 * @param r the red component to add
	 * @param g the green component to add
	 * @param b the blue component to add
	 * @param a the alpha component to add
	 * @return the resultant color
	 */
	public Color add(float r, float g, float b, float a) {
		return new Color(this.r + r, this.g + g, this.b + b, this.a + a);
	}

	/**
	 * Adds the color c to this color internally, and returns a handle to this color
	 * for easy chaining of calls. Each component is added separately.
	 * 
	 * @param color the color to add to this color
	 * @return this
	 */
	public Color addLocal(Color color) {
		this.r += color.r;
		this.g += color.g;
		this.b += color.b;
		this.a += color.a;
		return this;
	}

	/**
	 * Adds the provided components to this color internally, and returns a handle
	 * to this color for easy chaining of calls.
	 * 
	 * @param r the red component to add
	 * @param g the green component to add
	 * @param b the blue component to add
	 * @param a the alpha component to add
	 * 
	 * @return this
	 */
	public Color addLocal(float r, float g, float b, float a) {
		this.r += r;
		this.g += g;
		this.b += b;
		this.a += a;
		return this;
	}

	/**
	 * Subtracts the components of a given color from those of this color creating a
	 * new color object. Each component is subtracted separately. If the provided
	 * color is null, an exception is thrown.
	 * 
	 * @param color the color to subtract from this color
	 * @return the resultant color
	 */
	public Color subtract(Color color) {
		return subtract(color, null);
	}

	/**
	 * Subtracts the values of a given color from those of this color storing the
	 * result in the given color. Each component is subtracted separately. If the
	 * provided color c is null, an exception is thrown. If the provided result
	 * color is null, a new color is created.
	 * 
	 * @param color  the color to subtract from this color
	 * @param result the color to store the result in
	 * @return the resultant color
	 */
	public Color subtract(Color color, Color result) {
		if (result == null)
			result = new Color();
		result.r = this.r - color.r;
		result.g = this.g - color.g;
		result.b = this.b - color.b;
		result.a = this.a - color.a;
		return result;
	}

	/**
	 * * Subtracts the given r,g,b,a components from those of this color creating a
	 * new color object. Each component is subtracted separately.
	 * 
	 * @param r the red component to subtract
	 * @param g the green component to subtract
	 * @param b the blue component to subtract
	 * @param a the alpha component to subtract
	 * @return the resultant color
	 */
	public Color subtract(float r, float g, float b, float a) {
		return new Color(this.r - r, this.g - g, this.b - b, this.a - a);
	}

	/**
	 * Subtracts the color c from this color internally, and returns a handle to
	 * this color for easy chaining of calls. Each component is subtracted
	 * separately.
	 * 
	 * @param color the color to subtract from this color
	 * @return this
	 */
	public Color subtractLocal(Color color) {
		this.r -= color.r;
		this.g -= color.g;
		this.b -= color.b;
		this.a -= color.a;
		return this;
	}

	/**
	 * Subtracts the provided components from this color internally, and returns a
	 * handle to this color for easy chaining of calls.
	 * 
	 * @param r the red component to subtract
	 * @param g the green component to subtract
	 * @param b the blue component to subtract
	 * @param a the alpha component to subtract
	 * 
	 * @return this
	 */
	public Color subtractLocal(float r, float g, float b, float a) {
		this.r -= r;
		this.g -= g;
		this.b -= b;
		this.a -= a;
		return this;
	}

	/**
	 * Divides this color by <code>a</code> internally. Each component is scaled
	 * separately
	 * 
	 * @return this
	 */
	public Color divideLocal(float a) {
		r /= a;
		g /= a;
		b /= a;
		a /= a;
		return this;
	}

	/**
	 * Clamps the components of this color between 0.0f and 1.0f internally, and
	 * returns a handle to this color for easy chaining of calls.
	 * 
	 * @return this
	 */
	public Color clampLocal() {
		r = Mathf.clamp01(r);
		g = Mathf.clamp01(g);
		b = Mathf.clamp01(b);
		a = Mathf.clamp01(a);
		return this;
	}

	/**
	 * Sets all components of this color to 0.0f internally, and returns a handle to
	 * this color for easy chaining of calls.
	 * 
	 * @return this
	 */
	public Color clearLocal() {
		r = g = b = a = 0.0f;
		return this;
	}

	/**
	 * Returns the maximum color component value: Max(r,g,b). This method does not
	 * consider the alpha component.
	 * 
	 * @return the maximum color component
	 */
	public float maxComponent() {
		return Mathf.max(new float[] { r, g, b });
	}

	/**
	 * Returns a new float array containing the r,g,b,a components of this color in
	 * that order.
	 * 
	 * @return the components of this color as array
	 */
	public float[] toArray() {
		return new float[] { r, g, b, a };
	}

	/**
	 * Stores the r,g,b,a components in the given array. If the provided store array
	 * is null a new array is created to store the components in.
	 * 
	 * @param store the array to store the components into
	 * @return store
	 * @throws IndexOutOfBoundsException if store.length < 4
	 */
	public float[] toArray(float[] store) {
		if (store == null)
			store = new float[4];
		store[0] = r;
		store[1] = g;
		store[2] = b;
		store[3] = a;
		return store;
	}

	/**
	 * Sets the r,g,b,a components of this color to the specified new values.
	 * 
	 * @param r the new red component for this color
	 * @param g the new green component for this color
	 * @param b the new blue component for this color
	 * @param a the new alpha component for this color
	 */
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * Sets the r,g,b components of this color to the specified new values. The
	 * alpha component is set to 1.0f.
	 * 
	 * @param r the new red component for this color
	 * @param g the new green component for this color
	 * @param b the new blue component for this color
	 */
	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f;
	}

	/**
	 * Returns the red component of this color as float value (0f to 1f).
	 * 
	 * @return the red component of this color (0f to 1f)
	 */
	public float getRed() {
		return r;
	}

	/**
	 * Returns the green component of this color as float value (0f to 1f).
	 * 
	 * @return the green component of this color (0f to 1f)
	 */
	public float getGreen() {
		return g;
	}

	/**
	 * Returns the blue component of this color as float value (0f to 1f).
	 * 
	 * @return the blue component of this color (0f to 1f)
	 */
	public float getBlue() {
		return b;
	}

	/**
	 * Returns the alpha component of this color as float value (0f to 1f).
	 * 
	 * @return the alpha component of this color (0f to 1f)
	 */
	public float getAlpha() {
		return a;
	}

	/**
	 * Returns the red component of this color as an integer value (0 to 255).
	 * 
	 * @return the red component of this color (0 to 255)
	 */
	public int getRedInt() {
		return (int) (r * 255 + 0.5);
	}

	/**
	 * Returns the green component of this color as an integer value (0 to 255).
	 * 
	 * @return the green component of this color (0 to 255)
	 */
	public int getGreenInt() {
		return (int) (g * 255 + 0.5);
	}

	/**
	 * Returns the blue component of this color as an integer value (0 to 255).
	 * 
	 * @return the blue component of this color (0 to 255)
	 */
	public int getBlueInt() {
		return (int) (b * 255 + 0.5);
	}

	/**
	 * Returns the alpha component of this color as an integer value (0 to 255).
	 * 
	 * @return the alpha component of this color (0 to 255)
	 */
	public int getAlphaInt() {
		return (int) (a * 255 + 0.5);
	}

	/**
	 * Returns the RGBA value representing the color. (Bits 24-31 are alpha, 16-23
	 * are red, 8-15 are green, 0-7 are blue).
	 * 
	 * @return the RGBA value of this color as integer
	 */
	public int getRGBA() {
		int r = getRedInt();
		int g = getGreenInt();
		int b = getBlueInt();
		int a = getAlphaInt();
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}

	/**
	 * Returns a unique hash code for this color object based on it's values. If two
	 * colors are logically equivalent, they will return the same hash code value.
	 * 
	 * @return the hash code value of this color
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(a);
		result = prime * result + Float.floatToIntBits(b);
		result = prime * result + Float.floatToIntBits(g);
		result = prime * result + Float.floatToIntBits(r);
		return result;
	}

	/**
	 * Determines if this color is equals to the given object obj.
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
		Color other = (Color) obj;
		return Float.floatToIntBits(r) == Float.floatToIntBits(other.r)
				&& Float.floatToIntBits(g) == Float.floatToIntBits(other.g)
				&& Float.floatToIntBits(b) == Float.floatToIntBits(other.b)
				&& Float.floatToIntBits(a) == Float.floatToIntBits(other.a);
	}

	/**
	 * Returns a string representation of this color.
	 * 
	 * @return a string representation of this color
	 */
	@Override
	public String toString() {
		return "Color [r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]";
	}

}

package math;

public class Color {

	public static final Color BLACK = new Color(0f, 0f, 0f, 1f);
	public static final Color BLUE = new Color(0f, 0f, 1f, 1f);
	public static final Color CLEAR = new Color(0f, 0f, 0f, 0f);
	public static final Color CYAN = new Color(0f, 1f, 1f, 1f);
	public static final Color DARK_GRAY = new Color(0.25f, 0.25f, 0.25f, 1.0f);
	public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1f);
	public static final Color GREEN = new Color(0f, 1f, 0f, 1f);
	public static final Color LIGHT_GRAY = new Color(0.75f, 0.75f, 0.75f, 1f);
	public static final Color MAGENTA = new Color(1f, 0f, 1f, 1f);
	public static final Color RED = new Color(1f, 0f, 0f, 1f);
	public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
	public static final Color YELLOW = new Color(1f, 1f, 0f, 1f);

	private float r;
	private float g;
	private float b;
	private float a;

	public Color() {
		this(0, 0, 0, 0);
	}

	public Color(Color c) {
		this(c.r, c.g, c.b, c.a);
	}

	public Color(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public static Color getColorFromInt(int r, int g, int b) {
		r = Mathf.clampInt(r, 0, 255);
		g = Mathf.clampInt(g, 0, 255);
		b = Mathf.clampInt(b, 0, 255);
		return new Color(r / 255f, g / 255f, b / 255f);
	}

	public Color add(Color color) {
		return subtract(color, null);
	}

	public Color add(Color color, Color result) {
		if (result == null)
			result = new Color();
		result.r = this.r + color.r;
		result.g = this.g + color.g;
		result.b = this.b + color.b;
		result.a = this.a + color.a;
		return result;
	}

	public Color add(float r, float g, float b, float a) {
		return new Color(this.r + r, this.g + g, this.b + b, this.a + a);
	}

	public Color addLocal(Color color) {
		this.r += color.r;
		this.g += color.g;
		this.b += color.b;
		this.a += color.a;
		return this;
	}

	public Color addLocal(float r, float g, float b, float a) {
		this.r += r;
		this.g += g;
		this.b += b;
		this.a += a;
		return this;
	}

	public Color subtract(Color color) {
		return subtract(color, null);
	}

	public Color subtract(Color color, Color result) {
		if (result == null)
			result = new Color();
		result.r = this.r - color.r;
		result.g = this.g - color.g;
		result.b = this.b - color.b;
		result.a = this.a - color.a;
		return result;
	}

	public Color subtract(float r, float g, float b, float a) {
		return new Color(this.r - r, this.g - g, this.b - b, this.a - a);
	}

	public Color subtractLocal(Color color) {
		this.r -= color.r;
		this.g -= color.g;
		this.b -= color.b;
		this.a -= color.a;
		return this;
	}

	public Color subtractLocal(float r, float g, float b, float a) {
		this.r -= r;
		this.g -= g;
		this.b -= b;
		this.a -= a;
		return this;
	}

	public Color divideLocal(float a) {
		r /= a;
		g /= a;
		b /= a;
		a /= a;
		return this;
	}

	public Color clampLocal() {
		r = Mathf.clamp01(r);
		g = Mathf.clamp01(g);
		b = Mathf.clamp01(b);
		a = Mathf.clamp01(a);
		return this;
	}

	public Color clearLocal() {
		r = g = b = a = 0.0f;
		return this;
	}

	public float maxComponent() {
		return Mathf.max(new float[] { r, g, b });
	}

	public float[] toArray() {
		return new float[] { r, g, b, a };
	}

	public float[] toArray(float[] store) {
		if (store == null)
			store = new float[4];
		store[0] = r;
		store[1] = g;
		store[2] = b;
		store[3] = a;
		return store;
	}

	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f;
	}

	public float getRed() {
		return r;
	}

	public float getGreen() {
		return g;
	}

	public float getBlue() {
		return b;
	}

	public float getAlpha() {
		return a;
	}

	public int getRedInt() {
		return (int) (r * 255 + 0.5);
	}

	public int getGreenInt() {
		return (int) (g * 255 + 0.5);
	}

	public int getBlueInt() {
		return (int) (b * 255 + 0.5);
	}

	public int getAlphaInt() {
		return (int) (a * 255 + 0.5);
	}

	public int getRGBA() {
		int r = getRedInt();
		int g = getGreenInt();
		int b = getBlueInt();
		int a = getAlphaInt();
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color other = (Color) obj;
		if (Float.floatToIntBits(a) != Float.floatToIntBits(other.a))
			return false;
		if (Float.floatToIntBits(b) != Float.floatToIntBits(other.b))
			return false;
		if (Float.floatToIntBits(g) != Float.floatToIntBits(other.g))
			return false;
		if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Color [r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]";
	}

}

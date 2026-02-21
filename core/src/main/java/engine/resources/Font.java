package engine.resources;

import java.util.Objects;

/**
 * Represents a font, including its name, size, and style.
 *
 * <p>The {@link Font} class encapsulates the characteristics of a font that can be used for text
 * rendering. It includes properties for the font's name, size, and style (plain, bold, or italic).
 * The font is immutable, meaning its properties cannot be modified after instantiation.
 *
 * <p>Font objects are typically used to define the appearance of text in a rendering context, where
 * the font's name specifies the typeface, the size defines the text size, and the style determines
 * whether the text is rendered in plain, bold, or italic form.
 *
 * <p>Font style constants are provided for ease of use:
 *
 * <ul>
 *   <li>{@link #PLAIN} - Represents a normal, unstyled font.
 *   <li>{@link #BOLD} - Represents a bold font style.
 *   <li>{@link #ITALIC} - Represents an italic font style.
 * </ul>
 */
public class Font {

  // Font style constants
  public static final int PLAIN = 0;
  public static final int BOLD = 1;
  public static final int ITALIC = 2;

  private final String name;
  private final int size;
  private final int style;

  /**
   * Constructs a {@link Font} object with the specified name, size, and style.
   *
   * <p>This constructor allows you to create a font with a specific typeface, size, and style. If
   * you need a plain font, you can use the {@link #PLAIN} constant; for bold or italic fonts, use
   * the {@link #BOLD} or {@link #ITALIC} constants, respectively.
   *
   * @param name The name of the font (e.g., "Arial", "Times New Roman").
   * @param size The size of the font in points.
   * @param style The style of the font, which can be one of the following: {@link #PLAIN}, {@link
   *     #BOLD}, or {@link #ITALIC}.
   */
  public Font(String name, int size, int style) {
    this.name = name;
    this.size = size;
    this.style = style;
  }

  /**
   * Retrieves the name of the font.
   *
   * @return The name of the font (e.g., "Arial").
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the size of the font.
   *
   * @return The size of the font in points.
   */
  public int getSize() {
    return size;
  }

  /**
   * Retrieves the style of the font.
   *
   * @return The style of the font, which can be one of the following: {@link #PLAIN}, {@link
   *     #BOLD}, or {@link #ITALIC}.
   */
  public int getStyle() {
    return style;
  }

  /**
   * Computes a hash code for this {@link Font} object based on its name, size, and style.
   *
   * @return The hash code for this font.
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, size, style);
  }

  /**
   * Compares this {@link Font} object to another object for equality.
   *
   * <p>This method returns {@code true} if the specified object is a {@link Font} and has the same
   * name, size, and style as this font. Otherwise, it returns {@code false}.
   *
   * @param obj The object to compare this font to.
   * @return {@code true} if the object is equal to this font, {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Font other = (Font) obj;
    return Objects.equals(name, other.name) && size == other.size && style == other.style;
  }
}

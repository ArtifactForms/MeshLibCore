package engine.backend.processing;

import java.util.HashMap;
import java.util.Map;

import engine.resources.Font;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * A class to manage fonts in Processing by caching and loading fonts on demand. It supports
 * different font styles such as plain, bold, and italic.
 */
public class ProcessingFontManager {

  /** Cache for fonts to avoid redundant font loading */
  private Map<Font, PFont> fontCache = new HashMap<>();

  /** The PApplet instance used for creating fonts */
  private PApplet p;

  /**
   * Constructor to initialize the font manager with a PApplet instance.
   *
   * @param p the PApplet instance used for creating fonts
   */
  public ProcessingFontManager(PApplet p) {
    this.p = p;
  }

  private void loadFont(int size, boolean smooth) {
    try {
      // Load the font from a custom folder
      String filePath =
          "monogram/ttf/monogram-extended.ttf"; // Relative path to your custom font folder
      String fontPath =
          ProcessingTextureLoader.class.getClassLoader().getResource("fonts/" + filePath).getPath();

      PFont font = p.createFont(fontPath, size);
      fontCache.put(new Font("monogram-extended", size, Font.PLAIN), font);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads a font based on the provided Font object. If the font is already cached, it returns the
   * cached version. Otherwise, it creates a new font and stores it in the cache. Font smoothing is
   * enabled by default.
   *
   * @param font the Font object to load
   * @return the corresponding PFont object
   */
  public PFont loadFont(Font font) {
    if (!fontCache.containsKey(font)) {
      // Enable smoothing by default
      boolean smooth = true;
      // Create the PFont and add it to the cache
      if (font.getName().startsWith("monogram")) {
        loadFont(font.getSize(), smooth);
      } else {
        PFont pFont = p.createFont(getFontName(font), font.getSize(), smooth);
        fontCache.put(font, pFont);
      }
    }
    return fontCache.get(font);
  }

  /**
   * Constructs the font name string, appending the appropriate style based on the Font object's
   * style (PLAIN, BOLD, or ITALIC).
   *
   * @param font the Font object
   * @return the constructed font name string with the style
   */
  private String getFontName(Font font) {
    String style = "";
    switch (font.getStyle()) {
      case Font.BOLD:
        style = " Bold";
        break;
      case Font.ITALIC:
        style = " Italic";
        break;
      case Font.PLAIN:
        style = "";
        break;
      default:
        throw new IllegalArgumentException("Unexpected value: " + font.getStyle());
    }
    return font.getName() + style;
  }
}

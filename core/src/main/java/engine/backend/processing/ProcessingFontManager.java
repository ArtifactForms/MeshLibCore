package engine.backend.processing;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import engine.resources.Font;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Manages Processing fonts and caches loaded fonts. Supports custom monogram fonts and system
 * fonts.
 */
public class ProcessingFontManager {

  /** Cache for loaded fonts. */
  private final Map<Font, PFont> fontCache = new HashMap<>();

  /** Processing application instance. */
  private final PApplet p;

  /**
   * Creates a font manager.
   *
   * @param p Processing application instance
   */
  public ProcessingFontManager(PApplet p) {
    this.p = p;
  }

  /**
   * Loads a custom monogram font from the classpath.
   *
   * @param font font configuration
   * @param smooth whether font smoothing is enabled
   * @return loaded Processing font
   */
  private PFont loadMonogramFont(Font font, boolean smooth) {
    String filePath = "/resources/fonts/monogram/ttf/monogram-extended.ttf";

    try (InputStream stream = ProcessingFontManager.class.getResourceAsStream(filePath)) {

      if (stream == null) {
        throw new IllegalArgumentException("Font not found: " + filePath);
      }

      File temp = File.createTempFile("monogram-", ".ttf");
      temp.deleteOnExit();

      Files.copy(stream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

      return p.createFont(temp.getAbsolutePath(), font.getSize(), smooth);

    } catch (Exception e) {
      throw new RuntimeException("Failed to load font: " + filePath, e);
    }
  }

  /**
   * Loads a font or returns its cached instance.
   *
   * @param font font configuration
   * @return corresponding Processing font
   */
  public PFont loadFont(Font font) {
    PFont cachedFont = fontCache.get(font);

    if (cachedFont != null) {
      return cachedFont;
    }

    boolean smooth = true;
    PFont loadedFont;

    if (font.getName().startsWith("monogram")) {
      loadedFont = loadMonogramFont(font, smooth);
    } else {
      loadedFont = p.createFont(getFontName(font), font.getSize(), smooth);
    }

    fontCache.put(font, loadedFont);

    return loadedFont;
  }

  /**
   * Constructs the font name including its style.
   *
   * @param font font configuration
   * @return font name with style
   */
  private String getFontName(Font font) {
    String style;

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
        throw new IllegalArgumentException("Unexpected font style: " + font.getStyle());
    }

    return font.getName() + style;
  }
}

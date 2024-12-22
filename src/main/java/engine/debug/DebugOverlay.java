package engine.debug;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import math.Color;
import workspace.ui.Graphics;

/**
 * The {@code DebugOverlay} class is responsible for displaying a customizable debug overlay on the
 * screen. It provides functionality for managing debug items, rendering them with optional
 * alignment, and customizing appearance such as line spacing and visibility of categories.
 */
public class DebugOverlay {

  private final Map<String, Map<String, String>> debugItems = new LinkedHashMap<>();

  private final Map<String, Boolean> categoryVisibility = new HashMap<>();

  /** Toggle for overall visibility. */
  private boolean visible = true;

  /** Toggle for aligning values in columns. */
  private boolean alignValues = true;

  /** Spacing between individual lines in pixels (20px by default). */
  private int lineSpacing = 20;

  /** Spacing between categories in pixels (40px by default). */
  private int categorySpacing = 40;

  /**
   * Sets the visibility of the debug overlay.
   *
   * @param visible {@code true} to make the overlay visible, {@code false} otherwise.
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * Checks if the debug overlay is visible.
   *
   * @return {@code true} if the overlay is visible, {@code false} otherwise.
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Toggles the alignment of debug item values.
   *
   * @param alignValues {@code true} to align values, {@code false} for no alignment.
   */
  public void setAlignValues(boolean alignValues) {
    this.alignValues = alignValues;
  }

  /**
   * Sets the spacing between individual lines in the overlay.
   *
   * @param spacing the line spacing in pixels.
   */
  public void setLineSpacing(int spacing) {
    this.lineSpacing = spacing;
  }

  /**
   * Sets the spacing between categories in the overlay.
   *
   * @param spacing the category spacing in pixels.
   */
  public void setCategorySpacing(int spacing) {
    this.categorySpacing = spacing;
  }

  /**
   * Sets the visibility of a specific debug category.
   *
   * @param category the name of the category.
   * @param visible {@code true} to make the category visible, {@code false} otherwise.
   */
  public void setCategoryVisible(String category, boolean visible) {
    categoryVisibility.put(category, visible);
  }

  /**
   * Checks if a specific debug category is visible.
   *
   * @param category the name of the category.
   * @return {@code true} if the category is visible, {@code false} otherwise.
   */
  public boolean isCategoryVisible(String category) {
    return categoryVisibility.getOrDefault(category, true);
  }

  /**
   * Adds or updates a debug item. If the specified category or key does not exist, it will be
   * created.
   *
   * @param category the category under which the item belongs.
   * @param key the key of the debug item.
   * @param value the value of the debug item.
   */
  public void setDebugItem(String category, String key, String value) {
    debugItems.computeIfAbsent(category, k -> new LinkedHashMap<>()).put(key, value);
  }

  /**
   * Adds or updates a debug item with a float value. Internally, the float is converted to a
   * string.
   *
   * @param category the category under which the item belongs.
   * @param key the key of the debug item.
   * @param value the float value of the debug item.
   */
  public void setDebugItem(String category, String key, float value) {
    setDebugItem(category, key, String.valueOf(value));
  }

  /**
   * Renders the debug overlay. The items are displayed grouped by categories, with optional
   * alignment for values.
   *
   * @param g the {@code Graphics} context used for rendering.
   */
  public void render(Graphics g) {
    if (!visible) {
      return;
    }

    g.pushMatrix();
    g.translate(10, 20);
    g.setColor(Color.WHITE);

    int yOffset = 0;
    int columnWidth = 200; // Width for aligning values

    for (Map.Entry<String, Map<String, String>> category : debugItems.entrySet()) {
      if (!isCategoryVisible(category.getKey())) {
        continue;
      }

      // Render category header
      g.text(category.getKey() + ":", 0, yOffset);
      yOffset += lineSpacing;
      g.text("===========================", 0, yOffset);
      yOffset += lineSpacing;

      // Render items
      for (Map.Entry<String, String> item : category.getValue().entrySet()) {
        if (alignValues) {
          renderAlignedText(g, item.getKey(), item.getValue(), 0, yOffset, columnWidth);
        } else {
          g.text(item.getKey() + ": " + item.getValue(), 0, yOffset);
        }
        yOffset += lineSpacing;
      }

      // Add spacing between categories
      yOffset += categorySpacing - lineSpacing;
    }

    g.popMatrix();
  }

  /**
   * Helper method to render aligned text. Aligns the value column based on a specified width.
   *
   * @param g the {@code Graphics} context used for rendering.
   * @param key the key text.
   * @param value the value text.
   * @param x the x-coordinate to start rendering.
   * @param y the y-coordinate to render the text.
   * @param columnWidth the width reserved for the key and value columns.
   */
  private void renderAlignedText(
      Graphics g, String key, String value, int x, int y, int columnWidth) {
    g.text(key + ": ", x, y); // Draw the key
    int valueX = x + columnWidth - (int) g.textWidth(value); // Calculate
    // x-position for
    // value
    g.text(value, valueX, y); // Draw the value aligned to the right
  }
}

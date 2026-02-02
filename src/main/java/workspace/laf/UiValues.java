package workspace.laf;

import java.awt.Font;
import java.util.HashMap;

import math.Color;

public class UiValues {

  public static final Color UI_ELEMENT_FOREGROUND = Color.getColorFromInt(250, 250, 250);

  public static final Color BASE_BLUE = Color.getColorFromInt(82, 120, 180);

  public static final Color BASE_ORANGE = Color.getColorFromInt(199, 135, 83);

  public static final Color SLIDER_BACKGROUND_COLOR = Color.getColorFromInt(35, 35, 35);

  public static final Color SLIDER_LIGHT = Color.getColorFromInt(89, 89, 89);

  //	public static final float TEXT_SIZE = 12;

  private static HashMap<String, Object> mappings;

  static {
    mappings = new HashMap<String, Object>();
  }

  public static void put(String key, Object value) {
    mappings.put(key, value);
  }

  public static Color getColor(String key) {
    return (Color) mappings.get(key);
  }

  public static Font getFont(String key) {
    return (Font) mappings.get(key);
  }

  public static int getInt(String key) {
    return (int) mappings.get(key);
  }
}

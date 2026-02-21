package workspace.render;

import math.Color;

public class ColorGenerator {

  private int i = 0;

  public Color next() {
    i++;
    java.awt.Color c = new java.awt.Color(i);
    Color c1 = Color.getColorFromInt(c.getRed(), c.getGreen(), c.getBlue());
    return c1;
  }
}

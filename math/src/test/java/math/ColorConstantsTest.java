package math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ColorConstantsTest {

  @Test
  void tesConstantBlack() {
    Color color = Color.BLACK;
    assertEquals(color.getRed(), 0f, 0f);
    assertEquals(color.getGreen(), 0f, 0f);
    assertEquals(color.getBlue(), 0f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantBlue() {
    Color color = Color.BLUE;
    assertEquals(color.getRed(), 0f, 0f);
    assertEquals(color.getGreen(), 0f, 0f);
    assertEquals(color.getBlue(), 1f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantClear() {
    Color color = Color.CLEAR;
    assertEquals(color.getRed(), 0f, 0f);
    assertEquals(color.getGreen(), 0f, 0f);
    assertEquals(color.getBlue(), 0f, 0f);
    assertEquals(color.getAlpha(), 0f, 0f);
  }

  @Test
  void testConstantCyan() {
    Color color = Color.CYAN;
    assertEquals(color.getRed(), 0f, 0f);
    assertEquals(color.getGreen(), 1f, 0f);
    assertEquals(color.getBlue(), 1f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantDarkGray() {
    Color color = Color.DARK_GRAY;
    assertEquals(color.getRed(), 0.25f, 0f);
    assertEquals(color.getGreen(), 0.25f, 0f);
    assertEquals(color.getBlue(), 0.25f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantGray() {
    Color color = Color.GRAY;
    assertEquals(color.getRed(), 0.5f, 0);
    assertEquals(color.getGreen(), 0.5f, 0);
    assertEquals(color.getBlue(), 0.5f, 0);
    assertEquals(color.getAlpha(), 1, 0);
  }

  @Test
  void testConstantLightGray() {
    Color color = Color.LIGHT_GRAY;
    assertEquals(color.getRed(), 0.75f, 0f);
    assertEquals(color.getGreen(), 0.75f, 0f);
    assertEquals(color.getBlue(), 0.75f, 0f);
    assertEquals(color.getAlpha(), 1, 0f);
  }

  @Test
  void testConstantMagenta() {
    Color color = Color.MAGENTA;
    assertEquals(color.getRed(), 1f, 0f);
    assertEquals(color.getGreen(), 0f, 0f);
    assertEquals(color.getBlue(), 1f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantRed() {
    Color color = Color.RED;
    assertEquals(color.getRed(), 1f, 0f);
    assertEquals(color.getGreen(), 0f, 0f);
    assertEquals(color.getBlue(), 0f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantWhite() {
    Color color = Color.WHITE;
    assertEquals(color.getRed(), 1f, 0f);
    assertEquals(color.getGreen(), 1f, 0f);
    assertEquals(color.getBlue(), 1f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantYellow() {
    Color color = Color.YELLOW;
    assertEquals(color.getRed(), 1f, 0f);
    assertEquals(color.getGreen(), 1f, 0f);
    assertEquals(color.getBlue(), 0f, 0f);
    assertEquals(color.getAlpha(), 1f, 0f);
  }

  @Test
  void testConstantAplhaOpaqueTolerance() {
    float value = Color.ALPHA_OPAQUE_TOLERANCE;
    assertEquals(value, 1e-4f, 0f);
  }
}

package engine.runtime.debug;

import math.Color;
import workspace.ui.Graphics;

public class FloatHistoryGraphRenderer {

  private final String label;
  private final String unit;
  private final FloatHistory history;
  private final FloatScalingStrategy scalingStrategy;

  private float[] referenceLines = new float[0];

  // UI tuning
  private final int headerHeight = 22;
  private final int padding = 6;

  public FloatHistoryGraphRenderer(
      String label, String unit, FloatHistory history, FloatScalingStrategy scalingStrategy) {

    this.label = label;
    this.unit = unit;
    this.history = history;
    this.scalingStrategy = scalingStrategy;
  }

  public void setReferenceLines(float... lines) {
    this.referenceLines = lines;
  }

  public void render(Graphics g, int x, int y, int width, int height) {

    int size = history.getSize();
    int capacity = history.getCapacity();

    if (size < 2) return;

    float[] values = history.getValues();
    int writeIndex = history.getWriteIndex();

    float displayMax = scalingStrategy.getDisplayMax(history);
    if (displayMax <= 0f) displayMax = 1f;

    float currentValue = values[(writeIndex - 1 + capacity) % capacity];

    // ===== Background Panel =====
    g.setColor(new Color(0f, 0f, 0f, 0.35f));
    g.fillRect(x, y, width, height);

    // ===== Header Background =====
    g.setColor(new Color(0f, 0f, 0f, 0.6f));
    g.fillRect(x, y, width, headerHeight);

    // ===== Header Text =====
    g.setColor(Color.WHITE);

    String valueText = format(currentValue) + " " + unit;
    String headerText = label + "  " + valueText;

    g.text(headerText, x + padding, y + headerHeight - 7);

    // ===== Graph Area =====
    int graphX = x;
    int graphY = y + headerHeight;
    int graphHeight = height - headerHeight;

    float step = (float) width / (capacity - 1);
    int start = (writeIndex - size + capacity) % capacity;

    // ===== Reference Lines =====
    for (float ref : referenceLines) {

      if (ref > displayMax) continue;

      float normalized = ref / displayMax;
      int yRef = graphY + graphHeight - Math.round(normalized * graphHeight);

      g.setColor(new Color(1f, 1f, 1f, 0.15f));
      g.drawLine(graphX, yRef, graphX + width, yRef);
    }

    // ===== Graph Line =====
    g.setColor(Color.WHITE);

    for (int i = 0; i < size - 1; i++) {

      int idx0 = (start + i) % capacity;
      int idx1 = (start + i + 1) % capacity;

      float v0 = clamp(values[idx0], 0f, displayMax);
      float v1 = clamp(values[idx1], 0f, displayMax);

      int x0 = graphX + Math.round(i * step);
      int x1 = graphX + Math.round((i + 1) * step);

      int y0 = graphY + graphHeight - Math.round((v0 / displayMax) * graphHeight);
      int y1 = graphY + graphHeight - Math.round((v1 / displayMax) * graphHeight);

      g.drawLine(x0, y0, x1, y1);
    }
  }

  private float clamp(float v, float min, float max) {
    return (v < min) ? min : (v > max ? max : v);
  }

  private String format(float value) {
    return String.format("%.2f", value);
  }
}

package engine.runtime.debug;

import java.util.Queue;

import engine.Timer;
import math.Color;
import workspace.ui.Graphics;

public class FpsGraph {

  private final FpsHistory fpsHistory;

  public FpsGraph(FpsHistory fpsHistory) {
    this.fpsHistory = fpsHistory;
  }

  public void render(Graphics g) {
    renderFpsGraph(g, 300, 100);
  }

  public void update(Timer timer) {
    fpsHistory.addSample(timer.getFrameRate());
  }

  private void renderFpsGraph(Graphics g, int width, int height) {
    int x = g.getWidth() - width - 20;
    int y = 30;

    Queue<Float> fpsValues = fpsHistory.getHistory();

    float maxFps = fpsHistory.getMaxFps();

    // Draw background
    g.setColor(new Color(0.5f, 0.5f, 0.5f, 0.3f));
    g.fillRect(x, y, width, height);

    // Draw FPS values as a line graph
    g.setColor(Color.RED);
    int i = 0;
    int prevX = x, prevY = y + height;
    float step = (float) width / (float) fpsValues.size();

    for (float fps : fpsValues) {
      int barHeight = (int) ((fps / maxFps) * height);
      int currentX = x + (int) (i * step);
      int currentY = y + height - barHeight;

      g.drawLine(prevX, prevY, currentX, currentY);

      prevX = currentX;
      prevY = currentY;
      i++;
    }

    // Draw axis labels
    g.setColor(Color.BLACK);
    renderAxisLabels(g, x + 1, y + 1, height);
    g.setColor(Color.WHITE);
    renderAxisLabels(g, x, y, height);
  }

  private void renderAxisLabels(Graphics g, int x, int y, int height) {
    float maxFps = fpsHistory.getMaxFps();

    g.text("0 FPS", x, y + height + 15); // Min FPS label
    g.text((int) maxFps + " FPS", x, y - 5); // Max FPS label
  }
}

package engine.runtime.debug;

import engine.Timer;
import engine.render.Graphics;

public class Debug {

  private FloatHistoryGraphRenderer tpfGraph;
  private FloatHistory tpfHistory = new FloatHistory(300);

  private FloatHistory fpsHistory = new FloatHistory(300);
  FloatHistoryGraphRenderer fpsGraph;

  private static final Debug INSTANCE = new Debug();

  private Debug() {
    tpfGraph = new FloatHistoryGraphRenderer("TPF", "ms", tpfHistory, new FixedScaling(33.3f));
    tpfGraph.setReferenceLines(16.6f, 33.3f);

    fpsGraph = new FloatHistoryGraphRenderer("FPS", "fps", fpsHistory, new DynamicMaxScaling());
    fpsGraph.setReferenceLines(30f, 60f, 120f);
  }

  public void update(Timer timer) {
    tpfHistory.addSample(timer.getTimePerFrame() * 1000f);
    fpsHistory.addSample(timer.getFrameRate());
  }

  public static Debug getInstance() {
    return INSTANCE;
  }

  public void render(Graphics g) {
    tpfGraph.render(g, g.getWidth() - 310, 10, 300, 80);
    fpsGraph.render(g, g.getWidth() - 310, 100, 300, 80);
  }
}

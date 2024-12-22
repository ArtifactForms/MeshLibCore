package engine.debug;

public class DrawCallCounter {

  private int drawCalls = 0;

  public void increment() {
    drawCalls++;
  }

  public int getCount() {
    return drawCalls;
  }

  public void reset() {
    drawCalls = 0;
  }
}

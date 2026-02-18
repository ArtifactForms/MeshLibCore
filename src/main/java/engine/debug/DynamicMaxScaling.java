package engine.debug;

public class DynamicMaxScaling implements FloatScalingStrategy {

  @Override
  public float getDisplayMax(FloatHistory history) {
    float max = history.getMax();
    return max <= 0f ? 1f : max;
  }
}

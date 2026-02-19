package engine.runtime.debug;

public class FixedScaling implements FloatScalingStrategy {

  private final float maxValue;

  public FixedScaling(float maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public float getDisplayMax(FloatHistory history) {
    return maxValue;
  }
}

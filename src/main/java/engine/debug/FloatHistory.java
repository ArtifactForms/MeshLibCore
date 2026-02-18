package engine.debug;

public class FloatHistory {

  private final float[] values;
  private int writeIndex = 0;
  private int size = 0;

  public FloatHistory(int capacity) {
    this.values = new float[capacity];
  }

  public void addSample(float valueMs) {
    values[writeIndex] = valueMs;
    writeIndex = (writeIndex + 1) % values.length;

    if (size < values.length) {
      size++;
    }
  }

  public float[] getValues() {
    return values;
  }

  public int getWriteIndex() {
    return writeIndex;
  }

  public int getSize() {
    return size;
  }

  public int getCapacity() {
    return values.length;
  }

  public float getAverage() {
    if (size == 0) return 0f;

    float sum = 0f;
    for (int i = 0; i < size; i++) {
      sum += values[i];
    }
    return sum / size;
  }

  public float getMin() {
    if (size == 0) return 0f;

    float min = Float.MAX_VALUE;
    for (int i = 0; i < size; i++) {
      if (values[i] < min) min = values[i];
    }
    return min;
  }

  public float getMax() {
    if (size == 0) return 0f;

    float max = Float.MIN_VALUE;
    for (int i = 0; i < size; i++) {
      if (values[i] > max) max = values[i];
    }
    return max;
  }
}

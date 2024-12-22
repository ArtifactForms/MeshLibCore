package workspace.ui.elements;

import math.Mathf;
import workspace.ui.UiComponent;
import workspace.ui.border.Insets;
import workspace.ui.event.ISliderCallBack;
import workspace.ui.renderer.SliderRenderer;

/**
 * A customizable UI slider component for selecting a value within a defined range. Supports
 * dragging and value change callbacks.
 */
public class UiSlider extends UiComponent {

  private float value;

  private float minValue = 0;

  private float maxValue = 1;

  private float handlePosition;

  private String text = "Slider";

  private ISliderCallBack callback;

  public UiSlider() {
    setRenderer(new SliderRenderer());
  }

  @Override
  public void onMouseDragged(int mouseX, int mouseY) {
    super.onMouseDragged(mouseX, mouseY);
    updateHandlePosition(mouseX);
    updateValue();
    if (callback != null) {
      callback.valueChanged(value);
    }
  }

  @Override
  public void onMouseClicked(int mouseX, int mouseY) {
    super.onMouseClicked(mouseX, mouseY);
    updateHandlePosition(mouseX);
    updateValue();
    if (callback != null) {
      callback.valueChanged(value);
    }
  }

  private void updateHandlePosition(int mouseX) {
    Insets insets = getInsets();
    int trackStart = getX() + insets.getLeft(); // Account for global x position
    int trackWidth = getWidth() - insets.getHorizontalInsets();

    // Adjust handlePosition relative to the track's start position
    handlePosition = Mathf.clamp(mouseX - trackStart, 0, trackWidth);
  }

  private void updateValue() {
    Insets insets = getInsets();
    int trackWidth = getWidth() - insets.getHorizontalInsets();

    value = Mathf.map(handlePosition, 0, trackWidth, minValue, maxValue);
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = Mathf.clamp(value, minValue, maxValue);
    updateHandlePositionFromValue();
  }

  private void updateHandlePositionFromValue() {
    Insets insets = getInsets();
    int trackWidth = getWidth() - insets.getHorizontalInsets();

    //    handlePosition = Mathf.map(value, minValue, maxValue, 0, trackWidth);
  }

  public float getMinValue() {
    return minValue;
  }

  public void setMinValue(float minValue) {
    this.minValue = minValue;
    updateHandlePositionFromValue();
  }

  public float getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(float maxValue) {
    this.maxValue = maxValue;
    updateHandlePositionFromValue();
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public ISliderCallBack getSliderCallback() {
    return callback;
  }

  public void setSliderCallBack(ISliderCallBack callback) {
    this.callback = callback;
  }

  public float getHandlePosition() {
    return handlePosition;
  }
}

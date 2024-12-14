package workspace.ui;

import math.Mathf;
import workspace.laf.UiValues;
import workspace.ui.border.Insets;
import workspace.ui.event.ISliderCallBack;

public class Slider extends UiComponent {

	private float value;

	private float minValue = 0;

	private float maxValue = 3;

	private float posX;

	private ISliderCallBack sliderCallBack;

	private String text = "Slider";

	@Override
	public void renderSelf(Graphics g) {
		Insets insets = getInsets();

		g.setColor(getBackground());
		g.fillRect(insets.getLeft(), insets.getRight(),
		    width - insets.getHorizontalInsets(),
		    height - insets.getVerticalInsets());

		g.setColor(getForeground());
		g.fillRect(getWidth() + posX, 0, 5, getHeight());

		g.setColor(UiValues.SLIDER_LIGHT);
		g.fillRect(0, 0, getWidth() + posX, getHeight());

		g.setColor(foreground);
		g.text(" " + value, 4, g.getTextSize() + g.textDescent());
		g.text(text, getWidth() + 10, g.getTextSize() + g.textDescent());
	}

	public void onMouseDragged(int x, int y) {
		posX = x - getWidth() - getX();
		clampPosX(); // Ensure posX stays within bounds
		updateValue();
		if (sliderCallBack != null)
			sliderCallBack.valueChanged(value);
	}

	@Override
	public void onMouseClicked(int x, int y) {
		// Optionally, jump posX to the clicked position
	}

	private void updateValue() {
		// Map posX to the slider's value range
//		value = Mathf.map(posX, 0, getWidth(), minValue, maxValue);
//		value = Mathf.clamp(value, minValue, maxValue); // Ensure value stays in range
		value = maxValue + Mathf.map(posX, 0, getWidth(), minValue, maxValue);
	}

	private void updatePosX() {
//		posX = Mathf.map(value, minValue, maxValue, x, x + width);

		// Map value back to posX
//		posX = Mathf.map(value, minValue, maxValue, x, x + getWidth());
//		clampPosX(); // Ensure posX stays within bounds
	}

	private void clampPosX() {
//		// Ensure posX doesn't exceed slider's drawable area
//		posX = Mathf.clamp(posX, 0, getWidth());
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
//		this.value = Mathf.clamp(value, minValue, maxValue); // Clamp the value
		updatePosX(); // Update posX to reflect the new value
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ISliderCallBack getSliderCallBack() {
		return sliderCallBack;
	}

	public void setSliderCallBack(ISliderCallBack sliderCallBack) {
		this.sliderCallBack = sliderCallBack;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
		updatePosX(); // Update posX to reflect any change in range
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		updatePosX(); // Update posX to reflect any change in range
	}
}
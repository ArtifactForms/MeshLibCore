package workspace.ui;

import math.Mathf;
import workspace.laf.UiValues;
import workspace.ui.border.Insets;

public class Slider extends UiComponent {

    private float value;

    private float minValue = 0;

    private float maxValue = 3;

    private float posX;

    private ISliderCallBack sliderCallBack;

    private String text = "Slider";

    @Override
    public void onDraw(Graphics g) {
        Insets insets = getInsets();

        g.setColor(getBackground());
        g.fillRect(
                insets.left, insets.right, width - insets.getWidth(),
                height - insets.getHeight()
        );

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
        updateValue();
        if (sliderCallBack != null)
            sliderCallBack.valueChanged(value);
    }

    @Override
    public void onMouseClicked(int x, int y) {

    }

    private void updateValue() {
        value = maxValue + Mathf.map(posX, 0, getWidth(), minValue, maxValue);
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
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

}

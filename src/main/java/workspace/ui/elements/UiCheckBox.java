package workspace.ui;

import workspace.ui.border.Insets;

public class UiCheckBox extends UiComponent {

    protected boolean selected;

    protected String text;

    protected IActionListener actionListener;

    public UiCheckBox(String text) {
        this.text = text;
        this.width = 13;
        this.height = 13;
    }

    @Override
    public void onDraw(Graphics g) {
        Insets insets = getInsets();
        int offsetX = getWidth() / 6;
        int offsetY = getHeight() / 6;

        g.setColor(background);
        g.fillRect(
                insets.left, insets.top, width - insets.getWidth() - 1,
                height - insets.getHeight() - 1
        );

        g.setColor(foreground);
        g.text(text, width + 5, g.getTextSize());

        if (!selected)
            return;

        g.setColor(foreground);
        g.fillRect(
                insets.left + offsetX, insets.top + offsetY,
                width - insets.getWidth() - 1 - (2 * offsetX),
                height - insets.getHeight() - 1 - (2 * offsetY)
        );
    }

    @Override
    public void onMouseClicked(int x, int y) {
        super.onMouseClicked(x, y);
        setSelected(!isSelected());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean oldValue = this.selected;
        if (oldValue == selected)
            return;
        this.selected = selected;
        if (actionListener != null)
            actionListener.onActionPerformed();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(IActionListener actionListener) {
        this.actionListener = actionListener;
    }

}

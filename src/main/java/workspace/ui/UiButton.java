package workspace.ui;

public class UiButton extends UiComponent {

    private String text;

    private IActionListener actionListener;

    public UiButton(String text) {
        this(text, 0, 0, 0, 0);
    }

    public UiButton(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void onMouseClicked(int x, int y) {
        super.onMouseClicked(x, y);
        if (actionListener == null)
            return;
        actionListener.onActionPerformed();
    }

    @Override
    public void onDraw(Graphics g) {
        width = (int) g.textWidth(text);
        g.setColor(background);
        g.fillRect(0, 0, g.textWidth(text), g.textAscent() + g.textDescent());
        g.setColor(foreground);
        g.text(text, 0, g.getTextSize());
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

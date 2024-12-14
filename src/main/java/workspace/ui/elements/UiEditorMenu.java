package workspace.ui;

import workspace.laf.UiConstants;
import workspace.laf.UiValues;

public class UiEditorMenu extends UiComponent {

    private String text;

    public UiEditorMenu() {
        setText("");
        setForeground(UiValues.getColor(UiConstants.KEY_MENU_FOREGROUND_COLOR));
        setBackground(UiValues.getColor(UiConstants.KEY_MENU_BACKGROUND_COLOR));
    }

    @Override
    public void onDraw(Graphics g) {
        g.setColor(66, 66, 66);
        g.fillRect(0, 0, g.getWidth(), 55);

        drawBackground(g);
        drawText(g);

        g.setColor(31, 31, 31);
        g.fillRect(0, 28, g.getWidth(), 1);
    }

    private void drawBackground(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, g.getWidth(), 30);
    }

    private void drawText(Graphics g) {
        g.setColor(getForeground());
        g.textSize(UiValues.getInt(UiConstants.KEY_MENU_TEXT_SIZE));
        g.text(getText(), 10, 20);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

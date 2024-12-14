package workspace.ui;

import workspace.ui.border.Insets;

public class UiPanel extends UiComponent {

    @Override
    public void onDraw(Graphics g) {
        Insets insets = getInsets();
        g.setColor(getBackground());
        g.fillRect(
                insets.left, insets.right, width - insets.getWidth(),
                height - insets.getHeight()
        );
    }

}

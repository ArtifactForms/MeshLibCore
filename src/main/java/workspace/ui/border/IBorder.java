package workspace.ui.border;

import workspace.ui.Graphics;

public interface IBorder {

    void drawBorder(Graphics g2d, int x, int y, int width, int height);

    Insets getInsets();

}

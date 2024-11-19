package workspace.ui;

import java.util.ArrayList;
import java.util.List;

import workspace.ui.border.IBorder;
import workspace.ui.border.Insets;
import workspace.ui.layout.Layout;

public class UiComponent {

    protected int x;

    protected int y;

    protected int width;

    protected int height;

    protected boolean visible;

    protected Color foreground;

    protected Color background;

    protected IBorder border;

    protected List<UiComponent> components;

    protected Layout layout;

    public UiComponent() {
        this(0, 0, 0, 0, true, Color.BLACK, Color.GRAY);
    }

    public UiComponent(int x, int y, int width, int height, boolean visible,
            Color foreground, Color background) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
        this.foreground = foreground;
        this.background = background;
        this.components = new ArrayList<UiComponent>();
    }

    public void draw(Graphics g) {
        if (!visible)
            return;
        g.pushMatrix();
        g.translate(x, y);
        onDraw(g);
        drawBorder(g);
        g.translate(getInsets().left, getInsets().top);
        drawChildren(g);
        g.popMatrix();
    }

    public void onDraw(Graphics g) {

    }

    protected void layout() {
        if (layout == null)
            return;
        layout.layout(this);
    }

    protected void drawBorder(Graphics g) {
        IBorder border = getBorder();
        if (border == null)
            return;
        border.drawBorder(g, 0, 0, getWidth(), getHeight());
    }

    protected void drawChildren(Graphics g) {
        for (UiComponent component : components) {
            component.draw(g);
        }
    }

    public boolean contains(int x, int y) {
        return x >= this.x && y >= this.y && x <= (this.x + getWidth())
                && y <= (this.y + getHeight());
    }

    public void onMouseClicked(int x, int y) {
        if (!isVisible())
            return;
        for (UiComponent component : components) {
            if (component.contains(x - this.x, y - this.y)) {
                component.onMouseClicked(x, y);
            }
        }
    }

    public void onMouseDragged(int x, int y) {
        if (!isVisible())
            return;
        for (UiComponent component : components) {
            if (component.contains(x - this.x, y - this.y)) {
                component.onMouseDragged(x, y);
            }
        }
    }

    public void add(UiComponent component) {
        if (component == null)
            return;
        components.add(component);
        layout();
    }

    public void remove(UiComponent component) {
        if (component == null)
            return;
        components.remove(component);
        layout();
    }

    public int getComponentCount() {
        return components.size();
    }

    public UiComponent getComponentAt(int index) {
        return components.get(index);
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Insets getInsets() {
        IBorder border = getBorder();
        if (border == null)
            return new Insets();
        return border.getInsets();
    }

    public IBorder getBorder() {
        return border;
    }

    public void setBorder(IBorder border) {
        this.border = border;
    }

}

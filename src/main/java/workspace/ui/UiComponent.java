package workspace.ui;

import java.util.ArrayList;
import java.util.List;

import workspace.ui.border.Border;
import workspace.ui.border.Insets;
import workspace.ui.elements.UiElement;
import workspace.ui.layout.Layout;
import workspace.ui.renderer.Renderer;

public class UiComponent implements UiElement {

	protected int x;

	protected int y;

	protected int width;

	protected int height;

	protected boolean visible;

	protected Color foreground;

	protected Color background;

	protected Border border;

	protected Layout layout;

	private Renderer renderer;

	protected List<UiComponent> components;

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

	@Override
	public void render(Graphics g) {
		if (!visible)
			return;
		g.pushMatrix();
		g.translate(x, y);
		renderSelf(g);
		renderBorder(g);
		g.translate(getInsets().getLeft(), getInsets().getTop());
		renderChildren(g);
		g.popMatrix();
	}

	/**
	 * Renders the border of the UI element if a border is defined.
	 * <p>
	 * This method checks if the {@code border} is not null before calling the
	 * {@code renderBorder} method on the provided graphics context. It uses the
	 * element's current width and height to define the area of the border.
	 * </p>
	 *
	 * @param g the graphics context to draw on.
	 */
	protected void renderBorder(Graphics g) {
		if (border == null)
			return;
		border.renderBorder(g, 0, 0, getWidth(), getHeight());
	}

	protected void renderSelf(Graphics g) {
		if (renderer == null)
			return;
		renderer.render(g, this);
	}

	protected void renderChildren(Graphics g) {
		for (UiComponent component : components) {
			component.render(g);
		}
	}

	@Override
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public Renderer getRenderer() {
		return renderer;
	}

	@Override
	public Insets getInsets() {
		if (border == null)
			return new Insets();
		return border.getInsets();
	}

	@Override
	public void setBorder(Border border) {
		this.border = border;
	}

	@Override
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@Override
	public boolean contains(int x, int y) {
		return x >= this.x && y >= this.y && x <= (this.x + getWidth())
		    && y <= (this.y + getHeight());
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	protected void layout() {
		if (layout == null)
			return;
		layout.layout(this);
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

	public void onMouseReleased(int x, int y) {
		if (!isVisible())
			return;
		for (UiComponent component : components) {
			if (component.contains(x - this.x, y - this.y)) {
				component.onMouseReleased(x, y);
			}
		}
	}

	public void onMousePressed(int x, int y) {
		if (!isVisible())
			return;
		for (UiComponent component : components) {
			if (component.contains(x - this.x, y - this.y)) {
				component.onMousePressed(x, y);
			}
		}
	}

	// USED
	public void add(UiComponent component) {
		if (component == null)
			return;
		components.add(component);
		layout();
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
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

}

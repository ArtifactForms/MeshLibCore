package workspace.ui;

public class UiLabel extends UiComponent {

	private String title;

	public void draw(Graphics g) {
		g.setColor(background);
		g.fillRect(x, y, g.textWidth(title), g.textAscent() + g.textDescent());
		g.setColor(foreground);
		g.text(title, x, y + g.getTextSize());
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

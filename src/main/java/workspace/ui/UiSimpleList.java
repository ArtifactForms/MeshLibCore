package workspace.ui;

import java.util.ArrayList;
import java.util.List;

public class UiSimpleList extends UiComponent {

	private List<Object> elements;

	public UiSimpleList() {
		elements = new ArrayList<Object>();
		setBackground(background);
	}

	public List<Object> getElements() {
		return elements;
	}

	public void addElements(Object... elements) {
		for (Object e : elements) {
			this.elements.add(e);
		}
	}

	@Override
	public void renderSelf(Graphics g) {
		int y = 300;
		int padding = 5;
		int gap = 5;

		g.pushMatrix();
		g.setColor(getBackground());
		g.fillRect(0, 0, height, width);

		for (Object e : elements) {
			String text = e.toString();
			g.setColor(background);
			g.fillRect(0, y, getWidth(),
			    g.textAscent() + g.textDescent() + padding + padding);
			g.setColor(foreground);
			g.text(text, padding, y + g.getTextSize() + padding);
			y += g.getTextSize() + gap + padding + padding;
		}

		g.popMatrix();
	}

}

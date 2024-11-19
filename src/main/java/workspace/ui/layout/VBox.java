package workspace.ui.layout;

//package workspace.ui;
//
//import workspace.ui.layout.Layout;
//
//public class VBox implements Layout {
//
//	private int vGap;
//	private int padding;
//	private int boxWidth;
//	
//	public VBox(int vGap, int padding, int boxWidth) {
//		this.vGap = vGap;
//		this.padding = padding;
//		this.boxWidth = boxWidth;
//	}
//
//	@Override
//	public void layout(UiComponent component) {
//		int height = 0;
//		int width = 0;
//		int y = 0;
//		for (int i = 0; i < component.getComponentCount(); i++) {
//			UiComponent c = component.getComponentAt(i);
//			c.setX(padding);
//			c.setY(y + padding);
//			width = width < c.getWidth() ? c.getWidth() : width;
//			height += c.getHeight() + vGap;
//			y += c.getHeight();
//			y += vGap;
//		}
//		width = width < boxWidth ? boxWidth : width;
//		component.setWidth(width);
//		component.setHeight(height - vGap + padding + padding);
//	}
//	
//}

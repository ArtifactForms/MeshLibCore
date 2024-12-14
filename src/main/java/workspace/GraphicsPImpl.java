package workspace;

import mesh.Mesh3D;
import processing.core.PApplet;
import processing.core.PGraphics;
import workspace.render.Mesh3DRenderer;
import workspace.ui.Color;
import workspace.ui.Graphics;

public class GraphicsPImpl implements Graphics {

	private Color color;

	private PGraphics g;

	private Mesh3DRenderer renderer;

	public GraphicsPImpl(PApplet p) {
		this.g = p.g;
		renderer = new Mesh3DRenderer(p);
		color = Color.BLACK;
	}

	@Override
	public void fillFaces(Mesh3D mesh) {
		g.noStroke();
		fill();
		renderer.drawFaces(mesh);
	}

	@Override
	public int getWidth() {
		return g.width;
	}

	@Override
	public int getHeight() {
		return g.height;
	}

	private void stroke() {
		g.stroke(color.getRed(), color.getGreen(), color.getBlue(),
		    color.getAlpha());
	}

	private void fill() {
		g.fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	@Override
	public void pushMatrix() {
		g.pushMatrix();
	}

	@Override
	public void popMatrix() {
		g.popMatrix();
	}

	@Override
	public void scale(float sx, float sy, float sz) {
		g.scale(sx, sy, sz);
	}

	@Override
	public void translate(float x, float y) {
		g.translate(x, y);
	}

	@Override
	public void translate(float x, float y, float z) {
		g.translate(x, y, z);
	}

	@Override
	public void strokeWeight(float weight) {
		g.strokeWeight(weight);
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void setColor(int red, int green, int blue) {
		color = new Color(red, green, blue);
	}

	@Override
	public void setColor(math.Color color) {
		setColor(color.getRedInt(), color.getGreenInt(), color.getBlueInt());
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		g.pushStyle();
		g.noFill();
		stroke();
		g.rectMode(PApplet.CORNER);
		g.rect(x, y, width, height);
		g.popStyle();
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		g.pushStyle();
		g.noFill();
		stroke();
		g.line(x1, y1, x2, y2);
		g.popStyle();
	}

	@Override
	public void drawLine(float x1, float y1, float z1, float x2, float y2,
	    float z2) {
		g.pushStyle();
		g.noFill();
		stroke();
		g.line(x1, y1, z1, x2, y2, z2);
		g.popStyle();
	}

	@Override
	public void fillRect(float x, float y, float width, float height) {
		g.pushStyle();
		g.noStroke();
		fill();
		g.rectMode(PApplet.CORNER);
		g.rect(x, y, width, height);
		g.popStyle();
	}

	@Override
	public void drawOval(float x, float y, float width, float height) {
		g.pushStyle();
		g.noFill();
		stroke();
		g.ellipseMode(PApplet.CORNER);
		g.ellipse(x, y, height, width);
		g.popStyle();
	}

	@Override
	public void fillOval(float x, float y, float width, float height) {
		g.pushStyle();
		g.noStroke();
		fill();
		g.ellipseMode(PApplet.CORNER);
		g.ellipse(x, y, height, width);
		g.popStyle();
	}

	@Override
	public void textSize(float size) {
		g.textSize(size);
	}

	@Override
	public float getTextSize() {
		return g.textSize;
	}

	@Override
	public float textWidth(String text) {
		return g.textWidth(text);
	}

	@Override
	public float textAscent() {
		return g.textAscent();
	}

	@Override
	public float textDescent() {
		return g.textDescent();
	}

	@Override
	public void text(String text, float x, float y) {
		fill();
		g.text(text, x, y);
	}

	@Override
	public void enableDepthTest() {
		g.hint(PApplet.ENABLE_DEPTH_TEST);
	}

	@Override
	public void disableDepthTest() {
		g.hint(PApplet.DISABLE_DEPTH_TEST);
	}

	@Override
	public void rotate(float angle) {
		g.rotate(angle);
	}

	@Override
	public void rotateX(float angle) {
		g.rotateX(angle);
	}

	@Override
	public void rotateY(float angle) {
		g.rotateY(angle);
	}

	@Override
	public void rotateZ(float angle) {
		g.rotate(angle);
	}

}

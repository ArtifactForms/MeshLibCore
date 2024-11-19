package workspace.ui;

import mesh.Mesh3D;

public interface Graphics {

	int getWidth();

	int getHeight();

	void pushMatrix();

	void popMatrix();

	void translate(float x, float y);

	void strokeWeight(float weight);

	void setColor(Color color);

	void setColor(int red, int green, int blue);

	void drawRect(float x, float y, float width, float height);

	void fillRect(float x, float y, float width, float height);

	void fillFaces(Mesh3D mesh);

	void textSize(float size);

	float getTextSize();

	float textWidth(String text);

	float textAscent();

	float textDescent();

	void text(String text, float x, float y);

	void enableDepthTest();

	void disableDepthTest();

	void rotateX(float angle);

	void rotateY(float angle);

	void rotateZ(float angle);

}

package workspace.ui;

public interface Graphics2D {
		
	int getWidth();

	int getHeight();
	
	void setColor(Color color);

	void setColor(math.Color color);
	
	void setColor(int red, int green, int blue);
	
	void strokeWeight(float weight);
	
	void pushMatrix();

	void popMatrix();
	
	void translate(float x, float y);
	
	void scale(float sx, float sy);
	
	void rotate(float angle);

	void drawRect(float x, float y, float width, float height);

	void fillRect(float x, float y, float width, float height);

	void drawOval(float x, float y, float width, float height);

	void fillOval(float x, float y, float width, float height);

	void drawLine(float x1, float y1, float x2, float y2);

	void textSize(float size);

	float getTextSize();

	float textWidth(String text);

	float textAscent();

	float textDescent();

	void text(String text, float x, float y);
	
}

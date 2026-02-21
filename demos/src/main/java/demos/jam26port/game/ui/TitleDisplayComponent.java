package demos.jam26port.game.ui;

import demos.texture.Title;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import engine.resources.Font;
import math.Color;

public class TitleDisplayComponent extends AbstractComponent implements RenderableComponent {

  private float fadeInTime;
  private float stayTime;
  private float fadeOutTime;
  private float currentTime;
  private boolean finished;
  private Font font;
  private String text;
  private Color color;

  public TitleDisplayComponent() {
    this.finished = true;
  }

  @Override
  public void onUpdate(float tpf) {
    if (!finished) {
      currentTime += tpf;

      // Mark as finished when total time exceeds all phases
      if (currentTime >= (fadeInTime + stayTime + fadeOutTime)) {
        finished = true;
        currentTime = fadeInTime + stayTime + fadeOutTime;
      }
    }
  }

  @Override
  public void render(Graphics g) {
    if (finished) {
      return;
    }
    // Calculate alpha based on the current phase
    float alpha = calculateAlpha();

    // Set the font
    g.setFont(font);

    // Calculate text width and center horizontally
    float x = (g.getWidth() - g.textWidth(text)) / 2;

    // Get text metrics to center vertically
    float ascent = g.textAscent();
    float descent = g.textDescent();
    float y = (g.getHeight() + ascent - descent) / 2;

    // Set the color with the calculated alpha
    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    g.text(text, x, y);
  }

  private float calculateAlpha() {
    if (currentTime < fadeInTime) {
      // Fade-in phase
      return currentTime / fadeInTime;
    } else if (currentTime < fadeInTime + stayTime) {
      // Stay phase
      return 1.0f;
    } else {
      // Fade-out phase
      float fadeOutProgress = (currentTime - fadeInTime - stayTime) / fadeOutTime;
      return 1.0f - fadeOutProgress;
    }
  }

  public void display(Title title) {
    this.fadeInTime = title.getFadeInTime();
    this.stayTime = title.getStayTime();
    this.fadeOutTime = title.getFadeOutTime();
    this.currentTime = 0;
    this.finished = false;
    this.text = title.getText();
    this.font = new Font("monogram-extended", title.getSize(), Font.PLAIN);
    this.color = title.getColor();
  }
}

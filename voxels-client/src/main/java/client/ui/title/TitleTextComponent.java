package client.ui.title;

import java.util.ArrayDeque;
import java.util.Queue;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import math.Color;

public class TitleTextComponent extends AbstractComponent
    implements RenderableComponent, TitleView {

  private float fadeInTime;
  private float stayTime;
  private float fadeOutTime;
  private float currentTime;
  private boolean finished;

  private Color color;

  private String text;
  private Font font;

  private String subtitle;
  private Font subtitleFont;

  private final Queue<Title> queue = new ArrayDeque<>();

  public TitleTextComponent() {
    this.text = "";
    this.subtitle = "";
    this.finished = true;

    this.font = new Font("monogram-extended", 300, Font.PLAIN);
    this.subtitleFont = new Font("monogram-extended", 80, Font.PLAIN);

    this.color = new Color(1, 1, 1, 1);
  }

  @Override
  public void onUpdate(float tpf) {

    if (!finished) {

      currentTime += tpf;

      if (currentTime >= (fadeInTime + stayTime + fadeOutTime)) {
        finished = true;
      }
    }

    if (finished && !queue.isEmpty()) {
      startTitle(queue.poll());
    }
  }

  @Override
  public void render(Graphics g) {

    if (finished) return;

    updateFonts(g);

    float alpha = calculateAlpha();

    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));

    // TITLE
    g.setFont(font);

    float titleWidth = g.textWidth(text);
    float titleAscent = g.textAscent();
    float titleDescent = g.textDescent();

    float centerY = g.getHeight() * 0.45f;
    float titleX = (g.getWidth() - titleWidth) / 2f;
    float titleY = centerY;

    g.text(text, titleX, titleY);

    // SUBTITLE
    if (subtitle != null && !subtitle.isEmpty()) {

      g.setFont(subtitleFont);

      float subtitleWidth = g.textWidth(subtitle);
      float subtitleX = (g.getWidth() - subtitleWidth) / 2f;

      float spacing = g.getHeight() * 0.02f;
      float subtitleY = titleY + titleAscent + spacing;

      g.text(subtitle, subtitleX, subtitleY);
    }
  }

  private void updateFonts(Graphics g) {

    float h = g.getHeight();

    int titleSize = (int) (h * 0.25);
    int subtitleSize = (int) (h * 0.09f);

    this.font = new Font("monogram-extended", titleSize, Font.PLAIN);
    this.subtitleFont = new Font("monogram-extended", subtitleSize, Font.PLAIN);
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

  private void startTitle(Title title) {

    this.fadeInTime = title.getFadeInTime();
    this.stayTime = title.getStayTime();
    this.fadeOutTime = title.getFadeOutTime();

    this.currentTime = 0;
    this.finished = false;

    this.text = title.getText();
    this.subtitle = title.getSubtitle();
  }

  @Override
  public void displayTitle(Title title) {

    if (finished) {
      startTitle(title);
    } else {
      queue.add(title);
    }
  }

  @Override
  public void onAttach() {
    currentTime = 0;
    finished = false;
  }

  @Override
  public void onDetach() {
    // Optional: Cleanup resources if needed
  }
}

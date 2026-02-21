package engine.ui;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import math.Color;

public class LoadingScreen extends AbstractComponent implements RenderableComponent {

  private boolean visible = true;

  @Override
  public void onAttach() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onDetach() {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(Graphics g) {
    if (!visible) return;

    String text = "Scene is LOADING... Hang on!";
    float width = g.textWidth(text);
    float x = (g.getWidth() - width) / 2f;
    float y = (g.getHeight() - g.getTextSize()) / 2f;

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, g.getWidth(), g.getHeight());
    g.setColor(Color.WHITE);
    g.text(text, x, y);
  }

  @Override
  public void onUpdate(float tpf) {
    // TODO Auto-generated method stub

  }

  public void hide() {
    visible = false;
  }
}

package engine.demos.voxels;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.resources.Font;
import math.Color;
import workspace.ui.Graphics;

public class TextDisplay extends AbstractComponent implements RenderableComponent {

  private String text = "";

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
    //      g.setFont("");
    Font font = new Font("monogram-extended", 32, Font.PLAIN);
    g.setFont(font);
    g.setColor(Color.YELLOW);
    g.text(text, (g.getWidth() - g.textWidth(text)) / 2, g.getHeight() - 110);
  }

  @Override
  public void onUpdate(float tpf) {
    // TODO Auto-generated method stub

  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}

package client.usecases.openinventory;

import common.game.ItemStack;
import common.game.block.BlockRegistry;
import engine.rendering.Graphics;
import math.Color;

public class DefaultTooltipRenderer implements TooltipRenderer {

  @Override
  public void render(Graphics g, ItemStack stack, float x, float y) {
    String name = BlockRegistry.get(stack.getItemId()).getName();
    String amount = "Stack: " + stack.getAmount();

    int padding = 6;
    int lineHeight = 14;

    float width = Math.max(g.textWidth(name), g.textWidth(amount)) + padding * 2;
    int height = (lineHeight * 2) + padding * 2;

    g.setColor(new Color(0f, 0f, 0f, 0.9f));
    g.fillRect(x, y, width, height);

    g.setColor(new Color(1f, 1f, 1f, 0.2f));
    g.drawRect(x, y, width, height);

    g.setColor(Color.WHITE);
    g.text(name, x + padding, y + padding + lineHeight - 4);

    g.setColor(new Color(0.8f, 0.8f, 0.8f));
    g.text(amount, x + padding, y + padding + lineHeight * 2 - 4);
  }
}

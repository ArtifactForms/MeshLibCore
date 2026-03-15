package client.usecases.openinventory;

import common.game.ItemStack;
import engine.rendering.Graphics;

public interface TooltipRenderer {
  void render(Graphics g, ItemStack stack, float x, float y);
}

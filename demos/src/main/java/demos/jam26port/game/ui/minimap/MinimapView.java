package demos.jam26port.game.ui.minimap;

import engine.rendering.Graphics;
import math.Vector3f;

public interface MinimapView {
	
  void render(Graphics g);

  void setPlayerWorldPosition(Vector3f position);
}

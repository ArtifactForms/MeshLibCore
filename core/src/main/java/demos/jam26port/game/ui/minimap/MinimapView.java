package demos.jam26port.game.ui.minimap;

import math.Vector3f;
import workspace.ui.Graphics;

public interface MinimapView {
	
  void render(Graphics g);

  void setPlayerWorldPosition(Vector3f position);
}

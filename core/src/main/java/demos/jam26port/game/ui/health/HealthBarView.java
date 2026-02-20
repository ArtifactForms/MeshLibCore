package demos.jam26port.game.ui.health;

import workspace.ui.Graphics;

public interface HealthBarView {

  void update(float tpf);

  void render(Graphics g);

  void setHealth01(float value);
}

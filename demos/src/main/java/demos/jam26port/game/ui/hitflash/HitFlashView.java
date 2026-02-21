package demos.jam26port.game.ui.hitflash;

import workspace.ui.Graphics;

public interface HitFlashView {

  void trigger(float strength);

  void update(float tpf);

  void render(Graphics g);
}

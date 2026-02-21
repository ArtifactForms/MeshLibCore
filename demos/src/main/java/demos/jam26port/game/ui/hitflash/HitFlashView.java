package demos.jam26port.game.ui.hitflash;

import engine.render.Graphics;

public interface HitFlashView {

  void trigger(float strength);

  void update(float tpf);

  void render(Graphics g);
}

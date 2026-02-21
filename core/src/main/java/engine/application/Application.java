package engine.application;

import engine.rendering.Graphics;
import engine.runtime.input.Input;

public interface Application {

  void initialize();

  void update();

  void render(Graphics g);

  void cleanup();

  void setInput(Input input);
  
  void onResize(int width, int height);
}

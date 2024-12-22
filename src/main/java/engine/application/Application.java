package engine.application;

import engine.input.Input;
import workspace.ui.Graphics;

public interface Application {

  void initialize();

  void update();

  void render(Graphics g);

  void cleanup();

  void setInput(Input input);
}

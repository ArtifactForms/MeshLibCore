package engine.application;

import engine.input.Input;
import workspace.ui.Graphics;

public class ApplicationContainer {

  private boolean isInitialized = false;

  private Graphics graphics;

  private Application application;

  public ApplicationContainer(Application application) {
    if (application == null) {
      throw new IllegalArgumentException("Application cannot be null.");
    }
    this.application = application;
  }

  public void initialize() {
    if (isInitialized) {
      return;
    }
    application.initialize();
    isInitialized = true;
  }

  public void update() {
    application.update();
  }

  public void render() {
    checkInitialization();
    if (graphics == null) {
      throw new IllegalStateException(
          "Graphics context is not initialized. Call setGraphics() first.");
    }
    application.render(graphics);
  }

  public void cleanup() {
    application.cleanup();
  }

  private void checkInitialization() {
    if (!isInitialized) {
      throw new IllegalStateException(
          "ApplicationContainer is not initialized. Call initialize() first.");
    }
  }

  public void setInput(Input input) {
    application.setInput(input);
  }

  public void setGraphics(Graphics g) {
    if (g == null) {
      throw new IllegalArgumentException("Graphics cannot be null.");
    }
    this.graphics = g;
  }
  
  public void resize(int width, int height) {
      application.onResize(width, height);
  }
}

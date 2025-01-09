package engine.processing;

import engine.application.ApplicationContainer;
import engine.application.ApplicationSettings;
import engine.input.Input;
import engine.input.KeyInput;
import engine.input.MouseInput;
import engine.resources.ResourceManager;
import engine.resources.TextureManager;
import engine.vbo.VBOFactory;
import processing.core.PApplet;
import workspace.GraphicsPImpl;
import workspace.ui.Graphics;

public class ProcessingApplication extends PApplet {

  private static boolean launched = false;

  private static ApplicationContainer container;

  private static ApplicationSettings settings;

  @Override
  public void settings() {
    size(settings.getWidth(), settings.getHeight(), P3D);
    smooth(8);
    if (settings.isFullscreen()) {
      fullScreen();
    }
  }

  @Override
  public void setup() {
    Graphics g = new GraphicsPImpl(this);
    ResourceManager.getInstance().setImageLoader(new ProcessingImageLoader(this));
    TextureManager.getInstance().setTextureLoader(new ProcessingTextureLoader(this));
    VBOFactory.getInstance()
        .setVBOCreationStrategy(new ProcessingVBOCreationStrategy(getGraphics()));
    container.setGraphics(g);
    getSurface().setTitle(settings.getTitle());
    setupInput();
    container.initialize();
    noCursor();
  }

  private void setupInput() {
    KeyInput keyInput = new ProcessingKeyInput(this);
    MouseInput mouseInput = new ProcessingMouseInput(this);
    Input input = new ProcessingInput(keyInput, mouseInput);
    container.setInput(input);
  }

  @Override
  public void draw() {
    colorMode(RGB);
    background(0);
    noLights();
    container.update();
    container.render();
  }

  public static void launchApplication(
      ApplicationContainer appContainer, ApplicationSettings appSettings) {
    if (launched) {
      throw new IllegalStateException("Application already launched.");
    }
    if (appContainer == null) {
      throw new IllegalArgumentException("ApplicationContainer cannot be null.");
    }
    if (appSettings == null) {
      throw new IllegalArgumentException("ApplicationSettings cannot be null");
    }
    launched = true;
    settings = appSettings;
    container = appContainer;
    PApplet.main(ProcessingApplication.class);
  }

  @Override
  public void exit() {
    container.cleanup();
    super.exit();
  }
}

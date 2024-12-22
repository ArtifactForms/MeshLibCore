package engine.processing;

import engine.application.ApplicationContainer;
import engine.application.ApplicationSettings;
import engine.input.Input;
import engine.input.KeyInput;
import engine.input.MouseInput;
import processing.core.PApplet;
import workspace.GraphicsPImpl;
import workspace.Workspace;
import workspace.ui.Graphics;

public class ProcessingApplication extends PApplet {

  private static boolean launched = false;

  private static ApplicationContainer container;

  private static ApplicationSettings settings;

  private Workspace workspace;

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
    container.setGraphics(g);
    getSurface().setTitle(settings.getTitle());
    setupInput();
    //		workspace = new Workspace(this);
    //		workspace.setLoop(true);
    //		workspace.setGridVisible(false);
    //		workspace.setUiVisible(false);
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
    scale(100);
    strokeWeight(0.01f);
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

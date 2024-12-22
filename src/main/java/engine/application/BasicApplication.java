package engine.application;

import engine.Timer;
import engine.debug.DebugInfoUpdater;
import engine.debug.DebugOverlay;
import engine.debug.FpsGraph;
import engine.debug.FpsHistory;
import engine.input.Input;
import engine.input.Key;
import engine.processing.ProcessingApplication;
import engine.scene.Scene;
import engine.scene.SceneNode;
import workspace.ui.Graphics;

public abstract class BasicApplication implements Application {

  private boolean launched;

  private boolean displayInfoText = true;

  private boolean isPaused = false;

  private Timer timer;

  protected Input input;

  protected Scene activeScene;

  protected SceneNode rootUI;

  protected DebugOverlay debugOverlay;

  protected DebugInfoUpdater debugInfoUpdater;

  protected FpsGraph fpsGraph;

  public BasicApplication() {
    this.timer = new Timer();
  }

  public abstract void onInitialize();

  public abstract void onUpdate(float tpf);

  public abstract void onRender(Graphics g);

  public abstract void onCleanup();

  private boolean lastZ;

  public void launch(ApplicationSettings settings) {
    if (launched) {
      throw new IllegalStateException("Application already launched.");
    }
    launched = true;
    ApplicationContainer container = new ApplicationContainer(this);
    ProcessingApplication.launchApplication(container, settings);
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  System.out.println("Cleanup application.");
                  cleanup();
                }));
  }

  public void launch() {
    launch(ApplicationSettings.defaultSettings());
  }

  @Override
  public void initialize() {
    rootUI = new SceneNode();
    initializeDebugOverlay();
    fpsGraph = new FpsGraph(new FpsHistory());
    onInitialize();
  }

  private void initializeDebugOverlay() {
    debugOverlay = new DebugOverlay();
    debugInfoUpdater = new DebugInfoUpdater(debugOverlay);
  }

  @Override
  public void update() {
    if (activeScene != null) {

      if (input.isKeyPressed(Key.Z) && !lastZ) {
        activeScene.setWireframeMode(!activeScene.isWireframeMode());
      }

      lastZ = input.isKeyPressed(Key.Z);
    }

    timer.update();
    fpsGraph.update(timer);
    debugInfoUpdater.update(timer, activeScene, input);

    float tpf = timer.getTimePerFrame();
    if (input != null) {
      input.update();
    }
    if (!isPaused) {
      if (activeScene != null) {
        activeScene.update(tpf);
      }
    }
    rootUI.update(tpf);
    onUpdate(tpf);
  }

  @Override
  public void render(Graphics g) {
    if (activeScene != null) {
      activeScene.render(g);
    }

    onRender(g);

    g.disableDepthTest();
    g.lightsOff();
    g.camera();

    g.strokeWeight(1);
    renderUi(g);
    renderDebugUi(g);
    fpsGraph.render(g);

    g.enableDepthTest();
  }

  private void renderUi(Graphics g) {
    rootUI.render(g);
  }

  private void renderDebugUi(Graphics g) {
    if (!displayInfoText) return;
    debugOverlay.render(g);
  }

  @Override
  public void cleanup() {
    if (activeScene != null) {
      activeScene.cleanup();
    }
    rootUI.cleanup();
    onCleanup();
  }

  public void pause() {
    isPaused = true;
  }

  public void resume() {
    isPaused = false;
  }

  public void setInput(Input input) {
    this.input = input;
  }

  public Input getInput() {
    return input;
  }

  public Scene getActiveScene() {
    return activeScene;
  }

  public void setActiveScene(Scene activeScene) {
    this.activeScene = activeScene;
  }
}
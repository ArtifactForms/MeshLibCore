package engine.application;

import engine.Timer;
import engine.components.SmoothFlyByCameraControl;
import engine.debug.DebugInfoUpdater;
import engine.debug.DebugOverlay;
import engine.debug.FpsGraph;
import engine.debug.FpsHistory;
import engine.debug.core.DebugContext;
import engine.debug.core.DebugDraw;
import engine.input.Input;
import engine.processing.ProcessingApplication;
import engine.resources.Font;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import math.Mathf;
import workspace.ui.Graphics;

public abstract class BasicApplication implements Application {

  private static final float MAX_TPF = 1f / 60f;

  private boolean launched;

  private boolean displayInfo = true;

  private boolean isPaused = false;

  private Timer timer;

  protected Input input;

  protected Scene activeScene;

  protected DebugOverlay debugOverlay;

  protected DebugInfoUpdater debugInfoUpdater;

  protected FpsGraph fpsGraph;

  private Viewport viewport;

  private ApplicationSettings settings;

  private DebugContext debugContext;

  public BasicApplication() {
    this.timer = new Timer();
  }

  public abstract void onInitialize();

  public abstract void onUpdate(float tpf);

  public abstract void onRender(Graphics g);

  public abstract void onCleanup();

  public void launch(ApplicationSettings settings) {
    if (launched) {
      throw new IllegalStateException("Application already launched.");
    }
    this.settings = settings;
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
    viewport = new Viewport(0, 0, settings.getWidth(), settings.getHeight());
    initializeDebugOverlay();
    setupDebug();
    fpsGraph = new FpsGraph(new FpsHistory());
    onInitialize();
    setupDefaultCamera();
  }

  private void setupDefaultCamera() {
    if (activeScene == null) return;
    if (activeScene.getActiveCamera() != null) return;

    PerspectiveCamera defaultCamera = new PerspectiveCamera();
    activeScene.setActiveCamera(defaultCamera);
    SceneNode cameraNode = new SceneNode("DefaultCamera");
    cameraNode.addComponent(new SmoothFlyByCameraControl(input, defaultCamera));
    activeScene.addNode(cameraNode);
  }

  private void initializeDebugOverlay() {
    debugOverlay = new DebugOverlay();
    debugInfoUpdater = new DebugInfoUpdater(debugOverlay);
  }

  private void setupDebug() {
    debugContext = new DebugContext();
    DebugDraw.initialize(debugContext);
  }

  @Override
  public void update() {
    timer.update();

    if (settings.isUseGamePadInput()) input.updateGamepadState();

    fpsGraph.update(timer);
    debugInfoUpdater.update(timer, activeScene, input);

    float tpf = timer.getTimePerFrame();
    tpf = Mathf.clamp(tpf, 0, MAX_TPF);

    if (!isPaused) {
      if (activeScene != null) {
        activeScene.update(tpf);
      }
    }

    onUpdate(tpf);

    debugContext.update(tpf);

    // IMPORTANT! Called after scene update!
    input.update();
  }

  @Override
  public void render(Graphics g) {
    if (activeScene != null) {
      activeScene.render(g);
    }

    onRender(g);

    g.lightsOff();

    debugContext.render(g);
    debugContext.clearFrameCommands();

    g.disableDepthTest();

    g.camera();
    g.strokeWeight(1);

    if (activeScene != null) {
      activeScene.renderUI(g);
    }

    // lights off here in case scene UI uses light in materials
    g.lightsOff();

    renderDebugUi(g);

    g.enableDepthTest();
  }

  private void renderDebugUi(Graphics g) {
    if (!displayInfo) return;
    g.setFont(new Font("Lucida Sans", 12, Font.PLAIN));
    debugOverlay.render(g);
    fpsGraph.render(g);
  }

  @Override
  public void cleanup() {
    if (activeScene != null) {
      activeScene.cleanup();
    }
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

  public void setDisplayInfo(boolean displayInfo) {
    this.displayInfo = displayInfo;
  }

  public void setDebugDrawVisible(boolean visible) {
    debugContext.setVisible(visible);
  }

  public boolean isDebugDrawVisible() {
    return debugContext.isVisible();
  }

  public Viewport getViewport() {
    return viewport;
  }

  public Timer getTimer() {
    return timer;
  }

  @Override
  public void onResize(int width, int height) {
    viewport = new Viewport(0, 0, width, height);
  }
}

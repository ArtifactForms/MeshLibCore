package sandbox;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.ControlWASD;
import engine.components.Geometry;
import engine.render.Graphics;
import engine.render.Material;
import engine.runtime.debug.core.DebugDraw;
import engine.scene.Scene;
import engine.scene.SceneNode;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CapsuleCreator;

public class MovementApp extends BasicApplication {

  public static void main(String[] args) {
    MovementApp app = new MovementApp();
    app.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  private Scene scene;
  private SceneNode player;

  @Override
  public void onInitialize() {
    scene = new Scene();

    Mesh3D capsule = new CapsuleCreator().create();
    Geometry geometry = new Geometry(capsule, Material.DEFAULT_RED);

    ControlWASD control = new ControlWASD(input);
    control.mapArrowKeys();
    control.setSpeed(10);

    player = new SceneNode("Player", geometry, control);

    scene.addNode(player);
    setActiveScene(scene);
  }

  @Override
  public void onUpdate(float tpf) {
    DebugDraw.drawGrid(new Vector3f(), 100, 0.5f, 5, Color.LIGHT_GRAY, Color.GRAY);
    
    Vector3f position = player.getWorldPosition();
    
    Vector3f forward = player.getTransform().getForward();
    DebugDraw.drawRay(position, forward, 10, Color.YELLOW);
  }

  @Override
  public void onRender(Graphics g) {}

  @Override
  public void onCleanup() {}
}

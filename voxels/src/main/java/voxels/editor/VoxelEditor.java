package voxels.editor;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.rendering.Graphics;
import engine.runtime.debug.core.DebugDraw;
import engine.scene.Scene;
import math.Color;
import math.Vector3f;

public class VoxelEditor extends BasicApplication {

  public static void main(String[] args) {
    VoxelEditor editor = new VoxelEditor();
    editor.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }

  @Override
  public void onInitialize() {
    Scene scene = new EditorScene(input);
    setActiveScene(scene);
  }

  @Override
  public void onUpdate(float tpf) {
    DebugDraw.drawGrid(new Vector3f(), 100, 1, 10, Color.LIGHT_GRAY, Color.GRAY);
  }

  @Override
  public void onRender(Graphics g) {
    // Do nothing
  }

  @Override
  public void onCleanup() {
    // Do nothing
  }
}

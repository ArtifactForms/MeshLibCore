package client.scene.screen;

import client.app.GameClient;
import client.ui.cursor.SimpleCursorComponent;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.SceneNode;
import engine.scene.screen.GameScreen;
import engine.scene.screen.GlobalInput;
import math.Color;

public class OverlayTestScreen extends GameScreen {

  private boolean justOpened;
  private GameClient client;
  private SimpleCursorComponent component;
  private SceneNode cursor;

  public OverlayTestScreen(GameClient client) {
    this.client = client;

    OverlayComponent component = new OverlayComponent();
    SceneNode node = new SceneNode();
    node.addComponent(component);
    uiRoot.addChild(node);

    SimpleCursorComponent component2 = new SimpleCursorComponent(GlobalInput.input);
    cursor = new SceneNode("Cursor", component2);
    uiRoot.addChild(cursor);
  }

  @Override
  public void onEnter() {
    justOpened = true;
  }

  @Override
  public void onExit() {}

  @Override
  public void update(float tpf) {}

  @Override
  public boolean capturesMouse() {
    return false;
  }

  @Override
  public boolean isTransparent() {
    return true;
  }

  @Override
  public boolean blocksGameplay() {
    return true;
  }

  @Override
  public boolean onMouseClicked(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMousePressed(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseMoved(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseDragged(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent e) {
    return true;
  }

  @Override
  public boolean onKeyPressed(KeyEvent e) {
    return true;
  }

  @Override
  public boolean onKeyReleased(KeyEvent e) {
    return true;
  }

  @Override
  public boolean onKeyTyped(KeyEvent e) {
    return true;
  }

  public class OverlayComponent extends AbstractComponent implements RenderableComponent {

    @Override
    public void render(Graphics g) {
      g.setColor(new Color(0, 0, 0, 0.4f));
      g.fillRect(0, 0, g.getWidth(), g.getHeight());
    }

    @Override
    public void update(float tpf) {}
  }
}

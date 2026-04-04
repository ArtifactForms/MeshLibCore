package client.scene.screen;

import client.usecases.chat.ChatController;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.MouseEvent;
import engine.scene.SceneNode;
import engine.scene.screen.GameScreen;
import math.Color;

public class ChatScreen extends GameScreen {

  private final ChatController controller;

  private final Font font = new Font("monogram-extended", 32, Font.PLAIN);

  private boolean ignoreNextTyped = false;

  public ChatScreen(ChatController controller) {
    this.controller = controller;
    SceneNode view = new SceneNode("", new ChatRenderer());
    uiRoot.addChild(view);
  }

  @Override
  public void onEnter() {
    controller.openChat();
    ignoreNextTyped = true;
  }

  @Override
  public void onExit() {
    controller.closeChat();
  }

  @Override
  public void update(float tpf) {
    // Do nothing
  }

  private void closeScreen() {
    getScene().popScreen();
  }

  @Override
  public boolean onKeyPressed(KeyEvent e) {
    switch (e.getKey()) {
      case ENTER:
        controller.send();
        closeScreen();
        return true;
      case ESCAPE:
        closeScreen();
        return true;
      case BACKSPACE:
        controller.backspace();
        return true;
      case DELETE:
        controller.delete();
        return true;
      case ARROW_LEFT:
        controller.moveLeft();
        return true;
      case ARROW_RIGHT:
        controller.moveRight();
        return true;
      case ARROW_UP:
        controller.moveHistoryUp();
        return true;
      case ARROW_DOWN:
        controller.moveHistoryDown();
        return true;
      default:
        return true;
    }
  }

  @Override
  public boolean onKeyReleased(KeyEvent e) {
    return true;
  }

  @Override
  public boolean onKeyTyped(KeyEvent e) {
    if (ignoreNextTyped) {
      ignoreNextTyped = false;
      return true;
    }

    char c = e.getChar();
    if (Character.isISOControl(c)) return false;

    controller.insert(c);
    return true;
  }

  @Override
  public boolean capturesMouse() {
    return true;
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
    return false;
  }

  @Override
  public boolean onMousePressed(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMouseMoved(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMouseDragged(MouseEvent e) {
    return false;
  }

  @Override
  public boolean onMouseReleased(MouseEvent e) {
    return false;
  }

  public class ChatRenderer extends AbstractComponent implements RenderableComponent {

    private float cursorTimer = 0;
    private static final float BLINK_SPEED = 6.0f;

    @Override
    public void onUpdate(float tpf) {
      cursorTimer += tpf * BLINK_SPEED;

      if (cursorTimer > Math.PI * 2) {
        cursorTimer -= Math.PI * 2;
      }
    }

    @Override
    public void render(Graphics g) {
      g.setFont(font);

      int baseY = g.getHeight() - 20;
      int offsetX = 20;

      String message = controller.getText();

      // Background Bar
      g.setColor(new Color(0, 0, 0, 0.5f));
      g.fillRect(0, baseY - 30, g.getWidth(), 40);

      // Text
      g.setColor(Color.YELLOW);
      g.text(message, offsetX, baseY);

      // Cursor
      if (Math.sin(cursorTimer) > 0) {
        float cursorOffset = g.textWidth(message.substring(0, controller.getCursor()));
        float cursorX = offsetX + cursorOffset;

        g.setColor(Color.WHITE);
        g.drawLine(cursorX, baseY - 25, cursorX, baseY + 5);
      }
    }
  }
}

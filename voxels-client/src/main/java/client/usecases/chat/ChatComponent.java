package client.usecases.chat;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.KeyListener;
import math.Color;

public class ChatComponent extends AbstractComponent implements KeyListener, RenderableComponent {

  private final ChatController controller;

  private final Input input;

  private boolean ignoreNextTyped = false;

  private final Font font = new Font("monogram-extended", 32, Font.PLAIN);

  public ChatComponent(Input input, ChatController controller) {
    this.input = input;
    this.controller = controller;
  }

  @Override
  public void onKeyPressed(KeyEvent e) {

    if (!controller.isOpen() && e.getKey() == Key.T) {

      controller.openChat();
      ignoreNextTyped = true;
      return;
    }

    if (!controller.isOpen()) return;

    switch (e.getKey()) {
      case ENTER:
        controller.send();
        return;

      case BACKSPACE:
        controller.backspace();
        return;

      case DELETE:
        controller.delete();
        return;

      case ARROW_LEFT:
        controller.moveLeft();
        return;

      case ARROW_RIGHT:
        controller.moveRight();
        return;

      case ESCAPE:
        controller.closeChat();
        return;

      case ARROW_UP:
        controller.moveHistoryUp();
        return;

      case ARROW_DOWN:
        controller.moveHistoryDown();
        return;

      default:
        return;
    }
  }

  @Override
  public void onKeyTyped(KeyEvent e) {

    if (!controller.isOpen()) return;

    if (ignoreNextTyped) {
      ignoreNextTyped = false;
      return;
    }

    char c = e.getChar();

    if (Character.isISOControl(c)) return;

    controller.insert(c);
  }

  @Override
  public void render(Graphics g) {

    if (!controller.isOpen()) return;

    g.setFont(font);

    int baseY = g.getHeight() - 20;
    int offsetX = 20;

    String message = controller.getText();

    g.setColor(Color.YELLOW);
    g.text(message, offsetX, baseY);

    float cursorX = offsetX + g.textWidth(message.substring(0, controller.getCursor()));

    g.setColor(Color.WHITE);
    g.drawLine(cursorX, baseY - 25, cursorX, baseY + 5);
  }

  @Override
  public void onKeyReleased(KeyEvent e) {
    // Do nothing
  }

  @Override
  public void onAttach() {
    input.addKeyListener(this);
  }

  @Override
  public void onDetach() {
    input.removeKeyListener(this);
  }
}

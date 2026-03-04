package demos.voxels;

import demos.voxels.client.event.EventManager;
import demos.voxels.client.event.MessageSentEvent;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.KeyListener;
import engine.scene.camera.Camera;
import math.Color;

public class ChatComponent extends AbstractComponent implements KeyListener, RenderableComponent {

  private static final int MAX_MESSAGE_LENGTH = 256;

  private final StringBuilder textBuffer;
  private boolean textInputEnabled;
  private final Input input;
  private final Camera camera;
  private int cursorPosition;
  private final EventManager eventManager;

  public ChatComponent(Input input, Camera camera, EventManager eventManager) {
    this.input = input;
    this.camera = camera;
    this.textBuffer = new StringBuilder();
    this.cursorPosition = 0;
    this.eventManager = eventManager;

    input.addKeyListener(this);
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {
    input.removeKeyListener(this);
  }

  @Override
  public void onUpdate(float tpf) {}

  // =========================================================
  // Rendering
  // =========================================================

  @Override
  public void render(Graphics g) {
    if (!textInputEnabled) return;

    Font font = new Font("monogram-extended", 32, Font.PLAIN);

    int baseY = g.getHeight() - 20;
    int offsetX = 20;

    g.setFont(font);
    g.setColor(Color.YELLOW);

    String message = textBuffer.toString();
    g.text(message, offsetX, baseY);

    float cursorX = offsetX + g.textWidth(message.substring(0, cursorPosition));
    g.setColor(Color.WHITE);
    g.drawLine(cursorX, baseY - 25, cursorX, baseY + 5);
  }

  // =========================================================
  // Key Handling
  // =========================================================

  @Override
  public void onKeyPressed(KeyEvent e) {

    if (e.getKey() == Key.T && !textInputEnabled) {
      textInputEnabled = true;
      return;
    }

    if (!textInputEnabled) return;

    switch (e.getKey()) {
      case BACKSPACE:
        if (cursorPosition > 0) {
          textBuffer.deleteCharAt(cursorPosition - 1);
          cursorPosition--;
        }
        break;

      case DELETE:
        if (cursorPosition < textBuffer.length()) {
          textBuffer.deleteCharAt(cursorPosition);
        }
        break;

      case ARROW_LEFT:
        if (cursorPosition > 0) {
          cursorPosition--;
        }
        break;

      case ARROW_RIGHT:
        if (cursorPosition < textBuffer.length()) {
          cursorPosition++;
        }
        break;

      case ENTER:
        sendMessage(textBuffer.toString());
        textInputEnabled = false;
        textBuffer.setLength(0);
        cursorPosition = 0;
        break;

      default:
        break;
    }
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    if (!textInputEnabled) return;

    char c = e.getChar();

    if (Character.isISOControl(c)) return;

    if (textBuffer.length() >= MAX_MESSAGE_LENGTH) return;

    textBuffer.insert(cursorPosition, c);
    cursorPosition++;
  }

  @Override
  public void onKeyReleased(KeyEvent e) {}

  // =========================================================
  // Messaging
  // =========================================================

  private void sendMessage(String message) {
    if (message == null || message.trim().isEmpty()) return;

    MessageSentEvent event = new MessageSentEvent(message);
    eventManager.triggerEvent(event);
  }
}

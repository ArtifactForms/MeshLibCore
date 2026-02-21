package demos.voxels;

import demos.voxels.client.event.EventManager;
import demos.voxels.client.event.MessageSentEvent;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import engine.resources.Font;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.KeyListener;
import engine.scene.camera.Camera;
import math.Color;

public class ChatComponent extends AbstractComponent implements KeyListener, RenderableComponent {

  private static final int MAX_MESSAGE_LENGTH = 0;
  private StringBuffer textBuffer;
  private boolean textInputEnabled;
  private Input input;
  private Camera camera;
  private String message = "";
  private int cursorPosition;
  private EventManager eventManager;

  public ChatComponent(Input input, Camera camera, EventManager eventManager) {
    this.input = input;
    this.camera = camera;
    this.textBuffer = new StringBuffer();
    this.cursorPosition = 0;
    this.eventManager = eventManager;
    input.addKeyListener(this);
  }

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}

  @Override
  public void onUpdate(float tpf) {
    message = textBuffer.toString();
  }

  @Override
  public void render(Graphics g) {
    if (!textInputEnabled) return;

    Font font = new Font("monogram-extended", 32, Font.PLAIN);
    int y = g.getHeight() - 20;
    int cursorWidth = 10;
    int offsetX = 20;
    g.setColor(Color.YELLOW);

    g.setFont(font);
    g.text(message, 20, g.getHeight() - offsetX);
    g.setColor(Color.WHITE);

    float pos = g.textWidth(message);
    g.drawLine(pos + offsetX, y, pos + offsetX + cursorWidth, y);
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    if (!shouldProcess(e)) return;
    processEvent(e);
  }

  private boolean shouldProcess(KeyEvent e) {
    return e.getKey() == Key.BACKSPACE || e.getKey() == Key.ENTER || e.getKey() == Key.DELETE;
  }

  @Override
  public void onKeyReleased(KeyEvent e) {}

  private void processEvent(KeyEvent e) {
    if (!textInputEnabled) return;

    if (e.getKey() == Key.BACKSPACE) {
      if (cursorPosition > 0) {
        textBuffer.deleteCharAt(cursorPosition - 1);
        cursorPosition--;
      }
    } else if (e.getKey() == Key.DELETE) {
      if (cursorPosition < textBuffer.length()) {
        textBuffer.deleteCharAt(cursorPosition);
      }
    } else if (e.getKey() == Key.ENTER) {
      String messageToSend = textBuffer.toString();
      sendMessage(messageToSend); // Trigger the event here
      textInputEnabled = false;
      textBuffer.delete(0, textBuffer.length());
      cursorPosition = 0;
    } else {
      textBuffer.insert(cursorPosition, e.getChar());
      cursorPosition++;
    }
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    if (e.getKey() != Key.BACKSPACE && e.getKey() != Key.DELETE) {
      processEvent(e);
    }
    if (e.getKey() == Key.T && !textInputEnabled) {
      textInputEnabled = !textInputEnabled;
    }
  }

  // Method to send the message and trigger the event
  private void sendMessage(String message) {
    MessageSentEvent messageSentEvent = new MessageSentEvent(message);
    eventManager.triggerEvent(messageSentEvent);
  }
}

package client.usecases.chat;

import java.util.LinkedList;
import java.util.List;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.resources.Font;
import math.Color;

public class ChatViewComponent extends AbstractComponent implements RenderableComponent, ChatView {

  private static final int MAX_BUFFER = 200;
  private static final int VISIBLE_MESSAGES = 10;

  private static final float MESSAGE_LIFETIME = 10f; // seconds before fade
  private static final float FADE_TIME = 3f;

  private final List<ChatMessage> messages = new LinkedList<>();

  private final Font font = new Font("monogram-extended", 24, Font.PLAIN);

  private boolean chatOpen = false;

  @Override
  public void addMessage(ChatMessage message) {
    messages.add(message);

    if (messages.size() > MAX_BUFFER) {
      messages.remove(0);
    }
  }

  @Override
  public void render(Graphics g) {

    if (messages.isEmpty()) return;

    g.setFont(font);

    int offsetX = 20;
    int baseY = g.getHeight() - 80;
    int lineHeight = 26;

    int rendered = 0;

    for (int i = messages.size() - 1; i >= 0; i--) {

      ChatMessage msg = messages.get(i);

      float age = (System.currentTimeMillis() - msg.getTimestamp()) / 1000f;

      float alpha = 1f;

      if (!chatOpen) {

        if (age > MESSAGE_LIFETIME + FADE_TIME) {
          continue;
        }

        if (age > MESSAGE_LIFETIME) {
          float fade = (age - MESSAGE_LIFETIME) / FADE_TIME;
          alpha = 1f - fade;
        }
      }

      int y = baseY - rendered * lineHeight;

      drawMessage(g, msg, offsetX, y, alpha);

      rendered++;

      if (rendered >= VISIBLE_MESSAGES) break;
    }
  }

  private void drawMessage(Graphics g, ChatMessage msg, int x, int y, float alpha) {

    String text = msg.getText();

    float width = g.textWidth(text) + 10;

    // background
    g.setColor(new Color(0, 0, 0, 120 * alpha));
    g.fillRect(x - 5, y - 20, width, 24);

    // text
    g.setColor(new Color(255, 255, 255, 255 * alpha));
    g.text(text, x, y);
  }

  public void setChatOpen(boolean open) {
    this.chatOpen = open;
  }

  @Override
  public boolean isOpen() {
    return chatOpen;
  }
}

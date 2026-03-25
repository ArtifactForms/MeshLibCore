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
    int padding = 10;

    List<ChatMessage> visibleNow = new LinkedList<>();
    for (int i = messages.size() - 1; i >= 0; i--) {
      ChatMessage msg = messages.get(i);
      float age = (System.currentTimeMillis() - msg.getTimestamp()) / 1000f;

      if (!chatOpen && age > MESSAGE_LIFETIME + FADE_TIME) {
        continue;
      }
      visibleNow.add(msg);
      if (visibleNow.size() >= VISIBLE_MESSAGES) break;
    }

    if (visibleNow.isEmpty()) return;

    int bgHeight = visibleNow.size() * lineHeight + padding;
    int bgY = baseY - (visibleNow.size() - 1) * lineHeight - 20;
    int bgWidth = 400;

    float bgAlpha = chatOpen ? 1.0f : getAlphaForMessage(visibleNow.get(0));

    g.setColor(new Color(0, 0, 0, 0.5f * bgAlpha));
    g.fillRect(offsetX - 5, bgY, bgWidth, bgHeight);

    int rendered = 0;
    for (ChatMessage msg : visibleNow) {
      float alpha = chatOpen ? 1.0f : getAlphaForMessage(msg);
      int y = baseY - rendered * lineHeight;

      g.setColor(new Color(1, 1, 1, alpha));
      g.text(msg.getText(), offsetX, y);
      rendered++;
    }
  }

  private float getAlphaForMessage(ChatMessage msg) {
    float age = (System.currentTimeMillis() - msg.getTimestamp()) / 1000f;
    if (age <= MESSAGE_LIFETIME) return 1f;
    float fade = (age - MESSAGE_LIFETIME) / FADE_TIME;
    return Math.max(0, 1f - fade);
  }

  public void setChatOpen(boolean open) {
    this.chatOpen = open;
  }

  @Override
  public boolean isOpen() {
    return chatOpen;
  }
}

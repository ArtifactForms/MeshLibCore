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
  private static final int VISIBLE_MESSAGES = 20;

  private static final float MESSAGE_LIFETIME = 10f; // seconds before fade
  private static final float FADE_TIME = 3f;

  private final List<ChatMessage> messages = new LinkedList<>();

  private final Font font = new Font("monogram-extended", 32, Font.PLAIN);

  private boolean chatOpen = false;

  @Override
  public void addMessage(ChatMessage message) {
    String[] lines = message.getText().split("\n");
    long syncTime = message.getTimestamp();

    for (String line : lines) {
      if (line.isEmpty()) continue;

      messages.add(new ChatMessage(line, syncTime));

      if (messages.size() > MAX_BUFFER) {
        messages.remove(0);
      }
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
          if (!chatOpen && age > MESSAGE_LIFETIME + FADE_TIME) continue;
          visibleNow.add(msg);
          if (visibleNow.size() >= VISIBLE_MESSAGES) break;
      }

      if (visibleNow.isEmpty()) return;

      int maxTextWidth = 0;
      for (ChatMessage msg : visibleNow) {
          String cleanText = stripColor(msg.getText());
          int width = (int) g.textWidth(cleanText);
          if (width > maxTextWidth) {
              maxTextWidth = width;
          }
      }
      int bgWidth = maxTextWidth + (padding * 2) + 10;
      // ----------------------------------------

      int bgHeight = visibleNow.size() * lineHeight + padding;
      int bgY = baseY - (visibleNow.size() - 1) * lineHeight - 20;

      float bgAlpha = chatOpen ? 1.0f : getAlphaForMessage(visibleNow.get(0));
      g.setColor(new Color(0, 0, 0, 0.5f * bgAlpha));
      g.fillRect(offsetX - 5, bgY, bgWidth, bgHeight);

      int rendered = 0;
      for (ChatMessage msg : visibleNow) {
          float alpha = chatOpen ? 1.0f : getAlphaForMessage(msg);
          int y = baseY - rendered * lineHeight;
          
          drawColoredText(g, msg.getText(), offsetX, y, alpha);
          rendered++;
      }
  }

  private String stripColor(String text) {
      if (text == null) return "";
      return text.replaceAll("§.", "");
  }
  
  private void drawColoredText(Graphics g, String text, int x, int y, float alpha) {
      String[] parts = text.split("§");
      int currentX = x;
      Color currentColor = new Color(1, 1, 1, alpha);

      for (int i = 0; i < parts.length; i++) {
          String part = parts[i];
          if (part.isEmpty()) continue;

          if (i > 0 || text.startsWith("§")) {
              char colorCode = part.charAt(0);
              currentColor = getColorFromCode(colorCode, alpha);
              part = part.substring(1);
          }
          
          // Shadow
          g.setColor(new Color(0, 0, 0, alpha));
          g.text(part, currentX + 1, y + 1);

          // Text
          g.setColor(currentColor);
          g.text(part, currentX, y);
          
          currentX += g.textWidth(part);
      }
  }

  private Color getColorFromCode(char code, float alpha) {
	    return switch (code) {
	        case '0' -> new Color(0.0f, 0.0f, 0.0f, alpha);         // Black (#000000)
	        case '1' -> new Color(0.0f, 0.0f, 0.66f, alpha);        // Dark Blue (#0000AA)
	        case '2' -> new Color(0.0f, 0.66f, 0.0f, alpha);        // Dark Green (#00AA00)
	        case '3' -> new Color(0.0f, 0.66f, 0.66f, alpha);       // Dark Aqua (#00AAAA)
	        case '4' -> new Color(0.66f, 0.0f, 0.0f, alpha);        // Dark Red (#AA0000)
	        case '5' -> new Color(0.66f, 0.0f, 0.66f, alpha);       // Dark Purple (#AA00AA)
	        case '6' -> new Color(1.0f, 0.66f, 0.0f, alpha);        // Gold (#FFAA00)
	        case '7' -> new Color(0.66f, 0.66f, 0.66f, alpha);      // Gray (#AAAAAA)
	        case '8' -> new Color(0.33f, 0.33f, 0.33f, alpha);      // Dark Gray (#555555)
	        case '9' -> new Color(0.33f, 0.33f, 1.0f, alpha);       // Blue (#5555FF)
	        case 'a' -> new Color(0.33f, 1.0f, 0.33f, alpha);       // Green (#55FF55)
	        case 'b' -> new Color(0.33f, 1.0f, 1.0f, alpha);        // Aqua (#55FFFF)
	        case 'c' -> new Color(1.0f, 0.33f, 0.33f, alpha);       // Red (#FF5555)
	        case 'd' -> new Color(1.0f, 0.33f, 1.0f, alpha);        // Light Purple (#FF55FF)
	        case 'e' -> new Color(1.0f, 1.0f, 0.33f, alpha);        // Yellow (#FFFF55)
	        case 'f' -> new Color(1.0f, 1.0f, 1.0f, alpha);         // White (#FFFFFF)
	        default  -> new Color(1.0f, 1.0f, 1.0f, alpha);
	    };
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

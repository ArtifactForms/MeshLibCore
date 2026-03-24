package client.usecases.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatController {

  private static final int MAX_MESSAGE_LENGTH = 256;
  private static final int MAX_HISTORY_SIZE = 50;

  private final StringBuilder buffer = new StringBuilder();
  private int cursor = 0;
  private boolean open = false;

  private final List<String> history = new ArrayList<>();
  private int historyIndex = -1;
  private String currentDraft = "";

  private final SendChatMessageController sendController;

  public ChatController(SendChatMessageController sendController) {
    this.sendController = sendController;
  }

  public void moveHistoryUp() {
    if (history.isEmpty()) return;

    if (historyIndex == -1) {
      currentDraft = buffer.toString();
    }

    if (historyIndex < history.size() - 1) {
      historyIndex++;
      loadHistoryEntry(history.get(history.size() - 1 - historyIndex));
    }
  }

  public void moveHistoryDown() {
    if (historyIndex == -1) return;

    historyIndex--;

    if (historyIndex == -1) {
      loadHistoryEntry(currentDraft);
    } else {
      loadHistoryEntry(history.get(history.size() - 1 - historyIndex));
    }
  }

  private void loadHistoryEntry(String text) {
    buffer.setLength(0);
    buffer.append(text);
    cursor = buffer.length();
  }

  public void openChat() {
    open = true;
    historyIndex = -1;
    currentDraft = "";
    sendController.onOpenChat();
  }

  public void closeChat() {
    open = false;
    buffer.setLength(0);
    cursor = 0;
    historyIndex = -1;
    sendController.onCloseChat();
  }

  public void send() {
    String msg = buffer.toString().trim();

    if (!msg.isEmpty()) {
      sendController.onSendMessage(msg);

      if (history.isEmpty() || !history.get(history.size() - 1).equals(msg)) {
        history.add(msg);
        if (history.size() > MAX_HISTORY_SIZE) {
          history.remove(0);
        }
      }
    }

    closeChat();
  }

  public void insert(char c) {
    if (buffer.length() >= MAX_MESSAGE_LENGTH) return;

    if (historyIndex != -1) {
      historyIndex = -1;
    }

    buffer.insert(cursor, c);
    cursor++;
  }

  public void backspace() {
    if (cursor == 0) return;
    buffer.deleteCharAt(cursor - 1);
    cursor--;
    if (historyIndex != -1) historyIndex = -1;
  }

  public void delete() {
    if (cursor >= buffer.length()) return;
    buffer.deleteCharAt(cursor);
    if (historyIndex != -1) historyIndex = -1;
  }

  public void moveLeft() {
    if (cursor > 0) cursor--;
  }

  public void moveRight() {
    if (cursor < buffer.length()) cursor++;
  }

  public boolean isOpen() {
    return open;
  }

  public String getText() {
    return buffer.toString();
  }

  public int getCursor() {
    return cursor;
  }
}

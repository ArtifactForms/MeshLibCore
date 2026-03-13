package client.usecases.chat;

public class ChatController {

  private static final int MAX_MESSAGE_LENGTH = 256;

  private final StringBuilder buffer = new StringBuilder();

  private int cursor = 0;
  private boolean open = false;

  private final SendChatMessageController sendController;

  public ChatController(SendChatMessageController sendController) {
    this.sendController = sendController;
  }

  public void openChat() {
    open = true;
    sendController.onOpenChat();
  }

  public void closeChat() {
    open = false;
    buffer.setLength(0);
    cursor = 0;
    sendController.onCloseChat();
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

  // =========================
  // Editing
  // =========================

  public void insert(char c) {

    if (buffer.length() >= MAX_MESSAGE_LENGTH) return;

    buffer.insert(cursor, c);
    cursor++;
  }

  public void backspace() {

    if (cursor == 0) return;

    buffer.deleteCharAt(cursor - 1);
    cursor--;
  }

  public void delete() {

    if (cursor >= buffer.length()) return;

    buffer.deleteCharAt(cursor);
  }

  public void moveLeft() {

    if (cursor > 0) cursor--;
  }

  public void moveRight() {

    if (cursor < buffer.length()) cursor++;
  }

  public void send() {

    String msg = buffer.toString().trim();

    if (!msg.isEmpty()) {
      sendController.onSendMessage(msg);
    }

    closeChat();
  }
}

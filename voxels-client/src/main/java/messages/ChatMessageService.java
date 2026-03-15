package messages;

import client.usecases.chat.ChatMessage;
import client.usecases.chat.ChatView;

public class ChatMessageService implements MessageService {

  private ChatView chatView;

  public ChatMessageService(ChatView chatView) {
    this.chatView = chatView;
  }

  @Override
  public void displayMessage(MessagePrefix prefix, String message) {
    String msg = "[" + prefix + "] " + message;
    chatView.addMessage(new ChatMessage(msg));
  }
}

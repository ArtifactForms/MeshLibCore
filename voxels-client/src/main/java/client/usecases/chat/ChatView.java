package client.usecases.chat;

public interface ChatView {

  void addMessage(ChatMessage chatMessage);

  boolean isOpen();

  void setChatOpen(boolean open);
}

package client.usecases.chat;

import client.app.GameClient;
import common.network.packets.ChatMessagePacket;

public class SendChatMessageController {

  private final GameClient client;

  public SendChatMessageController(GameClient client) {
    this.client = client;
  }

  void onSendMessage(String message) {
    client.getNetwork().send(new ChatMessagePacket(message));
  }

  void onOpenChat() {
    client.getView().getChatView().setChatOpen(true);
  }

  void onCloseChat() {
    client.getView().getChatView().setChatOpen(false);
  }
}

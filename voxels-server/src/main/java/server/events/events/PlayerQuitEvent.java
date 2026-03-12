package server.events.events;

import server.events.GameEvent;
import server.player.ServerPlayer;

public class PlayerQuitEvent extends GameEvent {

  private final ServerPlayer player;

  private String quitMessage;

  public PlayerQuitEvent(ServerPlayer player, String quitMessage) {
    this.player = player;
    this.quitMessage = quitMessage;
  }

  public ServerPlayer getPlayer() {
    return player;
  }

  public String getQuitMessage() {
    return quitMessage;
  }

  public void setQuitMessage(String quitMessage) {
    this.quitMessage = quitMessage;
  }
}

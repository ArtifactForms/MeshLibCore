package server.usecases.changegamemode;

import java.util.UUID;

import common.game.GameMode;
import server.gateways.MessageGateway;
import server.usecases.changegamemode.ChangeGamemode.ChangeGameModeResponse;

public class ChangeGameModePresenter implements ChangeGameModeResponse {

  private final MessageGateway messages;

  public ChangeGameModePresenter(MessageGateway messages) {
    this.messages = messages;
  }

  @Override
  public void onGameModeChanged(UUID playerId, GameMode gameMode) {
    messages.sendMessage(playerId, "Gamemode set to: " + gameMode.name().toLowerCase());
  }

  @Override
  public void onFailed(UUID playerId) {
    messages.sendMessage(playerId, "Failed to update gamemode. Are you online?");
  }
}

package client.app;

import common.network.NetworkPackets;
import common.network.packets.PlayerJoinPacket;
import engine.application.BasicApplication;
import engine.runtime.input.Input;

public class StartGame {

  private Input input;

  public StartGame(Input input) {
    this.input = input;
  }

  public void execute() {

    try {

      // Register packets once
      NetworkPackets.register();

      // Create network and store globally
      ApplicationContext.network.connect("localhost");
      ApplicationContext.network.send(
          new PlayerJoinPacket(ApplicationContext.playerUiid, "Test-Player"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    // Switch scene
    GameScene scene = new GameScene(input);
    BasicApplication application = ApplicationContext.application;
    application.setActiveScene(scene);
  }
}

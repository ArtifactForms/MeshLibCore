package client.usecases.start;

import client.app.GameClient;
import client.scene.GameScene;
import client.usecases.connecttoserver.ConnectToServerController;
import engine.runtime.input.Input;

public class StartGame {

  private Input input;
  private GameClient client;

  public StartGame(Input input, GameClient client) {
    this.input = input;
    this.client = client;
  }

  public void execute() {

//    new ConnectToServerController(client).connect();

    // Switch scene
    GameScene scene = new GameScene(input, client);
    client.getSceneManager().setActiveScene(scene);
  }
}

package client.main;

import client.app.ClientApplication;
import engine.application.ApplicationSettings;

public class ClientMain {

  public static void main(String[] args) {
    ClientApplication application = new ClientApplication();
    application.launch(ApplicationSettings.defaultSettings().setFullscreen(true));
  }
}

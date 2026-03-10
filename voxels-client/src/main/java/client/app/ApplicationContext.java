package client.app;

import java.util.UUID;

import client.network.ClientNetwork;
import client.player.ClientMovementConsumer;
import client.player.FPSControl;
import client.ui.ClientView;
import client.ui.actionbar.ActionBarComponent;
import client.world.ChunkManager;
import client.world.ClientWorld;
import common.game.Hotbar;
import engine.application.BasicApplication;
import engine.scene.Scene;

public class ApplicationContext {

  public static BasicApplication application;

  public static ClientNetwork network;

  public static UUID playerUiid = UUID.randomUUID();

//  public static TextDisplay display = new TextDisplay();

  public static ChunkManager chunkManager;

  public static Scene activeScene;

  public static ClientMovementConsumer clientMovementConsumer;
  
  public static ClientWorld clientWorld;
  
  public static Hotbar hotbar;
  
  public static ClientView view;
  
  public static FPSControl fpsController;
}

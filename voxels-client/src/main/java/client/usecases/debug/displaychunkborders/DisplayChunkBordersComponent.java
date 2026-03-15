package client.usecases.debug.displaychunkborders;

import client.player.ClientPlayer;
import client.rendering.ChunkBordersRenderer;
import client.settings.KeyBinds;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import math.Vector3f;
import messages.MessagePrefix;
import messages.MessageService;

public class DisplayChunkBordersComponent extends AbstractComponent implements RenderableComponent {

  private boolean visible;
  private Input input;
  private ClientPlayer player;
  private MessageService messageService;

  public DisplayChunkBordersComponent(
      Input input, ClientPlayer player, MessageService messageService) {
    this.input = input;
    this.player = player;
    this.messageService = messageService;
  }

  @Override
  public void onUpdate(float tpf) {
    if (input.wasKeyPressed(KeyBinds.showHideChunkBorders)) {
      visible = !visible;
      String value = visible ? "shown" : "hidden";
      messageService.displayMessage(MessagePrefix.DEBUG, "Chunks borders: " + value);
    }
  }

  @Override
  public void render(Graphics g) {
    if (!visible) return;
    Vector3f position = player.getPosition();
    ChunkBordersRenderer.render(g, position);
  }
}

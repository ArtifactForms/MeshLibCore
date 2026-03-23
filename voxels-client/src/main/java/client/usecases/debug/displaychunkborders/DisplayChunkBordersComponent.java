package client.usecases.debug.displaychunkborders;

import client.player.ClientPlayer;
import client.rendering.ChunkBordersRenderer;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import math.Vector3f;
import messages.MessagePrefix;
import messages.MessageService;

public class DisplayChunkBordersComponent extends AbstractComponent implements RenderableComponent {

  private boolean visible;
  private ClientPlayer player;
  private MessageService messageService;

  public DisplayChunkBordersComponent(
      Input input, ClientPlayer player, MessageService messageService) {
    this.player = player;
    this.messageService = messageService;
  }

  public void toggle() {
    visible = !visible;
    String value = visible ? "shown" : "hidden";
    messageService.displayMessage(MessagePrefix.DEBUG, "Chunks borders: " + value);
  }
  
  @Override
  public void render(Graphics g) {
    if (!visible) return;
    Vector3f position = player.getPosition();
    ChunkBordersRenderer.render(g, position);
  }
}

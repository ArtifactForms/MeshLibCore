package debug;

import client.rendering.RenderSettings;
import client.usecases.debug.displaychunkborders.DisplayChunkBordersComponent;
import messages.MessagePrefix;
import messages.MessageService;

public class DebugController {

  private DisplayChunkBordersComponent displayChunkBordersComponent;

  private MessageService messageService;

  public DebugController(
      MessageService messageService, DisplayChunkBordersComponent displayChunkBordersComponent) {
    this.messageService = messageService;
    this.displayChunkBordersComponent = displayChunkBordersComponent;
  }

  public void onShowHideChunkBorders() {
    displayChunkBordersComponent.toggle();
  }

  public void onEnableDisableFrustumCulling() {
    RenderSettings.frustum_Culling = !RenderSettings.frustum_Culling;
    String value = RenderSettings.frustum_Culling ? "enabled" : "disabled";
    messageService.displayMessage(MessagePrefix.DEBUG, "Frustum culling: " + value);
  }

  public void onEnableDisableChunkBounds() {
    RenderSettings.debugChunkBounds = !RenderSettings.debugChunkBounds;
    String value = RenderSettings.debugChunkBounds ? "enabled" : "disabled";
    messageService.displayMessage(MessagePrefix.DEBUG, "Chunk bounds: " + value);
  }
}

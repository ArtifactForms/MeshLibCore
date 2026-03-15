package debug;

import client.rendering.RenderSettings;
import messages.MessagePrefix;
import messages.MessageService;

public class DebugController {

  private boolean chunkBordersVisible;
  private MessageService messageService;

  public DebugController(MessageService messageService) {
    this.messageService = messageService;
  }

  void onShowHideChunkBorders() {
	  
  }

  void onEnableDisableFrustumCulling() {
    RenderSettings.frustum_Culling = !RenderSettings.frustum_Culling;
    String value = RenderSettings.frustum_Culling ? "enabled" : "disabled";
    messageService.displayMessage(MessagePrefix.DEBUG, "Frustum culling: " + value);
  }
}

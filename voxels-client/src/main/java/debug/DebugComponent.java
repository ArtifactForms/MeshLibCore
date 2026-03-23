package debug;

import client.settings.KeyBinds;
import engine.components.AbstractComponent;
import engine.runtime.input.Input;

public class DebugComponent extends AbstractComponent {

  private Input input;
  private DebugController controller;

  public DebugComponent(Input input, DebugController controller) {
    this.input = input;
    this.controller = controller;
  }

  @Override
  public void onUpdate(float tpf) {
    if (isGameplayBlocked()) {
      return;
    }

    if (input.wasKeyReleased(KeyBinds.enableDisableFrustumCulling)) {
      controller.onEnableDisableFrustumCulling();
    }

    if (input.wasKeyReleased(KeyBinds.showHideChunkBorders)) {
      controller.onShowHideChunkBorders();
    }
  }

  private boolean isGameplayBlocked() {
    return getOwner().getScene().getTopScreen().blocksGameplay();
  }
}

package engine.debug.core.command;

import engine.debug.core.config.DebugDepthMode;
import engine.debug.core.config.DebugLayer;

public abstract class DebugCommand {

  /** seconds, <= 0 = one frame */
  public float ttl = 0f;

  public DebugDepthMode depthMode = DebugDepthMode.DEPTH_TESTED;

  public DebugLayer layer = DebugLayer.DEFAULT;
}

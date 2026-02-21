package engine.runtime.debug.core.command;

import engine.runtime.debug.core.config.DebugDepthMode;
import engine.runtime.debug.core.config.DebugLayer;

public abstract class DebugCommand {

  public static final float PERSISTENT = -1f;
  public static final float ONE_FRAME = 0;

  /**
   * <pre>
   * Time To Live (seconds).
   *  ttl == 0 : one frame
   *  ttl > 0 : seconds
   *  ttl < 0 : persistent
   *  </pre>
   */
  public float ttl = ONE_FRAME;

  public DebugDepthMode depthMode = DebugDepthMode.DEPTH_TESTED;

  public DebugLayer layer = DebugLayer.DEFAULT;
}

package engine.runtime.debug.core.render;

import java.util.List;

import engine.render.Graphics;
import engine.runtime.debug.core.command.DebugLineCommand;
import math.Color;
import math.Vector3f;

public final class DebugLineRenderer implements DebugCommandRenderer<DebugLineCommand> {

  @Override
  public Class<DebugLineCommand> getCommandType() {
    return DebugLineCommand.class;
  }

  @Override
  public void render(Graphics g, DebugLineCommand command) {
    g.setColor(command.color);
    g.drawLine(command.a.x, command.a.y, command.a.z, command.b.x, command.b.y, command.b.z);
  }
  
  @Override
  public void renderBatch(Graphics g, List<DebugLineCommand> commands) {
      int n = commands.size();
      Vector3f[] vertices = new Vector3f[n * 2];
      Color[] colors = new Color[n * 2];

      int i = 0;
      for (DebugLineCommand c : commands) {
          vertices[i] = c.a;
          colors[i++] = c.color;
          vertices[i] = c.b;
          colors[i++] = c.color;
      }

      g.drawLines(vertices, colors);
  }
}

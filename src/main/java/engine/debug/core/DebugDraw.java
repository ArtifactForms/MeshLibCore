package engine.debug.core;

import engine.components.Transform;
import engine.debug.core.command.DebugAxisCommand;
import engine.debug.core.command.DebugBoundsCommand;
import engine.debug.core.command.DebugGridCommand;
import engine.debug.core.command.DebugLineCommand;
import engine.debug.core.command.DebugPointCommand;
import engine.debug.core.command.DebugRayCommand;
import engine.debug.core.command.DebugSphereCommand;
import math.Bounds;
import math.Color;
import math.Vector3f;

public final class DebugDraw {

  private static DebugContext context;

  public static void initialize(DebugContext debugSystem) {
    context = debugSystem;
  }

  public static void drawLine(Vector3f a, Vector3f b, Color color) {
    context.submit(new DebugLineCommand(a, b, color));
  }

  public static void drawPoint(Vector3f p, float size, Color color) {
    context.submit(new DebugPointCommand(size, p, color));
  }

  public static void drawAxis(Transform transform, float size) {
    context.submit(new DebugAxisCommand(size, transform));
  }

  public static void drawRay(Vector3f origin, Vector3f direction, float length, Color color) {
    context.submit(new DebugRayCommand(origin, direction, length, color));
  }

  public static void drawBounds(Bounds bounds, Color color) {
    context.submit(new DebugBoundsCommand(bounds, color));
  }

  public static void drawSphere(Vector3f center, float radius, Color color) {
    context.submit(new DebugSphereCommand(radius, center, color));
  }

  public static void drawGrid(
      Vector3f origin,
      int cells,
      float spacing,
      int majorLineEvery,
      Color majorColor,
      Color minorColor) {
    context.submit(
        new DebugGridCommand(cells, spacing, majorLineEvery, origin, majorColor, minorColor));
  }
}

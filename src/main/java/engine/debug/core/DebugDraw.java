package engine.debug.core;

import engine.components.Transform;
import engine.debug.core.command.DebugAxisCommand;
import engine.debug.core.command.DebugBoundsCommand;
import engine.debug.core.command.DebugCommand;
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
    DebugLineCommand c = new DebugLineCommand(a, b, color);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawLine(Vector3f a, Vector3f b, Color color, float ttl) {
    DebugLineCommand c = new DebugLineCommand(a, b, color);
    c.ttl = ttl;
    context.submit(c);
  }

  public static void drawPoint(Vector3f p, float size, Color color) {
    DebugPointCommand c = new DebugPointCommand(size, p, color);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawPoint(Vector3f p, float size, Color color, float ttl) {
    DebugPointCommand c = new DebugPointCommand(size, p, color);
    c.ttl = ttl;
    context.submit(c);
  }

  public static void drawAxis(Transform transform, float size) {
    DebugAxisCommand c = new DebugAxisCommand(size, transform);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawAxis(Transform transform, float size, float ttl) {
    DebugAxisCommand c = new DebugAxisCommand(size, transform);
    c.ttl = ttl;
    context.submit(c);
  }

  public static void drawRay(Vector3f origin, Vector3f direction, float length, Color color) {
    DebugRayCommand c = new DebugRayCommand(origin, direction, length, color);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawRay(
      Vector3f origin, Vector3f direction, float length, Color color, float ttl) {
    DebugRayCommand c = new DebugRayCommand(origin, direction, length, color);
    c.ttl = ttl;
    context.submit(c);
  }

  public static void drawBounds(Bounds bounds, Color color) {
    DebugBoundsCommand c = new DebugBoundsCommand(bounds, color);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawBounds(Bounds bounds, Color color, float ttl) {
    DebugBoundsCommand c = new DebugBoundsCommand(bounds, color);
    c.ttl = ttl;
    context.submit(c);
  }

  public static void drawSphere(Vector3f center, float radius, Color color) {
    DebugSphereCommand c = new DebugSphereCommand(radius, center, color);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawSphere(Vector3f center, float radius, Color color, float ttl) {
    DebugSphereCommand c = new DebugSphereCommand(radius, center, color);
    c.ttl = ttl;
    context.submit(c);
  }

  public static void drawGrid(
      Vector3f origin,
      int cells,
      float spacing,
      int majorLineEvery,
      Color majorColor,
      Color minorColor) {
    DebugGridCommand c =
        new DebugGridCommand(cells, spacing, majorLineEvery, origin, majorColor, minorColor);
    c.ttl = DebugCommand.ONE_FRAME;
    context.submit(c);
  }

  public static void drawGrid(
      Vector3f origin,
      int cells,
      float spacing,
      int majorLineEvery,
      Color majorColor,
      Color minorColor,
      float ttl) {
    DebugGridCommand c =
        new DebugGridCommand(cells, spacing, majorLineEvery, origin, majorColor, minorColor);
    c.ttl = ttl;
    context.submit(c);
  }
}

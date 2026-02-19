package engine.backend.processing;

import processing.core.PApplet;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShapeOpenGL;

public class Processing {

  public static PApplet parent;

  public static PGL getPGL() {
    return ((PGraphicsOpenGL) parent.g).pgl;
  }

  public static PShapeOpenGL createShape() {
    return (PShapeOpenGL) parent.createShape();
  }
}

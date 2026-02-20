package engine.backend.processing;

import engine.render.Material;
import engine.resources.Texture;
import engine.vbo.VBO;
import processing.core.PApplet;
import processing.opengl.PShapeOpenGL;

public class BufferedShape {

  public static final int TRIANGLES = PApplet.TRIANGLES;
  public static final int QUADS = PApplet.QUADS;

  private PShapeOpenGL shape;
  private Material material;

  public BufferedShape(Material material) {
    if (material == null) {
      throw new IllegalArgumentException("Material cannot be null.");
    }
    this.material = material;
  }

  public void begin(int type) {
    shape = Processing.createShape(); // Exception handling if no context is available.
    shape.beginShape(type);
    shape.noStroke();
    applyMaterial();
  }

  public void end() {
    shape.endShape();
    applyTexture();
  }

  public void normal(float nx, float ny, float nz) {
    shape.normal(nx, ny, nz);
  }

  public void vertex(float x, float y, float z) {
    shape.vertex(x, y, z);
  }

  public void vertex(float x, float y, float z, float u, float v) {
    shape.vertex(x, y, z, u, v);
  }

  private void applyMaterial() {
    shape.fill(material.getColor().getRGBA());
  }

  private void applyTexture() {
    Texture texture = material.getDiffuseTexture();

    if (texture == null) return;

    ProcessingTexture processingTexture = (ProcessingTexture) texture.getBackendTexture();
    shape.setTextureMode(PApplet.NORMAL);
    shape.setTexture(processingTexture.getImage());
  }

  public VBO getVBO() {
    return new VBOProcessing(shape);
  }
}

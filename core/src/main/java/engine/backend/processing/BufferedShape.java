package engine.backend.processing;

import engine.rendering.Material;
import engine.resources.Texture;
import engine.vbo.VBO;
import math.Bounds;
import math.Vector3f;
import processing.core.PApplet;
import processing.opengl.PShapeOpenGL;

public class BufferedShape {

  public static final int TRIANGLES = PApplet.TRIANGLES;

  public static final int QUADS = PApplet.QUADS;

  private float minX;

  private float minY;

  private float minZ;

  private float maxX;

  private float maxY;

  private float maxZ;

  private PShapeOpenGL shape;

  private Material material;

  public BufferedShape(Material material) {
    if (material == null) {
      throw new IllegalArgumentException("Material cannot be null.");
    }
    this.material = material;
  }

  public void begin(int type) {
    shape = Processing.createShape();
    shape.beginShape(type);
    shape.noStroke();
    applyMaterial();

    minX = minY = minZ = Float.POSITIVE_INFINITY;
    maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
  }

  public void end() {
    shape.endShape();
    applyTexture();
  }

  public void normal(float nx, float ny, float nz) {
    shape.normal(nx, ny, nz);
  }

  public void vertex(float x, float y, float z) {
    updateBounds(x, y, z);
    shape.vertex(x, y, z);
  }

  public void vertex(float x, float y, float z, float u, float v) {
    updateBounds(x, y, z);
    shape.vertex(x, y, z, u, v);
  }

  public void color(float r, float g, float b) {
    shape.fill(r, g, b);
  }

  private void updateBounds(float x, float y, float z) {
    if (x < minX) minX = x;
    if (y < minY) minY = y;
    if (z < minZ) minZ = z;

    if (x > maxX) maxX = x;
    if (y > maxY) maxY = y;
    if (z > maxZ) maxZ = z;
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
    return new VBOProcessing(
        shape, new Bounds(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ)));
  }
}

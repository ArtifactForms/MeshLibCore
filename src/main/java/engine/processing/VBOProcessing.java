package engine.processing;

import engine.render.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.vbo.VBO;
import math.Vector2f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

public class VBOProcessing implements VBO {
  private PShape shape;
  private PGraphics graphics;
  private int faceCount;
  private int vertexCount;

  public VBOProcessing(PGraphics graphics) {
    this.graphics = graphics;
  }

  @Override
  public void create(Mesh3D mesh, Material material) { // TODO Auto-generated method stub
    faceCount = mesh.getFaceCount();
    vertexCount = mesh.getVertexCount();

    shape = graphics.createShape();

    shape.beginShape();

    shape.noStroke();

    for (Face3D f : mesh.getFaces()) {
      if (f.indices.length == 3) {
        shape.beginShape(PApplet.TRIANGLES);
      } else if (f.indices.length == 4) {
        shape.beginShape(PApplet.QUADS);
      } else {
        shape.beginShape(PApplet.POLYGON);
      }

      //      applyTexture();

      int[] indices = f.indices;
      for (int i = 0; i < indices.length; i++) {
        Vector3f v = mesh.vertices.get(f.indices[i]);
        int uvIndex = f.getUvIndexAt(i);
        if (uvIndex != -1) {
          Vector2f uv = mesh.getUvAt(uvIndex);
          shape.vertex(v.getX(), v.getY(), v.getZ(), uv.getX(), 1 - uv.getY());
        } else {
          shape.vertex(v.getX(), v.getY(), v.getZ());
        }
      }
      //      shape.endShape();
    }

    shape.endShape();

    Texture texture = material.getDiffuseTexture();
    if (texture != null) {
      ProcessingTexture processingTexture = (ProcessingTexture) texture.getBackendTexture();
      shape.setTextureMode(PApplet.NORMAL);
      shape.setTexture(processingTexture.getImage());
    }
  }

  private int getSamplingMode(FilterMode filterMode) {
    switch (filterMode) {
      case POINT:
        return 2;
      case LINEAR:
        return 3;
      case BILINEAR:
        return 4;
      case TRILINEAR:
        return 5;
      default:
        System.err.println("Warning: Unexpected filter mode value: " + filterMode);
        return 4; // Default to BILINEAR
    }
  }

  @Override
  public void create(float[] vertices, int[] indices) {
    // Create a PShape object for the mesh
    //    shape = new PShape(PShape.POINTS, vertices.length / 3);
    shape = new PShape();

    // Load vertices into the PShape
    shape.beginShape();
    for (int i = 0; i < vertices.length; i += 3) {
      shape.vertex(vertices[i], vertices[i + 1], vertices[i + 2]);
    }
    shape.endShape();

    // Optional: store indices if using indexed rendering
    vertexCount = vertices.length / 3; // Assuming vertices are in 3D (x, y, z)
  }

  @Override
  public void bind() {
    // Binding is not necessary for PShape in Processing
    // Processing automatically handles the shape for rendering
  }

  @Override
  public void unbind() {
    // Unbinding is not needed in Processing as it handles rendering context automatically
  }

  @Override
  public void updateData(float[] newData) {
    // Update the vertices in the PShape
    shape.beginShape();
    for (int i = 0; i < newData.length; i += 3) {
      shape.vertex(newData[i], newData[i + 1], newData[i + 2]);
    }
    shape.endShape();
  }

  @Override
  public void delete() {
    // In Processing, there's no explicit delete method for PShape
    shape = null;
  }

  @Override
  public int getVertexCount() {
    return vertexCount;
  }

  @Override
  public int getFaceCount() { // TODO Auto-generated method stub
    return faceCount;
  }

  public void draw(PGraphics graphics) {
    // Use Processing's drawing methods to render the shape
    graphics.shape(shape, 0, 0);
  }
}

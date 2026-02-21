package engine.backend.processing;

import java.util.ArrayList;

import engine.render.Material;
import engine.resources.Texture;
import engine.vbo.VBO;
import math.Bounds;
import math.Vector2f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.next.surface.SurfaceLayer;
import mesh.util.MeshBoundsCalculator;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

public class VBOProcessing implements VBO {
  private PShape shape;
  private PGraphics graphics;
  private int faceCount;
  private int vertexCount;
  private Bounds bounds;

  public VBOProcessing(PGraphics graphics) {
    this.graphics = graphics;
  }

  public VBOProcessing(PShape shape) {
    this.shape = shape;
  }

  @Override
  public void create(Mesh3D mesh, Material material) {
    SurfaceLayer surfaceLayer = mesh.getSurfaceLayer();
    bounds = MeshBoundsCalculator.calculateBounds(mesh);

    ArrayList<Vector3f> vertexNormals = mesh.getVertexNormals();

    faceCount = mesh.getFaceCount();
    vertexCount = mesh.getVertexCount();

    shape = graphics.createShape();

    shape.beginShape();
    shape.noStroke();
    // TODO Full material support
    shape.fill(material.getColor().getRGBA());

    for (int idx = 0; idx < mesh.getFaceCount(); idx++) {
      Face3D f = mesh.getFaceAt(idx);
      if (f.indices.length == 3) {
        shape.beginShape(PApplet.TRIANGLES);
      } else if (f.indices.length == 4) {
        shape.beginShape(PApplet.QUADS);
      } else {
        shape.beginShape(PApplet.POLYGON);
      }
      int[] indices = f.indices;
      for (int i = 0; i < indices.length; i++) {
        Vector3f v = mesh.getVertexAt(f.indices[i]);

        if (!vertexNormals.isEmpty()) {
          Vector3f vertexNormal = vertexNormals.get(f.indices[i]);
          shape.normal(vertexNormal.getX(), vertexNormal.getY(), vertexNormal.getZ());
        }

        int[] uvIndices = null;
        if (surfaceLayer.getUVCount() > 0) {
          uvIndices = surfaceLayer.getFaceUVIndices(idx);
        }

        if (uvIndices != null) {
          int uvIndex = uvIndices[i];
          Vector2f uv = surfaceLayer.getUvAt(uvIndex);
          shape.vertex(v.getX(), v.getY(), v.getZ(), uv.getX(), 1 - uv.getY());
        } else {
          shape.vertex(v.getX(), v.getY(), v.getZ());
        }
      }
    }
    shape.endShape();

    Texture texture = material.getDiffuseTexture();
    if (texture != null) {
      ProcessingTexture processingTexture = (ProcessingTexture) texture.getBackendTexture();
      shape.setTextureMode(PApplet.NORMAL);
      shape.setTexture(processingTexture.getImage());
    }
  }

  @Override
  public void create(float[] vertices, int[] indices) {
    throw new UnsupportedOperationException("ProcessingVBO requires Mesh3D");
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

  @Override
  public Bounds getBounds() {
    if (bounds == null)
      return new Bounds(
          new Vector3f(), new Vector3f()); // Hack in case of not created via constructor mesh
    return new Bounds(bounds.getMin(), bounds.getMax());
  }
}

package engine.vbo;

import engine.rendering.Material;
import math.Bounds;
import mesh.Mesh3D;

public interface VBO {
  void create(float[] vertices, int[] indices);

  void create(Mesh3D mesh, Material material);

  void bind();

  void unbind();

  void updateData(float[] newData);

  void delete();

  int getVertexCount();

  int getFaceCount();

  Bounds getBounds();
}

package mesh;

import java.util.Arrays;

import math.Color;
import math.Vector3f;

public class Face3D {

  public Color color;

  public int[] indices;

  public int[] uvIndices;

  public Vector3f normal;

  public String tag;

  public Face3D() {
    this(new int[0], new int[0]);
  }

  public Face3D(int... indices) {
    this(indices, new int[0]);
  }

  public Face3D(Face3D f) {
    this.color = new Color(f.color);
    this.indices = Arrays.copyOf(f.indices, f.indices.length);
    this.uvIndices = f.uvIndices != null ? Arrays.copyOf(f.uvIndices, f.uvIndices.length) : null;
    this.normal = new Vector3f(f.normal);
    this.tag = f.tag != null ? new String(f.tag) : "";
  }

  public Face3D(int[] indices, int[] uvIndices) {
    this.color = new Color();
    this.indices = new int[indices.length];
    this.normal = new Vector3f();
    this.tag = "";
    this.indices = Arrays.copyOf(indices, indices.length);
    this.uvIndices = Arrays.copyOf(uvIndices, uvIndices.length);
  }

  public boolean sharesSameIndices(Face3D face) {
    int[] indices0 = Arrays.copyOf(face.indices, face.indices.length);
    int[] indices1 = Arrays.copyOf(indices, indices.length);
    Arrays.sort(indices0);
    Arrays.sort(indices1);
    return Arrays.equals(indices0, indices1);
  }

  public int getIndexAt(int index) {
    return indices[index % indices.length];
  }

  // Get UV index, return -1 if no UVs are available
  public int getUvIndexAt(int index) {
    if (uvIndices == null || uvIndices.length == 0) {
      return -1; // No UVs available
    }
    return uvIndices[index % uvIndices.length];
  }

  public void setUvIndices(int... uvIndices) {
    this.uvIndices = uvIndices;
  }

  public int getVertexCount() {
    return indices.length;
  }

  @Override
  public String toString() {
    return "Face3D [indices=" + Arrays.toString(indices) + "]";
  }
}

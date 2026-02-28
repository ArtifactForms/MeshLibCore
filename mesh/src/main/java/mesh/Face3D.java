package mesh;

import java.util.Arrays;

public class Face3D implements FaceView {

  public int[] indices;

  private String tag;

  public Face3D(Face3D f) {
    this.indices = Arrays.copyOf(f.indices, f.indices.length);
    this.tag = f.tag != null ? new String(f.tag) : "";
  }

  public Face3D(int... indices) {
    this.indices = new int[indices.length];
    this.tag = "";
    this.indices = Arrays.copyOf(indices, indices.length);
  }

  public boolean sharesSameIndices(Face3D face) {
    int[] indices0 = Arrays.copyOf(face.indices, face.indices.length);
    int[] indices1 = Arrays.copyOf(indices, indices.length);
    Arrays.sort(indices0);
    Arrays.sort(indices1);
    return Arrays.equals(indices0, indices1);
  }

  @Override
  public int getIndexAt(int index) {
    return indices[index % indices.length];
  }

  @Override
  public int getVertexCount() {
    return indices.length;
  }

  @Override
  public String toString() {
    return "Face3D [indices=" + Arrays.toString(indices) + "]";
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
}

package mesh.next.surface;

import java.util.ArrayList;
import java.util.List;

import math.Vector2f;

public class SurfaceLayer {

  private final List<Vector2f> uvs = new ArrayList<>();
  private final List<int[]> faceUvIndices = new ArrayList<>();

  public void addUV(float u, float v) {
    uvs.add(new Vector2f(u, v));
  }

  public void ensureFaceCapacity(int faceCount) {
    while (faceUvIndices.size() < faceCount) {
      faceUvIndices.add(null);
    }
  }

  public void setFaceUVIndices(int faceIndex, int[] uvIndices) {
    ensureFaceCapacity(faceIndex + 1);
    faceUvIndices.set(faceIndex, uvIndices.clone());
  }

  public Vector2f getUvAt(int index) {
    return uvs.get(index);
  }

  public int[] getFaceUVIndices(int faceIndex) {
    return faceUvIndices.get(faceIndex);
  }

  public int getUVCount() {
    return uvs.size();
  }
}

package mesh.modifier.topology;

import math.Mathf;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class BevelFacesModifier implements IMeshModifier {

  private float size;

  public BevelFacesModifier() {
    setSize(0.1f);
  }

  public BevelFacesModifier(float size) {
    this.size = Mathf.clamp(size, 0f, 1f);
  }

  @Override
  public Mesh3D modify(Mesh3D mesh) {
    float scale = 1f - size;
    float amount = size;
    new ExtrudeModifier(scale, amount).modify(mesh);
    return mesh;
  }

  public float getSize() {
    return size;
  }

  public void setSize(float size) {
    this.size = Mathf.clamp(size, 0f, 1f);
  }
}

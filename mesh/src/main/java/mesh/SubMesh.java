package mesh;

import java.util.ArrayList;
import java.util.List;

public class SubMesh {

  private String materialName;

  private List<Face3D> faces;

  public SubMesh() {
    faces = new ArrayList<Face3D>();
  }

  public void addFace(Face3D face) {
    faces.add(face);
  }

  public String getMaterialName() {
    return materialName;
  }

  public void setMaterialName(String materialName) {
    this.materialName = materialName;
  }

  public List<Face3D> getFaces() {
    return faces;
  }
}

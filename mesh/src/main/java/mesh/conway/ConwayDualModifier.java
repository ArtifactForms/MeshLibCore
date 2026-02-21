package mesh.conway;

import mesh.Mesh3D;
import mesh.creator.special.DualCreator;
import mesh.modifier.IMeshModifier;

public class ConwayDualModifier implements IMeshModifier {

  @Override
  public Mesh3D modify(Mesh3D mesh) {
    Mesh3D dual = new DualCreator(mesh).create();
    mesh.clearFaces();
    mesh.clearVertices();
    mesh.addFaces(dual.getFaces());
    mesh.addVertices(dual.getVertices());
    return mesh;
  }
}

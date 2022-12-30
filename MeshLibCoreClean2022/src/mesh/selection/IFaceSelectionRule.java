package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;

public interface IFaceSelectionRule {

	boolean isValid(Mesh3D mesh, Face3D face);

}

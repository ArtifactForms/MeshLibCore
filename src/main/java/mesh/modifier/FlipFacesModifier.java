package mesh.modifier;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class FlipFacesModifier implements IMeshModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		for (Face3D face : mesh.faces) {
			Mesh3DUtil.flipDirection(mesh, face);
		}
		return mesh;
	}

}

package mesh.modifier;

import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class FlipFacesModifier implements IMeshModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		Mesh3DUtil.flipDirection(mesh);
		return mesh;
	}

}

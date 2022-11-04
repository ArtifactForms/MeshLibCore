package mesh.conway;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;

public class ConwayKisModifier implements IMeshModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		new PlanarVertexCenterModifier().modify(mesh);
		return mesh;
	}

}

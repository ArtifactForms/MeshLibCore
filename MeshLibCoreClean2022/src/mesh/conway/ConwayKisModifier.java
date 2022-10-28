package mesh.conway;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.modifier.subdivision.TessellationFaceCenterModifier;

public class ConwayKisModifier implements IMeshModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		new TessellationFaceCenterModifier().modify(mesh);
		return mesh;
	}

}

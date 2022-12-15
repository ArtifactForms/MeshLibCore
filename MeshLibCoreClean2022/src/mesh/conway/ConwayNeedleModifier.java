package mesh.conway;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class ConwayNeedleModifier implements IMeshModifier {

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	new ConwayDualModifier().modify(mesh);
	new ConwayKisModifier().modify(mesh);
	return mesh;
    }

}

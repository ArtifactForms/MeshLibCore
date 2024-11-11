package mesh.conway;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class ConwayJoinModifier implements IMeshModifier {

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        new ConwayAmboModifier().modify(mesh);
        new ConwayDualModifier().modify(mesh);
        return mesh;
    }

}

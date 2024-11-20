package mesh.modifier;

import mesh.Face3D;
import mesh.Mesh3D;

public class UpdateFaceNormalsModifier implements IMeshModifier {

    @Override
    public Mesh3D modify(Mesh3D mesh) {

        for (Face3D face : mesh.faces) {
            face.normal = mesh.calculateFaceNormal(face);
        }

        return mesh;
    }

}

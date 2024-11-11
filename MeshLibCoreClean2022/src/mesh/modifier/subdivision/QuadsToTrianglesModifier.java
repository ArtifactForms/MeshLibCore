package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class QuadsToTrianglesModifier implements IMeshModifier {

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        List<Face3D> facesToRemove = new ArrayList<Face3D>();
        List<Face3D> facesToAdd = new ArrayList<Face3D>();
        for (Face3D face : mesh.faces) {
            if (face.indices.length == 4) {
                Face3D triangle0 = new Face3D(face.indices[0], face.indices[1], face.indices[2]);
                Face3D triangle1 = new Face3D(face.indices[2], face.indices[3], face.indices[0]);
                facesToRemove.add(face);
                facesToAdd.add(triangle0);
                facesToAdd.add(triangle1);
            }
        }
        mesh.faces.removeAll(facesToRemove);
        mesh.faces.addAll(facesToAdd);
        return mesh;
    }

}

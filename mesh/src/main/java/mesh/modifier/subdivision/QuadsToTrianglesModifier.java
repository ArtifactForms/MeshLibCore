package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class QuadsToTrianglesModifier implements IMeshModifier {

    private Mesh3D mesh;

    private List<Face3D> facesToRemove;

    private List<Face3D> facesToAdd;

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        setMesh(mesh);
        initializeLists();
        subdivideFaces();
        removeOldFaces();
        addNewFaces();
        return mesh;
    }

    private void initializeLists() {
        facesToRemove = new ArrayList<Face3D>();
        facesToAdd = new ArrayList<Face3D>();
    }

    private void subdivideFaces() {
        for (Face3D face : mesh.getFaces())
            subdivideFace(face);
    }

    private void subdivideFace(Face3D face) {
        if (face.indices.length != 4)
            return;

        int[] indices = face.indices;
        Face3D triangle0 = new Face3D(indices[0], indices[1], indices[2]);
        Face3D triangle1 = new Face3D(indices[2], indices[3], indices[0]);

        facesToRemove.add(face);
        facesToAdd.add(triangle0);
        facesToAdd.add(triangle1);
    }

    private void removeOldFaces() {
        mesh.removeFaces(facesToRemove);
    }

    private void addNewFaces() {
        mesh.addFaces(facesToAdd);
    }

    private void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

}

package mesh.modifier;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;

public class WireframeModifier implements IMeshModifier {

    private float scaleExtrude;

    private float thickness;

    private Mesh3D mesh;

    public WireframeModifier() {
        scaleExtrude = 0.9f;
        thickness = 0.02f;
    }

    private void createHoles() {
        List<Face3D> faces = mesh.getFaces();
        mesh.apply(new ExtrudeModifier(scaleExtrude, 0));
        mesh.faces.removeAll(faces);
    }

    private void solidify() {
        mesh.apply(new SolidifyModifier(thickness));
    }

    private void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
        setMesh(mesh);
        createHoles();
        solidify();
        return mesh;
    }

}

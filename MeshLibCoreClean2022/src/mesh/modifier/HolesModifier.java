package mesh.modifier;

import java.util.ArrayList;
import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class HolesModifier implements IMeshModifier {

    private float scaleExtrude;

    public HolesModifier() {
	this.scaleExtrude = 0.5f;
    }

    public HolesModifier(float scaleExtrude) {
	this.scaleExtrude = scaleExtrude;
    }

    public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
	for (Face3D face : faces)
	    Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0.0f);
	mesh.faces.removeAll(faces);
	return mesh;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	return modify(mesh, new ArrayList<Face3D>(mesh.faces));
    }

    public float getScaleExtrude() {
	return scaleExtrude;
    }

    public void setScaleExtrude(float scaleExtrude) {
	this.scaleExtrude = scaleExtrude;
    }

}

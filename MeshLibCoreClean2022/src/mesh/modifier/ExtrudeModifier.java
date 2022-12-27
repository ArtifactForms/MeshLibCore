package mesh.modifier;

import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class ExtrudeModifier implements IMeshModifier {

    private boolean removeFaces;
    private float scale;
    private float amount;
    private Collection<Face3D> faces;
    
    public ExtrudeModifier() {
	
    }

    public ExtrudeModifier(float scale, float amount) {
	this.scale = scale;
	this.amount = amount;
    }

    public Mesh3D modify(Mesh3D mesh) {
	if (faces == null) 
	    faces = mesh.getFaces();
	modify(mesh, faces);
	return mesh;
    }

    public void modify(Mesh3D mesh, Collection<Face3D> faces) {
	for (Face3D face : faces)
	    Mesh3DUtil.extrudeFace(mesh, face, scale, amount);
	if (removeFaces)
	    mesh.faces.removeAll(faces);
    }

    public float getScale() {
	return scale;
    }

    public void setScale(float scale) {
	this.scale = scale;
    }

    public float getAmount() {
	return amount;
    }

    public void setAmount(float amount) {
	this.amount = amount;
    }
    
    public void setFacesToExtrude(Collection<Face3D> faces) {
	this.faces = faces;
    }

    public boolean isRemoveFaces() {
        return removeFaces;
    }

    public void setRemoveFaces(boolean removeFaces) {
        this.removeFaces = removeFaces;
    }
    
}

package mesh.modifier;

import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class InsetModifier implements IMeshModifier {

	private float inset;

	public InsetModifier(float inset) {
		this.inset = inset;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		modify(mesh, mesh.getFaces());
		return mesh;
	}

	public void modify(Mesh3D mesh, Collection<Face3D> faces) {
		for (Face3D face : faces) {
			Mesh3DUtil.insetFace(mesh, face, inset);
		}
	}

	public float getInset() {
		return inset;
	}

	public void setInset(float inset) {
		this.inset = inset;
	}

}

package mesh.modifier;

import java.util.ArrayList;
import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class ExtrudeModifier implements IMeshModifier {
	
	private float scale;
	
	private float amount;

	public ExtrudeModifier(float scale, float amount) {
		this.scale = scale;
		this.amount = amount;
	}
	
	public Mesh3D modify(Mesh3D mesh) {
		modify(mesh, new ArrayList<>(mesh.faces));
		return mesh;
	}

	public void modify(Mesh3D mesh, Collection<Face3D> faces) {
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, scale, amount);
		}
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

}

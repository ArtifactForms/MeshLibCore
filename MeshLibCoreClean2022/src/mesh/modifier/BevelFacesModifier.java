package mesh.modifier;

import java.util.ArrayList;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.Mesh3DUtil;

public class BevelFacesModifier implements IMeshModifier {

	private float size;

	public BevelFacesModifier() {
		setSize(0.1f);
	}

	public BevelFacesModifier(float size) {
		this.size = Mathf.clamp(size, 0f, 1f);
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		float scale = 1f - size;
		float amount = size;

		ArrayList<Face3D> faces = new ArrayList<Face3D>(mesh.faces);

		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, scale, amount);
		}

		mesh.faces.removeAll(faces);

		return mesh;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = Mathf.clamp(size, 0f, 1f);
	}

}

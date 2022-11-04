package mesh.modifier;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.Mesh3DUtil;

public class WireframeModifier implements IMeshModifier {

	private float scaleExtrude;
	private float thickness;
	private Mesh3D mesh;
	
	public WireframeModifier() {
		this.scaleExtrude = 0.9f;
		this.thickness = 0.02f;
	}
	
	private void createHoles() {
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0.0f);
		}
		mesh.faces.removeAll(faces);
	}
	
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.mesh = mesh;
		createHoles();
		new SolidifyModifier(thickness).modify(mesh);
		return mesh;
	}

}

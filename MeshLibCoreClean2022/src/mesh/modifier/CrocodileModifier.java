package mesh.modifier;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.conway.ConwayAmboModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class CrocodileModifier implements IMeshModifier {

	private float distance;
	private Mesh3D mesh;
	FaceSelection selection;

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		ambo();
		selectFaces();
		createSpikes();
		removeSelectedFaces();
		return mesh;
	}

	private void createSpikes() {
		int nextIndex = mesh.vertices.size();
		List<Face3D> facesToAdd = new ArrayList<Face3D>();

		for (Face3D face : selection.getFaces()) {
			Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, face);
			Vector3f normal = Mesh3DUtil.calculateFaceNormal(mesh, face);
			for (int i = 0; i < face.indices.length; i++) {
				int fromIndex = face.indices[i];
				int toIndex = face.indices[(i + 1) % face.indices.length];
				int centerIndex = nextIndex;
				Face3D newTriangle = new Face3D(fromIndex, toIndex, centerIndex);
				newTriangle.tag = "spikes";
				facesToAdd.add(newTriangle);
			}
			center.addLocal(normal.mult(distance));
			mesh.add(center);
			nextIndex++;
		}
		
		mesh.faces.addAll(facesToAdd);
	}
	
	private void ambo() {
		new ConwayAmboModifier().modify(mesh);
	}

	private void selectFaces() {
		selection = new FaceSelection(mesh);
		selection.selectByTag("ambo");
	}
	
	private void removeSelectedFaces() {
		mesh.faces.removeAll(selection.getFaces());
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

}

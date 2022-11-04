package mesh.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.Mesh3DUtil;

public class VertexNormals {

	private Mesh3D mesh;
	private List<Vector3f> vertexNormals;
	private HashMap<Face3D, Vector3f> faceNormals;
	private HashMap<Vector3f, List<Face3D>> vectorToFace;

	public VertexNormals(Mesh3D mesh) {
		this.mesh = mesh;
		vertexNormals = new ArrayList<>();
		faceNormals = new HashMap<>();
		vectorToFace = new HashMap<>();
		refresh();
	}
	
	private void calculateFaceNormals() {
		for (Face3D face : mesh.faces) {
			Vector3f faceNormal = Mesh3DUtil.calculateFaceNormal(mesh, face);
			faceNormals.put(face, faceNormal);
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(face.indices[i]);
				List<Face3D> faces = vectorToFace.get(v);
				if (faces == null) {
					faces = new ArrayList<Face3D>();
					vectorToFace.put(v, faces);
				}
				faces.add(face);
			}
		}
	}
	
	private void calculateVertexNormals() {
		for (Vector3f v : mesh.vertices) {
			Vector3f normal = new Vector3f();
			List<Face3D> faces = vectorToFace.get(v);
			if (faces == null)
				continue;
			for (Face3D face : faces) {
				normal.addLocal(faceNormals.get(face));
			}
			normal.divideLocal(faces.size());
			vertexNormals.add(normal.normalizeLocal());
		}
	}
	
	public List<Vector3f> getVertexNormals() {
		return vertexNormals;
	}
	
	public void refresh() {
		vertexNormals.clear();
		faceNormals.clear();
		vectorToFace.clear();
		calculateFaceNormals();
		calculateVertexNormals();
	}
	
}

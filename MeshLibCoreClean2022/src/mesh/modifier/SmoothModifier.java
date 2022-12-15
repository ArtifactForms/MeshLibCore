package mesh.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class SmoothModifier implements IMeshModifier {

    private float factor;

    private List<Vector3f> smoothedVertices;

    private HashMap<Vector3f, List<Vector3f>> map;

    public SmoothModifier() {
	factor = 0.5f;
	smoothedVertices = new ArrayList<>();
	map = new HashMap<Vector3f, List<Vector3f>>();
    }

    protected Vector3f getSmoothedVertex(Vector3f v) {
	Vector3f a = new Vector3f(v);
	List<Vector3f> list = map.get(v);

	for (Vector3f v0 : list)
	    a.addLocal(v0);

	a.divideLocal(list.size());
	return a.mult(factor).add(v.mult(1f - factor));
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	for (Face3D face : mesh.faces) {
	    int n = face.indices.length;
	    for (int i = 0; i < face.indices.length; i++) {
		Vector3f edgeFrom = mesh.getVertexAt(face.indices[i]);
		Vector3f edgeTo = mesh.getVertexAt(face.indices[(i + 1) % n]);
		List<Vector3f> list = map.get(edgeFrom);
		if (list == null) {
		    list = new ArrayList<Vector3f>();
		    map.put(edgeFrom, list);
		}
		list.add(edgeTo);
	    }
	}

	for (Vector3f v : mesh.vertices) {
	    smoothedVertices.add(getSmoothedVertex(v));
	}

	mesh.vertices.clear();
	mesh.vertices.addAll(smoothedVertices);

	map.clear();
	smoothedVertices.clear();

	return mesh;
    }

}

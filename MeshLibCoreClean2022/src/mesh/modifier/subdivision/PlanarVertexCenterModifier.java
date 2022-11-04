package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.Collection;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.wip.Mesh3DUtil;

/**
 * Divides the face (polygon) from the center to the corner vertices. This
 * modifier works for faces with n vertices. The resulting mesh consists of
 * triangular faces.
 * 
 * <pre>
 * o-------o       o-------o
 * |       |       | \   / |
 * |       | ----> |   o   |
 * |       |       | /   \ |
 * o-------o       o-------o
 * </pre>
 * 
 * @version 0.1, 20 June 2016
 */
public class PlanarVertexCenterModifier implements IMeshModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		return modify(mesh, mesh.faces);
	}

	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> selection) {
		int nextIndex = mesh.vertices.size();
		ArrayList<Face3D> toAdd = new ArrayList<>();

		for (Face3D f : mesh.faces) {
			if (!selection.contains(f)) {
				toAdd.add(f);
				continue;
			}

			int n = f.indices.length;
			Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, f);
			mesh.add(center);
			for (int i = 0; i < f.indices.length; i++) {
				Face3D f1 = new Face3D(f.indices[i % n], f.indices[(i + 1) % n], nextIndex);
				toAdd.add(f1);
			}
			nextIndex++;
		}

		mesh.faces.clear();
		mesh.faces.addAll(toAdd);

		return mesh;
	}

}

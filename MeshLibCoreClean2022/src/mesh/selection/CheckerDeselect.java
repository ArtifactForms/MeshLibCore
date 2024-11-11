package mesh.selection;

import java.util.ArrayList;
import java.util.HashSet;

import mesh.Face3D;
import mesh.util.TraverseHelper;

public class CheckerDeselect {

	private FaceSelection selection;
	
	private ArrayList<Face3D> faces;
	
	private TraverseHelper helper;
	
	private HashSet<Face3D> processed;

	public CheckerDeselect(FaceSelection selection) {
		this.selection = selection;
		this.faces = new ArrayList<Face3D>();
		this.helper = new TraverseHelper(selection.getMesh());
		this.processed = new HashSet<Face3D>();
	}

	public void deselect() {
		this.faces.addAll(selection.getFaces());
		processed.clear();

		if (faces.isEmpty())
			return;
		Face3D start = faces.get(0);
		select(start, false);
		selection.clear();
		selection.addAll(faces);
	}

	public void select(Face3D face, boolean select) {
		if (face == null)
			return;

		if (processed.contains(face))
			return;

		processed.add(face);

		if (select) {
			faces.remove(face);
		}

		for (int i = 0; i < face.indices.length; i++) {
			int from = face.indices[i];
			int to = face.indices[(i + 1) % face.indices.length];
			Face3D pair = helper.getFaceByEdge(to, from);
			select(pair, !select);
		}
	}

}

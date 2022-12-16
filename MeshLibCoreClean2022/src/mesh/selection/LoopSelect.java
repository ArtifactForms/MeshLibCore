package mesh.selection;

import java.util.HashSet;

import mesh.Face3D;
import mesh.util.TraverseHelper;

public class LoopSelect {

    private FaceSelection selection;
    private HashSet<Face3D> loopSelectedFaces;
    private TraverseHelper helper;

    public LoopSelect(FaceSelection selection) {
	this.selection = selection;
	this.loopSelectedFaces = new HashSet<Face3D>();
	this.helper = new TraverseHelper(selection.getMesh());
    }

    public void select(int index) {
	if (selection.getMesh().faces.isEmpty())
	    return;
	Face3D start = selection.getMesh().getFaceAt(index);
	loop(start);
	selection.addAll(loopSelectedFaces);
	loopSelectedFaces.clear();
    }

    private void loop(Face3D face) {
	if (loopSelectedFaces.contains(face))
	    return;

	loopSelectedFaces.add(face);

	int from = face.indices[0];
	int to = face.indices[1];
	Face3D pairFace = helper.getFaceByEdge(to, from);
	loop(pairFace);
    }

}

package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;

public class SelectFaceRuleVertexCount implements IFaceSelectionRule {
	
	private int vertexCount;

	public SelectFaceRuleVertexCount(int vertexCount, CompareType compare) {
		this.vertexCount = vertexCount;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		return face.indices.length == vertexCount;
	}

}

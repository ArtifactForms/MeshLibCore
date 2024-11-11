package mesh.selection;

import mesh.Face3D;
import mesh.Mesh3D;

public class SelectFaceRuleVertexCount implements IFaceSelectionRule {

	private int vertexCount;
	
	private CompareType compare;

	public SelectFaceRuleVertexCount(int vertexCount, CompareType compare) {
		this.vertexCount = vertexCount;
		this.compare = compare;
	}

	@Override
	public boolean isValid(Mesh3D mesh, Face3D face) {
		return Compare.compare(compare, face.indices.length, vertexCount);
	}

}

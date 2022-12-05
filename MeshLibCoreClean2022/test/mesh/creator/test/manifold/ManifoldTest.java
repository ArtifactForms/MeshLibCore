package mesh.creator.test.manifold;

import java.util.HashMap;

import org.junit.Assert;

import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * For a mesh to be manifold, every edge must have exactly two adjacent faces.
 */
public class ManifoldTest {

	private Mesh3D meshUnderTest;
	
	public ManifoldTest(Mesh3D meshUnderTest) {
		this.meshUnderTest = meshUnderTest;
	}
	
	public void executeTest() {
		eachEdgeHasExactlyTwoAdjacentFaces();
	}
	
	private void eachEdgeHasExactlyTwoAdjacentFaces() {			
		HashMap<Edge3D, Integer> edges = new HashMap<Edge3D, Integer>();
		
		for (Face3D face : meshUnderTest.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				int fromIndex = face.indices[i];
				int toIndex = face.indices[(i + 1) % face.indices.length];
				
				Edge3D edge = new Edge3D(fromIndex, toIndex);
				Edge3D pair = edge.createPair();
				
				if (edges.containsKey(pair)) {
					edges.put(pair, edges.get(pair) + 1);
				} else {
					edges.put(edge, 1);
				}				
			}
		}
		
		for (Edge3D edge : edges.keySet()) {
			Integer adjacentFacesCount = edges.get(edge);
			Assert.assertEquals(2, (int) adjacentFacesCount);
		}
	}
	
}

package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedOctahedronCreator;
import util.MeshTest;

public class TruncatedOctahedronTest {

	private Mesh3D mesh;
	
	@Before
	public void setUp() {
		mesh = new TruncatedOctahedronCreator().create();
	}
	
	@Test
	public void hasTwentyFourVertices() {
		Assert.assertEquals(24, mesh.getVertexCount());
	}
	
	@Test
	public void hasFourteenFaces() {
		Assert.assertEquals(14, mesh.getFaceCount());
	}
	
	@Test
	public void hasSixQuadFaces() {
		MeshTest.assertQuadCountEquals(mesh, 6);
	}
	
	@Test
	public void hasEightHexagonFaces() {
		MeshTest.assertHexagonCountEquals(mesh, 8);
	}
	
	@Test
	public void hasThirtySixEdges() {
		MeshTest.assertEdgeCountEquals(mesh, 36);
	}
	
	@Test
	public void isManifold() {
		MeshTest.assertIsManifold(mesh);
	}
	
	@Test
	public void fulfillsEulerCharacteristic() {
		MeshTest.assertFulfillsEulerCharacteristic(mesh);
	}
	
	@Test
	public void everyEdgeHasLengthOfSqrtOfTwo() {
		MeshTest.assertEveryEgdgeHasALengthOf(mesh, Mathf.sqrt(2), 0);
	}
	
}

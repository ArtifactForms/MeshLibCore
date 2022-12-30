package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedTetrahedronCreator;
import util.MeshTest;

public class TruncatedTetrahedronTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new TruncatedTetrahedronCreator().create();
	}

	@Test
	public void hasTwelveVertices() {
		Assert.assertEquals(12, mesh.getVertexCount());
	}

	@Test
	public void hasEightFaces() {
		Assert.assertEquals(8, mesh.getFaceCount());
	}

	@Test
	public void hasFourTriangularFaces() {
		MeshTest.assertTriangleCountEquals(mesh, 4);
	}

	@Test
	public void hasFourHexagonFaces() {
		MeshTest.assertHexagonCountEquals(mesh, 4);
	}

	@Test
	public void hasEighteenEdges() {
		MeshTest.assertEdgeCountEquals(mesh, 18);
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
	public void everyEdgeHasLengthOfSqrtOfEight() {
		float delta = 0;
		float expcted = Mathf.sqrt(8);
		MeshTest.assertEveryEdgeHasALengthOf(mesh, expcted, delta);
	}

}

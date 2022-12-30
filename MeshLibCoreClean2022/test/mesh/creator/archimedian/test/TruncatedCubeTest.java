package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedCubeCreator;
import util.MeshTest;

public class TruncatedCubeTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new TruncatedCubeCreator().create();
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
	public void hasEightTriangularFaces() {
		MeshTest.assertTriangleCountEquals(mesh, 8);
	}

	@Test
	public void hasSixOctagonFaces() {
		MeshTest.assertOctagonCountEquals(mesh, 6);
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

}

package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.SnubDodecahedronCreator;
import util.MeshTest;

public class SnubDodecahedronTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new SnubDodecahedronCreator().create();
	}

	@Test
	public void hasSixtyVertices() {
		Assert.assertEquals(60, mesh.getVertexCount());
	}

	@Test
	public void hasNintyTwoFaces() {
		Assert.assertEquals(92, mesh.getFaceCount());
	}

	@Test
	public void hasEightyTriangularFaces() {
		MeshTest.assertTriangleCountEquals(mesh, 80);
	}

	@Test
	public void hasTwelvePentagonFaces() {
		MeshTest.assertPentagonCountEquals(mesh, 12);
	}

	@Test
	public void hasHundredFiftyEdges() {
		MeshTest.assertEdgeCountEquals(mesh, 150);
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

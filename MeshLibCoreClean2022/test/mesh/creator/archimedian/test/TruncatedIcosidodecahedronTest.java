package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.TruncatedIcosidodecahedronCreator;
import mesh.util.Geometry;
import util.MeshTest;

public class TruncatedIcosidodecahedronTest {

	private Mesh3D mesh;
	
	@Before
	public void setUp() {
		mesh = new TruncatedIcosidodecahedronCreator().create();
	}
	
	@Test
	public void hasOneHundredAndTwentyVertices() {
		Assert.assertEquals(120, mesh.getVertexCount());
	}
	
	@Test
	public void hasSixtyTwoFaces() {
		Assert.assertEquals(62, mesh.getFaceCount());
	}
	
	@Test
	public void hasThirtyQuadFaces() {
		MeshTest.assertQuadCountEquals(mesh, 30);
	}
	
	@Test
	public void hasTwentyHexagonFaces() {
		MeshTest.assertHexagonCountEquals(mesh, 20);
	}
	
	@Test
	public void hasTwelveDacagonFaces() {
		MeshTest.assertDecagonCountEquals(mesh, 12);
	}
	
	@Test
	public void hasOneHundredAndEightyEdges() {
		MeshTest.assertEdgeCountEquals(mesh, 180);
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
	public void everyEdgeHasLengthOfTwiceTheGoldenRatioMinusTwo() {
		float delta = 0.000001f;
		float expcted = 2 * Geometry.GOLDEN_RATIO - 2;
		MeshTest.assertEveryEdgeHasALengthOf(mesh, expcted, delta);
	}
	
}

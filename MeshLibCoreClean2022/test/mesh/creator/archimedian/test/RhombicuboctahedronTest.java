package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.RhombicuboctahedronCreator;
import util.MeshTest;

public class RhombicuboctahedronTest {
	
	private Mesh3D mesh;
	
	@Before
	public void setUp() {
		mesh = new RhombicuboctahedronCreator().create();
	}
	
	@Test
	public void hasTwentyFourVertices() {
		Assert.assertEquals(24, mesh.getVertexCount());
	}
	
	@Test
	public void hasTwentySixFaces() {
		Assert.assertEquals(26, mesh.getFaceCount());
	}
	
	@Test
	public void hasEightTriangularFaces() {
		MeshTest.assertTriangleCountEquals(mesh, 8);
	}
	
	@Test
	public void hasEighteenQuadFaces() {
		MeshTest.assertQuadCountEquals(mesh, 18);
	}
	
	@Test
	public void hasFourtyEightEdges() {
		MeshTest.assertEdgeCountEquals(mesh, 48);
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
	public void everyEdgeHasLengthOfTwo() {
		MeshTest.assertEveryEdgeHasALengthOf(mesh, 2, 0);
	}

}

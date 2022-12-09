package mesh.creator.archimedian.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.RhombicosidodecahedronCreator;
import util.MeshTest;

public class RhombicosidodecahedronTest {
	
	private Mesh3D mesh;
	
	@Before
	public void setUp() {
		mesh = new RhombicosidodecahedronCreator().create();
	}
	
	@Test
	public void hasSixtyVertices() {
		Assert.assertEquals(60, mesh.getVertexCount());
	}
	
	@Test
	public void hasSixtyTwoFaces() {
		Assert.assertEquals(62, mesh.getFaceCount());
	}
	
	@Test
	public void hasTwentyTriangularFaces() {
		MeshTest.assertTriangleCountEquals(mesh, 20);
	}
	
	@Test
	public void hasThirtyQuadFaces() {
		MeshTest.assertQuadCountEquals(mesh, 30);
	}
	
	@Test
	public void hasTwelvePentagonFaces() {
		MeshTest.assertPentagonCountEquals(mesh, 12);
	}
	
	@Test
	public void hasHundredTwentyEdges() {
		MeshTest.assertEdgeCountEquals(mesh, 120);
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

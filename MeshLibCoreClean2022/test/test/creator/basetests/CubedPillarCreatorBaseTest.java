package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.CubedPillarCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CubedPillarCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new CubedPillarCreator().create();
	}

	public void implementsCreatorInterface() {
		CubedPillarCreator creator = new CubedPillarCreator();
		Assert.assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void createdMeshIsNotNullByDefault() {
		Assert.assertNotNull(mesh);
	}

	@Test
	public void vertexListIsNotEmpty() {
		Assert.assertFalse(mesh.vertices.isEmpty());
	}

	@Test
	public void getVertexCountReturnsSizeOfVertexList() {
		int vertexCount = mesh.getVertexCount();
		Assert.assertEquals(vertexCount, mesh.getVertices().size());
	}

	@Test
	public void getFaceCountReturnsSizeOfFaceList() {
		int faceCount = mesh.getFaceCount();
		Assert.assertEquals(faceCount, mesh.getFaces().size());
	}

	@Test
	public void createdMeshHasNoLooseVertices() {
		MeshTest.assertMeshHasNoLooseVertices(mesh);
	}

	@Test
	public void createdMeshHasNoDuplicatedFaces() {
		// Running this test is very time expensive
		MeshTest.assertMeshHasNoDuplicatedFaces(mesh);
	}

	@Test
	public void eachCallOfCreateReturnsNewUniqueMeshInstance() {
		Mesh3D mesh0 = new CubedPillarCreator().create();
		Mesh3D mesh1 = new CubedPillarCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new CubedPillarCreator().create();
		Mesh3D mesh1 = new CubedPillarCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetExtrude() {
		float expected = 2.879421545165706E38f;
		CubedPillarCreator creator = new CubedPillarCreator();
		creator.setExtrude(expected);
		Assert.assertEquals(expected, creator.getExtrude(), 0);
	}

	@Test
	public void getExtrudeReturnsDefaultValue() {
		float expected = 0.05f;
		CubedPillarCreator creator = new CubedPillarCreator();
		Assert.assertEquals(expected, creator.getExtrude(), 0);
	}

	@Test
	public void getSetSegmentCount() {
		int expected = 1874683399;
		CubedPillarCreator creator = new CubedPillarCreator();
		creator.setSegmentCount(expected);
		Assert.assertEquals(expected, creator.getSegmentCount());
	}

	@Test
	public void getSegmentCountReturnsDefaultValue() {
		int expected = 5;
		CubedPillarCreator creator = new CubedPillarCreator();
		Assert.assertEquals(expected, creator.getSegmentCount());
	}

	@Test
	public void getSetSegmentRadius() {
		float expected = 3.268724918116279E37f;
		CubedPillarCreator creator = new CubedPillarCreator();
		creator.setSegmentRadius(expected);
		Assert.assertEquals(expected, creator.getSegmentRadius(), 0);
	}

	@Test
	public void getSegmentRadiusReturnsDefaultValue() {
		float expected = 0.5f;
		CubedPillarCreator creator = new CubedPillarCreator();
		Assert.assertEquals(expected, creator.getSegmentRadius(), 0);
	}

}

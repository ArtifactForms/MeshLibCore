package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.creative.RingCageCreator;

// Auto-generated test class to execute base tests for mesh creators
public class RingCageCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new RingCageCreator().create();
	}

	public void implementsCreatorInterface() {
		RingCageCreator creator = new RingCageCreator();
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
		Mesh3D mesh0 = new RingCageCreator().create();
		Mesh3D mesh1 = new RingCageCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new RingCageCreator().create();
		Mesh3D mesh1 = new RingCageCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetVertices() {
		int expected = 1294279808;
		RingCageCreator creator = new RingCageCreator();
		creator.setVertices(expected);
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getVerticesReturnsDefaultValue() {
		int expected = 16;
		RingCageCreator creator = new RingCageCreator();
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getSetSubdivisions() {
		int expected = 1414241194;
		RingCageCreator creator = new RingCageCreator();
		creator.setSubdivisions(expected);
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSubdivisionsReturnsDefaultValue() {
		int expected = 1;
		RingCageCreator creator = new RingCageCreator();
		Assert.assertEquals(expected, creator.getSubdivisions());
	}

	@Test
	public void getSetOuterRadius() {
		float expected = 1.8014094131610644E38f;
		RingCageCreator creator = new RingCageCreator();
		creator.setOuterRadius(expected);
		Assert.assertEquals(expected, creator.getOuterRadius(), 0);
	}

	@Test
	public void getOuterRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		RingCageCreator creator = new RingCageCreator();
		Assert.assertEquals(expected, creator.getOuterRadius(), 0);
	}

}

package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.GridCreator;

// Auto-generated test class to execute base tests for mesh creators
public class GridCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new GridCreator().create();
	}

	public void implementsCreatorInterface() {
		GridCreator creator = new GridCreator();
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
		Mesh3D mesh0 = new GridCreator().create();
		Mesh3D mesh1 = new GridCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new GridCreator().create();
		Mesh3D mesh1 = new GridCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetSubdivisionsX() {
		int expected = 699370276;
		GridCreator creator = new GridCreator();
		creator.setSubdivisionsX(expected);
		Assert.assertEquals(expected, creator.getSubdivisionsX());
	}

	@Test
	public void getSubdivisionsXReturnsDefaultValue() {
		int expected = 10;
		GridCreator creator = new GridCreator();
		Assert.assertEquals(expected, creator.getSubdivisionsX());
	}

	@Test
	public void getSetSubdivisionsZ() {
		int expected = 1940189728;
		GridCreator creator = new GridCreator();
		creator.setSubdivisionsZ(expected);
		Assert.assertEquals(expected, creator.getSubdivisionsZ());
	}

	@Test
	public void getSubdivisionsZReturnsDefaultValue() {
		int expected = 10;
		GridCreator creator = new GridCreator();
		Assert.assertEquals(expected, creator.getSubdivisionsZ());
	}

	@Test
	public void getSetTileSizeZ() {
		float expected = 1.893442968629605E38f;
		GridCreator creator = new GridCreator();
		creator.setTileSizeZ(expected);
		Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
	}

	@Test
	public void getTileSizeZReturnsDefaultValue() {
		float expected = 0.1f;
		GridCreator creator = new GridCreator();
		Assert.assertEquals(expected, creator.getTileSizeZ(), 0);
	}

	@Test
	public void getSetTileSizeX() {
		float expected = 2.29060877992134E38f;
		GridCreator creator = new GridCreator();
		creator.setTileSizeX(expected);
		Assert.assertEquals(expected, creator.getTileSizeX(), 0);
	}

	@Test
	public void getTileSizeXReturnsDefaultValue() {
		float expected = 0.1f;
		GridCreator creator = new GridCreator();
		Assert.assertEquals(expected, creator.getTileSizeX(), 0);
	}

}

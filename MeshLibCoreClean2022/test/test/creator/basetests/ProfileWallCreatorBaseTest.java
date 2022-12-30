package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.ProfileWallCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ProfileWallCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new ProfileWallCreator().create();
	}

	public void implementsCreatorInterface() {
		ProfileWallCreator creator = new ProfileWallCreator();
		Assert.assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void createdMeshIsNotNullByDefault() {
		Assert.assertNotNull(mesh);
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
		Mesh3D mesh0 = new ProfileWallCreator().create();
		Mesh3D mesh1 = new ProfileWallCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new ProfileWallCreator().create();
		Mesh3D mesh1 = new ProfileWallCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetCorner() {
		boolean expected = true;
		ProfileWallCreator creator = new ProfileWallCreator();
		creator.setCorner(expected);
		Assert.assertEquals(expected, creator.isCorner());
	}

	@Test
	public void getCornerReturnsDefaultValue() {
		boolean expected = false;
		ProfileWallCreator creator = new ProfileWallCreator();
		Assert.assertEquals(expected, creator.isCorner());
	}

	@Test
	public void getSetWidth() {
		float expected = 1.2010695334969968E38f;
		ProfileWallCreator creator = new ProfileWallCreator();
		creator.setWidth(expected);
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

	@Test
	public void getWidthReturnsDefaultValue() {
		float expected = 2.0f;
		ProfileWallCreator creator = new ProfileWallCreator();
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

}

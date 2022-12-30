package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.CylinderCreator;

// Auto-generated test class to execute base tests for mesh creators
public class CylinderCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new CylinderCreator().create();
	}

	public void implementsCreatorInterface() {
		CylinderCreator creator = new CylinderCreator();
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
		Mesh3D mesh0 = new CylinderCreator().create();
		Mesh3D mesh1 = new CylinderCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new CylinderCreator().create();
		Mesh3D mesh1 = new CylinderCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetVertices() {
		int expected = 1949455341;
		CylinderCreator creator = new CylinderCreator();
		creator.setVertices(expected);
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getVerticesReturnsDefaultValue() {
		int expected = 32;
		CylinderCreator creator = new CylinderCreator();
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getSetHeight() {
		float expected = 3.8653385671066576E37f;
		CylinderCreator creator = new CylinderCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 2.0f;
		CylinderCreator creator = new CylinderCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetTopRadius() {
		float expected = 1.4189119877532809E38f;
		CylinderCreator creator = new CylinderCreator();
		creator.setTopRadius(expected);
		Assert.assertEquals(expected, creator.getTopRadius(), 0);
	}

	@Test
	public void getTopRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		CylinderCreator creator = new CylinderCreator();
		Assert.assertEquals(expected, creator.getTopRadius(), 0);
	}

	@Test
	public void getSetBottomRadius() {
		float expected = 3.0032815083411172E38f;
		CylinderCreator creator = new CylinderCreator();
		creator.setBottomRadius(expected);
		Assert.assertEquals(expected, creator.getBottomRadius(), 0);
	}

	@Test
	public void getBottomRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		CylinderCreator creator = new CylinderCreator();
		Assert.assertEquals(expected, creator.getBottomRadius(), 0);
	}

}

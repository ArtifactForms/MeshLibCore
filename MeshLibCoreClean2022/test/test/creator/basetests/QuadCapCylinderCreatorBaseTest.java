package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.QuadCapCylinderCreator;

// Auto-generated test class to execute base tests for mesh creators
public class QuadCapCylinderCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new QuadCapCylinderCreator().create();
	}

	public void implementsCreatorInterface() {
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
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
		Mesh3D mesh0 = new QuadCapCylinderCreator().create();
		Mesh3D mesh1 = new QuadCapCylinderCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new QuadCapCylinderCreator().create();
		Mesh3D mesh1 = new QuadCapCylinderCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetRadius() {
		float expected = 3.3697256168900375E38f;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		creator.setRadius(expected);
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getSetVertices() {
		int expected = 1156112944;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		creator.setVertices(expected);
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getVerticesReturnsDefaultValue() {
		int expected = 32;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		Assert.assertEquals(expected, creator.getVertices());
	}

	@Test
	public void getSetHeight() {
		float expected = 3.0532068595976703E38f;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 2.0f;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetHeightSegments() {
		int expected = 304515421;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		creator.setHeightSegments(expected);
		Assert.assertEquals(expected, creator.getHeightSegments());
	}

	@Test
	public void getHeightSegmentsReturnsDefaultValue() {
		int expected = 1;
		QuadCapCylinderCreator creator = new QuadCapCylinderCreator();
		Assert.assertEquals(expected, creator.getHeightSegments());
	}

}

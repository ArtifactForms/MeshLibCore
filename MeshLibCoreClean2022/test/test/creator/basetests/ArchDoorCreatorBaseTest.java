package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.ArchDoorCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ArchDoorCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new ArchDoorCreator().create();
	}

	public void implementsCreatorInterface() {
		ArchDoorCreator creator = new ArchDoorCreator();
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
		Mesh3D mesh0 = new ArchDoorCreator().create();
		Mesh3D mesh1 = new ArchDoorCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new ArchDoorCreator().create();
		Mesh3D mesh1 = new ArchDoorCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetDepth() {
		float expected = 2.8036524740290596E38f;
		ArchDoorCreator creator = new ArchDoorCreator();
		creator.setDepth(expected);
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getDepthReturnsDefaultValue() {
		float expected = 0.1f;
		ArchDoorCreator creator = new ArchDoorCreator();
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getSetSegments() {
		int expected = 1907568131;
		ArchDoorCreator creator = new ArchDoorCreator();
		creator.setSegments(expected);
		Assert.assertEquals(expected, creator.getSegments());
	}

	@Test
	public void getSegmentsReturnsDefaultValue() {
		int expected = 16;
		ArchDoorCreator creator = new ArchDoorCreator();
		Assert.assertEquals(expected, creator.getSegments());
	}

	@Test
	public void getSetExtendBottom() {
		float expected = 1.3795045119066558E38f;
		ArchDoorCreator creator = new ArchDoorCreator();
		creator.setExtendBottom(expected);
		Assert.assertEquals(expected, creator.getExtendBottom(), 0);
	}

	@Test
	public void getExtendBottomReturnsDefaultValue() {
		float expected = 3.0f;
		ArchDoorCreator creator = new ArchDoorCreator();
		Assert.assertEquals(expected, creator.getExtendBottom(), 0);
	}

	@Test
	public void getSetRadius() {
		float expected = 3.0244535959192784E38f;
		ArchDoorCreator creator = new ArchDoorCreator();
		creator.setRadius(expected);
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

	@Test
	public void getRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		ArchDoorCreator creator = new ArchDoorCreator();
		Assert.assertEquals(expected, creator.getRadius(), 0);
	}

}

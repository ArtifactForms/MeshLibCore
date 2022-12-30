package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.ChainLinkCreator;

// Auto-generated test class to execute base tests for mesh creators
public class ChainLinkCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new ChainLinkCreator().create();
	}

	public void implementsCreatorInterface() {
		ChainLinkCreator creator = new ChainLinkCreator();
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
		Mesh3D mesh0 = new ChainLinkCreator().create();
		Mesh3D mesh1 = new ChainLinkCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new ChainLinkCreator().create();
		Mesh3D mesh1 = new ChainLinkCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetMajorRadius() {
		float expected = 7.489594634262112E37f;
		ChainLinkCreator creator = new ChainLinkCreator();
		creator.setMajorRadius(expected);
		Assert.assertEquals(expected, creator.getMajorRadius(), 0);
	}

	@Test
	public void getMajorRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		ChainLinkCreator creator = new ChainLinkCreator();
		Assert.assertEquals(expected, creator.getMajorRadius(), 0);
	}

	@Test
	public void getSetCenterPieceSize() {
		float expected = 7.029039027119288E37f;
		ChainLinkCreator creator = new ChainLinkCreator();
		creator.setCenterPieceSize(expected);
		Assert.assertEquals(expected, creator.getCenterPieceSize(), 0);
	}

	@Test
	public void getCenterPieceSizeReturnsDefaultValue() {
		float expected = 1.5f;
		ChainLinkCreator creator = new ChainLinkCreator();
		Assert.assertEquals(expected, creator.getCenterPieceSize(), 0);
	}

	@Test
	public void getSetMinorRadius() {
		float expected = 2.0958584981512996E38f;
		ChainLinkCreator creator = new ChainLinkCreator();
		creator.setMinorRadius(expected);
		Assert.assertEquals(expected, creator.getMinorRadius(), 0);
	}

	@Test
	public void getMinorRadiusReturnsDefaultValue() {
		float expected = 0.25f;
		ChainLinkCreator creator = new ChainLinkCreator();
		Assert.assertEquals(expected, creator.getMinorRadius(), 0);
	}

	@Test
	public void getSetMinorSegments() {
		int expected = 2122393978;
		ChainLinkCreator creator = new ChainLinkCreator();
		creator.setMinorSegments(expected);
		Assert.assertEquals(expected, creator.getMinorSegments());
	}

	@Test
	public void getMinorSegmentsReturnsDefaultValue() {
		int expected = 12;
		ChainLinkCreator creator = new ChainLinkCreator();
		Assert.assertEquals(expected, creator.getMinorSegments());
	}

	@Test
	public void getSetMajorSegments() {
		int expected = 790583576;
		ChainLinkCreator creator = new ChainLinkCreator();
		creator.setMajorSegments(expected);
		Assert.assertEquals(expected, creator.getMajorSegments());
	}

	@Test
	public void getMajorSegmentsReturnsDefaultValue() {
		int expected = 12;
		ChainLinkCreator creator = new ChainLinkCreator();
		Assert.assertEquals(expected, creator.getMajorSegments());
	}

}

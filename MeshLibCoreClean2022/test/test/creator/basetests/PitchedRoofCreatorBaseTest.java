package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.assets.PitchedRoofCreator;

// Auto-generated test class to execute base tests for mesh creators
public class PitchedRoofCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new PitchedRoofCreator().create();
	}

	public void implementsCreatorInterface() {
		PitchedRoofCreator creator = new PitchedRoofCreator();
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
		Mesh3D mesh0 = new PitchedRoofCreator().create();
		Mesh3D mesh1 = new PitchedRoofCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new PitchedRoofCreator().create();
		Mesh3D mesh1 = new PitchedRoofCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetDepth() {
		float expected = 2.0213752590154494E38f;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		creator.setDepth(expected);
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getDepthReturnsDefaultValue() {
		float expected = 6.0f;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		Assert.assertEquals(expected, creator.getDepth(), 0);
	}

	@Test
	public void getSetHeight() {
		float expected = 1.8980909598545092E38f;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		creator.setHeight(expected);
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getHeightReturnsDefaultValue() {
		float expected = 2.0f;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		Assert.assertEquals(expected, creator.getHeight(), 0);
	}

	@Test
	public void getSetSnapToGround() {
		boolean expected = true;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		creator.setSnapToGround(expected);
		Assert.assertEquals(expected, creator.isSnapToGround());
	}

	@Test
	public void getSnapToGroundReturnsDefaultValue() {
		boolean expected = false;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		Assert.assertEquals(expected, creator.isSnapToGround());
	}

	@Test
	public void getSetTriangulate() {
		boolean expected = true;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		creator.setTriangulate(expected);
		Assert.assertEquals(expected, creator.isTriangulate());
	}

	@Test
	public void getTriangulateReturnsDefaultValue() {
		boolean expected = false;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		Assert.assertEquals(expected, creator.isTriangulate());
	}

	@Test
	public void getSetCapBottom() {
		boolean expected = true;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		creator.setCapBottom(expected);
		Assert.assertEquals(expected, creator.isCapBottom());
	}

	@Test
	public void getCapBottomReturnsDefaultValue() {
		boolean expected = false;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		Assert.assertEquals(expected, creator.isCapBottom());
	}

	@Test
	public void getSetWidth() {
		float expected = 2.0313571419447047E38f;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		creator.setWidth(expected);
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

	@Test
	public void getWidthReturnsDefaultValue() {
		float expected = 4.0f;
		PitchedRoofCreator creator = new PitchedRoofCreator();
		Assert.assertEquals(expected, creator.getWidth(), 0);
	}

}

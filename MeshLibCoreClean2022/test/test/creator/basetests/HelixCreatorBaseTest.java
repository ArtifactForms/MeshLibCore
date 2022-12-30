package test.creator.basetests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTest;
import mesh.creator.IMeshCreator;

import mesh.creator.primitives.HelixCreator;

// Auto-generated test class to execute base tests for mesh creators
public class HelixCreatorBaseTest {

	private Mesh3D mesh;

	@Before
	public void setUp() {
		mesh = new HelixCreator().create();
	}

	public void implementsCreatorInterface() {
		HelixCreator creator = new HelixCreator();
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
		Mesh3D mesh0 = new HelixCreator().create();
		Mesh3D mesh1 = new HelixCreator().create();
		Assert.assertTrue(mesh0 != mesh1);
	}

	@Test
	public void creationOfVerticesIsConsistentIfNotChangingParameters() {
		Mesh3D mesh0 = new HelixCreator().create();
		Mesh3D mesh1 = new HelixCreator().create();
		mesh0.vertices.removeAll(mesh1.getVertices());
		Assert.assertEquals(0, mesh0.getVertices().size());
		Assert.assertEquals(0, mesh0.getVertexCount());
	}

	@Test
	public void getSetMajorRadius() {
		float expected = 4.0513505591760687E37f;
		HelixCreator creator = new HelixCreator();
		creator.setMajorRadius(expected);
		Assert.assertEquals(expected, creator.getMajorRadius(), 0);
	}

	@Test
	public void getMajorRadiusReturnsDefaultValue() {
		float expected = 1.0f;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.getMajorRadius(), 0);
	}

	@Test
	public void getSetMinorRadius() {
		float expected = 2.6478122977849013E38f;
		HelixCreator creator = new HelixCreator();
		creator.setMinorRadius(expected);
		Assert.assertEquals(expected, creator.getMinorRadius(), 0);
	}

	@Test
	public void getMinorRadiusReturnsDefaultValue() {
		float expected = 0.25f;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.getMinorRadius(), 0);
	}

	@Test
	public void getSetMinorSegments() {
		int expected = 1487854318;
		HelixCreator creator = new HelixCreator();
		creator.setMinorSegments(expected);
		Assert.assertEquals(expected, creator.getMinorSegments());
	}

	@Test
	public void getMinorSegmentsReturnsDefaultValue() {
		int expected = 12;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.getMinorSegments());
	}

	@Test
	public void getSetMajorSegments() {
		int expected = 809010676;
		HelixCreator creator = new HelixCreator();
		creator.setMajorSegments(expected);
		Assert.assertEquals(expected, creator.getMajorSegments());
	}

	@Test
	public void getMajorSegmentsReturnsDefaultValue() {
		int expected = 48;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.getMajorSegments());
	}

	@Test
	public void getSetCap() {
		boolean expected = true;
		HelixCreator creator = new HelixCreator();
		creator.setCap(expected);
		Assert.assertEquals(expected, creator.isCap());
	}

	@Test
	public void getCapReturnsDefaultValue() {
		boolean expected = true;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.isCap());
	}

	@Test
	public void getSetTurns() {
		int expected = 1591864459;
		HelixCreator creator = new HelixCreator();
		creator.setTurns(expected);
		Assert.assertEquals(expected, creator.getTurns());
	}

	@Test
	public void getTurnsReturnsDefaultValue() {
		int expected = 4;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.getTurns());
	}

	@Test
	public void getSetDy() {
		float expected = 2.8161545446807614E38f;
		HelixCreator creator = new HelixCreator();
		creator.setDy(expected);
		Assert.assertEquals(expected, creator.getDy(), 0);
	}

	@Test
	public void getDyReturnsDefaultValue() {
		float expected = 0.6f;
		HelixCreator creator = new HelixCreator();
		Assert.assertEquals(expected, creator.getDy(), 0);
	}

}
